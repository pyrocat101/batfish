package org.batfish.filter.ast.node;

import org.batfish.filter.ast.Services;

public class Port extends ID {
    public final int port;

    public Port(int port) {
        this.port = port;
    }

    public Port(String name) {
        this(Services.get(name));
    }

    @Override
    public Type getType() {
        return Type.PORT;
    }

    @Override
    public String toString() {
        return Integer.toString(this.port);
    }

    @Override
    public <T> T accept(IDVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
