package org.laziji.blindchess.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Chess {
    R_SHUAI("帥", "shuai", 0, (board, point) -> {
        List<Point> next = Arrays.asList(point.toL(1), point.toR(1), point.toT(1), point.toB(1));
        return next.stream().filter(o -> board.inJiuGong(o, 0) && board.hasChess(o, 0)).collect(Collectors.toList());
    }),
    R_SHI("仕", "shi", 0, (board, point) -> {
        List<Point> next = Arrays.asList(point.toT(1).toL(1), point.toT(1).toR(1), point.toB(1).toL(1), point.toB(1).toR(1));
        return next.stream().filter(o -> board.inJiuGong(o, 0) && !board.hasChess(o, 0)).collect(Collectors.toList());
    }),
    R_XIANG("相", "xiang", 0, (board, point) -> {
        List<Point> next = Arrays.asList(point.toT(2).toL(2), point.toT(2).toR(2), point.toB(2).toL(2), point.toB(2).toR(2));
        return next.stream().filter(o -> {
            if (!(board.inJieNei(o, 0) && !board.hasChess(o, 0))) {
                return false;
            }
            return !board.hasChess(new Point((o.getX() + point.getX()) / 2, (o.getY() + point.getY()) / 2));
        }).collect(Collectors.toList());
    }),
    R_MA("馬", "ma", 0, (board, point) -> {
        List<Point> next = Arrays.asList(point.toT(2).toL(1), point.toT(2).toR(1), point.toT(1).toL(2), point.toT(1).toR(2), point.toB(2).toL(1), point.toB(2).toR(1), point.toB(1).toL(2), point.toB(1).toR(2));
        return next.stream().filter(o -> {
            if (!(board.inBoard(o) && !board.hasChess(o, 0))) {
                return false;
            }
            return !board.hasChess(new Point((int) Math.floor((o.getX() - point.getX()) / 2d) + point.getX(), (int) Math.floor((o.getY() - point.getY()) / 2d) + point.getY()));
        }).collect(Collectors.toList());
    }),
    R_JU("車", "che", 0, (board, point) -> {
        List<Point> next = new ArrayList<>();
        Point p;
        p = point;
        do {
            p = p.toL(1);
            if (board.inBoard(p) && !board.hasChess(p, 0)) {
                next.add(p);
            }
        } while (!board.hasChess(p));
        p = point;
        do {
            p = p.toR(1);
            if (board.inBoard(p) && !board.hasChess(p, 0)) {
                next.add(p);
            }
        } while (!board.hasChess(p));
        p = point;
        do {
            p = p.toT(1);
            if (board.inBoard(p) && !board.hasChess(p, 0)) {
                next.add(p);
            }
        } while (!board.hasChess(p));
        p = point;
        do {
            p = p.toB(1);
            if (board.inBoard(p) && !board.hasChess(p, 0)) {
                next.add(p);
            }
        } while (!board.hasChess(p));
        return next;
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
