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
import org.laziji.blindchess.base.Chess;
import org.laziji.blindchess.base.Point;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.exception.StepException;

import java.util.List;
import java.util.regex.Pattern;

public class UserAI implements AI {

    private Board board;
    private int rb;
    private Terminal terminal;
    private LineReader lineReader;

    @Override
    public void init(Board board, int rb) throws Exception {
        this.board = board;
        this.rb = rb;
        this.terminal = TerminalBuilder.terminal();
        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new StepCompleter())
                .build();

    }

    @Override
    public void close() throws Exception {
        this.terminal.close();
    }

    @Override
    public Step queryBest(Board board) throws Exception {
        String cmd = lineReader.readLine("象棋 > ");
        if ("exit".equals(cmd)) {
            return null;
        }
        return chCmdToXy(cmd);
    }

    private Step chCmdToXy(String cmd) throws Exception {
        cmd = cmd.replaceAll("\\s", "");
        if (cmd.length() != 4) {
            throw new StepException(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
        int ox = -1;
        int oy = -1;
        Chess chess = null;
        if (cmd.matches("^[\\u4E00-\\u9FA5][1-9]..$")) {
            String c = getPingYin(String.valueOf(cmd.charAt(0)));
            ox = Integer.parseInt(String.valueOf(cmd.charAt(1))) - 1;
            for (oy = 0; oy < 10; oy++) {
                chess = board.getChess(new Point(rb == 0 ? ox : (8 - ox), rb == 0 ? oy : (9 - oy)));
                if (chess != null && chess.getRb() == rb && chess.getPy().equals(c)) {
                    break;
                }
            }
            if (oy == 10) {
                throw new StepException(String.format("ERROR: [%c]路线上没有棋子[%s]", cmd.charAt(1), c));
            }
        } else if (cmd.matches("^([前中后][\\u4E00-\\u9FA5]|[一二三四五]兵)..$")) {

        } else {
            throw new StepException(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }

        int nx = ox;
        int ny = oy;
        if (cmd.matches("^..[进平退][1-9]$")) {
            if (cmd.charAt(2) == '进' || cmd.charAt(2) == '退') {
                int dy;
                if (chess == Chess.R_SHI || chess == Chess.B_SHI) {
                    dy = 1;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_XIANG || chess == Chess.B_XIANG) {
                    dy = 2;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_MA || chess == Chess.B_MA) {
                    dy = 3 - Math.abs(Integer.parseInt(String.valueOf(cmd.charAt(3))) - Integer.parseInt(String.valueOf(cmd.charAt(1))));
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else {
                    dy = Integer.parseInt(String.valueOf(cmd.charAt(3)));
                }
                if (cmd.charAt(2) == '退') {
                    dy = -dy;
                }
                ny += dy;
            } else if (cmd.charAt(2) == '平') {
                nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
            }
            if (rb != 0) {
                ox = 8 - ox;
                oy = 9 - oy;
                nx = 8 - ox;
                ny = 8 - ny;
            }
            return new Step(new Point(ox, oy), new Point(nx, ny));
        } else {
            throw new StepException(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
    }

    private String getPingYin(String val) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            StringBuilder output = new StringBuilder();
            char[] input = val.trim().toCharArray();
            for (char c : input) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    output.append(temp[0]);
                    continue;
                }
                output.append(c);
            }
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
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
