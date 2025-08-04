package org.laziji.blindchess;

import org.laziji.blindchess.ai.ApiAI;
import org.laziji.blindchess.ai.UserAI;
import org.laziji.blindchess.base.Board;

public class Main {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.run(new UserAI(), new ApiAI());
    }
}