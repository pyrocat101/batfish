package batfish.filter.ast.node;

public enum Type {
    PORT("port"),
    PORT_RANGE("portrange"),
    HOST("host"),
    NET("net");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
