package org.laziji.blindchess.io;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Point;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.consts.WinType;

public class EnIO implements IO {

    @Override
    public Step input(Board board, String cmd) {
        int ox = cmd.charAt(0) - 'a';
        int oy = cmd.charAt(1) - '0';
        int nx = cmd.charAt(2) - 'a';
        int ny = cmd.charAt(3) - '0';
        return new Step(new Point(ox, oy), new Point(nx, ny));
    }

    @Override
    public String output(Board board, Step step) {
        return null;
    }

    @Override
    public void printStep(Board board, Step step) {

    }

    @Override
    public void printWin(Color rb, WinType winType) {

    }

    @Override
    public void printBoard(Board board) {

    }

}
