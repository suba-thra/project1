package com.quizapp.repository;

import com.quizapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for Question entity.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuizId(Long quizId);
}
