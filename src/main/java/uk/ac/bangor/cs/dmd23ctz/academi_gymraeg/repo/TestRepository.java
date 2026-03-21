package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Tests;

@Repository
public interface TestRepository extends JpaRepository<Tests, Long> {

    List<Tests> findAllByUserId(Long userId);

}
