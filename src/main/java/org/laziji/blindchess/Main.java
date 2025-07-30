package org.laziji.blindchess;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception{

        Terminal terminal = TerminalBuilder.terminal();

        Completer commandCompleter = new StepCompleter();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(commandCompleter)
                .build();

        String line;
        while ((line = lineReader.readLine("象棋 > ")) != null) {
            if ("exit".equals(line)) {
                break;
            }
            System.out.println("你输入的命令是: " + line);
        }

        // 关闭终端
        terminal.close();
    }
}