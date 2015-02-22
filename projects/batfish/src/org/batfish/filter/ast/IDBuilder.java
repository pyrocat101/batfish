package org.batfish.filter.ast;

import org.batfish.filter.ast.node.*;
import org.batfish.grammar.bpf.BPFParser;
import org.batfish.grammar.bpf.BPFParserBaseVisitor;
import org.batfish.representation.Ip;
import org.batfish.representation.Prefix;
import org.antlr.v4.runtime.misc.NotNull;

public class IDBuilder extends BPFParserBaseVisitor<ID> {
    @Override
    public ID visitNetwork(@NotNull BPFParser.NetworkContext ctx) {
        return ctx.network_id().accept(this);
    }

    @Override
    public ID visitNotNetwork(@NotNull BPFParser.NotNetworkContext ctx) {
        return new NotID(ctx.network_id().accept(this));
    }

    @Override
    public ID visitNotPort(@NotNull BPFParser.NotPortContext ctx) {
        return new NotID(ctx.port_num().accept(this));
    }

    @Override
    public ID visitNotPortName(@NotNull BPFParser.NotPortNameContext ctx) {
        return new NotID(new Port(ctx.ID().getText()));
    }

    @Override
    public ID visitNotPortRange(@NotNull BPFParser.NotPortRangeContext ctx) {
        return new NotID(ctx.port_range().accept(this));
    }

    @Override
    public ID visitPort(@NotNull BPFParser.PortContext ctx) {
        return ctx.port_num().accept(this);
    }

    @Override
    public ID visitPortName(@NotNull BPFParser.PortNameContext ctx) {
        return new Port(ctx.ID().getText());
    }

    @Override
    public ID visitPortRange(@NotNull BPFParser.PortRangeContext ctx) {
        return ctx.port_range().accept(this);
    }

//    @Override
//    public ID visitNet1(@NotNull BPFParser.Net1Context ctx) {
//        Ip address = new Ip(ctx.NID1().getText() + ".0.0.0");
//        return new Net(address, 8);
//    }

    @Override
    public ID visitNet2(@NotNull BPFParser.Net2Context ctx) {
        Ip address = new Ip(ctx.NID2().getText() + ".0.0");
        return new Net(address, 16);
    }

    @Override
    public ID visitNet3(@NotNull BPFParser.Net3Context ctx) {
        Ip address = new Ip(ctx.NID3().getText() + ".0");
        return new Net(address, 24);
    }

    @Override
    public ID visitPrefix(@NotNull BPFParser.PrefixContext ctx) {
        return new Net(new Prefix(ctx.CIDR().getText()));
    }

    @Override
    public ID visitMasked(@NotNull BPFParser.MaskedContext ctx) {
        Ip address = new Ip(ctx.HID(0).getText());
        Ip mask = new Ip(ctx.HID(1).getText());
        Prefix net = new Prefix(address.networkString(mask));
        return new Net(net);
    }

    @Override
    public ID visitHost(@NotNull BPFParser.HostContext ctx) {
        return new Host(new Ip(ctx.HID().getText()));
    }

    @Override
    public ID visitPortNum(@NotNull BPFParser.PortNumContext ctx) {
        return new Port(Integer.decode(ctx.NUM().getText()));
    }

    @Override
    public ID visitSmallPortNum(@NotNull BPFParser.SmallPortNumContext ctx) {
        return new Port(Integer.decode(ctx.NID1().getText()));
    }

    @Override
    public ID visitParenPortNum(@NotNull BPFParser.ParenPortNumContext ctx) {
        return ctx.port_num().accept(this);
    }

    @Override
    public ID visitPortNumRange(@NotNull BPFParser.PortNumRangeContext ctx) {
        String[] range = ctx.RANGE().getText().split("-");
        int low = Integer.decode(range[0]);
        int high = Integer.decode(range[1]);
        return new PortRange(low, high);
    }

    @Override
    public ID visitParenPortNumRange(@NotNull BPFParser.ParenPortNumRangeContext ctx) {
        return ctx.port_range().accept(this);
    }
}
