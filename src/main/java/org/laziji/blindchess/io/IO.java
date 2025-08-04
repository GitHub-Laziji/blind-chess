package org.laziji.blindchess.io;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;

public interface IO {

    Step input(String cmd, Board board, Color rb);

    String output(Step step, Board board, Color rb);
}
