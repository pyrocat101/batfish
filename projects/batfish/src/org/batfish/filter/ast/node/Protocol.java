package org.batfish.filter.ast.node;

public enum Protocol {
    IP("ip"),
    TCP("tcp"),
    UDP("udp"),
    SCTP("sctp"),
    ICMP("icmp"),
    IGMP("igmp");

    private String name;

    private Protocol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
