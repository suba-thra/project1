package com.quizapp.repository;

import com.quizapp.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for Option entity.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByQuestionId(Long questionId);
}
