package batfish.filter.node;

public class NotID extends ID {
    public final ID negated;

    public NotID(ID negated) {
        this.negated = negated;
    }

    @Override
    public Type getType() {
        return this.negated.getType();
    }

    @Override
    public String toString() {
        return String.format("not %s", this.negated);
    }
}
