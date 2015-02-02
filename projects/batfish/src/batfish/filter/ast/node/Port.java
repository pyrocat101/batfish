package batfish.filter.ast.node;

import batfish.filter.ast.PortNames;

public class Port extends ID {
    public final int port;

    public Port(int port) {
        this.port = port;
    }

    public Port(String name) {
        this(PortNames.get(name));
    }

    @Override
    public Type getType() {
        return Type.PORT;
    }

    @Override
    public String toString() {
        return Integer.toString(this.port);
    }
}
