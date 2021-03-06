package org.batfish.filter.ast;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Services {
    // from `/etc/services`
    private static final Map<String, Integer> services = ImmutableMap.<String, Integer>builder()
            .put("tcpmux", 1)
            .put("echo", 7)
            .put("discard", 9)
            .put("systat", 11)
            .put("daytime", 13)
            .put("netstat", 15)
            .put("qotd", 17)
            .put("msp", 18)
            .put("chargen", 19)
            .put("ftp-data", 20)
            .put("ftp", 21)
            .put("fsp", 21)
            .put("ssh", 22)
            .put("telnet", 23)
            .put("smtp", 25)
            .put("time", 37)
            .put("rlp", 39)
            .put("nameserver", 42)
            .put("whois", 43)
            .put("tacacs", 49)
            .put("re-mail-ck", 50)
            .put("domain", 53)
            .put("mtp", 57)
            .put("tacacs-ds", 65)
            .put("bootps", 67)
            .put("bootpc", 68)
            .put("tftp", 69)
            .put("gopher", 70)
            .put("rje", 77)
            .put("finger", 79)
            .put("http", 80)
            .put("link", 87)
            .put("kerberos", 88)
            .put("supdup", 95)
            .put("hostnames", 101)
            .put("iso-tsap", 102)
            .put("acr-nema", 104)
            .put("csnet-ns", 105)
            .put("rtelnet", 107)
            .put("pop2", 109)
            .put("pop3", 110)
            .put("sunrpc", 111)
            .put("auth", 113)
            .put("sftp", 115)
            .put("uucp-path", 117)
            .put("nntp", 119)
            .put("ntp", 123)
            .put("pwdgen", 129)
            .put("loc-srv", 135)
            .put("netbios-ns", 137)
            .put("netbios-dgm", 138)
            .put("netbios-ssn", 139)
            .put("imap2", 143)
            .put("snmp", 161)
            .put("snmp-trap", 162)
            .put("cmip-man", 163)
            .put("cmip-agent", 164)
            .put("mailq", 174)
            .put("xdmcp", 177)
            .put("nextstep", 178)
            .put("bgp", 179)
            .put("prospero", 191)
            .put("irc", 194)
            .put("smux", 199)
            .put("at-rtmp", 201)
            .put("at-nbp", 202)
            .put("at-echo", 204)
            .put("at-zis", 206)
            .put("qmtp", 209)
            .put("z3950", 210)
            .put("ipx", 213)
            .put("imap3", 220)
            .put("pawserv", 345)
            .put("zserv", 346)
            .put("fatserv", 347)
            .put("rpc2portmap", 369)
            .put("codaauth2", 370)
            .put("clearcase", 371)
            .put("ulistserv", 372)
            .put("ldap", 389)
            .put("imsp", 406)
            .put("svrloc", 427)
            .put("https", 443)
            .put("snpp", 444)
            .put("microsoft-ds", 445)
            .put("kpasswd", 464)
            .put("urd", 465)
            .put("saft", 487)
            .put("isakmp", 500)
            .put("rtsp", 554)
            .put("nqs", 607)
            .put("npmp-local", 610)
            .put("npmp-gui", 611)
            .put("hmmp-ind", 612)
            .put("asf-rmcp", 623)
            .put("qmqp", 628)
            .put("ipp", 631)
            .put("exec", 512)
            .put("biff", 512)
            .put("login", 513)
            .put("who", 513)
            .put("shell", 514)
            .put("syslog", 514)
            .put("printer", 515)
            .put("talk", 517)
            .put("ntalk", 518)
            .put("route", 520)
            .put("timed", 525)
            .put("tempo", 526)
            .put("courier", 530)
            .put("conference", 531)
            .put("netnews", 532)
            .put("netwall", 533)
            .put("gdomap", 538)
            .put("uucp", 540)
            .put("klogin", 543)
            .put("kshell", 544)
            .put("dhcpv6-client", 546)
            .put("dhcpv6-server", 547)
            .put("afpovertcp", 548)
            .put("idfp", 549)
            .put("remotefs", 556)
            .put("nntps", 563)
            .put("submission", 587)
            .put("ldaps", 636)
            .put("tinc", 655)
            .put("silc", 706)
            .put("kerberos-adm", 749)
            .put("webster", 765)
            .put("rsync", 873)
            .put("ftps-data", 989)
            .put("ftps", 990)
            .put("telnets", 992)
            .put("imaps", 993)
            .put("ircs", 994)
            .put("pop3s", 995)
            .put("socks", 1080)
            .put("proofd", 1093)
            .put("rootd", 1094)
            .put("openvpn", 1194)
            .put("rmiregistry", 1099)
            .put("kazaa", 1214)
            .put("nessus", 1241)
            .put("lotusnote", 1352)
            .put("ms-sql-s", 1433)
            .put("ms-sql-m", 1434)
            .put("ingreslock", 1524)
            .put("prospero-np", 1525)
            .put("datametrics", 1645)
            .put("sa-msg-port", 1646)
            .put("kermit", 1649)
            .put("groupwise", 1677)
            .put("l2f", 1701)
            .put("radius", 1812)
            .put("radius-acct", 1813)
            .put("msnp", 1863)
            .put("unix-status", 1957)
            .put("log-server", 1958)
            .put("remoteping", 1959)
            .put("cisco-sccp", 2000)
            .put("search", 2010)
            .put("pipe-server", 2010)
            .put("nfs", 2049)
            .put("gnunet", 2086)
            .put("rtcm-sc104", 2101)
            .put("gsigatekeeper", 2119)
            .put("gris", 2135)
            .put("cvspserver", 2401)
            .put("venus", 2430)
            .put("venus-se", 2431)
            .put("codasrv", 2432)
            .put("codasrv-se", 2433)
            .put("mon", 2583)
            .put("dict", 2628)
            .put("f5-globalsite", 2792)
            .put("gsiftp", 2811)
            .put("gpsd", 2947)
            .put("gds-db", 3050)
            .put("icpv2", 3130)
            .put("iscsi-target", 3260)
            .put("mysql", 3306)
            .put("nut", 3493)
            .put("distcc", 3632)
            .put("daap", 3689)
            .put("svn", 3690)
            .put("suucp", 4031)
            .put("sysrqd", 4094)
            .put("sieve", 4190)
            .put("epmd", 4369)
            .put("remctl", 4373)
            .put("f5-iquery", 4353)
            .put("ipsec-nat-t", 4500)
            .put("iax", 4569)
            .put("mtn", 4691)
            .put("radmin-port", 4899)
            .put("rfe", 5002)
            .put("mmcc", 5050)
            .put("sip", 5060)
            .put("sip-tls", 5061)
            .put("aol", 5190)
            .put("xmpp-client", 5222)
            .put("xmpp-server", 5269)
            .put("cfengine", 5308)
            .put("mdns", 5353)
            .put("postgresql", 5432)
            .put("freeciv", 5556)
            .put("amqp", 5672)
            .put("ggz", 5688)
            .put("x11", 6000)
            .put("x11-1", 6001)
            .put("x11-2", 6002)
            .put("x11-3", 6003)
            .put("x11-4", 6004)
            .put("x11-5", 6005)
            .put("x11-6", 6006)
            .put("x11-7", 6007)
            .put("gnutella-svc", 6346)
            .put("gnutella-rtr", 6347)
            .put("sge-qmaster", 6444)
            .put("sge-execd", 6445)
            .put("mysql-proxy", 6446)
            .put("afs3-fileserver", 7000)
            .put("afs3-callback", 7001)
            .put("afs3-prserver", 7002)
            .put("afs3-vlserver", 7003)
            .put("afs3-kaserver", 7004)
            .put("afs3-volser", 7005)
            .put("afs3-errors", 7006)
            .put("afs3-bos", 7007)
            .put("afs3-update", 7008)
            .put("afs3-rmtsys", 7009)
            .put("font-service", 7100)
            .put("http-alt", 8080)
            .put("bacula-dir", 9101)
            .put("bacula-fd", 9102)
            .put("bacula-sd", 9103)
            .put("xmms2", 9667)
            .put("nbd", 10809)
            .put("zabbix-agent", 10050)
            .put("zabbix-trapper", 10051)
            .put("amanda", 10080)
            .put("dicom", 11112)
            .put("hkp", 11371)
            .put("bprd", 13720)
            .put("bpdbm", 13721)
            .put("bpjava-msvc", 13722)
            .put("vnetd", 13724)
            .put("bpcd", 13782)
            .put("vopied", 13783)
            .put("db-lsp", 17500)
            .put("dcap", 22125)
            .put("gsidcap", 22128)
            .put("wnn6", 22273)
            .put("kerberos4", 750)
            .put("kerberos-master", 751)
            .put("passwd-server", 752)
            .put("krb-prop", 754)
            .put("krbupdate", 760)
            .put("swat", 901)
            .put("kpop", 1109)
            .put("knetd", 2053)
            .put("zephyr-srv", 2102)
            .put("zephyr-clt", 2103)
            .put("zephyr-hm", 2104)
            .put("eklogin", 2105)
            .put("kx", 2111)
            .put("iprop", 2121)
            .put("supfilesrv", 871)
            .put("supfiledbg", 1127)
            .put("linuxconf", 98)
            .put("poppassd", 106)
            .put("moira-db", 775)
            .put("moira-update", 777)
            .put("moira-ureg", 779)
            .put("spamd", 783)
            .put("omirr", 808)
            .put("customs", 1001)
            .put("skkserv", 1178)
            .put("predict", 1210)
            .put("rmtcfg", 1236)
            .put("wipld", 1300)
            .put("xtel", 1313)
            .put("xtelw", 1314)
            .put("support", 1529)
            .put("cfinger", 2003)
            .put("frox", 2121)
            .put("ninstall", 2150)
            .put("zebrasrv", 2600)
            .put("zebra", 2601)
            .put("ripd", 2602)
            .put("ripngd", 2603)
            .put("ospfd", 2604)
            .put("bgpd", 2605)
            .put("ospf6d", 2606)
            .put("ospfapi", 2607)
            .put("isisd", 2608)
            .put("afbackup", 2988)
            .put("afmbackup", 2989)
            .put("xtell", 4224)
            .put("fax", 4557)
            .put("hylafax", 4559)
            .put("distmp3", 4600)
            .put("munin", 4949)
            .put("enbd-cstatd", 5051)
            .put("enbd-sstatd", 5052)
            .put("pcrd", 5151)
            .put("noclog", 5354)
            .put("hostmon", 5355)
            .put("rplay", 5555)
            .put("nrpe", 5666)
            .put("nsca", 5667)
            .put("mrtd", 5674)
            .put("bgpsim", 5675)
            .put("canna", 5680)
            .put("syslog-tls", 6514)
            .put("sane-port", 6566)
            .put("ircd", 6667)
            .put("zope-ftp", 8021)
            .put("tproxy", 8081)
            .put("omniorb", 8088)
            .put("clc-build-daemon", 8990)
            .put("xinetd", 9098)
            .put("mandelspawn", 9359)
            .put("git", 9418)
            .put("zope", 9673)
            .put("webmin", 10000)
            .put("kamanda", 10081)
            .put("amandaidx", 10082)
            .put("amidxtape", 10083)
            .put("smsqp", 11201)
            .put("xpilot", 15345)
            .put("sgi-cmsd", 17001)
            .put("sgi-crsd", 17002)
            .put("sgi-gcd", 17003)
            .put("sgi-cad", 17004)
            .put("isdnlog", 20011)
            .put("vboxd", 20012)
            .put("binkp", 24554)
            .put("asp", 27374)
            .put("csync2", 30865)
            .put("dircproxy", 57000)
            .put("tfido", 60177)
            .put("fido", 60179)
            .build();

    public static int get(String name) {
        if (services.containsKey(name)) {
            return services.get(name);
        } else {
            throw new IllegalArgumentException("packet filter: unknown port name " + name);
        }
    }
}
