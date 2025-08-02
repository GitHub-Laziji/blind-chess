package org.laziji.blindchess.consts;

import org.laziji.blindchess.base.Board;
import org.laziji.blindchess.base.Point;

import java.util.List;

public enum Chess {
    R_SHUAI("帥", "shuai", Color.RED, BaseChess.SHUAI),
    R_SHI("仕", "shi", Color.RED, BaseChess.SHI),
    R_XIANG("相", "xiang", Color.RED, BaseChess.XIANG),
    R_MA("馬", "ma", Color.RED, BaseChess.MA),
    R_JU("車", "ju", Color.RED, BaseChess.JU),
    R_PAO("炮", "pao", Color.RED, BaseChess.PAO),
    R_BING("兵", "bing", Color.RED, BaseChess.BING),
    B_JIANG("将", "jiang", Color.BLACK, BaseChess.SHUAI),
    B_SHI("士", "shi", Color.BLACK, BaseChess.SHI),
    B_XIANG("象", "xiang", Color.BLACK, BaseChess.XIANG),
    B_MA("马", "ma", Color.BLACK, BaseChess.MA),
    B_JU("车", "ju", Color.BLACK, BaseChess.JU),
    B_PAO("砲", "pao", Color.BLACK, BaseChess.PAO),
    B_ZU("卒", "zu", Color.BLACK, BaseChess.BING);

    private final String name;
    private final String py;
    private final Color rb;
    private final BaseChess baseChess;

    Chess(String name, String py, Color rb, BaseChess baseChess) {
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

    public Color getRb() {
        return rb;
    }

    public BaseChess getBaseChess() {
        return baseChess;
    }

    public List<Point> getNextPoint(Board board, Point from) {
        return baseChess.getNextPoint(board, from, rb);
    }

}
