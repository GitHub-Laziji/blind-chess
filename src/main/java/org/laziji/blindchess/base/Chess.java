package org.laziji.blindchess.base;

import java.util.List;

public enum Chess {
    R_SHUAI("帥", "shuai", 0, (b, p) -> {
        return null;
    }),
    R_SHI("仕", "shi", 0, (b, p) -> {
        return null;
    }),
    R_XIANG("相", "xiang", 0, (b, p) -> {
        return null;
    }),
    R_MA("馬", "ma", 0, (b, p) -> {
        return null;
    }),
    R_JU("車", "che", 0, (b, p) -> {
        return null;
    }),
    R_PAO("炮", "pao", 0, (b, p) -> {
        return null;
    }),
    R_BING("兵", "bing", 0, (b, p) -> {
        return null;
    }),
    B_JIANG("将", "jiang", 1, (b, p) -> {
        return null;
    }),
    B_SHI("士", "shi", 1, (b, p) -> {
        return null;
    }),
    B_XIANG("象", "xiang", 1, (b, p) -> {
        return null;
    }),
    B_MA("马", "ma", 1, (b, p) -> {
        return null;
    }),
    B_JU("车", "che", 1, (b, p) -> {
        return null;
    }),
    B_PAO("砲", "pao", 1, (b, p) -> {
        return null;
    }),
    B_ZU("卒", "zu", 1, (b, p) -> {
        return null;
    });

    private final String name;
    private final String py;
    private final int rb;
    private final NextPoint nextPoint;

    Chess(String name, String py, int rb, NextPoint nextPoint) {
        this.name = name;
        this.py = py;
        this.rb = rb;
        this.nextPoint = nextPoint;
    }

    public String getName() {
        return name;
    }

    public String getPy() {
        return py;
    }

    public int getRb() {
        return rb;
    }

    public NextPoint getNextPoint() {
        return nextPoint;
    }

    @FunctionalInterface
    public interface NextPoint {
        List<Point> get(Board board, Point from);
    }
}
