import React from 'react';
import { Sparkles, Trophy, BookOpen, PlusCircle, CheckCircle2, AlertCircle } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, onOpenCreateModal, isApiConnected }) {
  return (
    <nav className="navbar" id="main-navbar">
      <div className="nav-container">
        {/* Brand */}
        <div 
          className="brand-logo" 
          id="brand-logo-btn" 
          onClick={() => setActiveTab('quizzes')}
        >
          <div className="brand-icon-box">
            <Sparkles size={22} />
          </div>
          <span className="brand-gradient-text">QuizMaster</span>
        </div>

        {/* Navigation Tabs */}
        <div className="nav-links" id="nav-tabs-container">
          <button
            id="nav-tab-quizzes"
            className={`nav-tab-btn ${activeTab === 'quizzes' ? 'active' : ''}`}
            onClick={() => setActiveTab('quizzes')}
          >
            <BookOpen size={16} />
            <span>Quizzes</span>
          </button>
          <button
            id="nav-tab-attempts"
            className={`nav-tab-btn ${activeTab === 'attempts' ? 'active' : ''}`}
            onClick={() => setActiveTab('attempts')}
          >
            <Trophy size={16} />
            <span>Leaderboard</span>
          </button>
        </div>

        {/* Right Actions */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          {/* API Status Badge */}
          <div 
            id="api-status-indicator" 
            className="nav-status-pill"
            style={{
              background: isApiConnected ? 'rgba(16, 185, 129, 0.12)' : 'rgba(244, 63, 94, 0.12)',
              borderColor: isApiConnected ? 'rgba(16, 185, 129, 0.3)' : 'rgba(244, 63, 94, 0.3)',
              color: isApiConnected ? 'var(--accent-emerald)' : 'var(--accent-rose)'
            }}
          >
            {isApiConnected ? (
              <>
                <span className="status-dot"></span>
                <span>Backend Live</span>
              </>
            ) : (
              <>
                <AlertCircle size={14} />
                <span>Backend Offline</span>
              </>
            )}
          </div>

          {/* New Quiz Button */}
          <button
            id="btn-nav-create-quiz"
            className="btn-primary"
            onClick={onOpenCreateModal}
          >
            <PlusCircle size={18} />
            <span>Create Quiz</span>
          </button>
        </div>
      </div>
    </nav>
  );
}
