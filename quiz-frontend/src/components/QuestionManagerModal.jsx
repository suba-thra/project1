import React, { useState, useEffect } from 'react';
import { X, Plus, Trash2, CheckCircle2, HelpCircle, Sparkles } from 'lucide-react';
import { api } from '../services/api';

export default function QuestionManagerModal({ isOpen, onClose, quiz, onQuestionsUpdated }) {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [addingNew, setAddingNew] = useState(false);
  const [error, setError] = useState(null);

  // New Question Form State
  const [newQuestionText, setNewQuestionText] = useState('');
  const [points, setPoints] = useState(1);
  const [options, setOptions] = useState([
    { optionText: '', isCorrect: true },
    { optionText: '', isCorrect: false },
    { optionText: '', isCorrect: false },
    { optionText: '', isCorrect: false },
  ]);

  useEffect(() => {
    if (isOpen && quiz) {
      loadQuestions();
    }
  }, [isOpen, quiz]);

  const loadQuestions = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await api.getQuizQuestions(quiz.id);
      setQuestions(data || []);
    } catch (err) {
      setError(err.message || 'Failed to load questions');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen || !quiz) return null;

  const handleOptionTextChange = (index, value) => {
    const updated = [...options];
    updated[index].optionText = value;
    setOptions(updated);
  };

  const handleSetCorrect = (index) => {
    const updated = options.map((opt, i) => ({
      ...opt,
      isCorrect: i === index,
    }));
    setOptions(updated);
  };

  const handleAddQuestion = async (e) => {
    e.preventDefault();
    if (!newQuestionText.trim()) {
      setError('Question text is required.');
      return;
    }

    const filledOptions = options.filter((o) => o.optionText.trim());
    if (filledOptions.length < 2) {
      setError('Please provide at least 2 options.');
      return;
    }

    const hasCorrect = filledOptions.some((o) => o.isCorrect);
    if (!hasCorrect) {
      setError('Please mark at least one option as the correct answer.');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      await api.addQuestionToQuiz(quiz.id, {
        questionText: newQuestionText.trim(),
        points: Number(points),
        options: filledOptions,
      });

      // Reset form
      setNewQuestionText('');
      setPoints(1);
      setOptions([
        { optionText: '', isCorrect: true },
        { optionText: '', isCorrect: false },
        { optionText: '', isCorrect: false },
        { optionText: '', isCorrect: false },
      ]);
      setAddingNew(false);

      await loadQuestions();
      if (onQuestionsUpdated) onQuestionsUpdated();
    } catch (err) {
      setError(err.message || 'Failed to save question');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteQuestion = async (questionId) => {
    if (!window.confirm('Delete this question from the quiz?')) return;
    try {
      setLoading(true);
      await api.deleteQuestion(questionId);
      await loadQuestions();
      if (onQuestionsUpdated) onQuestionsUpdated();
    } catch (err) {
      setError(err.message || 'Failed to delete question');
    } finally {
      setLoading(false);
    }
  };

  const handleLoadSampleQuestion = () => {
    setNewQuestionText('Which HTTP method is typically used to update an existing resource idempotently in REST?');
    setPoints(1);
    setOptions([
      { optionText: 'PUT', isCorrect: true },
      { optionText: 'POST', isCorrect: false },
      { optionText: 'DELETE', isCorrect: false },
      { optionText: 'PATCH', isCorrect: false },
    ]);
  };

  return (
    <div className="modal-overlay" id="question-manager-overlay">
      <div className="modal-content" id="question-manager-modal" style={{ maxWidth: '750px' }}>
        <div className="modal-header">
          <div>
            <h3 style={{ fontSize: '1.25rem' }}>Manage Questions</h3>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
              Quiz: {quiz.title} ({questions.length} questions)
            </span>
          </div>
          <button className="btn-icon-action" onClick={onClose}>
            <X size={18} />
          </button>
        </div>

        <div className="modal-body">
          {error && (
            <div
              style={{
                background: 'rgba(244, 63, 94, 0.15)',
                border: '1px solid rgba(244, 63, 94, 0.3)',
                color: '#fda4af',
                padding: '0.75rem 1rem',
                borderRadius: 'var(--radius-md)',
                marginBottom: '1.25rem',
                fontSize: '0.85rem',
              }}
            >
              {error}
            </div>
          )}

          {/* New Question Form Accordion */}
          {addingNew ? (
            <form
              onSubmit={handleAddQuestion}
              style={{
                background: 'rgba(255, 255, 255, 0.03)',
                border: '1px solid var(--border-active)',
                borderRadius: 'var(--radius-lg)',
                padding: '1.5rem',
                marginBottom: '1.5rem',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <h4 style={{ color: 'var(--text-primary)' }}>Add New Question</h4>
                <button
                  type="button"
                  className="btn-secondary"
                  style={{ fontSize: '0.75rem', padding: '0.25rem 0.6rem' }}
                  onClick={handleLoadSampleQuestion}
                >
                  <Sparkles size={13} />
                  <span>Insert Sample</span>
                </button>
              </div>

              <div className="form-group">
                <label className="form-label">Question Text *</label>
                <textarea
                  className="form-textarea"
                  rows="2"
                  placeholder="Enter the question statement..."
                  value={newQuestionText}
                  onChange={(e) => setNewQuestionText(e.target.value)}
                  required
                />
              </div>

              <div className="form-group" style={{ maxWidth: '150px' }}>
                <label className="form-label">Points</label>
                <input
                  type="number"
                  min="1"
                  className="form-input"
                  value={points}
                  onChange={(e) => setPoints(e.target.value)}
                  required
                />
              </div>

              <label className="form-label" style={{ marginTop: '1rem' }}>
                Answer Options (select the radio to indicate the correct answer):
              </label>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', marginBottom: '1.5rem' }}>
                {options.map((opt, idx) => (
                  <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                    <input
                      type="radio"
                      name="correctOption"
                      checked={opt.isCorrect}
                      onChange={() => handleSetCorrect(idx)}
                      style={{ cursor: 'pointer', width: '18px', height: '18px' }}
                      title="Select as correct answer"
                    />
                    <span style={{ fontWeight: 600, color: 'var(--text-secondary)', width: '20px' }}>
                      {String.fromCharCode(65 + idx)}.
                    </span>
                    <input
                      type="text"
                      className="form-input"
                      placeholder={`Option ${String.fromCharCode(65 + idx)} text`}
                      value={opt.optionText}
                      onChange={(e) => handleOptionTextChange(idx, e.target.value)}
                      required={idx < 2}
                    />
                  </div>
                ))}
              </div>

              <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end' }}>
                <button
                  type="button"
                  className="btn-secondary"
                  onClick={() => setAddingNew(false)}
                >
                  Cancel
                </button>
                <button type="submit" className="btn-primary" disabled={loading}>
                  Save Question
                </button>
              </div>
            </form>
          ) : (
            <div style={{ marginBottom: '1.5rem' }}>
              <button
                id="btn-show-add-question"
                className="btn-primary"
                onClick={() => setAddingNew(true)}
              >
                <Plus size={16} />
                <span>Add Question to Quiz</span>
              </button>
            </div>
          )}

          {/* Existing Questions List */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {questions.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '2rem 0', color: 'var(--text-secondary)' }}>
                <HelpCircle size={24} style={{ margin: '0 auto 0.5rem', opacity: 0.6 }} />
                <p>No questions yet for this quiz. Click "+ Add Question to Quiz" above.</p>
              </div>
            ) : (
              questions.map((q, idx) => (
                <div
                  key={q.id}
                  style={{
                    background: 'rgba(255, 255, 255, 0.03)',
                    border: '1px solid var(--border-subtle)',
                    borderRadius: 'var(--radius-md)',
                    padding: '1.25rem',
                  }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: '1rem', marginBottom: '0.75rem' }}>
                    <div>
                      <span style={{ fontSize: '0.75rem', color: 'var(--primary)', fontWeight: 700, marginRight: '0.5rem' }}>
                        Q{idx + 1} ({q.points || 1} pt)
                      </span>
                      <strong style={{ fontSize: '1rem', color: 'var(--text-primary)' }}>{q.content || q.questionText}</strong>
                    </div>
                    <button
                      className="btn-icon-action danger"
                      onClick={() => handleDeleteQuestion(q.id)}
                      title="Delete Question"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>

                  {/* Options List */}
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem', marginTop: '0.5rem' }}>
                    {q.options?.map((opt, oIdx) => (
                      <div
                        key={opt.id || oIdx}
                        style={{
                          fontSize: '0.85rem',
                          padding: '0.5rem 0.75rem',
                          borderRadius: 'var(--radius-sm)',
                          background: opt.isCorrect ? 'rgba(16, 185, 129, 0.15)' : 'rgba(255, 255, 255, 0.02)',
                          border: `1px solid ${opt.isCorrect ? 'rgba(16, 185, 129, 0.3)' : 'var(--border-subtle)'}`,
                          color: opt.isCorrect ? 'var(--accent-emerald)' : 'var(--text-secondary)',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '0.4rem',
                        }}
                      >
                        {opt.isCorrect && <CheckCircle2 size={14} />}
                        <span>
                          {String.fromCharCode(65 + oIdx)}. {opt.optionText}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn-secondary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
