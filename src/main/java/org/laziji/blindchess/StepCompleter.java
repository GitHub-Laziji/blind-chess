package org.laziji.blindchess;

import org.apache.commons.lang3.StringUtils;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;
import java.util.regex.Pattern;

public class StepCompleter implements Completer {


    public StepCompleter() {

    }

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