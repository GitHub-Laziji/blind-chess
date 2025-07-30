package org.laziji.blindchess;

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

    public void active(String cmd) {
        int ox = cmd.charAt(5) - 'a';
        int oy = cmd.charAt(6) - '0';
        int nx = cmd.charAt(7) - 'a';
        int ny = cmd.charAt(8) - '0';
        map[ny][nx] = map[oy][ox];
        map[oy][ox] = ' ';
        wb = 1 - wb;
    }

    public String getStatus() {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < 10; y++) {
            if (y > 0) {
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
}
