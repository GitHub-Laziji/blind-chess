package org.laziji.blindchess;

public enum Chess {
    R_SHUAI("K", "帥", "shuai", 0),
    R_SHI("A", "仕", "shi", 0),
    R_XIANG("B", "相", "xiang", 0),
    R_MA("N", "馬", "ma", 0),
    R_JU("R", "車", "ju", 0),
    R_PAO("C", "炮", "pao", 0),
    R_BING("P", "兵", "bing", 0),
    B_JIANG("k", "将", "jiang", 1),
    B_SHI("a", "士", "shi", 1),
    B_XIANG("b", "象", "xiang", 1),
    B_MA("n", "马", "ma", 1),
    B_JU("r", "车", "ju", 1),
    B_PAO("c", "砲", "pao", 1),
    B_ZU("p", "卒", "zu", 1);

    private final String code;
    private final String name;
    private final String py;
    private final Integer rb;

    public static Chess find(String code, String name, String py, Integer rb) {
        for (Chess chess : values()) {
            if ((code == null || chess.code.equals(code)) && (name == null || chess.name.equals(name)) && (py == null || chess.py.equals(py)) && (rb == null || chess.rb.equals(rb))) {
                return chess;
            }
        }
        return null;
    }

    Chess(String code, String name, String py, Integer rb) {
        this.code = code;
        this.name = name;
        this.py = py;
        this.rb = rb;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPy() {
        return py;
    }

    public Integer getRb() {
        return rb;
    }
}
