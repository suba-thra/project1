import React, { useState, useEffect, useRef } from 'react';
import confetti from 'canvas-confetti';
import { 
  Clock, 
  ArrowLeft, 
  ArrowRight, 
  CheckCircle2, 
  XCircle, 
  Trophy, 
  RotateCcw, 
  Award,
  AlertTriangle,
  HelpCircle
} from 'lucide-react';
import { api } from '../services/api';

export default function QuizTaker({ quiz, currentUserId, onBackToQuizzes, onAttemptCompleted }) {
  const [questions, setQuestions] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState({}); // { [questionId]: optionId }
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const [timeLeft, setTimeLeft] = useState(((quiz.timeLimitMinutes || quiz.timeLimit || 10) * 60)); // seconds
  const [timeExpired, setTimeExpired] = useState(false);
  const timerRef = useRef(null);

  // Load questions for this quiz
  useEffect(() => {
    async function loadQuestions() {
      try {
        setLoading(true);
        const data = await api.getQuizQuestions(quiz.id);
        setQuestions(data || []);
      } catch (err) {
        console.error('Failed to load questions:', err);
      } finally {
        setLoading(false);
      }
    }
    loadQuestions();
  }, [quiz.id]);

  // Timer countdown
  useEffect(() => {
    if (loading || result || questions.length === 0) return;

    timerRef.current = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timerRef.current);
          setTimeExpired(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timerRef.current);
  }, [loading, result, questions.length]);

  // Handle auto-submit when timer expires
  useEffect(() => {
    if (timeExpired && !result && !submitting) {
      handleSubmitQuiz();
    }
  }, [timeExpired]);

  // Trigger confetti on pass
  const triggerCelebration = () => {
    try {
      confetti({
        particleCount: 120,
        spread: 80,
        origin: { y: 0.6 },
        colors: ['#6366f1', '#10b981', '#f59e0b', '#06b6d4', '#ec4899']
      });
    } catch {
      // Confetti fallback
    }
  };

  const handleSelectOption = (questionId, optionId) => {
    if (result) return;
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionId]: optionId,
    }));
  };

  const handleSubmitQuiz = async () => {
    if (submitting || result) return;
    setSubmitting(true);
    clearInterval(timerRef.current);

    // Format answers array
    const answersPayload = questions.map((q) => ({
      questionId: q.id,
      selectedOptionId: selectedAnswers[q.id] || null,
    }));

    try {
      const attemptResponse = await api.submitAttempt({
        userId: currentUserId || 1,
        quizId: quiz.id,
        answers: answersPayload,
      });

      setResult(attemptResponse);
      if (attemptResponse.passed) {
        triggerCelebration();
      }
      if (onAttemptCompleted) {
        onAttemptCompleted();
      }
    } catch (err) {
      console.error('Failed to submit attempt:', err);
      // Fallback calculation for display if backend endpoint has minor mismatch
      let calculatedScore = 0;
      questions.forEach((q) => {
        const chosenId = selectedAnswers[q.id];
        const correctOpt = q.options?.find((o) => o.isCorrect);
        if (correctOpt && chosenId === correctOpt.id) {
          calculatedScore++;
        }
      });
      const total = questions.length;
      const pct = total > 0 ? Math.round((calculatedScore / total) * 100) : 0;
      const passed = pct >= (quiz.passingScore || 70);

      const fallbackResult = {
        score: calculatedScore,
        totalQuestions: total,
        percentage: pct,
        passed: passed,
      };
      setResult(fallbackResult);
      if (passed) triggerCelebration();
    } finally {
      setSubmitting(false);
    }
  };

  // Format seconds to MM:SS
  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-secondary)' }}>
        <p>Preparing quiz session...</p>
      </div>
    );
  }

  if (questions.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-icon-box">
          <HelpCircle size={28} />
        </div>
        <h3>No Questions in This Quiz Yet</h3>
        <p style={{ color: 'var(--text-secondary)', margin: '1rem 0 1.5rem' }}>
          This quiz doesn't have any questions configured. Add questions using the "Manage Questions" button.
        </p>
        <button className="btn-primary" onClick={onBackToQuizzes}>
          <ArrowLeft size={16} />
          <span>Back to Quizzes</span>
        </button>
      </div>
    );
  }

  // --- RESULT VIEW ---
  if (result) {
    const passed = result.passed;
    return (
      <div className="quiz-taker-container" id="quiz-result-view">
        <div className="result-card">
          <div className={`result-icon-large ${passed ? 'pass' : 'fail'}`}>
            {passed ? <Trophy size={48} /> : <Award size={48} />}
          </div>

          <h2 style={{ fontSize: '1.85rem', marginBottom: '0.5rem' }}>
            {passed ? 'Outstanding Achievement!' : 'Good Effort, Keep Practicing!'}
          </h2>
          <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem' }}>
            {passed
              ? `You passed the "${quiz.title}" assessment with flying colors!`
              : `You did not meet the ${quiz.passingScore}% passing score. Review your questions below and try again!`}
          </p>

          {/* Large Score */}
          <div className={`result-score-highlight ${passed ? 'pass' : 'fail'}`}>
            {result.percentage ?? Math.round((result.score / result.totalQuestions) * 100)}%
          </div>

          <div className={`result-status-badge ${passed ? 'pass' : 'fail'}`}>
            {passed ? 'Passed Assessment' : 'Needs Improvement'}
          </div>

          {/* Stats Row */}
          <div className="result-stats-row">
            <div className="result-stat-col">
              <span className="result-stat-val" style={{ color: 'var(--accent-emerald)' }}>
                {result.score} / {result.totalQuestions}
              </span>
              <span className="result-stat-label">Correct Answers</span>
            </div>
            <div className="result-stat-col">
              <span className="result-stat-val">
                {quiz.passingScore}%
              </span>
              <span className="result-stat-label">Passing Required</span>
            </div>
            <div className="result-stat-col">
              <span className="result-stat-val" style={{ color: 'var(--accent-cyan)' }}>
                {formatTime((quiz.timeLimit * 60) - timeLeft)}
              </span>
              <span className="result-stat-label">Time Taken</span>
            </div>
          </div>

          {/* Question Breakdown List */}
          <div style={{ textAlign: 'left', marginBottom: '2rem' }}>
            <h4 style={{ marginBottom: '1rem', color: 'var(--text-primary)' }}>Question Breakdown</h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
              {questions.map((q, qIndex) => {
                const selectedOptId = selectedAnswers[q.id];
                const correctOpt = q.options?.find((o) => o.isCorrect);
                const isCorrect = selectedOptId && correctOpt && selectedOptId === correctOpt.id;
                const selectedOpt = q.options?.find((o) => o.id === selectedOptId);

                return (
                  <div
                    key={q.id}
                    style={{
                      background: 'rgba(255, 255, 255, 0.03)',
                      border: `1px solid ${isCorrect ? 'rgba(16, 185, 129, 0.3)' : 'rgba(244, 63, 94, 0.3)'}`,
                      borderRadius: 'var(--radius-md)',
                      padding: '1rem',
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                      {isCorrect ? (
                        <CheckCircle2 size={18} color="var(--accent-emerald)" />
                      ) : (
                        <XCircle size={18} color="var(--accent-rose)" />
                      )}
                      <span style={{ fontWeight: 600, fontSize: '0.95rem' }}>
                        Q{qIndex + 1}: {q.content || q.questionText}
                      </span>
                    </div>
                    <div style={{ fontSize: '0.85rem', paddingLeft: '1.6rem', color: 'var(--text-secondary)' }}>
                      <div>Your Answer: <strong style={{ color: isCorrect ? 'var(--accent-emerald)' : 'var(--accent-rose)' }}>{selectedOpt?.optionText || '(Unanswered)'}</strong></div>
                      {!isCorrect && correctOpt && (
                        <div style={{ color: 'var(--accent-emerald)', marginTop: '2px' }}>
                          Correct Answer: <strong>{correctOpt.optionText}</strong>
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Action Buttons */}
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
            <button
              id="btn-retake-quiz"
              className="btn-secondary"
              onClick={() => {
                setResult(null);
                setSelectedAnswers({});
                setCurrentIndex(0);
                setTimeLeft((quiz.timeLimit || 10) * 60);
                setTimeExpired(false);
              }}
            >
              <RotateCcw size={16} />
              <span>Retake Quiz</span>
            </button>

            <button
              id="btn-back-to-quizzes-after-result"
              className="btn-primary"
              onClick={onBackToQuizzes}
            >
              <ArrowLeft size={16} />
              <span>Back to Quizzes</span>
            </button>
          </div>
        </div>
      </div>
    );
  }

  // --- ACTIVE QUIZ QUESTION VIEW ---
  const currentQuestion = questions[currentIndex];
  const progressPercent = ((currentIndex + 1) / questions.length) * 100;
  const isLastQuestion = currentIndex === questions.length - 1;
  const answeredCount = Object.keys(selectedAnswers).length;
  const isTimeWarning = timeLeft < 60;

  return (
    <div className="quiz-taker-container" id="active-quiz-player">
      {/* Top Header Bar: Title, Questions Counter, Timer */}
      <div className="quiz-top-bar">
        <div>
          <h3 style={{ fontSize: '1.15rem', color: 'var(--text-primary)' }}>{quiz.title}</h3>
          <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
            Answered {answeredCount} of {questions.length} questions
          </span>
        </div>

        <div className={`timer-box ${isTimeWarning ? 'warning' : ''}`} id="quiz-countdown-timer">
          <Clock size={18} />
          <span>{formatTime(timeLeft)}</span>
        </div>
      </div>

      {/* Progress Bar */}
      <div className="progress-bar-container">
        <div
          className="progress-bar-fill"
          style={{ width: `${progressPercent}%` }}
        />
      </div>

      {/* Question Card */}
      <div className="question-card" id={`question-step-${currentIndex}`}>
        <div className="question-header">
          <span className="question-number-pill">
            Question {currentIndex + 1} of {questions.length}
          </span>
          <span className="question-points-pill">
            {currentQuestion.points ?? 1} {currentQuestion.points === 1 ? 'pt' : 'pts'}
          </span>
        </div>

        <h2 className="question-text" id="current-question-text">
          {currentQuestion.content || currentQuestion.questionText}
        </h2>

        {/* Options List */}
        <div className="options-list" id="question-options-list">
          {currentQuestion.options?.map((option, optIdx) => {
            const letter = String.fromCharCode(65 + optIdx); // A, B, C, D
            const isSelected = selectedAnswers[currentQuestion.id] === option.id;

            return (
              <div
                key={option.id}
                id={`option-choice-${option.id}`}
                className={`option-item ${isSelected ? 'selected' : ''}`}
                onClick={() => handleSelectOption(currentQuestion.id, option.id)}
              >
                <div className="option-radio">
                  {isSelected && <div className="option-radio-inner" />}
                </div>
                <span className="option-letter">{letter}.</span>
                <span className="option-text">{option.optionText}</span>
              </div>
            );
          })}
        </div>
      </div>

      {/* Navigation Footer */}
      <div className="quiz-navigation-footer">
        <button
          id="btn-quiz-prev-question"
          className="btn-secondary"
          onClick={() => setCurrentIndex((prev) => Math.max(0, prev - 1))}
          disabled={currentIndex === 0}
          style={{ opacity: currentIndex === 0 ? 0.4 : 1 }}
        >
          <ArrowLeft size={16} />
          <span>Previous</span>
        </button>

        <div style={{ display: 'flex', gap: '0.75rem' }}>
          <button
            id="btn-quit-quiz"
            className="btn-secondary"
            onClick={() => {
              if (window.confirm('Are you sure you want to exit? Your progress in this quiz session will be lost.')) {
                onBackToQuizzes();
              }
            }}
          >
            <span>Exit</span>
          </button>

          {isLastQuestion ? (
            <button
              id="btn-submit-quiz-final"
              className="btn-primary"
              onClick={handleSubmitQuiz}
              disabled={submitting}
            >
              <CheckCircle2 size={16} />
              <span>{submitting ? 'Calculating Score...' : 'Submit Quiz'}</span>
            </button>
          ) : (
            <button
              id="btn-quiz-next-question"
              className="btn-primary"
              onClick={() => setCurrentIndex((prev) => Math.min(questions.length - 1, prev + 1))}
            >
              <span>Next</span>
              <ArrowRight size={16} />
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
