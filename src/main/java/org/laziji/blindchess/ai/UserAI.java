package org.laziji.blindchess.ai;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.consts.Chess;
import org.laziji.blindchess.base.Point;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.exception.StepException;
import org.laziji.blindchess.io.ChIO;
import org.laziji.blindchess.io.IO;

import java.util.List;
import java.util.regex.Pattern;

public class UserAI implements AI {

    private Board board;
    private Color rb;
    private Terminal terminal;
    private LineReader lineReader;
    private IO io;

    @Override
    public void init(Board board, Color rb) throws Exception {
        this.board = board;
        this.rb = rb;
        this.terminal = TerminalBuilder.terminal();
        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new StepCompleter())
                .build();
        this.io = new ChIO();
    }

    @Override
    public void close() throws Exception {
        this.terminal.close();
    }

    @Override
    public Step queryBest() throws Exception {
//        board.print();
        String cmd = lineReader.readLine("象棋 > ");
        if ("exit".equals(cmd)) {
            return null;
        }
        return io.input(cmd, board, rb);
    }


    private class StepCompleter implements Completer {
        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String input = line.line().replaceAll("\\s", "");
            Completer completer = null;
            if (StringUtils.isBlank(input)) {
                completer = new StringsCompleter("帅", "士", "相", "马", "车", "炮", "兵", "前", "中", "后", "一", "二", "三", "四", "五");
            } else if (Pattern.matches("^[帅士相马车炮兵]$", input)) {
                completer = new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9");
            } else if (Pattern.matches("^..$", input)) {
                completer = new StringsCompleter("进", "平", "退");
            } else if (Pattern.matches("^..[进平退]$", input)) {
                completer = new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9");
            }
            if (completer != null) {
                completer.complete(reader, line, candidates);
            }
        }

    }
}
