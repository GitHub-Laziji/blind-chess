package org.laziji.blindchess.base;

public class Step {

    private final Point from;
    private final Point to;

    public Step(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }
}
