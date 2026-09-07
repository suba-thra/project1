package com.quizapp.repository;

import com.quizapp.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for Answer entity.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByAttemptId(Long attemptId);
}
