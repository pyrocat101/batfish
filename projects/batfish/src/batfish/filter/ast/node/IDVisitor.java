package batfish.filter.ast.node;

public interface IDVisitor<T> {
    public T visit(Net net);
    public T visit(Host host);
    public T visit(Port port);
    public T visit(PortRange range);
}
