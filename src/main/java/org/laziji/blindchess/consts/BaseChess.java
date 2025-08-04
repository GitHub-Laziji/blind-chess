package org.laziji.blindchess.consts;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum BaseChess {

    SHUAI(true, (board, point, rb) -> {
        List<Point> next = Arrays.asList(point.toL(1), point.toR(1), point.toT(1), point.toB(1)).stream().filter(o -> board.inJiuGong(o, rb) && !board.hasChess(o, rb)).collect(Collectors.toList());
        Point o = point;
        do {
            o = rb == Color.RED ? o.toT(1) : o.toB(1);
        } while (board.inBoard(o) && (!board.hasChess(o) || o.equals(point)));
        if (board.inBoard(o) && board.getChess(o).getBaseChess().isKing()) {
            next.add(o);
        }
        return next;
    }),
    SHI((board, point, rb) -> {
        List<Point> next = Arrays.asList(point.toT(1).toL(1), point.toT(1).toR(1), point.toB(1).toL(1), point.toB(1).toR(1));
        return next.stream().filter(o -> board.inJiuGong(o, rb) && !board.hasChess(o, rb)).collect(Collectors.toList());
    }),
    XIANG((board, point, rb) -> {
        List<Point> next = Arrays.asList(point.toT(2).toL(2), point.toT(2).toR(2), point.toB(2).toL(2), point.toB(2).toR(2));
        return next.stream().filter(o -> {
            if (!(board.inJieNei(o, rb) && !board.hasChess(o, rb))) {
                return false;
            }
            return !board.hasChess(new Point((o.getX() + point.getX()) / 2, (o.getY() + point.getY()) / 2));
        }).collect(Collectors.toList());
    }),
    MA((board, point, rb) -> {
        List<Point> next = Arrays.asList(point.toT(2).toL(1), point.toT(2).toR(1), point.toT(1).toL(2), point.toT(1).toR(2), point.toB(2).toL(1), point.toB(2).toR(1), point.toB(1).toL(2), point.toB(1).toR(2));
        return next.stream().filter(o -> {
            if (!(board.inBoard(o) && !board.hasChess(o, rb))) {
                return false;
            }
            return !board.hasChess(new Point((int) Math.floor((o.getX() - point.getX()) / 2d) + point.getX(), (int) Math.floor((o.getY() - point.getY()) / 2d) + point.getY()));
        }).collect(Collectors.toList());
    }),
    JU((board, point, rb) -> {
        List<Point> next = new ArrayList<>();
        for (int[] dxy : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
            Point p = point;
            do {
                p = p.to(dxy[0], dxy[1]);
                if (board.inBoard(p) && !board.hasChess(p, rb)) {
                    next.add(p);
                }
            } while (board.inBoard(p) && !board.hasChess(p));
        }
        return next;
    }),
    PAO((board, point, rb) -> {
        List<Point> next = new ArrayList<>();
        for (int[] dxy : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
            Point p = point;
            while (board.inBoard(p)) {
                p = p.to(dxy[0], dxy[1]);
                if (board.inBoard(p) && !board.hasChess(p)) {
                    next.add(p);
                } else if (board.inBoard(p)) {
                    do {
                        p = p.to(dxy[0], dxy[1]);
                    } while (board.inBoard(p) && !board.hasChess(p));
                    if (board.inBoard(p) && board.getChess(p).getRb() == rb.opposite()) {
                        next.add(p);
                    }
                    break;
                }
            }
        }
        return next;
    }),
    BING((board, point, rb) -> {
        List<Point> next;
        if (board.inJieNei(point, rb)) {
            next = Collections.singletonList(rb == Color.RED ? point.toT(1) : point.toB(1));
        } else {
            next = Arrays.asList(point.toL(1), point.toR(1), rb == Color.RED ? point.toT(1) : point.toB(1));
        }
        return next.stream().filter(board::inBoard).collect(Collectors.toList());
    });

    private final boolean king;
    private final NextPoint nextPoint;

    BaseChess(NextPoint nextPoint) {
        this.king = false;
        this.nextPoint = nextPoint;
    }

    BaseChess(boolean king, NextPoint nextPoint) {
        this.king = king;
        this.nextPoint = nextPoint;
    }

    public boolean isKing() {
        return king;
    }

    public List<Point> getNextPoint(Board board, Point from, Color rb) {
        Chess chess = board.getChess(from);
        if (chess == null || chess.getRb() != rb || chess.getBaseChess() != this) {
            return new ArrayList<>();
        }
        return nextPoint.get(board, from, rb);
    }

    @FunctionalInterface
    public interface NextPoint {
        List<Point> get(Board board, Point from, Color rb);
    }
}
