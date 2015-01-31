package batfish.filter.node;

import batfish.representation.Ip;

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
}
