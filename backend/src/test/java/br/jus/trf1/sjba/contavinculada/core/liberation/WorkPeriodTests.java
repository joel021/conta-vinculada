package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkPeriodTests {


    @Test
    public void workMonthsLessFifteenTest() {

        LocalDate startDate = LocalDate.of(2024, 1, 18);
        LocalDate endDate = LocalDate.of(2024, 6, 1);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(4, months);
    }

    @Test
    public void workMonthsGreaterFifteenTest() {

        LocalDate startDate = LocalDate.of(2024, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 1);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(5, months);
    }

    @Test
    public void workMonthsMoreThanFifteenTest() {

        LocalDate startDate = LocalDate.of(2024, 1, 18);
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(5, months);
    }

    @Test
    public void workMonthsTest() {

        LocalDate startDate = LocalDate.of(2024, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(6, months);
    }

    @Test
    public void workMonthsAfterYearTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 18);
        LocalDate endDate = LocalDate.of(2024, 6, 1);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(16, months);
    }

    @Test
    public void workMonthsAfterYearWithFifteenTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 1);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(17, months);
    }

    @Test
    public void workMonthsAfterYearPlusTwiceTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        int months = new WorkPeriod(startDate, endDate).workMonths();
        assertEquals(18, months);
    }

    @Test
    public void workYearsTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        int years = new WorkPeriod(startDate, endDate).workYears();
        assertEquals(2, years);
    }

    @Test
    public void workFullYearsTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        int years = new WorkPeriod(startDate, endDate).workFullYears();
        assertEquals(1, years);
    }

    @Test
    public void workFullYearsTwoTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 12);
        LocalDate endDate = LocalDate.of(2024, 12, 30);

        int years = new WorkPeriod(startDate, endDate).workFullYears();
        assertEquals(2, years);
    }

    @Test
    public void startDateNotOnlyEndYearTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 12);
        LocalDate endDate = LocalDate.of(2024, 12, 30);

        WorkPeriod workPeriod = new WorkPeriod(startDate, endDate);
        assertEquals(LocalDate.of(2023, 1, 12), workPeriod.getStartDate());
    }

}
