package org.laziji.blindchess;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class Board {

    private final String[][] map = new String[][]{
            {"R", "N", "B", "A", "K", "A", "B", "N", "R"},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", "C", " ", " ", " ", " ", " ", "C", " "},
            {"P", " ", "P", " ", "P", " ", "P", " ", "P"},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"p", " ", "p", " ", "p", " ", "p", " ", "p"},
            {" ", "c", " ", " ", " ", " ", " ", "c", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"r", "n", "b", "a", "k", "a", "b", "n", "r"},
    };

    private Integer rb = 0;
    private Integer stepCount = 0;

    public Board() {

    }

    public void active(int ox, int oy, int nx, int ny) {
        System.out.printf("%d\t%s: %s\n", ++stepCount, rb == 0 ? "红" : "黑", xyToChCmd(ox, oy, nx, ny));
        map[ny][nx] = map[oy][ox];
        map[oy][ox] = " ";
        rb = 1 - rb;
    }

    public void activeByEnCmd(String cmd) throws Exception {
        int[] xy = enCmdToXy(cmd);

        active(xy[0], xy[1], xy[2], xy[3]);
    }

    public void activeByChCmd(String cmd) throws Exception {
        int[] xy = chCmdToXy(cmd, 0);
        active(xy[0], xy[1], xy[2], xy[3]);
    }

    public String getStatus() {
        StringBuilder result = new StringBuilder();
        for (int y = 9; y >= 0; y--) {
            if (y < 9) {
                result.append('/');
            }
            int space = 0;
            for (int x = 0; x < 9; x++) {
                if (" ".equals(map[y][x])) {
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

    public String getRb() {
        return rb == 0 ? "w" : "b";
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


    private int[] chCmdToXy(String cmd, Integer rb) throws Exception {
        cmd = cmd.replaceAll("\\s", "");
        if (cmd.length() != 4) {
            throw new Exception(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
        int ox = -1;
        int oy = -1;
        // 炮8平5
        Chess chess = null;
        if (cmd.matches("^[\\u4E00-\\u9FA5][1-9]..$")) {
            String c = getPingYin(String.valueOf(cmd.charAt(0)));
//            System.out.println(c);
            ox = Integer.parseInt(String.valueOf(cmd.charAt(1))) - 1;
//            System.out.println(ox);
            for (oy = 0; oy < 10; oy++) {
                chess = Chess.find(map[rb == 0 ? oy : (9 - oy)][rb == 0 ? ox : (8 - ox)], null, c, rb);
                if (chess != null) {
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
                if (chess == Chess.R_SHI) {
                    ny += 1;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_XIANG) {
                    ny += 2;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_MA) {
                    ny += 3 - Math.abs(Integer.parseInt(String.valueOf(cmd.charAt(3))) - Integer.parseInt(String.valueOf(cmd.charAt(1))));
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else {
                    ny += Integer.parseInt(String.valueOf(cmd.charAt(3)));
                }
            } else if (cmd.charAt(2) == '平') {
                nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
            } else if (cmd.charAt(2) == '退') {
                if (chess == Chess.R_SHI) {
                    ny -= 1;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_XIANG) {
                    ny -= 2;
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else if (chess == Chess.R_MA) {
                    ny -= 3 - Math.abs(Integer.parseInt(String.valueOf(cmd.charAt(3))) - Integer.parseInt(String.valueOf(cmd.charAt(1))));
                    nx = Integer.parseInt(String.valueOf(cmd.charAt(3))) - 1;
                } else {
                    ny -= Integer.parseInt(String.valueOf(cmd.charAt(3)));
                }
            }
            if (rb != 0) {
                ox = 8 - ox;
                oy = 9 - oy;
                nx = 8 - ox;
                ny = 8 - ny;
            }
            return new int[]{ox, oy, nx, ny};
        } else {
            throw new Exception(String.format("ERROR: [%s]走棋不符合规范", cmd));
        }
    }

    private int[] enCmdToXy(String cmd) throws Exception {
        int ox = cmd.charAt(5) - 'a';
        int oy = cmd.charAt(6) - '0';
        int nx = cmd.charAt(7) - 'a';
        int ny = cmd.charAt(8) - '0';
        return new int[]{ox, oy, nx, ny};
    }

    private String xyToChCmd(int ox, int oy, int nx, int ny) {
        Chess chess = Chess.find(map[oy][ox], null, null, null);
//        System.out.printf("%d %d %d %d, %s",ox,oy,nx,ny,chess.name());
        StringBuilder cmd = new StringBuilder();
        cmd.append(chess.getName());
        cmd.append(chess.getRb() == 0 ? (ox + 1) : (8 - ox + 1));
        if (oy == ny) {
            cmd.append("平");
            cmd.append(chess.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
        } else if (oy < ny) {
            if (chess.getRb() == 0) {
                cmd.append("进");
            } else {
                cmd.append("退");
            }
            if (ox == nx) {
                cmd.append(ny - oy);
            } else {
                cmd.append(chess.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
            }
        } else {
            if (chess.getRb() == 0) {
                cmd.append("退");
            } else {
                cmd.append("进");
            }
            if (ox == nx) {
                cmd.append(oy - ny);
            } else {
                cmd.append(chess.getRb() == 0 ? (nx + 1) : (8 - nx + 1));
            }
        }
        return cmd.toString();
    }
}
