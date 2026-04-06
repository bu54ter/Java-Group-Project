package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Nouns;

/**
 * Repository interface for managing {@link Nouns} entities.
 *
 * <p>This repository provides standard CRUD operations via
 * {@link JpaRepository}, as well as a custom query to retrieve
 * a random selection of nouns from the database.</p>
 *
 * <p>The random selection is typically used for generating tests,
 * quizzes, or exercises where varied data is required.</p>
 *
 * <p><b>Note:</b> The custom query uses a native SQL statement
 * with database-specific functionality (RAND()), which may
 * impact portability across different database systems.</p>
 */
@Repository
public interface NounRepository extends JpaRepository<Nouns, Long> {

    /**
     * Retrieves a random selection of 20 nouns from the database.
     *
     * <p>This method uses a native SQL query with ORDER BY RAND()
     * to shuffle the results and LIMIT to restrict the number of
     * returned records.</p>
     *
     * <p>This is useful for generating dynamic and varied content
     * such as quizzes or learning exercises.</p>
     *
     * @return a list of 20 randomly selected nouns
     */
    @Query(
        value = "SELECT * FROM ag_nouns ORDER BY RAND() LIMIT 20",
        nativeQuery = true
    )
    List<Nouns> findRandomNouns();
}