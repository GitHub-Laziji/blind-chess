package org.laziji.blindchess;

import com.google.common.collect.ImmutableList;
import org.laziji.blindchess.bean.Point;

import java.util.List;

public enum ChessType {
    R_SHUAI("帥", ImmutableList.of("shuai"), 0, (b, p) -> {
        return null;
    }),
    R_SHI("仕", ImmutableList.of("shi"), 0, (b, p) -> {
        return null;
    }),
    R_XIANG("相", ImmutableList.of("xiang"), 0, (b, p) -> {
        return null;
    }),
    R_MA("馬", ImmutableList.of("ma"), 0, (b, p) -> {
        return null;
    }),
    R_JU("車", ImmutableList.of("che", "ju"), 0, (b, p) -> {
        return null;
    }),
    R_PAO("炮", ImmutableList.of("pao"), 0, (b, p) -> {
        return null;
    }),
    R_BING("兵", ImmutableList.of("bing"), 0, (b, p) -> {
        return null;
    }),
    B_JIANG("将", ImmutableList.of("jiang"), 1, (b, p) -> {
        return null;
    }),
    B_SHI("士", ImmutableList.of("shi"), 1, (b, p) -> {
        return null;
    }),
    B_XIANG("象", ImmutableList.of("xiang"), 1, (b, p) -> {
        return null;
    }),
    B_MA("马", ImmutableList.of("ma"), 1, (b, p) -> {
        return null;
    }),
    B_JU("车", ImmutableList.of("che", "ju"), 1, (b, p) -> {
        return null;
    }),
    B_PAO("砲", ImmutableList.of("pao"), 1, (b, p) -> {
        return null;
    }),
    B_ZU("卒", ImmutableList.of("zu"), 1, (b, p) -> {
        return null;
    });

    private final String name;
    private final List<String> py;
    private final Integer rb;
    private final NextPoint nextPoint;

    ChessType(String name, List<String> py, Integer rb, NextPoint nextPoint) {
        this.name = name;
        this.py = py;
        this.rb = rb;
        this.nextPoint = nextPoint;
    }

    public String getName() {
        return name;
    }

    public List<String> getPy() {
        return py;
    }

    public Integer getRb() {
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
