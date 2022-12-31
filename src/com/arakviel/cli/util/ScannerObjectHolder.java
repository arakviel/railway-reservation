package com.arakviel.cli.util;

import java.nio.charset.Charset;
import java.util.Scanner;

public final class ScannerObjectHolder {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase()
            .startsWith("win");

    public static final Scanner SCANNER = new Scanner(System.in,
            Charset.forName(IS_WINDOWS ? "Windows-1251" : "UTF-8"));

//    public static final Scanner SCANNER = new Scanner(System.in);

    private ScannerObjectHolder() {
    }
}
