package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.model.Testss;

@Repository
public interface TestRepository extends JpaRepository<Testss, Long> {

    List<Testss> findAllByUserId(Long userId);

}
