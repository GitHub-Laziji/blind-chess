package org.laziji.blindchess.base;

import com.google.common.collect.ImmutableList;
import org.laziji.blindchess.ai.AI;
import org.laziji.blindchess.consts.BaseChess;
import org.laziji.blindchess.consts.Chess;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.consts.WinType;
import org.laziji.blindchess.exception.StepException;
import org.laziji.blindchess.io.ChIO;
import org.laziji.blindchess.io.IO;

import java.util.ArrayList;
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
    private boolean done = false;
    private Color win = null;
    private List<Step> steps = new ArrayList<>();
    private IO io;

    public Board() {
        this.io = new ChIO();
    }

    public Board(IO io) {
        this.io = io;
    }

    public Board(Chess[][] map, Color rb, IO io) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                this.map[y][x] = map[y][x];
            }
        }
        this.rb = rb;
        this.io = io;
    }

    public Color getRb() {
        return rb;
    }

    public int getStepCount() {
        return steps.size();
    }

    public boolean isDone() {
        return done;
    }

    public Color getWin() {
        return win;
    }

    public List<Step> getSteps() {
        return ImmutableList.copyOf(steps);
    }

    public void run(AI rPlayer, AI bPlayer) throws Exception {
        rPlayer.init(this, Color.RED);
        bPlayer.init(this, Color.BLACK);
        players.put(Color.RED, rPlayer);
        players.put(Color.BLACK, bPlayer);
        while (!isFinal()) {
            Step step = players.get(rb).queryBest();
            if (step == null) {
                win = rb.opposite();
                done = true;
                io.printWin(win, WinType.CHECKMATE);
                break;
            }
            try {
                action(step);
            } catch (StepException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rPlayer.close();
        bPlayer.close();
    }


    public Chess getChess(Point point) {
        return map[point.getY()][point.getX()];
    }

    public Chess getChess(int x, int y) {
        return map[y][x];
    }

    public List<Point> findAll(BaseChess baseChess, Color rb) {
        List<Point> all = new ArrayList<>();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                if (map[y][x] != null && map[y][x].getRb() == rb && map[y][x].getBaseChess() == baseChess) {
                    all.add(new Point(x, y));
                }
            }
        }
        return all;
    }

    public Point find(BaseChess baseChess, Color rb) {
        List<Point> all = findAll(baseChess, rb);
        return all.isEmpty() ? null : all.get(0);
    }

    public List<Step> getAllNextStep(Color rb) {
        List<Step> allStep = new ArrayList<>();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                if (map[y][x] == null || map[y][x].getRb() != rb) {
                    continue;
                }
                for (Point o : map[y][x].getNextPoint(this, new Point(x, y))) {
                    allStep.add(new Step(new Point(x, y), o));
                }
            }
        }
        return allStep;
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

    public void action(Step step) {
        stepVerify(step);
        if (io != null) {
            io.printStep(this, step);
        }
        move(step);
    }

    private void move(Step step) {
        steps.add(step);
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
        Board subBoard = new Board(this.map, rb, null);
        subBoard.move(step);
        if (subBoard.isWin()) {
            throw new StepException("ERROR: 不能送将");
        }
    }

    private boolean isWin() {
        return getAllNextStep(rb).contains(find(BaseChess.SHUAI, rb.opposite()));
    }

    private boolean isFinal() {
        if (done) {
            return true;
        }
        if (isWin()) {
            win = rb;
            done = true;
            io.printWin(win, WinType.CHECKMATE);
            return true;
        }

        List<Step> allStep = getAllNextStep(rb);
        if (allStep.isEmpty()) {
            win = rb.opposite();
            done = true;
            io.printWin(win, WinType.STALEMATE);
            return true;
        }
        boolean lose = true;
        for (Step step : allStep) {
            Board subBoard = new Board(this.map, rb, null);
            subBoard.move(step);
            if (!subBoard.isWin()) {
                lose = false;
            }
        }
        if (lose) {
            win = rb.opposite();
            done = true;
            List<Step> oppAllStep = getAllNextStep(rb.opposite());
            if (oppAllStep.contains(find(BaseChess.SHUAI, rb))) {
                io.printWin(win, WinType.CHECKMATE);
            } else {
                io.printWin(win, WinType.STALEMATE);
            }
            return true;
        }
        return false;
    }

}
