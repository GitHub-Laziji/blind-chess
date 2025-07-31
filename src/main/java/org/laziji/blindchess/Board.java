package org.laziji.blindchess;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class Board {

    private final char[][] map = new char[][]{
            {'R', 'N', 'B', 'A', 'K', 'A', 'B', 'N', 'R'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'C', ' ', ' ', ' ', ' ', ' ', 'C', ' '},
            {'P', ' ', 'P', ' ', 'P', ' ', 'P', ' ', 'P'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'p', ' ', 'p', ' ', 'p', ' ', 'p', ' ', 'p'},
            {' ', 'c', ' ', ' ', ' ', ' ', ' ', 'c', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'r', 'n', 'b', 'a', 'k', 'a', 'b', 'n', 'r'},
    };

    private int wb = 0;

    public Board() {

    }

    public void active(int ox, int oy, int nx, int ny) {
        System.out.println(ox + " " + oy + "," + nx + " " + ny);
        map[ny][nx] = map[oy][ox];
        map[oy][ox] = ' ';
        wb = 1 - wb;
    }

    public void activeByEnCmd(String cmd) throws Exception {
        int ox = cmd.charAt(5) - 'a';
        int oy = cmd.charAt(6) - '0';
        int nx = cmd.charAt(7) - 'a';
        int ny = cmd.charAt(8) - '0';
        active(ox, oy, nx, ny);
    }

    public void activeByChCmd(String cmd) throws Exception {

        cmd = cmd.replaceAll("\\s", "");
        if (cmd.length() != 4) {
            throw new Exception(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
        int ox = -1;
        int oy = -1;
        // 炮8平5
        if (cmd.matches("^[\\u4E00-\\u9FA5][1-9]..$")) {
            String c = getPingYin(String.valueOf(cmd.charAt(0)));
            System.out.println(c);
            ox = Integer.parseInt(String.valueOf(cmd.charAt(1))) - 1;
            System.out.println(ox);
            for (oy = 0; oy < 10; oy++) {
                if (map[oy][ox] == 'K' && "shuai".equals(c)
                        || map[oy][ox] == 'A' && "shi".equals(c)
                        || map[oy][ox] == 'B' && "xiang".equals(c)
                        || map[oy][ox] == 'N' && "ma".equals(c)
                        || map[oy][ox] == 'R' && ("ju".equals(c) || "che".equals(c))
                        || map[oy][ox] == 'C' && "pao".equals(c)
                        || map[oy][ox] == 'P' && "bing".equals(c)) {
                    break;
                }
            }
            if (oy == 10) {
                throw new Exception(String.format("ERROR: [%c]路线上没有棋子[%s]", cmd.charAt(1), c));
            }
        } else if (cmd.matches("^([前中后][\\u4E00-\\u9FA5]|[一二三四五]兵)..$")) {

        } else {
            throw new Exception(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }

        int nx = ox;
        int ny = oy;
        if (cmd.matches("^..[进平退][1-9]$")) {
            if (cmd.charAt(2) == '进') {
                ny += Integer.parseInt(String.valueOf(cmd.charAt(3)));
            } else if (cmd.charAt(2) == '平') {
                nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
            } else if (cmd.charAt(2) == '退') {
                ny -= Integer.parseInt(String.valueOf(cmd.charAt(3)));
            }
            active(ox, oy, nx, ny);
        } else {
            throw new Exception(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
    }

    public String getStatus() {
        StringBuilder result = new StringBuilder();
        for (int y = 9; y >= 0; y--) {
            if (y < 9) {
                result.append('/');
            }
            int space = 0;
            for (int x = 0; x < 9; x++) {
                if (map[y][x] == ' ') {
                    space++;
                    continue;
                }
                if (space > 0) {
                    result.append(space);
                    space = 0;
                }
                result.append(map[y][x]);
            }
            if (space > 0) {
                result.append(space);
            }
        }
        return result.toString();
    }

    public String getWb() {
        return wb == 0 ? "w" : "b";
    }

    public void print() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                System.out.print(map[y][x]);
            }
            System.out.println();
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
}
