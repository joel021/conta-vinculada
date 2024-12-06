package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.liberation.PeriodWorkIterator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorkPeriod {

    private final LocalDate startDate;
    private LocalDate endDate;
    private int workMonths;

    public WorkPeriod(LocalDate startDate, LocalDate endDate) {

        this.startDate = startDate;
        this.endDate = endDate;

        workMonths();
    }

    public PeriodWorkIterator iterator() {

        return new PeriodWorkIterator(
                getStartDate(),
                getEndDate());
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        workMonths();
    }

    public int workMonths() {

        final int monthsEndDate = monthsCalendar(endDate) - 1;
        final int monthsStartDate = monthsCalendar(startDate);
        workMonths = (monthsEndDate - monthsStartDate) + firstMonthFifteen() + endDayGreaterThanFifteen();
        return workMonths;
    }

    public int workYears() {
        return endDate.getYear() - startDate.getYear() + 1;
    }

    public int workFullYears() {
        return workMonths() / 12;
    }

    public int endDayGreaterThanFifteen() {

        return endDate.getDayOfMonth() + 1 >= 15 ? 1 : 0;
    }

    public int firstMonthFifteen() {

        final int diffDaysStartMonth = startDate.getMonth().maxLength() - startDate.getDayOfMonth() + 1;

        return diffDaysStartMonth >= 15 ? 1: 0;
    }

    public int monthsCalendar(LocalDate date) {
        return 12 * date.getYear() + date.getMonth().getValue();
    }

}
