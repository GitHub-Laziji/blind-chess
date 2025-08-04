package org.laziji.blindchess.base;

import org.laziji.blindchess.ai.AI;
import org.laziji.blindchess.consts.Chess;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.exception.StepException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final Chess[][] map = new Chess[][]{
            {Chess.R_JU, Chess.R_MA, Chess.R_XIANG, Chess.R_SHI, Chess.R_SHUAI, Chess.R_SHI, Chess.R_XIANG, Chess.R_MA, Chess.R_JU},
            {null, null, null, null, null, null, null, null, null},
            {null, Chess.R_PAO, null, null, null, null, null, Chess.R_PAO, null},
            {Chess.R_BING, null, Chess.R_BING, null, Chess.R_BING, null, Chess.R_BING, null, Chess.R_BING},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {Chess.B_ZU, null, Chess.B_ZU, null, Chess.B_ZU, null, Chess.B_ZU, null, Chess.B_ZU},
            {null, Chess.B_PAO, null, null, null, null, null, Chess.B_PAO, null},
            {null, null, null, null, null, null, null, null, null},
            {Chess.B_JU, Chess.B_MA, Chess.B_XIANG, Chess.B_SHI, Chess.B_JIANG, Chess.B_SHI, Chess.B_XIANG, Chess.B_MA, Chess.B_JU},
    };

    private Color rb = Color.RED;
    private final Map<Color, AI> players = new HashMap<>();
    private int stepCount = 0;
    private boolean done = false;
    private Color win = null;

    public Board(AI rPlayer, AI bPlayer) {
        players.put(Color.RED, rPlayer);
        players.put(Color.BLACK, bPlayer);
    }

    public void run() throws Exception {
        players.get(Color.RED).init(this, Color.RED);
        players.get(Color.BLACK).init(this, Color.BLACK);
        while (!isFinal()) {
            Step step = players.get(rb).queryBest();
            if (step == null) {
                win = rb.opposite();
                done = true;
                System.out.printf("%s胜\n", win.getName());
                break;
            }
            try {
                active(step);
            } catch (StepException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        players.get(Color.RED).close();
        players.get(Color.BLACK).close();
    }

    public Chess getChess(Point point) {
        return map[point.getY()][point.getX()];
    }

    public Chess getChess(int x, int y) {
        return map[y][x];
    }

    public boolean hasChess(Point point, Color rb) {
        return map[point.getY()][point.getX()] != null && map[point.getY()][point.getX()].getRb() == rb;
    }

    public boolean hasChess(Point point) {
        return map[point.getY()][point.getX()] != null;
    }

    public boolean inBoard(Point point) {
        return point.getX() >= 0 && point.getX() < 9 && point.getY() >= 0 && point.getY() < 10;
    }

    public boolean inJiuGong(Point point, Color rb) {
        if (rb == Color.RED) {
            return point.getX() >= 3 && point.getX() < 6 && point.getY() >= 0 && point.getY() < 3;
        } else {
            return point.getX() >= 3 && point.getX() < 6 && point.getY() >= 7 && point.getY() < 10;
        }
    }

    public boolean inJieNei(Point point, Color rb) {
        if (rb == Color.RED) {
            return inBoard(point) && point.getY() < 5;
        } else {
            return inBoard(point) && point.getY() >= 5;
        }
    }

    public void print() {
        for (int y = 9; y >= 0; y--) {
            for (int x = 8; x >= 0; x--) {
                System.out.printf("%s\t", map[y][x] == null ? " " : map[y][x].getName());
            }
            System.out.println();
        }
    }

    private void active(Step step) throws Exception {
        stepVerify(step);
        System.out.printf("第%04d步 %s: %s\n", ++stepCount, rb.getName(), stepToChCmd(step));
        map[step.getTo().getY()][step.getTo().getX()] = map[step.getFrom().getY()][step.getFrom().getX()];
        map[step.getFrom().getY()][step.getFrom().getX()] = null;
        rb = rb.opposite();
    }

    private void stepVerify(Step step) {
        Chess from = getChess(step.getFrom());
        if (from == null) {
            throw new StepException("ERROR: 起始点不存在棋子");
        }
        if (from.getRb() != rb) {
            throw new StepException("ERROR: 起始点为对方棋子");
        }
        List<Point> points = getChess(step.getFrom()).getNextPoint(this, step.getFrom());
        if (!points.contains(step.getTo())) {
            throw new StepException("ERROR: 走棋不符合规范");
        }
    }

    private boolean isFinal() {
        if (done) {
            return true;
        }
        boolean kunbi = true;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                if (map[y][x] != null && map[y][x].getRb() == rb) {
                    if (!map[y][x].getNextPoint(this, new Point(x, y)).isEmpty()) {
                        kunbi = false;
                        break;
                    }
                }
            }
            if (!kunbi) {
                break;
            }
        }
        if (kunbi) {
            win = rb.opposite();
            done = true;
            System.out.printf("困毙 %s胜\n", win.getName());
            return true;
        }
        return false;
    }

    private String stepToChCmd(Step step) {
        int ox = step.getFrom().getX();
        int oy = step.getFrom().getY();
        int nx = step.getTo().getX();
        int ny = step.getTo().getY();
        Chess from = map[oy][ox];
        StringBuilder cmd = new StringBuilder();
        boolean repeat = false;
        int repeatIndex = 0;
        int repeatCount = 0;
        for (int x = 0; x < 9; x++) {
            int colIndex = -1;
            int colCount = 0;
            for (int y = 9; y >= 0; y--) {
                if (map[from.getRb() == Color.RED ? y : (9 - y)][from.getRb() == Color.RED ? x : (8 - x)] == from) {
                    colCount++;
                    if (x == ox && from.getRb() == Color.RED ? (y >= oy) : (y <= oy)) {
                        colIndex++;
                    }
                }
            }
            if (colCount >= 2) {
                if (x == ox) {
                    repeat = true;
                    repeatIndex = repeatCount + colIndex;
                }
                repeatCount += colCount;
            }
        }

        if (repeat) {
            String[] t;
            if (repeatCount <= 2) {
                t = new String[]{"前", "后"};
            } else if (repeatCount == 3) {
                t = new String[]{"前", "中", "后"};
            } else {
                t = new String[]{"一", "二", "三", "四", "五"};
            }
            cmd.append(t[repeatIndex]);
            cmd.append(from.getName());
        } else {
            cmd.append(from.getName());
            cmd.append(from.getRb() == Color.RED ? (ox + 1) : (8 - ox + 1));
        }

        if (oy == ny) {
            cmd.append("平");
            cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
        } else if (oy < ny) {
            if (from.getRb() == Color.RED) {
                cmd.append("进");
            } else {
                cmd.append("退");
            }
            if (ox == nx) {
                cmd.append(ny - oy);
            } else {
                cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
            }
        } else {
            if (from.getRb() == Color.RED) {
                cmd.append("退");
            } else {
                cmd.append("进");
            }
            if (ox == nx) {
                cmd.append(oy - ny);
            } else {
                cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
            }
        }
        return cmd.toString();
    }
}
