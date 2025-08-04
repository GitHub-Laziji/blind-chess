package org.laziji.blindchess.ai;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Chess;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.io.EnIO;
import org.laziji.blindchess.io.IO;

import java.util.HashMap;
import java.util.Map;

public class ApiAI implements AI {

    private static final Map<Chess, String> chessCode = new HashMap<Chess, String>() {
        {
            put(Chess.R_SHUAI, "K");
            put(Chess.R_SHI, "A");
            put(Chess.R_XIANG, "B");
            put(Chess.R_MA, "N");
            put(Chess.R_JU, "R");
            put(Chess.R_PAO, "C");
            put(Chess.R_BING, "P");
            put(Chess.B_JIANG, "k");
            put(Chess.B_SHI, "a");
            put(Chess.B_XIANG, "b");
            put(Chess.B_MA, "n");
            put(Chess.B_JU, "r");
            put(Chess.B_PAO, "c");
            put(Chess.B_ZU, "p");
        }
    };

    private Board board;
    private Color rb;
    private CloseableHttpClient httpClient;
    private IO io;

    @Override
    public void init(Board board, Color rb) throws Exception {
        this.board = board;
        this.rb = rb;
        this.httpClient = HttpClients.createDefault();
        this.io = new EnIO();
    }

    @Override
    public void close() throws Exception {
        this.httpClient.close();
    }

    @Override
    public Step queryBest() throws Exception {
        StringBuilder result = new StringBuilder();
        for (int y = 9; y >= 0; y--) {
            if (y < 9) {
                result.append('/');
            }
            int space = 0;
            for (int x = 0; x < 9; x++) {
                if (board.getChess(x, y) == null) {
                    space++;
                    continue;
                }
                if (space > 0) {
                    result.append(space);
                    space = 0;
                }
                result.append(chessCode.get(board.getChess(x, y)));
            }
            if (space > 0) {
                result.append(space);
            }
        }
        String url = String.format("http://www.chessdb.cn/chessdb.php?action=querybest&board=%s%%20%s", result, rb == Color.RED ? "w" : "b");
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String cmd = EntityUtils.toString(response.getEntity());
//            System.out.println(cmd);
            if (!cmd.startsWith("move:")) {
                return null;
            }
            return io.input(board, cmd.substring(5));
        }
    }
}
