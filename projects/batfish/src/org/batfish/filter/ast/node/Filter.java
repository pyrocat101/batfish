package org.batfish.filter.ast.node;

public class Filter {
    public final Protocol protocol;
    public final Direction direction;
    public final Type type;
    public final ID id;

    public Filter(Protocol protocol) {
        this(protocol, null, null, null);
    }

    public Filter(Direction direction, Type type, ID id) {
        this(null, direction, type, id);
    }

    public Filter(Protocol protocol, Direction direction, Type type, ID id) {
        this.direction = direction;
        this.protocol = protocol;
        this.type = type;
        this.id = id;
        if (this.id != null && this.type != this.id.getType()) {
            throw new IllegalArgumentException("invalid syntax (type qualifier and primitive type mismatch)");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (protocol != null) {
            sb.append(' ');
            sb.append(protocol.toString());
        }
        if (direction != null) {
            sb.append(' ');
            sb.append(direction.toString());
        }
        if (type != null) {
            sb.append(' ');
            sb.append(type.toString());
        }
        if (id != null) {
            sb.append(' ');
            sb.append(id.toString());
        }
        return sb.toString().trim();
    }
}
