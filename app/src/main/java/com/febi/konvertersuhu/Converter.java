package com.febi.konvertersuhu;

public class Converter {

    public static double cToF(double c) {
        return (c * 9.0/5.0) + 32;
    }

    public static double fToC(double f) {
        return (f - 32) * 5.0/9.0;
    }

}
