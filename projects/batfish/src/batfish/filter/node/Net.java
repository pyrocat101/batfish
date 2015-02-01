package batfish.filter.node;

import batfish.representation.Ip;
import batfish.representation.Prefix;

public class Net extends ID {
    public Prefix net;

    public Net(Prefix net) {
        this.net = net;
    }

    public Net(Ip network, int prefixLength) {
        this(new Prefix(network, prefixLength));
    }

    @Override
    public Type getType() {
        return Type.NET;
    }

    @Override
    public String toString() {
        return this.net.toString();
    }
}