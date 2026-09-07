import React, { useState } from 'react';
import { X, Sparkles, PlusCircle } from 'lucide-react';
import { api } from '../services/api';

export default function QuizCreatorModal({ isOpen, onClose, onQuizCreated }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    timeLimit: 10,
    passingScore: 70,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  if (!isOpen) return null;

  const handleTemplateLoad = (type) => {
    if (type === 'java') {
      setFormData({
        title: 'Core Java & OOP Fundamentals',
        description: 'Challenge your understanding of Object-Oriented Programming, Polymorphism, Collections, and Garbage Collection in Java.',
        timeLimit: 15,
        passingScore: 75,
      });
    } else if (type === 'spring') {
      setFormData({
        title: 'Spring Boot 3 & REST API Architecture',
        description: 'Test your grasp on Dependency Injection, Spring Data JPA, Hibernate cascade types, and modern microservice APIs.',
        timeLimit: 12,
        passingScore: 70,
      });
    } else if (type === 'web') {
      setFormData({
        title: 'Full-Stack Web Development Essentials',
        description: 'Evaluate your knowledge of React hooks, asynchronous JavaScript, state management, and HTTP protocols.',
        timeLimit: 10,
        passingScore: 70,
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.title.trim()) {
      setError('Please provide a quiz title.');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const created = await api.createQuiz({
        title: formData.title.trim(),
        description: formData.description.trim(),
        timeLimit: Number(formData.timeLimit),
        passingScore: Number(formData.passingScore),
      });

      onQuizCreated(created);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to create quiz');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" id="create-quiz-modal-overlay">
      <div className="modal-content" id="create-quiz-modal">
        <div className="modal-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <div className="brand-icon-box" style={{ width: '32px', height: '32px' }}>
              <PlusCircle size={18} />
            </div>
            <h3 style={{ fontSize: '1.25rem' }}>Create New Quiz</h3>
          </div>
          <button
            id="btn-close-create-quiz-modal"
            className="btn-icon-action"
            onClick={onClose}
          >
            <X size={18} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
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

            {/* Quick Template Fill */}
            <div style={{ marginBottom: '1.5rem' }}>
              <span className="form-label" style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                <Sparkles size={14} color="var(--primary)" />
                Quick Starter Templates:
              </span>
              <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                <button
                  type="button"
                  className="btn-secondary"
                  style={{ fontSize: '0.8rem', padding: '0.35rem 0.75rem' }}
                  onClick={() => handleTemplateLoad('java')}
                >
                  Java OOP
                </button>
                <button
                  type="button"
                  className="btn-secondary"
                  style={{ fontSize: '0.8rem', padding: '0.35rem 0.75rem' }}
                  onClick={() => handleTemplateLoad('spring')}
                >
                  Spring Boot 3
                </button>
                <button
                  type="button"
                  className="btn-secondary"
                  style={{ fontSize: '0.8rem', padding: '0.35rem 0.75rem' }}
                  onClick={() => handleTemplateLoad('web')}
                >
                  React & Web Dev
                </button>
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="quiz-title-input">
                Quiz Title *
              </label>
              <input
                id="quiz-title-input"
                type="text"
                className="form-input"
                placeholder="e.g. Master Spring Boot Architecture"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="quiz-desc-input">
                Description
              </label>
              <textarea
                id="quiz-desc-input"
                className="form-textarea"
                rows="3"
                placeholder="Explain what topics are covered and who this quiz is designed for..."
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label className="form-label" htmlFor="quiz-timelimit-input">
                  Time Limit (Minutes)
                </label>
                <input
                  id="quiz-timelimit-input"
                  type="number"
                  min="1"
                  max="180"
                  className="form-input"
                  value={formData.timeLimit}
                  onChange={(e) => setFormData({ ...formData, timeLimit: e.target.value })}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="quiz-passingscore-input">
                  Passing Score (%)
                </label>
                <input
                  id="quiz-passingscore-input"
                  type="number"
                  min="1"
                  max="100"
                  className="form-input"
                  value={formData.passingScore}
                  onChange={(e) => setFormData({ ...formData, passingScore: e.target.value })}
                  required
                />
              </div>
            </div>
          </div>

          <div className="modal-footer">
            <button
              id="btn-cancel-create-quiz"
              type="button"
              className="btn-secondary"
              onClick={onClose}
              disabled={loading}
            >
              Cancel
            </button>
            <button
              id="btn-save-new-quiz"
              type="submit"
              className="btn-primary"
              disabled={loading}
            >
              {loading ? 'Creating Quiz...' : 'Create Quiz'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
