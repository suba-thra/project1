import React from 'react';
import { 
  Clock, 
  HelpCircle, 
  Award, 
  Play, 
  Trash2, 
  ListPlus, 
  Search, 
  Sparkles,
  Layers,
  BookOpen
} from 'lucide-react';

export default function QuizList({
  quizzes,
  searchTerm,
  setSearchTerm,
  onStartQuiz,
  onManageQuestions,
  onDeleteQuiz,
  onOpenCreateModal,
  onSeedDemoQuizzes,
  loading,
}) {
  return (
    <div id="quiz-list-section">
      {/* Hero Banner */}
      <section className="hero-banner">
        <div className="hero-pill">
          <Sparkles size={14} />
          <span>Interactive Quiz Experience</span>
        </div>
        <h1 className="hero-title">
          Master Your Skills with <span className="brand-gradient-text">Interactive Quizzes</span>
        </h1>
        <p className="hero-subtitle">
          Test your knowledge across Java, Spring Boot, Web Development, and more. Challenge yourself against the clock and view instant performance analytics.
        </p>
      </section>

      {/* Control Bar: Search & Quick Actions */}
      <div className="control-bar">
        <div className="search-input-wrapper">
          <Search size={18} className="search-icon" />
          <input
            id="quiz-search-input"
            type="text"
            className="search-input"
            placeholder="Search quizzes by title or topic..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div style={{ display: 'flex', gap: '0.75rem' }}>
          <button
            id="btn-seed-sample-quizzes"
            className="btn-secondary"
            onClick={onSeedDemoQuizzes}
            title="Populate pre-made quizzes with questions"
          >
            <Sparkles size={16} />
            <span>Load Demo Data</span>
          </button>

          <button
            id="btn-create-quiz-hero"
            className="btn-primary"
            onClick={onOpenCreateModal}
          >
            <Layers size={16} />
            <span>New Quiz</span>
          </button>
        </div>
      </div>

      {/* Quizzes Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-secondary)' }}>
          <div className="brand-icon-box" style={{ margin: '0 auto 1rem', animation: 'pulseGlow 1.5s infinite' }}>
            <BookOpen size={20} />
          </div>
          <p>Loading quizzes from database...</p>
        </div>
      ) : quizzes.length === 0 ? (
        <div className="empty-state" id="empty-quizzes-state">
          <div className="empty-icon-box">
            <BookOpen size={28} />
          </div>
          <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>
            {searchTerm ? 'No quizzes match your search' : 'No Quizzes Available Yet'}
          </h3>
          <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem', fontSize: '0.9rem' }}>
            {searchTerm
              ? `We couldn't find any quiz matching "${searchTerm}". Try another keyword or clear the search.`
              : 'Create your first customized quiz or click below to populate rich starter quizzes with instant questions.'}
          </p>
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
            <button
              id="btn-empty-load-demo"
              className="btn-primary"
              onClick={onSeedDemoQuizzes}
            >
              <Sparkles size={16} />
              <span>Load Ready-Made Quizzes</span>
            </button>
            <button
              id="btn-empty-create"
              className="btn-secondary"
              onClick={onOpenCreateModal}
            >
              <span>Create From Scratch</span>
            </button>
          </div>
        </div>
      ) : (
        <div className="quiz-grid" id="quizzes-container">
          {quizzes.map((quiz) => (
            <div key={quiz.id} className="quiz-card" id={`quiz-card-${quiz.id}`}>
              <div className="quiz-card-header">
                <span className="quiz-badge">Interactive</span>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  ID #{quiz.id}
                </span>
              </div>

              <h3 className="quiz-card-title">{quiz.title}</h3>
              <p className="quiz-card-desc">
                {quiz.description || 'Test your proficiency and accuracy with this timed assessment.'}
              </p>

              {/* Meta Stats */}
              <div className="quiz-meta-stats">
                <div className="meta-stat-item">
                  <span className="meta-stat-val">
                    <HelpCircle size={15} color="var(--primary)" />
                    {quiz.questionCount ?? quiz.totalQuestions ?? quiz.questions?.length ?? 0}
                  </span>
                  <span className="meta-stat-label">Questions</span>
                </div>

                <div className="meta-stat-item">
                  <span className="meta-stat-val">
                    <Clock size={15} color="var(--accent-cyan)" />
                    {quiz.timeLimitMinutes ?? quiz.timeLimit ?? 10}m
                  </span>
                  <span className="meta-stat-label">Time Limit</span>
                </div>

                <div className="meta-stat-item">
                  <span className="meta-stat-val">
                    <Award size={15} color="var(--accent-amber)" />
                    {quiz.passingScore ?? 70}%
                  </span>
                  <span className="meta-stat-label">Pass Score</span>
                </div>
              </div>

              {/* Actions */}
              <div className="quiz-card-actions">
                <button
                  id={`btn-start-quiz-${quiz.id}`}
                  className="btn-start-quiz"
                  onClick={() => onStartQuiz(quiz)}
                >
                  <Play size={16} />
                  <span>Start Challenge</span>
                </button>

                <button
                  id={`btn-manage-questions-${quiz.id}`}
                  className="btn-icon-action"
                  title="View & Add Questions"
                  onClick={() => onManageQuestions(quiz)}
                >
                  <ListPlus size={18} />
                </button>

                <button
                  id={`btn-delete-quiz-${quiz.id}`}
                  className="btn-icon-action danger"
                  title="Delete Quiz"
                  onClick={() => onDeleteQuiz(quiz.id, quiz.title)}
                >
                  <Trash2 size={18} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
