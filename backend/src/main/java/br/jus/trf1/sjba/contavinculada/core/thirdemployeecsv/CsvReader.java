package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public List<String> readCsvFileAsLinesFromResources(String fileName) throws IOException {

        List<String> lines = new ArrayList<>();
        final InputStream inputStream = ContratosTerceirizadosFromCsv.class.getResourceAsStream("/"+fileName);

        if (inputStream == null) {
            throw new IOException("O arquivo não foi possível ler o arquivo.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        while(reader.ready()) {
            lines.add(reader.readLine());
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
