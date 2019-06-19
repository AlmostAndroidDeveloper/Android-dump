package com.example.scalecalc;
import java.math.BigInteger;

public class Converter {
    static String alphabet = "0123456789abcdefghijklmnopqrstuvwxyz .!',"; // current = 41

    static String calculate(String number, int from, int to) {
        return fromDecimal(toDecimal(number, from), to);
    }

    static char digitToChar(BigInteger digit) {
        return alphabet.charAt(digit.intValue());
    }

    static BigInteger charToDigit(char symbol) {
        for (int i = 0; i < alphabet.length(); i++)
            if (alphabet.charAt(i) == symbol) return BigInteger.valueOf(i);
        return BigInteger.ZERO;
    }

    static String toDecimal(String number, int from) {
        BigInteger result = BigInteger.ZERO;
        int power = number.length() - 1;
        for (int i = 0; i < number.length(); i++) {
            BigInteger fromInPower = BigInteger.valueOf(from).pow(power--);
            result = result.add((charToDigit(number.charAt(i)).multiply(fromInPower)));
        }
        return String.valueOf(result);
    }

    static String fromDecimal(String number, int to) {
        BigInteger parsedNumber = new BigInteger(number);
        StringBuilder result = new StringBuilder();
        while (parsedNumber.compareTo(BigInteger.ZERO) == 1) {
            result.insert(0, digitToChar(parsedNumber.mod(BigInteger.valueOf(to))));
            parsedNumber = parsedNumber.divide(BigInteger.valueOf(to));
        }
        return result.toString();
    }
}
