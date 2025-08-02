package org.laziji.blindchess.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Chess {
    R_SHUAI("帥", "shuai", 0, BaseChess.SHUAI),
    R_SHI("仕", "shi", 0, BaseChess.SHI),
    R_XIANG("相", "xiang", 0, BaseChess.XIANG),
    R_MA("馬", "ma", 0, BaseChess.MA),
    R_JU("車", "ju", 0, BaseChess.JU),
    R_PAO("炮", "pao", 0, BaseChess.PAO),
    R_BING("兵", "bing", 0, BaseChess.BING),
    B_JIANG("将", "jiang", 1, BaseChess.SHUAI),
    B_SHI("士", "shi", 1, BaseChess.SHI),
    B_XIANG("象", "xiang", 1, BaseChess.XIANG),
    B_MA("马", "ma", 1, BaseChess.MA),
    B_JU("车", "ju", 1, BaseChess.JU),
    B_PAO("砲", "pao", 1, BaseChess.PAO),
    B_ZU("卒", "zu", 1, BaseChess.BING);

    private final String name;
    private final String py;
    private final int rb;
    private final BaseChess baseChess;

    Chess(String name, String py, int rb, BaseChess baseChess) {
        this.name = name;
        this.py = py;
        this.rb = rb;
        this.baseChess = baseChess;
    }

    public String getName() {
        return name;
    }

    public String getPy() {
        return py;
    }

    public int getRb() {
        return rb;
    }

    public BaseChess getBaseChess() {
        return baseChess;
    }

    public List<Point> getNextPoint(Board board, Point from) {
        return baseChess.getNextPoint(board, from, rb);
    }

}
