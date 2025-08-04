package org.laziji.blindchess.ai;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.io.IO;

public interface AI {

    void init(Board board, Color rb) throws Exception;

    void close() throws Exception;

    Step queryBest() throws Exception;

}
