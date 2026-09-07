package com.quizapp.repository;

import com.quizapp.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for Quiz entity.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCreatedById(Long userId);

    List<Quiz> findByTitleContainingIgnoreCase(String keyword);
}
