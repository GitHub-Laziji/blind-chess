package org.laziji.blindchess.io;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Point;
import org.laziji.blindchess.base.Step;
import org.laziji.blindchess.consts.Chess;
import org.laziji.blindchess.consts.Color;
import org.laziji.blindchess.exception.StepException;

public class ChIO implements IO {

    @Override
    public Step input(String cmd, Board board, Color rb) {
        cmd = cmd.replaceAll("\\s", "");
        if (cmd.length() != 4) {
            throw new StepException(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
        int ox = -1;
        int oy = -1;
        Chess chess = null;
        if (cmd.matches("^[\\u4E00-\\u9FA5][1-9]..$")) {
            String c = getPingYin(String.valueOf(cmd.charAt(0)));
            if ("che".equals(c)) {
                c = "ju";
            }
            ox = Integer.parseInt(String.valueOf(cmd.charAt(1))) - 1;
            for (oy = 0; oy < 10; oy++) {
                chess = board.getChess(new Point(rb == Color.RED ? ox : (8 - ox), rb == Color.RED ? oy : (9 - oy)));
                if (chess != null && chess.getRb() == rb && chess.getPy().equals(c)) {
                    break;
                }
            }
            if (oy == 10) {
                throw new StepException(String.format("ERROR: [%c]路线上没有棋子[%s]", cmd.charAt(1), c));
            }
        } else if (cmd.matches("^([前中后][\\u4E00-\\u9FA5]|[一二三四五]兵)..$")) {
            // TODO
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
            if (rb == Color.BLACK) {
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

    @Override
    public String output(Step step, Board board, Color rb) {
        int ox = step.getFrom().getX();
        int oy = step.getFrom().getY();
        int nx = step.getTo().getX();
        int ny = step.getTo().getY();
        Chess from = board.getChess(step.getFrom());
        StringBuilder cmd = new StringBuilder();
        boolean repeat = false;
        int repeatIndex = 0;
        int repeatCount = 0;
        for (int x = 0; x < 9; x++) {
            int colIndex = -1;
            int colCount = 0;
            for (int y = 9; y >= 0; y--) {
                if (board.getChess(from.getRb() == Color.RED ? x : (8 - x), from.getRb() == Color.RED ? y : (9 - y)) == from) {
                    colCount++;
                    if (x == ox && from.getRb() == Color.RED ? (y >= oy) : (y <= oy)) {
                        colIndex++;
                    }
                }
            }
            if (colCount >= 2) {
                if (x == ox) {
                    repeat = true;
                    repeatIndex = repeatCount + colIndex;
                }
                repeatCount += colCount;
            }
        }

        if (repeat) {
            String[] t;
            if (repeatCount <= 2) {
                t = new String[]{"前", "后"};
            } else if (repeatCount == 3) {
                t = new String[]{"前", "中", "后"};
            } else {
                t = new String[]{"一", "二", "三", "四", "五"};
            }
            cmd.append(t[repeatIndex]);
            cmd.append(from.getName());
        } else {
            cmd.append(from.getName());
            cmd.append(from.getRb() == Color.RED ? (ox + 1) : (8 - ox + 1));
        }

        if (oy == ny) {
            cmd.append("平");
            cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
        } else if (oy < ny) {
            if (from.getRb() == Color.RED) {
                cmd.append("进");
            } else {
                cmd.append("退");
            }
            if (ox == nx) {
                cmd.append(ny - oy);
            } else {
                cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
            }
        } else {
            if (from.getRb() == Color.RED) {
                cmd.append("退");
            } else {
                cmd.append("进");
            }
            if (ox == nx) {
                cmd.append(oy - ny);
            } else {
                cmd.append(from.getRb() == Color.RED ? (nx + 1) : (8 - nx + 1));
            }
        }
        return cmd.toString();
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
}
