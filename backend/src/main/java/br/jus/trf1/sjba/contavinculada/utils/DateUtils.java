package br.jus.trf1.sjba.contavinculada.utils;
import java.time.LocalDate;
import java.util.Calendar;

public class DateUtils {

    public static LocalDate maxDay(LocalDate date) {

        Calendar dateCalendar = fromLocalDate(date);
        dateCalendar.set(Calendar.DAY_OF_MONTH, dateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return LocalDate.of(
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH) + 1,
                dateCalendar.get(Calendar.DAY_OF_MONTH));
    }
    public static boolean equalsOrAfter(LocalDate dateA, LocalDate dateB) {
        return !dateA.isBefore(dateB);
    }

    public static LocalDate getMinDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    public static boolean sameMonth(LocalDate dateA, LocalDate dateB) {
        return dateA.getYear() == dateB.getYear()
                && dateA.getMonth() == dateB.getMonth();
    }

    public static boolean equalsOrBefore(LocalDate dateA, LocalDate dateB) {
        return !dateA.isAfter(dateB);
    }

    public static LocalDate fromCalendar(Calendar date) {
        return LocalDate.of(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar fromLocalDate(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear(), localDate.getMonth().getValue()-1, localDate.getDayOfMonth());
        return calendar;
    }

}