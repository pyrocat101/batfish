package org.batfish.filter.ast.node;

public enum Direction {
    SRC("src"),
    DST("dst"),
    SRC_OR_DST("src or dst"),
    SRC_AND_DST("src and dst");

    private String name;

    private Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
