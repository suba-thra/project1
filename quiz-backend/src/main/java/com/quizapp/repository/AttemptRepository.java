package com.quizapp.repository;

import com.quizapp.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for Attempt entity.
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findByUserId(Long userId);

    List<Attempt> findByQuizId(Long quizId);
}
