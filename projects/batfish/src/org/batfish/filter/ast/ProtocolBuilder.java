package org.batfish.filter.ast;

import org.batfish.filter.ast.node.Protocol;
import org.batfish.grammar.bpf.BPFParser;
import org.batfish.grammar.bpf.BPFParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

public class ProtocolBuilder extends BPFParserBaseVisitor<Protocol> {
    @Override
    public Protocol visitProtoICMP(@NotNull BPFParser.ProtoICMPContext ctx) {
        return Protocol.ICMP;
    }

    @Override
    public Protocol visitProtoIGMP(@NotNull BPFParser.ProtoIGMPContext ctx) {
        return Protocol.IGMP;
    }

    @Override
    public Protocol visitProtoIP(@NotNull BPFParser.ProtoIPContext ctx) {
        return Protocol.IP;
    }

    @Override
    public Protocol visitProtoSCTP(@NotNull BPFParser.ProtoSCTPContext ctx) {
        return Protocol.SCTP;
    }

    @Override
    public Protocol visitProtoTCP(@NotNull BPFParser.ProtoTCPContext ctx) {
        return Protocol.TCP;
    }

    @Override
    public Protocol visitProtoUDP(@NotNull BPFParser.ProtoUDPContext ctx) {
        return Protocol.UDP;
    }
}
