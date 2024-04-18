package com.lucendar.common.types;

import java.util.ArrayList;
import java.util.List;

public class LinesValidateResultReceiver extends AbstractValidateResultReceiver {

    private final List<String> lines = new ArrayList<>();

    @Override
    protected void onError(String message) {
        lines.add("ERROR: " + message);
    }

    @Override
    public void warn(String message) {
        lines.add("WARN : " + message);
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLinesString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            String ln = lines.get(i);
            if (i > 0)
                s.append("\n");

            s.append(ln);
        }

        return s.toString();
    }
}
