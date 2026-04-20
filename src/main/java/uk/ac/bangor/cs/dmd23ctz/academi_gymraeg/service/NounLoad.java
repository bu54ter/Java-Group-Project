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

/**
 * Configuration class responsible for loading initial noun data from a CSV
 * file.
 *
 * <p>
 * This runs automatically on application startup using a
 * {@link CommandLineRunner}. If noun data already exists in the database, the
 * CSV import is skipped to prevent duplicates.
 * </p>
 *
 * <p>
 * The CSV file is expected to be located in the classpath and contain columns
 * such as: welshWord, englishWord, welshSent, englishSent, and gender.
 * </p>
 */
@Configuration
public class NounLoad {

	/**
	 * Loads nouns from a CSV file into the database on application startup.
	 *
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Checks if noun data already exists</li>
	 * <li>Skips loading if data is present</li>
	 * <li>Otherwise reads from CSV and persists all records</li>
	 * </ul>
	 *
	 * @param nounRepository the repository used to persist noun data
	 * @return a {@link CommandLineRunner} that executes on startup
	 */
	@Bean
	CommandLineRunner loadNounsFromCsv(NounRepository nounRepository) {
		return args -> {

			// Skip loading if nouns already exist in the database
			if (nounRepository.existsBy()) {
				System.out.println("Nouns already exist. Skipping CSV load.");
				return;
			}

			// Read nouns from CSV file
			List<Nouns> nouns = readCsv("data/InitialNouns.csv");

			// Persist all loaded nouns
			nounRepository.saveAll(nouns);

			System.out.println("Loaded " + nouns.size() + " nouns from CSV.");
		};
	}

	/**
	 * Reads noun data from a CSV file and converts it into a list of {@link Nouns}.
	 *
	 * <p>
	 * This method parses the CSV file using Apache Commons CSV, maps each record to
	 * a {@link Nouns} object, and applies basic transformations such as trimming
	 * and handling optional fields.
	 * </p>
	 *
	 * @param path the classpath location of the CSV file
	 * @return a list of populated {@link Nouns} entities
	 * @throws IOException if the file cannot be read
	 */
	private List<Nouns> readCsv(String path) throws IOException {

		List<Nouns> list = new ArrayList<>();
		ClassPathResource resource = new ClassPathResource(path);

		try (
				// Open file input stream from classpath
				InputStream is = resource.getInputStream();

				// Wrap in reader with UTF-8 encoding
				Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

			// Parse CSV with header row and trimmed values
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
					.setTrim(true).build().parse(reader);

			for (CSVRecord record : records) {

				Nouns noun = new Nouns();

				// Map basic fields
				noun.setWelshWord(record.get("welshWord"));
				noun.setEnglishWord(record.get("englishWord"));

				// Handle optional sentence fields safely
				noun.setWelshSent(emptyToNull(record.get("welshSent")));
				noun.setEnglishSent(emptyToNull(record.get("englishSent")));

				// Convert gender string to enum (uppercase required)
				noun.setGender(Gender.valueOf(record.get("gender").toUpperCase()));

				// Set audit fields
				noun.setCreatedAt(LocalDateTime.now());
				noun.setCreatedBy("Auto Load");

				// Optional audit field
				noun.setEditedBy(null);

				list.add(noun);
			}
		}
		return list;
	}
	/**
	 * Converts empty or blank strings to {@code null}.
	 *
	 * <p>
	 * This helps avoid storing empty strings in the database for optional fields.
	 * </p>
	 *
	 * @param value the input string
	 * @return {@code null} if the value is blank, otherwise the original string
	 */
	private String emptyToNull(String value) {
		// Return null if value is null or contains only whitespace
		return (value == null || value.isBlank()) ? null : value;
	}
}