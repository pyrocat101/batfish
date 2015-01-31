package batfish.filter.node;

public class PortRange extends ID {
    public final int low;
    public final int high;

    public PortRange(int low, int high) {
        if (low > high) {
            throw new IllegalArgumentException("invalid portrange (low > high)");
        }
        this.low = low;
        this.high = high;
    }

    @Override
    public Type getType() {
        return Type.PORT_RANGE;
    }

    @Override
    public String toString() {
        return String.format("%d-%d", low, high);
    }
}
