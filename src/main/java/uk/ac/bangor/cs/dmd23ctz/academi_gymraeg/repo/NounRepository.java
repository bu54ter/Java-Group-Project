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
     * Retrieves a random selection of 20 nouns that have NOT been copied
     * to the deleted nouns table.
     *
     * <p>This excludes any noun whose ID exists in ag_nouns_deleted,
     * ensuring only active nouns are used.</p>
     *
     * @return a list of 20 randomly selected active nouns
     */
	@Query(
		    value = """
		        SELECT *
		        FROM ag_nouns n
		        WHERE NOT EXISTS (
		            SELECT 1
		            FROM ag_deleted_nouns d
		            WHERE d.noun_id = n.noun_id
		        )
		        ORDER BY RAND()
		        LIMIT 20
		    """,
		    nativeQuery = true
		)
		List<Nouns> findRandomActiveNouns();
    
    /**
     * Retrieves all active nouns.
     *
     * <p>This query excludes any noun whose ID exists in the
     * {@link uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.NounsDeleted}
     * entity.</p>
     *
     * <p>No ordering or limit is applied.</p>
     *
     * @return a list of all active nouns
     */
	@Query("""
    	    SELECT n FROM Nouns n
    	    WHERE NOT EXISTS (
    	        SELECT 1 FROM NounsDeleted d
    	        WHERE d.nounId = n.nounId
    	    )
    	""")
    	List<Nouns> findAllActiveNouns();
    
    /**
     * Checks if any {@link Nouns} entity exists.
     *
     * <p> Check if any item in the noun table, if not an auto load will take place.</p>
     *
     * @return {@code true} if at least one record exists (if implemented correctly)
     */
	boolean existsBy();
	
	boolean existsByWelshWordIgnoreCase(String welshWord);
}