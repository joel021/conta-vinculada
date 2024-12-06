package br.jus.trf1.sjba.contavinculada.core.liberation;

import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;


public class PeriodWorkIteratorTests {

    private static LocalDate endDate;

    private static LocalDate startDate;

    @BeforeAll
    public static void setup() {

        startDate = LocalDate.of(2021, 11, 8);
        endDate = LocalDate.of(2021, 12, 31);
    }

}
