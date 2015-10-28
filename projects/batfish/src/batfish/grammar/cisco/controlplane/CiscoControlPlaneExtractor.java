package batfish.grammar.cisco.controlplane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import batfish.grammar.BatfishCombinedParser;
import batfish.grammar.ControlPlaneExtractor;
import batfish.grammar.ParseTreePrettyPrinter;
import batfish.grammar.cisco.CiscoParser.*;
import batfish.grammar.cisco.*;
import batfish.main.BatfishException;
import batfish.main.PedanticBatfishException;
import batfish.representation.Ip;
import batfish.representation.IpProtocol;
import batfish.representation.LineAction;
import batfish.representation.OriginType;
import batfish.representation.OspfMetricType;
import batfish.representation.Prefix;
import batfish.representation.RoutingProtocol;
import batfish.representation.SwitchportEncapsulationType;
import batfish.representation.SwitchportMode;
import batfish.representation.VendorConfiguration;
import batfish.representation.cisco.BgpAggregateNetwork;
import batfish.representation.cisco.BgpPeerGroup;
import batfish.representation.cisco.BgpProcess;
import batfish.representation.cisco.BgpRedistributionPolicy;
import batfish.representation.cisco.CiscoConfiguration;
import batfish.representation.cisco.CiscoVendorConfiguration;
import batfish.representation.cisco.DynamicBgpPeerGroup;
import batfish.representation.cisco.ExpandedCommunityList;
import batfish.representation.cisco.ExpandedCommunityListLine;
import batfish.representation.cisco.ExtendedAccessList;
import batfish.representation.cisco.ExtendedAccessListLine;
import batfish.representation.cisco.Interface;
import batfish.representation.cisco.IpAsPathAccessList;
import batfish.representation.cisco.IpAsPathAccessListLine;
import batfish.representation.cisco.IpBgpPeerGroup;
import batfish.representation.cisco.Ipv6BgpPeerGroup;
import batfish.representation.cisco.MasterBgpPeerGroup;
import batfish.representation.cisco.NamedBgpPeerGroup;
import batfish.representation.cisco.OspfProcess;
import batfish.representation.cisco.OspfRedistributionPolicy;
import batfish.representation.cisco.OspfWildcardNetwork;
import batfish.representation.cisco.PrefixList;
import batfish.representation.cisco.PrefixListLine;
import batfish.representation.cisco.RouteMap;
import batfish.representation.cisco.RouteMapClause;
import batfish.representation.cisco.RouteMapMatchAsPathAccessListLine;
import batfish.representation.cisco.RouteMapMatchCommunityListLine;
import batfish.representation.cisco.RouteMapMatchIpAccessListLine;
import batfish.representation.cisco.RouteMapMatchIpPrefixListLine;
import batfish.representation.cisco.RouteMapMatchTagLine;
import batfish.representation.cisco.RouteMapSetAdditiveCommunityLine;
import batfish.representation.cisco.RouteMapSetAsPathPrependLine;
import batfish.representation.cisco.RouteMapSetCommunityLine;
import batfish.representation.cisco.RouteMapSetCommunityNoneLine;
import batfish.representation.cisco.RouteMapSetDeleteCommunityLine;
import batfish.representation.cisco.RouteMapSetLine;
import batfish.representation.cisco.RouteMapSetLocalPreferenceLine;
import batfish.representation.cisco.RouteMapSetMetricLine;
import batfish.representation.cisco.RouteMapSetNextHopLine;
import batfish.representation.cisco.RouteMapSetOriginTypeLine;
import batfish.representation.cisco.StandardAccessList;
import batfish.representation.cisco.StandardAccessListLine;
import batfish.representation.cisco.StandardCommunityList;
import batfish.representation.cisco.StaticRoute;
import batfish.util.SubRange;
import batfish.util.Util;

