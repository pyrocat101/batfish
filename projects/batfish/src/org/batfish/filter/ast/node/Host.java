package org.batfish.filter.ast.node;

import org.batfish.representation.Ip;

public class Host extends ID {
    public final Ip host;

    public Host(Ip host) {
        this.host = host;
    }

    public Host(String host) {
        this(new Ip(host));
    }

    @Override
    public Type getType() {
        return Type.HOST;
    }

    @Override
    public String toString() {
        return this.host.toString();
    }

    @Override
    public <T> T accept(IDVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
