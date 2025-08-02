package org.laziji.blindchess.ai;

import org.laziji.blindchess.Board;
import org.laziji.blindchess.bean.Step;

public interface AI {

    void init(Board board, int rb) throws Exception;

    void close() throws Exception;

    Step queryBest(Board board) throws Exception;

}
