import React, { useState, useEffect } from 'react';
import { Trophy, CheckCircle2, XCircle, RefreshCw, Calendar, Award } from 'lucide-react';
import { api } from '../services/api';

export default function AttemptsView({ onTakeQuizPrompt }) {
  const [attempts, setAttempts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAttempts();
  }, []);

  const loadAttempts = async () => {
    try {
      setLoading(true);
      const data = await api.getAllAttempts();
      setAttempts(data || []);
    } catch (err) {
      console.error('Failed to load attempts:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (isoString) => {
    if (!isoString) return 'Just now';
    try {
      const date = new Date(isoString);
      return date.toLocaleDateString(undefined, {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return isoString;
    }
  };

  return (
    <div id="attempts-history-section">
      <div className="hero-banner" style={{ padding: '1.5rem 1rem' }}>
        <div className="hero-pill">
          <Trophy size={14} />
          <span>Performance & Score History</span>
        </div>
        <h1 className="hero-title">
          Quiz <span className="brand-gradient-text">Leaderboard & Results</span>
        </h1>
        <p className="hero-subtitle">
          Review historical quiz attempts, track overall pass rates, and monitor student proficiency over time.
        </p>
      </div>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '1.25rem' }}>
        <button
          id="btn-refresh-attempts"
          className="btn-secondary"
          onClick={loadAttempts}
          disabled={loading}
        >
          <RefreshCw size={15} className={loading ? 'spinning' : ''} />
          <span>Refresh Records</span>
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-secondary)' }}>
          <p>Loading historical records...</p>
        </div>
      ) : attempts.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon-box">
            <Award size={28} />
          </div>
          <h3>No Quiz Attempts Recorded Yet</h3>
          <p style={{ color: 'var(--text-secondary)', margin: '1rem 0 1.5rem' }}>
            Take any quiz to see your score, percentage, and pass/fail evaluation logged here in real-time.
          </p>
          <button className="btn-primary" onClick={onTakeQuizPrompt}>
            <span>Browse Quizzes</span>
          </button>
        </div>
      ) : (
        <div className="attempts-table-card">
          <table className="attempts-table" id="attempts-history-table">
            <thead>
              <tr>
                <th>Attempt #</th>
                <th>Quiz Name</th>
                <th>Score</th>
                <th>Percentage</th>
                <th>Status</th>
                <th>Completed At</th>
              </tr>
            </thead>
            <tbody>
              {attempts.map((att) => {
                const passed = att.passed;
                return (
                  <tr key={att.id} id={`attempt-row-${att.id}`}>
                    <td style={{ fontWeight: 600, color: 'var(--primary)' }}>
                      #{att.id}
                    </td>
                    <td style={{ fontWeight: 600, color: 'var(--text-primary)' }}>
                      {att.quizTitle || `Quiz #${att.quizId}`}
                    </td>
                    <td>
                      <span style={{ fontWeight: 700, color: 'var(--text-primary)' }}>
                        {att.score}
                      </span>{' '}
                      / {att.totalQuestions}
                    </td>
                    <td style={{ fontWeight: 700 }}>
                      <span style={{ color: passed ? 'var(--accent-emerald)' : 'var(--accent-rose)' }}>
                        {att.percentage ?? Math.round((att.score / (att.totalQuestions || 1)) * 100)}%
                      </span>
                    </td>
                    <td>
                      <span
                        className={`result-status-badge ${passed ? 'pass' : 'fail'}`}
                        style={{ padding: '0.2rem 0.65rem', fontSize: '0.75rem', marginBottom: 0 }}
                      >
                        {passed ? 'Passed' : 'Failed'}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', fontSize: '0.85rem' }}>
                        <Calendar size={13} color="var(--text-muted)" />
                        <span>{formatDate(att.completedAt)}</span>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
