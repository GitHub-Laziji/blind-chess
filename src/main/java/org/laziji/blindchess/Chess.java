package org.laziji.blindchess;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum Chess {
    R_SHUAI("K", "帥", ImmutableList.of("shuai"), 0),
    R_SHI("A", "仕", ImmutableList.of("shi"), 0),
    R_XIANG("B", "相", ImmutableList.of("xiang"), 0),
    R_MA("N", "馬", ImmutableList.of("ma"), 0),
    R_JU("R", "車", ImmutableList.of("che", "ju"), 0),
    R_PAO("C", "炮", ImmutableList.of("pao"), 0),
    R_BING("P", "兵", ImmutableList.of("bing"), 0),
    B_JIANG("k", "将", ImmutableList.of("jiang"), 1),
    B_SHI("a", "士", ImmutableList.of("shi"), 1),
    B_XIANG("b", "象", ImmutableList.of("xiang"), 1),
    B_MA("n", "马", ImmutableList.of("ma"), 1),
    B_JU("r", "车", ImmutableList.of("che", "ju"), 1),
    B_PAO("c", "砲", ImmutableList.of("pao"), 1),
    B_ZU("p", "卒", ImmutableList.of("zu"), 1);

    private final String code;
    private final String name;
    private final List<String> py;
    private final Integer rb;

    public static Chess find(String code, String name, String py, Integer rb) {
        for (Chess chess : values()) {
            if ((code == null || chess.code.equals(code)) && (name == null || chess.name.equals(name)) && (py == null || chess.py.contains(py)) && (rb == null || chess.rb.equals(rb))) {
                return chess;
            }
        }
        return null;
    }

    Chess(String code, String name, List<String> py, Integer rb) {
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

    public List<String> getPy() {
        return py;
    }

    public Integer getRb() {
        return rb;
    }
}
