package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Gender;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo.NounRepository;

@Configuration
public class NounLoad {

    @Bean
    CommandLineRunner loadNounsFromCsv(NounRepository nounRepository) {
        return args -> {

            if (nounRepository.existsBy()) {
                System.out.println("Nouns already exist. Skipping CSV load.");
                return;
            }

            List<Nouns> nouns = readCsv("data/InitialNouns.csv");
            nounRepository.saveAll(nouns);

            System.out.println("Loaded " + nouns.size() + " nouns from CSV.");
        };
    }

    private List<Nouns> readCsv(String path) throws IOException {

        List<Nouns> list = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(path);

        try (
            InputStream is = resource.getInputStream();
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : records) {

                Nouns noun = new Nouns();

                noun.setWelshWord(record.get("welshWord"));
                noun.setEnglishWord(record.get("englishWord"));

                // Handle optional sentences safely
                noun.setWelshSent(emptyToNull(record.get("welshSent")));
                noun.setEnglishSent(emptyToNull(record.get("englishSent")));

                noun.setGender(Gender.valueOf(record.get("gender").toUpperCase()));

                // 🔹 Your requirements
                noun.setCreatedAt(LocalDateTime.now());
                noun.setCreatedBy("Auto Load");

                // Optional
                noun.setEditedBy(null);

                list.add(noun);
            }
        }

        return list;
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
