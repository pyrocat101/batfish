package org.batfish.filter.ast.node;

public abstract class ID {
    public abstract Type getType();
    public abstract <T> T accept(IDVisitor<? extends T> visitor);
}
