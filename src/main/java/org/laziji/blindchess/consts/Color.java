package org.laziji.blindchess.consts;

public enum Color {

    RED("红"),
    BLACK("黑");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Color opposite() {
        return this == RED ? BLACK : RED;
    }

}
