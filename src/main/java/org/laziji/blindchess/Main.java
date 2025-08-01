package org.laziji.blindchess;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception {

        Terminal terminal = TerminalBuilder.terminal();
        Completer commandCompleter = new StepCompleter();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(commandCompleter)
                .build();

        Board board = new Board();
        String line;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            while ((line = lineReader.readLine("象棋 > ")) != null) {
                if ("exit".equals(line)) {
                    break;
                }
//                System.out.println("你输入的命令是: " + line);
                try {
                    board.activeByChCmd(line);
                    String url = String.format("http://www.chessdb.cn/chessdb.php?action=querybest&board=%s%%20b", board.getStatus());
//                    System.out.println(url);
//                    board.print();
                    HttpGet httpGet = new HttpGet(url);
                    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                        String bEnCmd=EntityUtils.toString(response.getEntity());
                        board.activeByEnCmd(bEnCmd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 关闭终端
        terminal.close();
    }
}