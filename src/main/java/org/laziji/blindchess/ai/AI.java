package org.laziji.blindchess.ai;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;

public interface AI {

    void init(Board board, Color rb) throws Exception;

    void close() throws Exception;

    Step queryBest(Board board) throws Exception;

}
