package br.jus.trf1.sjba.contavinculada.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTests {


    @Test
    public void equalsOrAfterTest() {

        LocalDate before = LocalDate.of(2023, 1, 1);
        LocalDate after = LocalDate.of(2024, 1, 1);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(after, before);
        assertTrue(equalsOrAfter);
    }

    @Test
    public void equalsOrAfterMonthBeforeTest() {

        LocalDate before = LocalDate.of(2024, 1, 1);
        LocalDate after = LocalDate.of(2024, 2, 1);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(before, after);
        assertFalse(equalsOrAfter);
    }
    
    @Test
    public void equalsOrAfterDayBeforeTest() {

        LocalDate before = LocalDate.of(2024, 1, 1);
        LocalDate after = LocalDate.of(2024, 1, 2);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(before, after);
        assertFalse(equalsOrAfter);
    }

    @Test
    public void equalsOrAfterYearAfterTest() {

        LocalDate before = LocalDate.of(2024, 2, 2);
        LocalDate after = LocalDate.of(2025, 1, 1);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(after, before);
        assertTrue(equalsOrAfter);
    }

    @Test
    public void equalsOrAfterMonthAfterTest() {

        LocalDate before = LocalDate.of(2024, 2, 2);
        LocalDate after = LocalDate.of(2024, 3, 1);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(after, before);
        assertTrue(equalsOrAfter);
    }

    @Test
    public void equalsOrAfterDayAfterTest() {

        LocalDate before = LocalDate.of(2024, 2, 2);
        LocalDate after = LocalDate.of(2024, 2, 3);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(after, before);
        assertTrue(equalsOrAfter);
    }

    @Test
    public void equalsOrAfterEqualsTest() {

        LocalDate before = LocalDate.of(2024, 2, 2);
        LocalDate after = LocalDate.of(2024, 2, 2);

        boolean equalsOrAfter = DateUtils.equalsOrAfter(after, before);
        assertTrue(equalsOrAfter);
    }

    @Test
    public void equalsOrBeforeYearBeforeTest() {

        LocalDate before = LocalDate.of(2023, 2, 2);
        LocalDate after = LocalDate.of(2024, 2, 2);

        boolean equalsOrBefore = before.isBefore(after) || before.isEqual(after);
        assertTrue(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeYearAfterTest() {

        LocalDate before = LocalDate.of(2024, 2, 2);
        LocalDate after = LocalDate.of(2025, 2, 2);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(after, before);
        assertFalse(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeMonthBeforeTest() {

        LocalDate before = LocalDate.of(2024, 1, 2);
        LocalDate after = LocalDate.of(2024, 2, 2);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(before, after);
        assertTrue(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeMonthAfterTest() {

        LocalDate before = LocalDate.of(2024, 1, 2);
        LocalDate after = LocalDate.of(2024, 3, 2);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(after, before);
        assertFalse(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeDayBeforeTest() {

        LocalDate before = LocalDate.of(2024, 1, 1);
        LocalDate after = LocalDate.of(2024, 1, 2);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(before, after);
        assertTrue(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeDayAfterTest() {

        LocalDate before = LocalDate.of(2024, 1, 2);
        LocalDate after = LocalDate.of(2024, 1, 3);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(after, before);
        assertFalse(equalsOrBefore);
    }

    @Test
    public void equalsOrBeforeEqualsTest() {

        LocalDate equals = LocalDate.of(2024, 1, 3);

        boolean equalsOrBefore = DateUtils.equalsOrBefore(equals, equals);
        assertTrue(equalsOrBefore);
    }

    @Test
    public void sameMonth() {

        LocalDate before = LocalDate.of(2024, 1, 2);
        LocalDate after = LocalDate.of(2024, 1, 3);

        boolean sameMonth = DateUtils.sameMonth(before, after);
        assertTrue(sameMonth);
    }

    @Test
    public void sameMonthFalse() {

        LocalDate before = LocalDate.of(2023, 1, 2);
        LocalDate after = LocalDate.of(2024, 1, 3);

        boolean sameMonth = DateUtils.sameMonth(before, after);
        assertFalse(sameMonth);
    }

    @Test
    public void getMaxDateTest() {

        LocalDate date = DateUtils.maxDay(LocalDate.of(2024, 2, 2));

        LocalDate expectedDate = LocalDate.of(2024, 2, 29);
        assertEquals(expectedDate, date);
    }


}
