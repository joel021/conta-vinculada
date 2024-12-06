package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.utils.DateUtils;

import java.time.LocalDate;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.maxDay;

public class PeriodWorkIterator {

    private final LocalDate endDate;
    private final LocalDate startDate;
    private LocalDate currentDate;

    public PeriodWorkIterator(LocalDate from, LocalDate until) {

        endDate = until;
        currentDate = from;
        this.startDate = currentDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean hasNext() {

        return DateUtils.equalsOrBefore(currentDate, endDate)
                || DateUtils.sameMonth(currentDate, endDate);
    }

    public LocalDate next() {

        LocalDate nextDate;

        if (DateUtils.sameMonth(currentDate, endDate)) {
            nextDate = endDate;
        } else {
            currentDate = maxDay(currentDate);
            nextDate = currentDate;
        }
        currentDate = currentDate.plusMonths(1);

        return nextDate;
    }

}
