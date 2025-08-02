package org.laziji.blindchess.ai;

import org.laziji.blindchess.Board;
import org.laziji.blindchess.bean.Step;

public interface AI {

    Step queryBest(Board board);

}
