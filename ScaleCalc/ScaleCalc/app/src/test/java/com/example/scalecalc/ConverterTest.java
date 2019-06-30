package com.example.scalecalc;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class ConverterTest {
    @Test
    public void digitToCharTest() {
        assertEquals('0', Converter.digitToChar(BigInteger.ZERO));
        assertEquals('a', Converter.digitToChar(BigInteger.valueOf(10)));
        assertEquals('z', Converter.digitToChar(BigInteger.valueOf(35)));
    }

    @Test
    public void charToDigitTest() {
        assertEquals(BigInteger.valueOf(10), Converter.charToDigit('a'));
        assertEquals(BigInteger.valueOf(0), Converter.charToDigit('0'));
        assertEquals(BigInteger.valueOf(35), Converter.charToDigit('z'));
    }

    @Test
    public void toDecimalTest() {
        assertEquals("8", Converter.toDecimal("1000", 2));
        assertEquals("255", Converter.toDecimal("ff", 16));
        assertEquals("29234652", Converter.toDecimal("hello", 36));
        assertEquals("1767707668033969", Converter.toDecimal("helloworld", 36));
        assertEquals("8509879461511142604590906984936860", Converter.toDecimal("holyfuckingshititworks", 36));
    }

    @Test
    public void fromDecimalTest() {
        assertEquals("ff", Converter.fromDecimal("255", 16));
        assertEquals("helloworld", Converter.fromDecimal("1767707668033969", 36));
        assertEquals("1000", Converter.fromDecimal("8", 2));
        assertEquals("holyfuckingshititworks", Converter.fromDecimal("8509879461511142604590906984936860", 36));
    }

    @Test
    public void calculateTest() {
        assertEquals("1", Converter.calculate("1", 10, 2));
        assertEquals("1", Converter.calculate("1", 2, 10));
        assertEquals("jgu0r", Converter.calculate("hello", 36, 35));
        assertEquals("MEYR3NA7T4".toLowerCase(), Converter.calculate("helloworld", 36, 35));
    }
}