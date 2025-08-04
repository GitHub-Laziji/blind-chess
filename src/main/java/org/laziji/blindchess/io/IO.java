package org.laziji.blindchess.io;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.consts.WinType;

public interface IO {

    Step input(Board board, String cmd);

    String output(Board board, Step step);

    void printStep(Board board, Step step);

    void printWin(Color rb, WinType winType);

    void printBoard(Board board);
}
