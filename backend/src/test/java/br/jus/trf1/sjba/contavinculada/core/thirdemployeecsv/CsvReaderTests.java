package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CsvReaderTests {

    private final String FILE_NAME = "application.properties";

    private static CsvReader csvReader;

    @BeforeAll
    public static void setup() {
        csvReader = new CsvReader();
    }

    @Test
    public void readCsvFileAsLinesFromResourcesTest() throws IOException {

        var lines = csvReader.readCsvFileAsLinesFromResources(FILE_NAME);
        assertFalse(lines.isEmpty());
    }
}
