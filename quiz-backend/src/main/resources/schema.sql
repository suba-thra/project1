-- =============================================================================
-- ONLINE QUIZ APPLICATION - DATABASE SCHEMA (3NF NORMALIZED)
-- Target DBMS: MySQL 8.0+
-- =============================================================================
-- Normalization Analysis:
-- 1NF (First Normal Form):
--   - All table rows contain atomic (single-valued) columns.
--   - No repeating groups or multivalued attributes (e.g. options are normalized
--     into their own table rather than comma-separated lists).
-- 2NF (Second Normal Form):
--   - In 1NF and all non-key columns are fully functionally dependent on the entire
--     primary key (every table uses an atomic surrogate primary key 'id').
-- 3NF (Third Normal Form):
--   - In 2NF and there are no transitive functional dependencies (non-key columns
--     depend only on the primary key, never on other non-key columns).
-- =============================================================================

CREATE DATABASE IF NOT EXISTS quiz_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quiz_db;

-- 1. USERS TABLE
-- Stores credentials and roles for students/takers and quiz administrators.
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_username (username),
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. QUIZZES TABLE
-- Stores quiz metadata created by instructors/admins.
CREATE TABLE IF NOT EXISTS quizzes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    time_limit_minutes INT NOT NULL DEFAULT 10,
    passing_score INT NOT NULL DEFAULT 60,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_quizzes_created_by FOREIGN KEY (created_by) 
        REFERENCES users (id) ON DELETE RESTRICT,
    INDEX idx_quizzes_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. QUESTIONS TABLE
-- Stores individual quiz questions linked to a specific quiz.
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    points INT NOT NULL DEFAULT 1,
    question_type VARCHAR(30) NOT NULL DEFAULT 'SINGLE_CHOICE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_questions_quiz_id FOREIGN KEY (quiz_id) 
        REFERENCES quizzes (id) ON DELETE CASCADE,
    INDEX idx_questions_quiz_id (quiz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. OPTIONS TABLE
-- Stores choices/options for each question, including correctness flag.
CREATE TABLE IF NOT EXISTS options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_options_question_id FOREIGN KEY (question_id) 
        REFERENCES questions (id) ON DELETE CASCADE,
    INDEX idx_options_question_id (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. ATTEMPTS TABLE
-- Tracks each quiz attempt session taken by a user.
CREATE TABLE IF NOT EXISTS attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    score INT DEFAULT 0,
    passed BOOLEAN DEFAULT FALSE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    CONSTRAINT fk_attempts_user_id FOREIGN KEY (user_id) 
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_attempts_quiz_id FOREIGN KEY (quiz_id) 
        REFERENCES quizzes (id) ON DELETE CASCADE,
    INDEX idx_attempts_user_id (user_id),
    INDEX idx_attempts_quiz_id (quiz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. ANSWERS TABLE
-- Records the user's selected choice(s) for each question in an attempt.
CREATE TABLE IF NOT EXISTS answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT NOT NULL,
    CONSTRAINT fk_answers_attempt_id FOREIGN KEY (attempt_id) 
        REFERENCES attempts (id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_question_id FOREIGN KEY (question_id) 
        REFERENCES questions (id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_selected_option_id FOREIGN KEY (selected_option_id) 
        REFERENCES options (id) ON DELETE CASCADE,
    INDEX idx_answers_attempt_id (attempt_id),
    INDEX idx_answers_question_id (question_id),
    INDEX idx_answers_option_id (selected_option_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
