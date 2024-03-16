package com.lucendar.common.types;

import info.gratour.common.error.Errors;

import java.util.ArrayList;
import java.util.List;

public class LinesValidateResultReceiver extends AbstractValidateResultReceiver {

    private final List<String> lines = new ArrayList<>();

    @Override
    public void invalidField(String fieldName) {
        String msg = Errors.errorMessageFormat(Errors.INVALID_CONFIG, fieldName);
        lines.add(msg);
        err = true;
    }

    @Override
    public void error(String message) {
        lines.add(message);
        err = true;
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
