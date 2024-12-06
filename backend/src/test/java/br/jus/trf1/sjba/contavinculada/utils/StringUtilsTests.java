package br.jus.trf1.sjba.contavinculada.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTests {

    @Test
    public void cleanStringTest() {

        StringUtils stringUtils = new StringUtils();
        String string = " ,  Hello / S...";
        String cleanned = stringUtils.cleanString(string);
        assertEquals(" Hello / S", cleanned);
    }

}
