package com.spea.api.utils;

import java.text.Normalizer;

public class StringUtil {

    public static String normalizarEspacos(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s+", " ");
    }

}
