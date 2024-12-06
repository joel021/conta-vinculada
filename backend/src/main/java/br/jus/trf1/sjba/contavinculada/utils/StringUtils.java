package br.jus.trf1.sjba.contavinculada.utils;

import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private final Pattern numbersPattern = Pattern.compile("\\d+");

    private final Pattern lettersAndSpaces = Pattern.compile("[/^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$/]");

    public static boolean isEmpty(String string) {

        if (string == null) {
            return true;
        }
        return string.trim().isEmpty();
    }

    public String onlyAllNumbers(String string) {

        StringBuilder numbers = new StringBuilder();
        Matcher matcher = numbersPattern.matcher(string);
        while (matcher.find()){
            numbers.append(matcher.group());
        }
        return numbers.toString();
    }

    public String onlyNumbersOrNull(String string) {

        final var numbers = onlyAllNumbers(string);
        if (isEmpty(numbers)) {
            return null;
        }
        return numbers;
    }

    public String contentOrNull(String string) {

        if (isEmpty(string)) {
            return null;
        } else {
            return string;
        }
    }

    public int integerOrDefault(String string, int d) {

        if (isEmpty(string)) {
            return d;
        }else{
            return Integer.parseInt(string.trim());
        }
    }

    public String onlyNumbersOrThrows(String string) throws NotAcceptableException {

        final var numbers = onlyAllNumbers(string);
        if (isEmpty(numbers)) {
            throw new NotAcceptableException("É permitdo apenas números.");
        }
        return numbers;
    }

    public String cleanString(String string) {

        if (isEmpty(string)) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        Matcher matcher = lettersAndSpaces.matcher(string);
        while(matcher.find()) {
            result.append(matcher.group());
        }
        return result.toString().replace("\\xFFFD", "");
    }
}