public class CiscoControlPlaneExtractor extends CiscoParserBaseListener
      implements ControlPlaneExtractor {

   private static final Map<String, String> CISCO_INTERFACE_PREFIXES = getCiscoInterfacePrefixes();

   private static final int DEFAULT_STATIC_ROUTE_DISTANCE = 1;

   private static final String NXOS_MANAGEMENT_INTERFACE_PREFIX = "mgmt";

   public static LineAction getAccessListAction(Access_list_actionContext ctx) {
      if (ctx.PERMIT() != null) {
         return LineAction.ACCEPT;
      }
      else if (ctx.DENY() != null) {
         return LineAction.REJECT;
      }
      else {
         throw new BatfishException("bad LineAction");
      }
   }

   private static String getCanonicalInterfaceNamePrefix(String prefix) {
      for (Entry<String, String> e : CISCO_INTERFACE_PREFIXES.entrySet()) {
         String matchPrefix = e.getKey();
         String canonicalPrefix = e.getValue();
         if (matchPrefix.toLowerCase().startsWith(prefix.toLowerCase())) {
            return canonicalPrefix;
         }
      }
      throw new BatfishException("Invalid interface name prefix: \"" + prefix
            + "\"");
   }

   private static Map<String, String> getCiscoInterfacePrefixes() {
      Map<String, String> prefixes = new LinkedHashMap<String, String>();
      prefixes.put("Async", "Async");
      prefixes.put("ATM", "ATM");
      prefixes.put("cmp-mgmt", "cmp-mgmt");
      prefixes.put("Embedded-Service-Engine", "Embedded-Service-Engine");
      prefixes.put("Ethernet", "Ethernet");
      prefixes.put("FastEthernet", "FastEthernet");
      prefixes.put("fe", "FastEthernet");
      prefixes.put("GigabitEthernet", "GigabitEthernet");
      prefixes.put("ge", "GigabitEthernet");
      prefixes.put("GMPLS", "GMPLS");
      prefixes.put("Group-Async", "Group-Async");
      prefixes.put("Loopback", "Loopback");
      prefixes.put("Management", "Management");
      prefixes.put("mgmt", NXOS_MANAGEMENT_INTERFACE_PREFIX);
      prefixes.put("Null", "Null");
      prefixes.put("Port-channel", "Port-channel");
      prefixes.put("Serial", "Serial");
      prefixes.put("TenGigabitEthernet", "TenGigabitEthernet");
      prefixes.put("te", "TenGigabitEthernet");
      prefixes.put("Tunnel", "Tunnel");
      prefixes.put("Vlan", "Vlan");
      return prefixes;
   }

   public static Ip getIp(Access_list_ip_rangeContext ctx) {
      if (ctx.ip != null) {
         return toIp(ctx.ip);
      }
      else if (ctx.prefix != null) {
         return getPrefixIp(ctx.prefix);
      }
      else {
         return new Ip(0l);
      }
   }

   public static int getPortNumber(PortContext ctx) {
      if (ctx.DEC() != null) {
         return toInteger(ctx.DEC());
      }
      else if (ctx.BOOTPC() != null) {
         return 68;
      }
      else if (ctx.BOOTPS() != null) {
         return 67;
      }
      else if (ctx.BGP() != null) {
         return 179;
      }
      else if (ctx.CMD() != null) {
         return 514;
      }
      else if (ctx.DOMAIN() != null) {
         return 53;
      }
      else if (ctx.EXEC() != null) {
         return 512;
      }
      else if (ctx.FTP() != null) {
         return 21;
      }
      else if (ctx.FTP_DATA() != null) {
         return 20;
      }
      else if (ctx.HOSTNAME() != null) {
         return 42;
      }
      else if (ctx.IDENT() != null) {
         return 113;
      }
      else if (ctx.ISAKMP() != null) {
         return 500;
      }
      else if (ctx.LPD() != null) {
         return 515;
      }
      else if (ctx.MLAG() != null) {
         return 6784;
      }
      else if (ctx.NETBIOS_DGM() != null) {
         return 138;
      }
      else if (ctx.NETBIOS_NS() != null) {
         return 137;
      }
      else if (ctx.NETBIOS_SS() != null) {
         return 139;
      }
      else if (ctx.NNTP() != null) {
         return 119;
      }
      else if (ctx.NON500_ISAKMP() != null) {
         return 4500;
      }
      else if (ctx.NTP() != null) {
         return 123;
      }
      else if (ctx.PIM_AUTO_RP() != null) {
         return 496;
      }
      else if (ctx.POP3() != null) {
         return 110;
      }
      else if (ctx.RIP() != null) {
         return 520;
      }
      else if (ctx.SMTP() != null) {
         return 25;
      }
      else if (ctx.SNMP() != null) {
         return 161;
      }
      else if (ctx.SNMPTRAP() != null) {
         return 162;
      }
      else if (ctx.SSH() != null) {
         return 22;
      }
      else if (ctx.SUNRPC() != null) {
         return 111;
      }
      else if (ctx.SYSLOG() != null) {
         return 514;
      }
      else if (ctx.TACACS() != null) {
         return 49;
      }
      else if (ctx.TELNET() != null) {
         return 23;
      }
      else if (ctx.TFTP() != null) {
         return 69;
      }
      else if (ctx.WHOIS() != null) {
         return 43;
      }
      else if (ctx.WWW() != null) {
         return 80;
      }
      else {
         throw new BatfishException("bad port");
      }
   }

   private static List<SubRange> getPortRanges(Port_specifierContext ps) {
      List<SubRange> ranges = new ArrayList<SubRange>();
      if (ps.EQ() != null) {
         for (PortContext pc : ps.args) {
            int port = getPortNumber(pc);
            ranges.add(new SubRange(port, port));
         }
      }
      else if (ps.GT() != null) {
         int port = getPortNumber(ps.arg);
         ranges.add(new SubRange(port + 1, 65535));
      }
      else if (ps.NEQ() != null) {
         int port = getPortNumber(ps.arg);
         SubRange beforeRange = new SubRange(0, port - 1);
         SubRange afterRange = new SubRange(port + 1, 65535);
         ranges.add(beforeRange);
         ranges.add(afterRange);
      }
      else if (ps.LT() != null) {
         int port = getPortNumber(ps.arg);
         ranges.add(new SubRange(0, port - 1));
      }
      else if (ps.RANGE() != null) {
         int lowPort = getPortNumber(ps.arg1);
         int highPort = getPortNumber(ps.arg2);
         ranges.add(new SubRange(lowPort, highPort));
      }
      else {
         throw new BatfishException("bad port range");
      }
      return ranges;
   }

   public static Ip getPrefixIp(Token ipPrefixToken) {
      if (ipPrefixToken.getType() != CiscoLexer.IP_PREFIX) {
         throw new BatfishException(
               "attempted to get prefix length from non-IP_PREFIX token: "
                     + ipPrefixToken.getType());
      }
      String text = ipPrefixToken.getText();
      String[] parts = text.split("/");
      String prefixIpStr = parts[0];
      Ip prefixIp = new Ip(prefixIpStr);
      return prefixIp;
   }

   public static int getPrefixLength(Token ipPrefixToken) {
      if (ipPrefixToken.getType() != CiscoLexer.IP_PREFIX) {
         throw new BatfishException(
               "attempted to get prefix length from non-IP_PREFIX token: "
                     + ipPrefixToken.getType());
      }
      String text = ipPrefixToken.getText();
      String[] parts = text.split("/");
      String prefixLengthStr = parts[1];
      int prefixLength = Integer.parseInt(prefixLengthStr);
      return prefixLength;
   }

   public static Ip getWildcard(Access_list_ip_rangeContext ctx) {
      if (ctx.wildcard != null) {
         return toIp(ctx.wildcard);
      }
      else if (ctx.ANY() != null) {
         return new Ip(0xFFFFFFFFl);
      }
      else if (ctx.HOST() != null) {
         return new Ip(0l);
      }
      else if (ctx.prefix != null) {
         int pfxLength = getPrefixLength(ctx.prefix);
         long ipAsLong = 0xFFFFFFFFl >>> pfxLength;
         return new Ip(ipAsLong);
      }
      else {
         throw new BatfishException("bad extended ip access list ip range");
      }
   }

   public static int toInteger(TerminalNode t) {
      return Integer.parseInt(t.getText());
   }

   public static int toInteger(Token t) {
      return Integer.parseInt(t.getText());
   }

   private static String toInterfaceName(Interface_nameContext ctx) {
      String canonicalNamePrefix = getCanonicalInterfaceNamePrefix(ctx.name_prefix_alpha
            .getText());
      String name = canonicalNamePrefix;
      for (Token part : ctx.name_middle_parts) {
         name += part.getText();
      }
      if (ctx.range().range_list.size() != 1) {
         throw new PedanticBatfishException(
               "got interface range where single interface was expected: \""
                     + ctx.getText() + "\"");
      }
      name += ctx.range().getText();
      return name;
   }

   public static Ip toIp(Token t) {
      return new Ip(t.getText());
   }

   public static IpProtocol toIpProtocol(ProtocolContext ctx) {
      if (ctx.DEC() != null) {
         int num = toInteger(ctx.DEC());
         return IpProtocol.fromNumber(num);
      }
      else if (ctx.AHP() != null) {
         return IpProtocol.AHP;
      }
      else if (ctx.EIGRP() != null) {
         return IpProtocol.EIGRP;
      }
      else if (ctx.ESP() != null) {
         return IpProtocol.ESP;
      }
      else if (ctx.GRE() != null) {
         return IpProtocol.GRE;
      }
      else if (ctx.ICMP() != null) {
         return IpProtocol.ICMP;
      }
      else if (ctx.IGMP() != null) {
         return IpProtocol.IGMP;
      }
      else if (ctx.IP() != null) {
         return IpProtocol.IP;
      }
      else if (ctx.IPINIP() != null) {
         return IpProtocol.IPINIP;
      }
      else if (ctx.OSPF() != null) {
         return IpProtocol.OSPF;
      }
      else if (ctx.PIM() != null) {
         return IpProtocol.PIM;
      }
      else if (ctx.SCTP() != null) {
         return IpProtocol.SCTP;
      }
      else if (ctx.TCP() != null) {
         return IpProtocol.TCP;
      }
      else if (ctx.UDP() != null) {
         return IpProtocol.UDP;
      }
      else if (ctx.VRRP() != null) {
         return IpProtocol.VRRP;
      }
      else {
         throw new BatfishException("missing token-protocol mapping");
      }
   }

   public static long toLong(CommunityContext ctx) {
      switch (ctx.com.getType()) {
      case CiscoLexer.COMMUNITY_NUMBER:
         String numberText = ctx.com.getText();
         String[] parts = numberText.split(":");
         String leftStr = parts[0];
         String rightStr = parts[1];
         long left = Long.parseLong(leftStr);
         long right = Long.parseLong(rightStr);
         return (left << 16) | right;

      case CiscoLexer.DEC:
         return toLong(ctx.com);

      case CiscoLexer.INTERNET:
         return 0l;

      case CiscoLexer.LOCAL_AS:
         return 0xFFFFFF03l;

      case CiscoLexer.NO_ADVERTISE:
         return 0xFFFFFF02l;

      case CiscoLexer.NO_EXPORT:
         return 0xFFFFFF01l;

      default:
         throw new BatfishException("bad community");
      }
   }

   public static long toLong(TerminalNode t) {
      return Long.parseLong(t.getText());
   }

   public static long toLong(Token t) {
      return Long.parseLong(t.getText());
   }

   public static List<SubRange> toRange(RangeContext ctx) {
      List<SubRange> range = new ArrayList<SubRange>();
      for (SubrangeContext sc : ctx.range_list) {
         SubRange sr = toSubRange(sc);
         range.add(sr);
      }
      return range;
   }

   private static SubRange toSubrange(As_path_regex_rangeContext ctx) {
      if (ctx.DEC() != null) {
         int as = toInteger(ctx.DEC());
         return new SubRange(as, as);
      }
      else if (ctx.PERIOD() != null) {
         return new SubRange(0, 65535);
      }
      else {
         throw new BatfishException("Invalid as path regex range");
      }
   }

   public static SubRange toSubRange(SubrangeContext ctx) {
      int low = toInteger(ctx.low);
      if (ctx.DASH() != null) {
         int high = toInteger(ctx.high);
         return new SubRange(low, high);
      }
      else {
         return new SubRange(low, low);
      }
   }

   private CiscoVendorConfiguration _configuration;

   private IpAsPathAccessList _currentAsPathAcl;

   private DynamicBgpPeerGroup _currentDynamicPeerGroup;

   private ExpandedCommunityList _currentExpandedCommunityList;

   private ExtendedAccessList _currentExtendedAcl;

   private List<Interface> _currentInterfaces;

   private IpBgpPeerGroup _currentIpPeerGroup;

   private Ipv6BgpPeerGroup _currentIpv6PeerGroup;

   private NamedBgpPeerGroup _currentNamedPeerGroup;

   private BgpPeerGroup _currentPeerGroup;

   private NamedBgpPeerGroup _currentPeerSession;

   private PrefixList _currentPrefixList;

   private RouteMap _currentRouteMap;

   private RouteMapClause _currentRouteMapClause;

   private StandardAccessList _currentStandardAcl;

   private StandardCommunityList _currentStandardCommunityList;

   private String _currentVrf;

   private BgpPeerGroup _dummyPeerGroup;

   private final BatfishCombinedParser<?, ?> _parser;

   private boolean _pedantic;

   private final Set<String> _rulesWithSuppressedWarnings;

   private final String _text;

   private final List<String> _warnings;

   public CiscoControlPlaneExtractor(String text,
         BatfishCombinedParser<?, ?> parser,
         Set<String> rulesWithSuppressedWarnings, boolean pedantic) {
      _text = text;
      _warnings = new ArrayList<String>();
      _parser = parser;
      _rulesWithSuppressedWarnings = rulesWithSuppressedWarnings;
      _pedantic = pedantic;
   }

   @Override
   public void enterCisco_configuration(Cisco_configurationContext ctx) {
      _configuration = new CiscoVendorConfiguration();
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void enterDescription_if_stanza(Description_if_stanzaContext ctx) {
      Token descriptionToken = ctx.description_line().text;
      String description = descriptionToken != null ? descriptionToken
            .getText().trim() : "";
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setDescription(description);
      }
   }

   @Override
   public void enterExtended_access_list_stanza(
         Extended_access_list_stanzaContext ctx) {
      String name;
      if (ctx.named != null) {
         name = ctx.named.name.getText();
      }
      else {
         name = ctx.numbered.name.getText();
      }
      _currentExtendedAcl = new ExtendedAccessList(name);
      _configuration.getExtendedAcls().put(name, _currentExtendedAcl);
   }

   @Override
   public void enterInterface_stanza(Interface_stanzaContext ctx) {
      String nameAlpha = ctx.iname.name_prefix_alpha.getText();
      String canonicalNamePrefix = getCanonicalInterfaceNamePrefix(nameAlpha);
      String vrf = canonicalNamePrefix.equals(NXOS_MANAGEMENT_INTERFACE_PREFIX) ? CiscoConfiguration.MANAGEMENT_VRF_NAME
            : CiscoConfiguration.MASTER_VRF_NAME;
      double bandwidth = Interface.getDefaultBandwidth(canonicalNamePrefix);
      String namePrefix = canonicalNamePrefix;
      for (Token part : ctx.iname.name_middle_parts) {
         namePrefix += part.getText();
      }
      _currentInterfaces = new ArrayList<Interface>();
      List<SubRange> ranges = toRange(ctx.iname.range());
      for (SubRange range : ranges) {
         for (int i = range.getStart(); i <= range.getEnd(); i++) {
            String name = namePrefix + i;
            Interface newInterface = _configuration.getInterfaces().get(name);
            if (newInterface == null) {
               newInterface = new Interface(name);
               _configuration.getInterfaces().put(name, newInterface);
            }
            else {
               _warnings.add("Interface: \"" + name
                     + "\" altered more than once");
            }
            _currentInterfaces.add(newInterface);
            newInterface.setBandwidth(bandwidth);
            newInterface.setVrf(vrf);
         }
      }
      if (ctx.MULTIPOINT() != null) {
         todo(ctx);
      }
   }

   @Override
   public void enterIp_as_path_access_list_stanza(
         Ip_as_path_access_list_stanzaContext ctx) {
      String name = ctx.numbered.name.getText();
      _currentAsPathAcl = new IpAsPathAccessList(name);
      _configuration.getAsPathAccessLists().put(name, _currentAsPathAcl);
   }

   @Override
   public void enterIp_community_list_expanded_stanza(
         Ip_community_list_expanded_stanzaContext ctx) {
      String name;
      if (ctx.numbered != null) {
         name = ctx.numbered.name.getText();
      }
      else {
         name = ctx.named.name.getText();
      }
      _currentExpandedCommunityList = new ExpandedCommunityList(name);
      _configuration.getExpandedCommunityLists().put(name,
            _currentExpandedCommunityList);
   }

   @Override
   public void enterIp_community_list_standard_stanza(
         Ip_community_list_standard_stanzaContext ctx) {
      String name;
      if (ctx.numbered != null) {
         name = ctx.numbered.name.getText();
      }
      else {
         name = ctx.named.name.getText();
      }
      _currentStandardCommunityList = new StandardCommunityList(name);
      _configuration.getStandardCommunityLists().put(name,
            _currentStandardCommunityList);
   }

   @Override
   public void enterIp_prefix_list_stanza(Ip_prefix_list_stanzaContext ctx) {

      boolean isIpV6 = (ctx.named.IPV6() != null);

      if (isIpV6) {
         todo(ctx, "IPV6 is not supported yet");
      }

      String name = ctx.named.name.getText();
      _currentPrefixList = new PrefixList(name, isIpV6);
      _configuration.getPrefixLists().put(name, _currentPrefixList);
   }

   @Override
   public void enterIp_route_stanza(Ip_route_stanzaContext ctx) {
      if (ctx.vrf != null) {
         _currentVrf = ctx.vrf.getText();
      }
   }

   @Override
   public void enterNeighbor_rb_stanza(Neighbor_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      // we must create peer group if it does not exist and this is a remote_as
      // declaration
      boolean create = ctx.remote_as_bgp_tail() != null
            || ctx.inherit_peer_session_bgp_tail() != null;
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         if (_currentIpPeerGroup == null) {
            if (create) {
               proc.addIpPeerGroup(ip);
               _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
            }
            else {
               String message = "reference to undeclared peer group: \""
                     + ip.toString() + "\"";
               pedantic(message);
               _currentPeerGroup = _dummyPeerGroup;
            }
         }
         _currentPeerGroup = _currentIpPeerGroup;
      }
      else if (ctx.ip6 != null) {
         todo(ctx, "Do not handle IPv6 yet");
         _currentIpv6PeerGroup = Ipv6BgpPeerGroup.INSTANCE;
         _currentPeerGroup = _currentIpv6PeerGroup;
      }
      else if (ctx.peergroup != null) {
         String name = ctx.peergroup.getText();
         _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
         _currentDynamicPeerGroup = proc.getDynamicPeerGroups().get(name);
         if (_currentDynamicPeerGroup == null) {
            if (_currentNamedPeerGroup == null) {
               if (create) {
                  proc.addNamedPeerGroup(name);
                  _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
               }
               else {
                  throw new BatfishException(
                        "reference to undeclared peer group: \"" + name + "\"");
               }
            }
            _currentPeerGroup = _currentNamedPeerGroup;
         }
         else {
            if (_currentNamedPeerGroup != null) {
               throw new BatfishException(
                     "There exist both a dynamic and named peer group named: \""
                           + name + "\"");
            }
            _currentPeerGroup = _currentDynamicPeerGroup;
         }
      }
      else {
         throw new BatfishException("unknown neighbor type");
      }
   }

   @Override
   public void enterNexus_access_list_stanza(Nexus_access_list_stanzaContext ctx) {

      boolean ipV6 = (ctx.IPV6() != null);

      if (ipV6) {
         todo(ctx, "Do not handle IPv6 yet");
      }

      String name = ctx.name.getText();

      _currentExtendedAcl = new ExtendedAccessList(name, ipV6);
      _configuration.getExtendedAcls().put(name, _currentExtendedAcl);
   }

   @Override
   public void enterNexus_neighbor_rb_stanza(Nexus_neighbor_rb_stanzaContext ctx) {
      if (ctx.ipv6_address != null || ctx.ipv6_prefix != null) {
         todo(ctx, "IPv6 is not supported yet");
         _currentIpv6PeerGroup = Ipv6BgpPeerGroup.INSTANCE;
         _currentPeerGroup = _currentIpv6PeerGroup;
         return;
      }
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip_address != null) {
         Ip ip = toIp(ctx.ip_address);
         _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         if (_currentIpPeerGroup == null) {
            proc.addIpPeerGroup(ip);
            _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         }
         _currentPeerGroup = _currentIpPeerGroup;
      }
      if (ctx.ip_prefix != null) {
         Ip ip = getPrefixIp(ctx.ip_prefix);
         int prefixLength = getPrefixLength(ctx.ip_prefix);
         Prefix prefix = new Prefix(ip, prefixLength);
         _currentDynamicPeerGroup = proc.getDynamicPeerGroups().get(prefix);
         if (_currentDynamicPeerGroup == null) {
            _currentDynamicPeerGroup = proc.addDynamicPeerGroup(prefix);
         }
         _currentPeerGroup = _currentDynamicPeerGroup;
      }
      if (ctx.REMOTE_AS() != null) {
         int remoteAs = toInteger(ctx.asnum);
         _currentPeerGroup.setRemoteAS(remoteAs);
      }
      // TODO: verify if this is correct for nexus
      _currentPeerGroup.setActive(true);
      _currentPeerGroup.setShutdown(false);
   }

   @Override
   public void enterNexus_prefix_list_stanza(Nexus_prefix_list_stanzaContext ctx) {
      boolean isIpv6 = ctx.IPV6() != null;
      String name = ctx.name.getText();
      _currentPrefixList = new PrefixList(name, isIpv6);
   }

   @Override
   public void enterNexus_vrf_rb_stanza(Nexus_vrf_rb_stanzaContext ctx) {
      _currentVrf = ctx.name.getText();
      // BgpProcess masterProc =
      // _configuration.getBgpProcesses().get(BgpProcess.MASTER_VRF_NAME);
      BgpProcess proc = new BgpProcess(0); // TODO: fix vrf bgp process number
      _configuration.getBgpProcesses().put(_currentVrf, proc);
   }

   @Override
   public void enterRoute_map_stanza(Route_map_stanzaContext ctx) {
      String name = ctx.named.name.getText();
      _currentRouteMap = new RouteMap(name);
      _configuration.getRouteMaps().put(name, _currentRouteMap);
   }

   @Override
   public void enterRoute_map_tail(Route_map_tailContext ctx) {
      int num = toInteger(ctx.num);
      LineAction action = getAccessListAction(ctx.rmt);
      _currentRouteMapClause = new RouteMapClause(action,
            _currentRouteMap.getMapName(), num);
      Map<Integer, RouteMapClause> clauses = _currentRouteMap.getClauses();
      if (clauses.containsKey(num)) {
         throw new BatfishException("Route map '"
               + _currentRouteMap.getMapName()
               + "' already contains clause numbered '" + num + "'");
      }
      else {
         clauses.put(num, _currentRouteMapClause);
      }
   }

   @Override
   public void enterRouter_bgp_stanza(Router_bgp_stanzaContext ctx) {
      int procNum = toInteger(ctx.procnum);
      BgpProcess proc = new BgpProcess(procNum);
      _configuration.getBgpProcesses().put(_currentVrf, proc);
      _currentPeerGroup = proc.getMasterBgpPeerGroup();
      _dummyPeerGroup = new MasterBgpPeerGroup();
   }

   @Override
   public void enterRouter_ospf_stanza(Router_ospf_stanzaContext ctx) {
      int procNum = toInteger(ctx.procnum);
      OspfProcess proc = new OspfProcess(procNum);
      _configuration.setOspfProcess(proc);
   }

   @Override
   public void enterRouter_rip_stanza(Router_rip_stanzaContext ctx) {
      todo(ctx);
   }

   @Override
   public void enterStandard_access_list_stanza(
         Standard_access_list_stanzaContext ctx) {
      String name;
      if (ctx.named != null) {
         name = ctx.named.name.getText();
      }
      else {
         name = ctx.numbered.name.getText();
      }
      _currentStandardAcl = new StandardAccessList(name);
      _configuration.getStandardAcls().put(name, _currentStandardAcl);
   }

   @Override
   public void enterTemplate_peer_rb_stanza(Template_peer_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      if (_currentNamedPeerGroup == null) {
         proc.addNamedPeerGroup(name);
         _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      }
      _currentPeerGroup = _currentNamedPeerGroup;
   }

   @Override
   public void enterTemplate_peer_session_rb_stanza(
         Template_peer_session_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      _currentPeerSession = proc.getPeerSessions().get(name);
      if (_currentPeerSession == null) {
         proc.addPeerSession(name);
         _currentPeerSession = proc.getPeerSessions().get(name);
      }
      _currentPeerGroup = _currentPeerSession;
   }

   @Override
   public void enterVrf_context_stanza(Vrf_context_stanzaContext ctx) {
      _currentVrf = ctx.name.getText();
   }

   @Override
   public void exitActivate_bgp_tail(Activate_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         _currentPeerGroup.setActive(true);
      }
      else {
         throw new BatfishException(
               "no peer or peer group to activate in this context");
      }
   }

   @Override
   public void exitAggregate_address_bgp_tail(
         Aggregate_address_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         throw new BatfishException(
               "unexpected occurrence in peer group/neighbor context");
      }
      boolean summaryOnly = ctx.SUMMARY_ONLY() != null;
      boolean asSet = ctx.AS_SET() != null;
      if (ctx.network != null || ctx.prefix != null) {
         // ipv4
         Prefix prefix;
         if (ctx.network != null) {
            Ip network = toIp(ctx.network);
            Ip subnet = toIp(ctx.subnet);
            int prefixLength = subnet.numSubnetBits();
            prefix = new Prefix(network, prefixLength);
         }
         else {
            // ctx.prefix != null
            prefix = new Prefix(ctx.prefix.getText());
         }
         BgpAggregateNetwork net = new BgpAggregateNetwork(prefix);
         net.setAsSet(asSet);
         net.setSummaryOnly(summaryOnly);
         proc.getAggregateNetworks().put(prefix, net);
      }
      else if (ctx.ipv6_prefix != null) {
         todo(ctx);
      }
   }

   @Override
   public void exitAllowas_in_bgp_tail(Allowas_in_bgp_tailContext ctx) {
      _currentPeerGroup.SetAllowAsIn(true);
      if (ctx.num != null) {
         todo(ctx, "allowas-in number ignored and effectively infinite for now");
      }
   }

   @Override
   public void exitAlways_compare_med_rb_stanza(
         Always_compare_med_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      proc.setAlwaysCompareMed(true);
   }

   @Override
   public void exitArea_nssa_ro_stanza(Area_nssa_ro_stanzaContext ctx) {
      OspfProcess proc = _configuration.getOspfProcess();
      int area = (ctx.area_int != null) ? toInteger(ctx.area_int) : (int) toIp(
            ctx.area_ip).asLong();
      boolean noSummary = ctx.NO_SUMMARY() != null;
      boolean defaultOriginate = ctx.DEFAULT_INFORMATION_ORIGINATE() != null;
      if (defaultOriginate) {
         // TODO: implement
         todo(ctx);
      }
      proc.getNssas().put(area, noSummary);
   }

   @Override
   public void exitAuto_summary_bgp_tail(Auto_summary_bgp_tailContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitBgp_listen_range_rb_stanza(
         Bgp_listen_range_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.IP_PREFIX() != null) {
         Ip ip = getPrefixIp(ctx.IP_PREFIX().getSymbol());
         int prefixLength = getPrefixLength(ctx.IP_PREFIX().getSymbol());
         Prefix prefix = new Prefix(ip, prefixLength);
         DynamicBgpPeerGroup pg = proc.addDynamicPeerGroup(prefix);
         NamedBgpPeerGroup namedGroup = proc.getNamedPeerGroups().get(name);
         if (namedGroup == null) {
            proc.addNamedPeerGroup(name);
            namedGroup = proc.getNamedPeerGroups().get(name);
         }
         namedGroup.addNeighborPrefix(prefix);
         if (ctx.as != null) {
            int remoteAs = toInteger(ctx.as);
            pg.setRemoteAS(remoteAs);
         }
      }
      else if (ctx.IPV6_PREFIX() != null) {
         todo(ctx, "Ipv6 not yet implemented");
      }
   }

   @Override
   public void exitCluster_id_bgp_tail(Cluster_id_bgp_tailContext ctx) {
      Ip clusterId = toIp(ctx.id);
      _currentPeerGroup.setClusterId(clusterId);
   }

   @Override
   public void exitDefault_information_ro_stanza(
         Default_information_ro_stanzaContext ctx) {
      OspfProcess proc = _configuration.getOspfProcess();
      proc.setDefaultInformationOriginate(true);
      boolean always = ctx.ALWAYS().size() > 0;
      proc.setDefaultInformationOriginateAlways(always);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         proc.setDefaultInformationMetric(metric);
      }
      if (ctx.metric_type != null) {
         int metricTypeInt = toInteger(ctx.metric_type);
         OspfMetricType metricType = OspfMetricType.fromInteger(metricTypeInt);
         proc.setDefaultInformationMetricType(metricType);
      }
      if (ctx.map != null) {
         proc.setDefaultInformationOriginateMap(ctx.map.getText());
      }
   }

   @Override
   public void exitDefault_metric_bgp_tail(Default_metric_bgp_tailContext ctx) {
      int metric = toInteger(ctx.metric);
      _currentPeerGroup.setDefaultMetric(metric);
   }

   @Override
   public void exitDefault_originate_bgp_tail(
         Default_originate_bgp_tailContext ctx) {
      String mapName = ctx.map != null ? ctx.map.getText() : null;
      if (_currentIpv6PeerGroup != null) {
         todo(ctx, "IPv6 not supported yet");
         return;
      }
      else {
         _currentPeerGroup.setDefaultOriginate(true);
         _currentPeerGroup.setDefaultOriginateMap(mapName);
      }
   }

   @Override
   public void exitDefault_shutdown_bgp_tail(
         Default_shutdown_bgp_tailContext ctx) {
      _currentPeerGroup.setShutdown(true);
   }

   @Override
   public void exitDistribute_list_bgp_tail(Distribute_list_bgp_tailContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitEbgp_multihop_bgp_tail(Ebgp_multihop_bgp_tailContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitExtended_access_list_stanza(
         Extended_access_list_stanzaContext ctx) {
      _currentExtendedAcl = null;
   }

   @Override
   public void exitExtended_access_list_tail(
         Extended_access_list_tailContext ctx) {

      if (_currentExtendedAcl.isIpV6()) {
         return;
      }

      LineAction action = getAccessListAction(ctx.ala);
      IpProtocol protocol = toIpProtocol(ctx.prot);
      Ip srcIp = getIp(ctx.srcipr);
      Ip srcWildcard = getWildcard(ctx.srcipr);
      Ip dstIp = getIp(ctx.dstipr);
      Ip dstWildcard = getWildcard(ctx.dstipr);
      List<SubRange> srcPortRanges = ctx.alps_src != null ? getPortRanges(ctx.alps_src)
            : Collections.<SubRange> emptyList();
      List<SubRange> dstPortRanges = ctx.alps_dst != null ? getPortRanges(ctx.alps_dst)
            : Collections.<SubRange> emptyList();
      ExtendedAccessListLine line = new ExtendedAccessListLine(action,
            protocol, srcIp, srcWildcard, dstIp, dstWildcard, srcPortRanges,
            dstPortRanges);
      _currentExtendedAcl.addLine(line);
   }

   @Override
   public void exitHostname_stanza(Hostname_stanzaContext ctx) {
      _configuration.setHostname(ctx.name.getText());
   }

   @Override
   public void exitInherit_peer_session_bgp_tail(
         Inherit_peer_session_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      String groupName = ctx.name.getText();
      if (_currentIpPeerGroup != null) {
         _currentIpPeerGroup.setGroupName(groupName);
      }
      else if (_currentNamedPeerGroup != null) {
         _currentNamedPeerGroup.setPeerSession(groupName);
      }
      else if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         throw new BatfishException("Invalid peer context for inheritance");
      }
      else {
         todo(ctx, "inheritance not implemented for this peer type");
      }
   }

   @Override
   public void exitInterface_stanza(Interface_stanzaContext ctx) {
      _currentInterfaces = null;
   }

   @Override
   public void exitIp_access_group_if_stanza(
         Ip_access_group_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      if (ctx.IN() != null) {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setIncomingFilter(name);
         }
      }
      else if (ctx.OUT() != null) {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setOutgoingFilter(name);
         }
      }
      else {
         throw new BatfishException("bad direction");
      }
   }

   @Override
   public void exitIp_address_if_stanza(Ip_address_if_stanzaContext ctx) {
      Ip address;
      Ip mask;
      if (ctx.prefix != null) {
         address = getPrefixIp(ctx.prefix);
         int prefixLength = getPrefixLength(ctx.prefix);
         long maskLong = Util.numSubnetBitsToSubnetLong(prefixLength);
         mask = new Ip(maskLong);
      }
      else {
         address = new Ip(ctx.ip.getText());
         mask = new Ip(ctx.subnet.getText());
      }
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setIp(address);
         currentInterface.setSubnetMask(mask);
      }
   }

   @Override
   public void exitIp_address_secondary_if_stanza(
         Ip_address_secondary_if_stanzaContext ctx) {
      Ip address;
      Ip mask;
      Prefix prefix;
      if (ctx.prefix != null) {
         prefix = new Prefix(ctx.prefix.getText());
      }
      else {
         address = new Ip(ctx.ip.getText());
         mask = new Ip(ctx.subnet.getText());
         prefix = new Prefix(address, mask.numSubnetBits());
      }
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.getSecondaryPrefixes().add(prefix);
      }
   }

   @Override
   public void exitIp_as_path_access_list_stanza(
         Ip_as_path_access_list_stanzaContext ctx) {
      _currentAsPathAcl = null;
   }

   @Override
   public void exitIp_as_path_access_list_tail(
         Ip_as_path_access_list_tailContext ctx) {
      LineAction action = getAccessListAction(ctx.action);
      As_path_regexContext asPath = ctx.as_path_regex();
      if (asPath == null) {
         // not an as-path we can use right now
         return;
      }
      IpAsPathAccessListLine line = new IpAsPathAccessListLine(action);
      boolean atBeginning = asPath.CARAT() != null;
      boolean matchEmpty = asPath.ranges.size() == asPath.ASTERISK().size();
      line.setAtBeginning(atBeginning);
      line.setMatchEmpty(matchEmpty);
      switch (asPath.ranges.size()) {
      case 0:
         break;

      case 2:
         As_path_regex_rangeContext range2ctx = asPath.ranges.get(1);
         SubRange asRange2 = toSubrange(range2ctx);
         line.setAs2Range(asRange2);
      case 1:
         As_path_regex_rangeContext range1ctx = asPath.ranges.get(0);
         SubRange asRange1 = toSubrange(range1ctx);
         line.setAs1Range(asRange1);
         break;

      default:
         throw new BatfishException(
               "Do not currently support more than two AS'es in Cisco as-path regexes");
      }
      _currentAsPathAcl.addLine(line);
   }

   @Override
   public void exitIp_community_list_expanded_stanza(
         Ip_community_list_expanded_stanzaContext ctx) {
      _currentExpandedCommunityList = null;
   }

   @Override
   public void exitIp_community_list_expanded_tail(
         Ip_community_list_expanded_tailContext ctx) {
      LineAction action = getAccessListAction(ctx.ala);
      String regex = "";
      for (Token remainder : ctx.remainder) {
         regex += remainder.getText();
      }
      ExpandedCommunityListLine line = new ExpandedCommunityListLine(action,
            regex);
      _currentExpandedCommunityList.addLine(line);
   }

   @Override
   public void exitIp_community_list_standard_stanza(
         Ip_community_list_standard_stanzaContext ctx) {
      _currentStandardCommunityList = null;
   }

   @Override
   public void exitIp_default_gateway_stanza(
         Ip_default_gateway_stanzaContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitIp_ospf_cost_if_stanza(Ip_ospf_cost_if_stanzaContext ctx) {
      int cost = toInteger(ctx.cost);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOspfCost(cost);
      }
   }

   @Override
   public void exitIp_ospf_dead_interval_if_stanza(
         Ip_ospf_dead_interval_if_stanzaContext ctx) {
      int seconds = toInteger(ctx.seconds);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOSPFDeadInterval(seconds);
         currentInterface.setOSPFHelloMultiplier(0);
      }
   }

   @Override
   public void exitIp_ospf_dead_interval_minimal_if_stanza(
         Ip_ospf_dead_interval_minimal_if_stanzaContext ctx) {
      int multiplier = toInteger(ctx.mult);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOSPFDeadInterval(1);
         currentInterface.setOSPFHelloMultiplier(multiplier);
      }
   }

   @Override
   public void exitIp_policy_if_stanza(Ip_policy_if_stanzaContext ctx) {
      String policyName = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setRoutingPolicy(policyName);
      }
   }

   @Override
   public void exitIp_prefix_list_stanza(Ip_prefix_list_stanzaContext ctx) {
      _currentPrefixList = null;
   }

   @Override
   public void exitIp_prefix_list_tail(Ip_prefix_list_tailContext ctx) {
      /*
       * if (ctx.seqnum != null) { int seqnum = toInteger(ctx.seqnum); }
       */

      if (_currentPrefixList.isIpV6()) {
         return;
      }

      LineAction action = getAccessListAction(ctx.action);
      Prefix prefix = new Prefix(ctx.prefix.getText());
      int prefixLength = prefix.getPrefixLength();
      int minLen = prefixLength;
      int maxLen = prefixLength;
      if (ctx.minpl != null) {
         minLen = toInteger(ctx.minpl);
         maxLen = 32;
      }
      if (ctx.maxpl != null) {
         maxLen = toInteger(ctx.maxpl);
      }
      if (ctx.eqpl != null) {
         minLen = toInteger(ctx.eqpl);
         maxLen = toInteger(ctx.eqpl);
      }
      SubRange lengthRange = new SubRange(minLen, maxLen);
      PrefixListLine line = new PrefixListLine(action, prefix, lengthRange);
      _currentPrefixList.addLine(line);
   }

   @Override
   public void exitIp_route_stanza(Ip_route_stanzaContext ctx) {
      if (ctx.vrf != null) {
         _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
      }
   }

   @Override
   public void exitIp_route_tail(Ip_route_tailContext ctx) {
      if (!_currentVrf.equals(CiscoConfiguration.MASTER_VRF_NAME)) {
         todo(ctx, "vrfs not implemented yet");
         return;
      }
      Prefix prefix;
      if (ctx.prefix != null) {
         prefix = new Prefix(ctx.prefix.getText());
      }
      else {
         Ip address = toIp(ctx.address);
         Ip mask = toIp(ctx.mask);
         int prefixLength = mask.numSubnetBits();
         prefix = new Prefix(address, prefixLength);
      }
      Ip nextHopIp = null;
      String nextHopInterface = null;
      int distance = DEFAULT_STATIC_ROUTE_DISTANCE;
      Integer tag = null;
      Integer track = null;
      boolean permanent = ctx.perm != null;
      if (ctx.nexthopip != null) {
         nextHopIp = toIp(ctx.nexthopip);
      }
      if (ctx.nexthopint != null) {
         nextHopInterface = ctx.nexthopint.getText();
      }
      if (ctx.distance != null) {
         distance = toInteger(ctx.distance);
      }
      if (ctx.tag != null) {
         tag = toInteger(ctx.tag);
      }
      if (ctx.track != null) {
         track = toInteger(ctx.track);
      }
      StaticRoute route = new StaticRoute(prefix, nextHopIp, nextHopInterface,
            distance, tag, track, permanent);
      _configuration.getStaticRoutes().add(route);
   }

   @Override
   public void exitIp_route_vrfc_stanza(Ip_route_vrfc_stanzaContext ctx) {
      todo(ctx, "vrfs not implemented yet");
   }

   @Override
   public void exitMatch_as_path_access_list_rm_stanza(
         Match_as_path_access_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (VariableContext name : ctx.name_list) {
         names.add(name.getText());
      }
      RouteMapMatchAsPathAccessListLine line = new RouteMapMatchAsPathAccessListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_community_list_rm_stanza(
         Match_community_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (Token t : ctx.name_list) {
         names.add(t.getText());
      }
      RouteMapMatchCommunityListLine line = new RouteMapMatchCommunityListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ip_access_list_rm_stanza(
         Match_ip_access_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (Token t : ctx.name_list) {
         names.add(t.getText());
      }
      RouteMapMatchIpAccessListLine line = new RouteMapMatchIpAccessListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ip_prefix_list_rm_stanza(
         Match_ip_prefix_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (Token t : ctx.name_list) {
         names.add(t.getText());
      }
      RouteMapMatchIpPrefixListLine line = new RouteMapMatchIpPrefixListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ipv6_rm_stanza(Match_ipv6_rm_stanzaContext ctx) {
      _currentRouteMap.setIgnore(true);
   }

   @Override
   public void exitMatch_tag_rm_stanza(Match_tag_rm_stanzaContext ctx) {
      Set<Integer> tags = new TreeSet<Integer>();
      for (Token t : ctx.tag_list) {
         tags.add(toInteger(t));
      }
      RouteMapMatchTagLine line = new RouteMapMatchTagLine(tags);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMaximum_paths_ro_stanza(Maximum_paths_ro_stanzaContext ctx) {
      todo(ctx);
      /*
       * Note that this is very difficult to enforce, and may not help the
       * analysis without major changes
       */
   }

   @Override
   public void exitMaximum_peers_bgp_tail(Maximum_peers_bgp_tailContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitNeighbor_rb_stanza(Neighbor_rb_stanzaContext ctx) {
      _currentDynamicPeerGroup = null;
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      _currentPeerGroup = _configuration.getBgpProcesses().get(_currentVrf)
            .getMasterBgpPeerGroup();
   }

   @Override
   public void exitNetwork_bgp_tail(Network_bgp_tailContext ctx) {
      if (ctx.mapname != null) {
         todo(ctx);
      }
      else {
         Prefix prefix;

         if (ctx.prefix != null) {
            prefix = new Prefix(ctx.prefix.getText());
         }
         else {
            Ip address = toIp(ctx.ip);
            Ip mask = (ctx.mask != null) ? toIp(ctx.mask) : address
                  .getClassMask();
            int prefixLength = mask.numSubnetBits();
            prefix = new Prefix(address, prefixLength);
         }
         _configuration.getBgpProcesses().get(_currentVrf).getNetworks()
               .add(prefix);
      }
   }

   @Override
   public void exitNetwork_ro_stanza(Network_ro_stanzaContext ctx) {
      Ip prefix = toIp(ctx.ip);
      Ip wildcard = toIp(ctx.wildcard);
      long area;
      if (ctx.area_int != null) {
         area = toLong(ctx.area_int);
      }
      else if (ctx.area_ip != null) {
         area = toIp(ctx.area_ip).asLong();
      }
      else {
         throw new BatfishException("bad area");
      }
      OspfWildcardNetwork network = new OspfWildcardNetwork(prefix, wildcard,
            area);
      _configuration.getOspfProcess().getWildcardNetworks().add(network);
   }

   @Override
   public void exitNext_hop_self_bgp_tail(Next_hop_self_bgp_tailContext ctx) {
      todo(ctx);
      // note that this rule matches "no next-hop-self"
   }

   @Override
   public void exitNexus_neighbor_inherit(Nexus_neighbor_inheritContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      String groupName = ctx.name.getText();
      if (_currentIpPeerGroup != null) {
         _currentIpPeerGroup.setGroupName(groupName);
      }
      else if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         throw new BatfishException("Invalid peer context for inheritance");
      }
      else {
         todo(ctx, "inheritance not implemented for this peer type");
      }
   }

   @Override
   public void exitNexus_neighbor_rb_stanza(Nexus_neighbor_rb_stanzaContext ctx) {
      _currentDynamicPeerGroup = null;
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      _currentPeerGroup = _configuration.getBgpProcesses().get(_currentVrf)
            .getMasterBgpPeerGroup();
   }

   @Override
   public void exitNexus_prefix_list_stanza(Nexus_prefix_list_stanzaContext ctx) {
      _currentPrefixList = null;
   }

   @Override
   public void exitNexus_vrf_rb_stanza(Nexus_vrf_rb_stanzaContext ctx) {
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void exitNo_neighbor_activate_rb_stanza(
         No_neighbor_activate_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         IpBgpPeerGroup pg = proc.getIpPeerGroups().get(ip);
         if (pg == null) {
            String message = "reference to undefined ip peer group: "
                  + ip.toString();
            pedantic(message);
         }
         else {
            pg.setActive(false);
         }
      }
      else if (ctx.ip6 != null) {
         todo(ctx, "IPv6 not supported yet");
      }
      else if (ctx.peergroup != null) {
         throw new BatfishException("deactivating peer group unsupported");
      }
   }

   @Override
   public void exitNo_neighbor_shutdown_rb_stanza(
         No_neighbor_shutdown_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         IpBgpPeerGroup pg = proc.getIpPeerGroups().get(ip);
         // TODO: see if it is always ok to set active on 'no shutdown'
         if (pg == null) {
            String message = "reference to undefined ip peer group: "
                  + ip.toString();
            if (_pedantic) {
               throw new BatfishException(message);
            }
            else {
               _warnings.add(message);
            }
         }
         else {
            pg.setActive(true);
            pg.setShutdown(false);
         }
      }
      else if (ctx.ip6 != null) {
         todo(ctx, "IPv6 not supported yet");
      }
      else if (ctx.peergroup != null) {
         throw new BatfishException("'no shutdown' of  peer group unsupported");
      }
   }

   @Override
   public void exitNo_redistribute_connected_rb_stanza(
         No_redistribute_connected_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
      RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
      proc.getRedistributionPolicies().remove(sourceProtocol);
   }

   @Override
   public void exitNo_shutdown_rb_stanza(No_shutdown_rb_stanzaContext ctx) {
      // TODO: see if it is always ok to set active on 'no shutdown'
      _currentPeerGroup.setShutdown(false);
      _currentPeerGroup.setActive(true);
   }

   @Override
   public void exitNull_as_path_regex(Null_as_path_regexContext ctx) {
      pedantic("as-path regexes this complicated are not supported yet");
   }

   @Override
   public void exitPassive_interface_default_ro_stanza(
         Passive_interface_default_ro_stanzaContext ctx) {
      _configuration.getOspfProcess().setPassiveInterfaceDefault(true);
   }

   @Override
   public void exitPassive_interface_ro_stanza(
         Passive_interface_ro_stanzaContext ctx) {
      boolean passive = ctx.NO() == null;
      String iname = ctx.i.getText();
      OspfProcess proc = _configuration.getOspfProcess();
      if (passive) {
         proc.getInterfaceBlacklist().add(iname);
      }
      else {
         proc.getInterfaceWhitelist().add(iname);
      }
   }

   @Override
   public void exitPeer_group_assignment_rb_stanza(
         Peer_group_assignment_rb_stanzaContext ctx) {
      if (ctx.address != null) {
         Ip address = toIp(ctx.address);
         String peerGroupName = ctx.name.getText();
         _configuration.getBgpProcesses().get(_currentVrf)
               .addPeerGroupMember(address, peerGroupName);
      }
      else if (ctx.address6 != null) {
         todo(ctx);
      }
   }

   @Override
   public void exitPeer_group_creation_rb_stanza(
         Peer_group_creation_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (proc.getNamedPeerGroups().get(name) == null) {
         proc.addNamedPeerGroup(name);
      }
   }

   @Override
   public void exitPrefix_list_bgp_tail(Prefix_list_bgp_tailContext ctx) {
      if (_currentIpv6PeerGroup != null) {
         todo(ctx, "Ipv6 not supported yet");
      }
      else {
         String listName = ctx.list_name.getText();
         if (ctx.IN() != null) {
            _currentPeerGroup.setInboundPrefixList(listName);
         }
         else if (ctx.OUT() != null) {
            _currentPeerGroup.setOutboundPrefixList(listName);
         }
         else {
            throw new BatfishException("bad direction");
         }
      }
   }

   @Override
   public void exitRedistribute_aggregate_bgp_tail(
         Redistribute_aggregate_bgp_tailContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitRedistribute_bgp_ro_stanza(
         Redistribute_bgp_ro_stanzaContext ctx) {
      OspfProcess proc = _configuration.getOspfProcess();
      RoutingProtocol sourceProtocol = RoutingProtocol.BGP;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      int as = toInteger(ctx.as);
      r.getSpecialAttributes().put(OspfRedistributionPolicy.BGP_AS, as);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRedistribute_connected_bgp_tail(
         Redistribute_connected_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
      RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
      BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
   }

   @Override
   public void exitRedistribute_connected_ro_stanza(
         Redistribute_connected_ro_stanzaContext ctx) {
      OspfProcess proc = _configuration.getOspfProcess();
      RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRedistribute_ospf_bgp_tail(
         Redistribute_ospf_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
      RoutingProtocol sourceProtocol = RoutingProtocol.OSPF;
      BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      int procNum = toInteger(ctx.procnum);
      r.getSpecialAttributes().put(BgpRedistributionPolicy.OSPF_PROCESS_NUMBER,
            procNum);
   }

   @Override
   public void exitRedistribute_rip_ro_stanza(
         Redistribute_rip_ro_stanzaContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitRedistribute_static_bgp_tail(
         Redistribute_static_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
      RoutingProtocol sourceProtocol = RoutingProtocol.STATIC;
      BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
   }

   @Override
   public void exitRedistribute_static_ro_stanza(
         Redistribute_static_ro_stanzaContext ctx) {
      OspfProcess proc = _configuration.getOspfProcess();
      RoutingProtocol sourceProtocol = RoutingProtocol.STATIC;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRemote_as_bgp_tail(Remote_as_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      int as = toInteger(ctx.as);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         _currentPeerGroup.setRemoteAS(as);
      }
      else {
         throw new BatfishException("no peer or peer group in context");
      }
   }

   @Override
   public void exitRemove_private_as_bgp_tail(
         Remove_private_as_bgp_tailContext ctx) {
      _currentPeerGroup.setRemovePrivateAs(true);
   }

   @Override
   public void exitRoute_map_bgp_tail(Route_map_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      String mapName = ctx.name.getText();
      if (ctx.IN() != null) {
         _currentPeerGroup.setInboundRouteMap(mapName);
      }
      else if (ctx.OUT() != null) {
         _currentPeerGroup.setOutboundRouteMap(mapName);
      }
      else {
         throw new BatfishException("bad direction");
      }
   }

   @Override
   public void exitRoute_map_stanza(Route_map_stanzaContext ctx) {
      _currentRouteMap = null;
   }

   @Override
   public void exitRoute_map_tail(Route_map_tailContext ctx) {
      _currentRouteMapClause = null;
   }

   @Override
   public void exitRoute_reflector_client_bgp_tail(
         Route_reflector_client_bgp_tailContext ctx) {
      _currentPeerGroup.setRouteReflectorClient(true);
   }

   @Override
   public void exitRouter_bgp_stanza(Router_bgp_stanzaContext ctx) {
      _currentPeerGroup = null;
   }

   @Override
   public void exitRouter_id_bgp_tail(Router_id_bgp_tailContext ctx) {
      Ip routerId = toIp(ctx.routerid);
      _configuration.getBgpProcesses().get(_currentVrf).setRouterId(routerId);
   }

   @Override
   public void exitRouter_id_ro_stanza(Router_id_ro_stanzaContext ctx) {
      Ip routerId = toIp(ctx.ip);
      _configuration.getOspfProcess().setRouterId(routerId);
   }

   @Override
   public void exitRouter_ospf_stanza(Router_ospf_stanzaContext ctx) {
      _configuration.getOspfProcess().computeNetworks(
            _configuration.getInterfaces().values());
   }

   @Override
   public void exitSend_community_bgp_tail(Send_community_bgp_tailContext ctx) {
      _currentPeerGroup.setSendCommunity(true);
   }

   @Override
   public void exitSet_as_path_prepend_rm_stanza(
         Set_as_path_prepend_rm_stanzaContext ctx) {
      List<Integer> asList = new ArrayList<Integer>();
      for (Token t : ctx.as_list) {
         int as = toInteger(t);
         asList.add(as);
      }
      RouteMapSetAsPathPrependLine line = new RouteMapSetAsPathPrependLine(
            asList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_comm_list_delete_rm_stanza(
         Set_comm_list_delete_rm_stanzaContext ctx) {
      String name = ctx.name.getText();
      RouteMapSetDeleteCommunityLine line = new RouteMapSetDeleteCommunityLine(
            name);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_additive_rm_stanza(
         Set_community_additive_rm_stanzaContext ctx) {
      List<Long> commList = new ArrayList<Long>();
      for (CommunityContext c : ctx.comm_list) {
         long community = toLong(c);
         commList.add(community);
      }
      RouteMapSetAdditiveCommunityLine line = new RouteMapSetAdditiveCommunityLine(
            commList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_none_rm_stanza(
         Set_community_none_rm_stanzaContext ctx) {
      RouteMapSetCommunityNoneLine line = new RouteMapSetCommunityNoneLine();
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_rm_stanza(Set_community_rm_stanzaContext ctx) {
      List<Long> commList = new ArrayList<Long>();
      for (CommunityContext c : ctx.comm_list) {
         long community = toLong(c);
         commList.add(community);
      }
      RouteMapSetCommunityLine line = new RouteMapSetCommunityLine(commList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_ipv6_rm_stanza(Set_ipv6_rm_stanzaContext ctx) {
      _currentRouteMap.setIgnore(true);
   }

   @Override
   public void exitSet_local_preference_rm_stanza(
         Set_local_preference_rm_stanzaContext ctx) {
      int localPreference = toInteger(ctx.pref);
      RouteMapSetLocalPreferenceLine line = new RouteMapSetLocalPreferenceLine(
            localPreference);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_metric_rm_stanza(Set_metric_rm_stanzaContext ctx) {
      int metric = toInteger(ctx.metric);
      RouteMapSetMetricLine line = new RouteMapSetMetricLine(metric);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_metric_type_rm_stanza(
         Set_metric_type_rm_stanzaContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitSet_next_hop_rm_stanza(Set_next_hop_rm_stanzaContext ctx) {
      Set<Ip> nextHops = new TreeSet<Ip>();
      for (Token t : ctx.nexthop_list) {
         Ip nextHop = toIp(t);
         nextHops.add(nextHop);
      }
      RouteMapSetNextHopLine line = new RouteMapSetNextHopLine(nextHops);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_origin_rm_stanza(Set_origin_rm_stanzaContext ctx) {
      OriginType originType;
      Integer asNum = null;
      if (ctx.IGP() != null) {
         originType = OriginType.IGP;
      }
      else if (ctx.INCOMPLETE() != null) {
         originType = OriginType.INCOMPLETE;
      }
      else if (ctx.as != null) {
         asNum = toInteger(ctx.as);
         originType = OriginType.EGP;
      }
      else {
         throw new BatfishException("bad origin type");
      }
      RouteMapSetLine line = new RouteMapSetOriginTypeLine(originType, asNum);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitShutdown_bgp_tail(Shutdown_bgp_tailContext ctx) {
      _currentPeerGroup.setShutdown(true);
   }

   @Override
   public void exitShutdown_if_stanza(Shutdown_if_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setActive(false);
      }
   }

   @Override
   public void exitStandard_access_list_stanza(
         Standard_access_list_stanzaContext ctx) {
      _currentStandardAcl = null;
   }

   @Override
   public void exitStandard_access_list_tail(
         Standard_access_list_tailContext ctx) {

      if (_currentStandardAcl.isIpV6()) {
         return;
      }

      LineAction action = getAccessListAction(ctx.ala);
      Ip srcIp = getIp(ctx.ipr);
      Ip srcWildcard = getWildcard(ctx.ipr);
      StandardAccessListLine line = new StandardAccessListLine(action, srcIp,
            srcWildcard);
      _currentStandardAcl.addLine(line);
   }

   @Override
   public void exitSwitching_mode_stanza(Switching_mode_stanzaContext ctx) {
      todo(ctx);
   }

   @Override
   public void exitSwitchport_access_if_stanza(
         Switchport_access_if_stanzaContext ctx) {
      int vlan = toInteger(ctx.vlan);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.ACCESS);
         currentInterface.setAccessVlan(vlan);
      }
   }

   @Override
   public void exitSwitchport_mode_access_stanza(
         Switchport_mode_access_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.ACCESS);
      }
   }

   @Override
   public void exitSwitchport_mode_dynamic_auto_stanza(
         Switchport_mode_dynamic_auto_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.DYNAMIC_AUTO);
      }
   }

   @Override
   public void exitSwitchport_mode_dynamic_desirable_stanza(
         Switchport_mode_dynamic_desirable_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.DYNAMIC_DESIRABLE);
      }
   }

   @Override
   public void exitSwitchport_mode_trunk_stanza(
         Switchport_mode_trunk_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.TRUNK);
      }
   }

   @Override
   public void exitSwitchport_trunk_allowed_if_stanza(
         Switchport_trunk_allowed_if_stanzaContext ctx) {
      List<SubRange> ranges = toRange(ctx.r);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.addAllowedRanges(ranges);
      }
   }

   @Override
   public void exitSwitchport_trunk_encapsulation_if_stanza(
         Switchport_trunk_encapsulation_if_stanzaContext ctx) {
      SwitchportEncapsulationType type = toEncapsulation(ctx.e);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportTrunkEncapsulation(type);
      }
   }

   @Override
   public void exitSwitchport_trunk_native_if_stanza(
         Switchport_trunk_native_if_stanzaContext ctx) {
      int vlan = toInteger(ctx.vlan);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setNativeVlan(vlan);
      }
   }

   @Override
   public void exitTemplate_peer_rb_stanza(Template_peer_rb_stanzaContext ctx) {
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      _currentPeerGroup = _configuration.getBgpProcesses().get(_currentVrf)
            .getMasterBgpPeerGroup();
   }

   @Override
   public void exitTemplate_peer_session_rb_stanza(
         Template_peer_session_rb_stanzaContext ctx) {
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      _currentPeerSession = null;
      _currentPeerGroup = _configuration.getBgpProcesses().get(_currentVrf)
            .getMasterBgpPeerGroup();
   }

   @Override
   public void exitUpdate_source_bgp_tail(Update_source_bgp_tailContext ctx) {
      if (_currentIpv6PeerGroup != null) {
         todo(ctx, "IPv6 not supported yet");
      }
      else {
         String source = toInterfaceName(ctx.source);
         _currentPeerGroup.setUpdateSource(source);
      }
   }

   @Override
   public void exitVrf_context_stanza(Vrf_context_stanzaContext ctx) {
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void exitVrf_forwarding_if_stanza(Vrf_forwarding_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setVrf(name);
         currentInterface.setIp(null);
      }
   }

   @Override
   public void exitVrf_member_if_stanza(Vrf_member_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setVrf(name);
      }
   }

   public CiscoConfiguration getConfiguration() {
      return _configuration;
   }

   public String getText() {
      return _text;
   }

   @Override
   public VendorConfiguration getVendorConfiguration() {
      return _configuration;
   }

   @Override
   public List<String> getWarnings() {
      return _warnings;
   }

   private void pedantic(String msg) {
      if (_pedantic) {
         throw new PedanticBatfishException(msg);
      }
      else {
         String prefix = "WARNING " + (_warnings.size() + 1) + ": PEDANTIC: ";
         String warning = prefix + msg + "\n";
         _warnings.add(warning);
      }
   }

   @Override
   public void processParseTree(ParserRuleContext tree) {
      ParseTreeWalker walker = new ParseTreeWalker();
      walker.walk(this, tree);
   }

   private void todo(ParserRuleContext ctx) {
      todo(ctx, "Unknown");
   }

   private void todo(ParserRuleContext ctx, String reason) {
      String ruleName = _parser.getParser().getRuleNames()[ctx.getRuleIndex()];
      if (_rulesWithSuppressedWarnings.contains(ruleName)) {
         return;
      }
      String prefix = "WARNING " + (_warnings.size() + 1) + ": ";
      StringBuilder sb = new StringBuilder();
      List<String> ruleNames = Arrays.asList(CiscoParser.ruleNames);
      String ruleStack = ctx.toString(ruleNames);
      sb.append(prefix
            + "Missing implementation for top (leftmost) parser rule in stack: '"
            + ruleStack + "'.\n");
      sb.append(prefix + "Reason: " + reason + "\n");
      sb.append(prefix + "Rule context follows:\n");
      int start = ctx.start.getStartIndex();
      int startLine = ctx.start.getLine();
      int end = ctx.stop.getStopIndex();
      String ruleText = _text.substring(start, end + 1);
      String[] ruleTextLines = ruleText.split("\\n");
      for (int line = startLine, i = 0; i < ruleTextLines.length; line++, i++) {
         String contextPrefix = prefix + " line " + line + ": ";
         sb.append(contextPrefix + ruleTextLines[i] + "\n");
      }
      sb.append(prefix + "Parse tree follows:\n");
      String parseTreePrefix = prefix + "PARSE TREE: ";
      String parseTreeText = ParseTreePrettyPrinter.print(ctx, _parser);
      String[] parseTreeLines = parseTreeText.split("\n");
      for (String parseTreeLine : parseTreeLines) {
         sb.append(parseTreePrefix + parseTreeLine + "\n");
      }
      _warnings.add(sb.toString());
   }

   public SwitchportEncapsulationType toEncapsulation(
         Switchport_trunk_encapsulationContext ctx) {
      if (ctx.DOT1Q() != null) {
         return SwitchportEncapsulationType.DOT1Q;
      }
      else if (ctx.ISL() != null) {
         return SwitchportEncapsulationType.ISL;
      }
      else if (ctx.NEGOTIATE() != null) {
         return SwitchportEncapsulationType.NEGOTIATE;
      }
      else {
         throw new BatfishException("bad encapsulation");
      }
   }

}
