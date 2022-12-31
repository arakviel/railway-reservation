package com.arakviel.cli.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Формує таблицю для друку.
 */
public final class TableFormatter {

    public static String run(List<String> headers, LinkedList<List<String>> rows) {
        rows.push(headers.stream().map(h -> "—".repeat(h.length())).toList());
        rows.push(headers);
        int[] maxLengths = new int[headers.size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        int index = 0;
        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            if (index >= 2) {
                result.append("[%d] | ".formatted(index - 1));
            } else {
                result.append(" ".repeat(6));
            }
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
            index++;
        }
        return result.toString();
    }

    private TableFormatter() {
    }
}
