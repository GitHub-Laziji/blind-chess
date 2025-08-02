package org.laziji.blindchess.base;

import org.laziji.blindchess.ai.AI;
import org.laziji.blindchess.exception.StepException;

import java.util.List;

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

    private int rb = 0;
    private final AI[] players = new AI[2];
    private int stepCount = 0;
    private boolean done = false;
    private int win = -1;

    public Board(AI rPlayer, AI bPlayer) {
        players[0] = rPlayer;
        players[1] = bPlayer;
    }


    public void run() throws Exception {
        players[0].init(this, 0);
        players[1].init(this, 1);
        while (!done) {
            Step step = players[rb].queryBest(this);
            if (step == null) {
                win = 1 - rb;
                done = true;
                System.out.printf("%s胜\n", win == 0 ? "红" : "黑");
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
        players[0].close();
        players[0].close();
    }

    public Chess getChess(Point point) {
        return map[point.getY()][point.getX()];
    }

    public Chess getChess(int x, int y) {
        return map[y][x];
    }

    private void active(Step step) throws Exception {
        stepVerify(step);
        System.out.printf("%d\t%s: %s\n", ++stepCount, rb == 0 ? "红" : "黑", stepToChCmd(step));
        map[step.getTo().getY()][step.getTo().getX()] = map[step.getFrom().getY()][step.getFrom().getX()];
        map[step.getFrom().getY()][step.getFrom().getX()] = null;
        rb = 1 - rb;
        finalVerify();
    }

    private void stepVerify(Step step) {
        Chess from = getChess(step.getFrom());
        Chess to = getChess(step.getTo());
        if (from == null) {
            throw new StepException("ERROR: 起始点不存在棋子");
        }
        if (from.getRb() != rb) {
            throw new StepException("ERROR: 起始点为对方棋子");
        }
        if (to != null && to.getRb() == from.getRb()) {
            throw new StepException("ERROR: 目标点已存在友方棋子");
        }

        List<Point> points = getChess(step.getFrom()).getNextPoint().get(this, step.getFrom());
        if (!points.contains(step.getTo())) {
            throw new StepException("ERROR: 走棋不符合规范");
        }
    }

    private void finalVerify() {

    }

    private String stepToChCmd(Step step) {
        int ox = step.getFrom().getX();
        int oy = step.getFrom().getY();
        int nx = step.getTo().getX();
        int ny = step.getTo().getY();
        Chess from = map[oy][ox];
        StringBuilder cmd = new StringBuilder();
        cmd.append(from.getName());
        cmd.append(from.getRb() == 0 ? (ox + 1) : (8 - ox + 1));
        if (oy == ny) {
            cmd.append("平");
            cmd.append(from.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
        } else if (oy < ny) {
            if (from.getRb() == 0) {
                cmd.append("进");
            } else {
                cmd.append("退");
            }
            if (ox == nx) {
                cmd.append(ny - oy);
            } else {
                cmd.append(from.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
            }
        } else {
            if (from.getRb() == 0) {
                cmd.append("退");
            } else {
                cmd.append("进");
            }
            if (ox == nx) {
                cmd.append(oy - ny);
            } else {
                cmd.append(from.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
            }
        }
        return cmd.toString();
    }
}
