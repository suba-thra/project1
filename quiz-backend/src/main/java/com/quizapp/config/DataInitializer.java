package com.quizapp.config;

import com.quizapp.entity.Option;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.User;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;
import com.quizapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Automatically seeds initial user, quizzes, and questions on startup if database is empty.
 * Ensures the application is immediately interactive when deployed to cloud platforms like Render.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public DataInitializer(UserRepository userRepository,
                           QuizRepository quizRepository,
                           QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User("admin", "admin@quizapp.com", "password123", "ROLE_ADMIN");
            admin = userRepository.save(admin);

            if (quizRepository.count() == 0) {
                // 1. Spring Boot Quiz
                Quiz springQuiz = new Quiz(
                        "Spring Boot 3 & Microservices Architecture",
                        "Comprehensive assessment on Spring Data JPA, IoC container, RESTful conventions, and security.",
                        10,
                        70,
                        admin
                );
                springQuiz = quizRepository.save(springQuiz);

                Question q1 = new Question("Which Spring annotation is used to designate a class as a RESTful web service controller?", 1, "SINGLE_CHOICE", springQuiz);
                q1.addOption(new Option("@RestController", true));
                q1.addOption(new Option("@Controller", false));
                q1.addOption(new Option("@Component", false));
                q1.addOption(new Option("@Service", false));
                questionRepository.save(q1);

                Question q2 = new Question("What is the primary benefit of Spring Data JPA repository interfaces?", 1, "SINGLE_CHOICE", springQuiz);
                q2.addOption(new Option("Automatic CRUD methods and dynamic query execution without boilerplate SQL", true));
                q2.addOption(new Option("Compiling Java directly to machine code", false));
                q2.addOption(new Option("Styling browser CSS pages", false));
                q2.addOption(new Option("Managing system hardware drivers", false));
                questionRepository.save(q2);

                Question q3 = new Question("Which HTTP status code represents successful resource creation in REST APIs?", 1, "SINGLE_CHOICE", springQuiz);
                q3.addOption(new Option("201 Created", true));
                q3.addOption(new Option("200 OK", false));
                q3.addOption(new Option("204 No Content", false));
                q3.addOption(new Option("404 Not Found", false));
                questionRepository.save(q3);

                // 2. Java OOP Quiz
                Quiz javaQuiz = new Quiz(
                        "Java Core & Modern OOP Fundamentals",
                        "Test your understanding of interfaces, garbage collection, exceptions, and the Java Memory Model.",
                        8,
                        65,
                        admin
                );
                javaQuiz = quizRepository.save(javaQuiz);

                Question jq1 = new Question("In Java, which keyword is used to inherit a class?", 1, "SINGLE_CHOICE", javaQuiz);
                jq1.addOption(new Option("extends", true));
                jq1.addOption(new Option("implements", false));
                jq1.addOption(new Option("inherits", false));
                jq1.addOption(new Option("instanceof", false));
                questionRepository.save(jq1);

                Question jq2 = new Question("Which data structure in Java provides key-value mappings with average O(1) retrieval time?", 1, "SINGLE_CHOICE", javaQuiz);
                jq2.addOption(new Option("HashMap", true));
                jq2.addOption(new Option("ArrayList", false));
                jq2.addOption(new Option("LinkedList", false));
                jq2.addOption(new Option("TreeSet", false));
                questionRepository.save(jq2);
            }
        }
    }
}
