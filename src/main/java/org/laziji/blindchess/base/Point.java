package org.laziji.blindchess.base;

import java.util.Objects;

public class Point {

    private int x;
    private int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point toL(int n) {
        return new Point(x + n, y);
    }

    public Point toR(int n) {
        return new Point(x - n, y);
    }

    public Point toT(int n) {
        return new Point(x, y + n);
    }

    public Point toB(int n) {
        return new Point(x, y - n);
    }

    public Point to(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
