import React, { useState, useEffect, useCallback } from 'react';
import Navbar from './components/Navbar';
import QuizList from './components/QuizList';
import QuizTaker from './components/QuizTaker';
import AttemptsView from './components/AttemptsView';
import QuizCreatorModal from './components/QuizCreatorModal';
import QuestionManagerModal from './components/QuestionManagerModal';
import { api } from './services/api';

export default function App() {
  const [activeTab, setActiveTab] = useState('quizzes'); // 'quizzes' | 'attempts'
  const [quizzes, setQuizzes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loadingQuizzes, setLoadingQuizzes] = useState(true);
  const [isApiConnected, setIsApiConnected] = useState(false);

  // Active Quiz Taker state
  const [activeQuizForTaking, setActiveQuizForTaking] = useState(null);

  // Modals state
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [managingQuestionsQuiz, setManagingQuestionsQuiz] = useState(null);

  // Toast Notification
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (message) => {
    setToastMessage(message);
    setTimeout(() => {
      setToastMessage(null);
    }, 4000);
  };

  // Health check
  const verifyBackendHealth = useCallback(async () => {
    try {
      await api.checkHealth();
      setIsApiConnected(true);
    } catch {
      setIsApiConnected(false);
    }
  }, []);

  // Fetch quizzes
  const loadQuizzes = useCallback(async (search = '') => {
    try {
      setLoadingQuizzes(true);
      const data = await api.getQuizzes(search);
      setQuizzes(data || []);
      setIsApiConnected(true);
    } catch (err) {
      console.error('Failed to fetch quizzes:', err);
      setIsApiConnected(false);
    } finally {
      setLoadingQuizzes(false);
    }
  }, []);

  // Initial load
  useEffect(() => {
    verifyBackendHealth();
    loadQuizzes();
    const healthInterval = setInterval(verifyBackendHealth, 15000);
    return () => clearInterval(healthInterval);
  }, [verifyBackendHealth, loadQuizzes]);

  // Debounced search
  useEffect(() => {
    const handler = setTimeout(() => {
      loadQuizzes(searchTerm);
    }, 300);
    return () => clearTimeout(handler);
  }, [searchTerm, loadQuizzes]);

  // Create demo questions helper
  const handleSeedDemoQuizzes = async () => {
    try {
      showToast('Creating rich starter quizzes & questions...');
      
      // 1. Create Spring Boot Quiz
      const springQuiz = await api.createQuiz({
        title: 'Spring Boot 3 & Microservices Architecture',
        description: 'Comprehensive assessment on Spring Data JPA, IoC container, RESTful conventions, and security.',
        timeLimit: 10,
        passingScore: 70,
      });

      // Add Questions to Spring Quiz
      await api.addQuestionToQuiz(springQuiz.id, {
        questionText: 'Which Spring annotation is used to designate a class as a RESTful web service controller?',
        points: 1,
        options: [
          { optionText: '@RestController', isCorrect: true },
          { optionText: '@Controller', isCorrect: false },
          { optionText: '@Component', isCorrect: false },
          { optionText: '@Service', isCorrect: false },
        ],
      });

      await api.addQuestionToQuiz(springQuiz.id, {
        questionText: 'What is the purpose of Spring Data JPA repository interfaces (e.g., JpaRepository)?',
        points: 1,
        options: [
          { optionText: 'To automatically provide standard CRUD methods and query execution without boilerplate SQL', isCorrect: true },
          { optionText: 'To compile Java bytecode directly into C++', isCorrect: false },
          { optionText: 'To format HTML and CSS web views', isCorrect: false },
          { optionText: 'To encrypt local hard drive partitions', isCorrect: false },
        ],
      });

      await api.addQuestionToQuiz(springQuiz.id, {
        questionText: 'Which HTTP status code is typically returned upon successful creation of a new database entity?',
        points: 1,
        options: [
          { optionText: '201 Created', isCorrect: true },
          { optionText: '200 OK', isCorrect: false },
          { optionText: '204 No Content', isCorrect: false },
          { optionText: '404 Not Found', isCorrect: false },
        ],
      });

      // 2. Create Java Core Quiz
      const javaQuiz = await api.createQuiz({
        title: 'Java Core & Modern OOP Fundamentals',
        description: 'Test your understanding of interfaces, garbage collection, exceptions, and the Java Memory Model.',
        timeLimit: 8,
        passingScore: 65,
      });

      await api.addQuestionToQuiz(javaQuiz.id, {
        questionText: 'In Java, which keyword is used to inherit a class?',
        points: 1,
        options: [
          { optionText: 'extends', isCorrect: true },
          { optionText: 'implements', isCorrect: false },
          { optionText: 'inherits', isCorrect: false },
          { optionText: 'instanceof', isCorrect: false },
        ],
      });

      await api.addQuestionToQuiz(javaQuiz.id, {
        questionText: 'Which data structure in Java provides key-value mappings and average O(1) retrieval time?',
        points: 1,
        options: [
          { optionText: 'HashMap', isCorrect: true },
          { optionText: 'ArrayList', isCorrect: false },
          { optionText: 'LinkedList', isCorrect: false },
          { optionText: 'TreeSet', isCorrect: false },
        ],
      });

      await loadQuizzes(searchTerm);
      showToast('Demo quizzes with interactive questions ready!');
    } catch (err) {
      console.error('Failed to seed demo data:', err);
      showToast('Error seeding demo data: ' + err.message);
    }
  };

  const handleDeleteQuiz = async (quizId, quizTitle) => {
    if (!window.confirm(`Are you sure you want to delete the quiz "${quizTitle}"? All associated questions will be removed.`)) {
      return;
    }
    try {
      await api.deleteQuiz(quizId);
      showToast(`Quiz "${quizTitle}" successfully deleted.`);
      loadQuizzes(searchTerm);
    } catch (err) {
      alert('Failed to delete quiz: ' + err.message);
    }
  };

  return (
    <div className="app-layout">
      {/* Top Navbar */}
      <Navbar
        activeTab={activeTab}
        setActiveTab={(tab) => {
          setActiveTab(tab);
          setActiveQuizForTaking(null);
        }}
        onOpenCreateModal={() => setIsCreateModalOpen(true)}
        isApiConnected={isApiConnected}
      />

      {/* Main Content Area */}
      <main className="main-content">
        {activeQuizForTaking ? (
          <QuizTaker
            quiz={activeQuizForTaking}
            currentUserId={1}
            onBackToQuizzes={() => setActiveQuizForTaking(null)}
            onAttemptCompleted={() => {
              // Refresh quiz counts or attempts
            }}
          />
        ) : activeTab === 'quizzes' ? (
          <QuizList
            quizzes={quizzes}
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
            onStartQuiz={(quiz) => setActiveQuizForTaking(quiz)}
            onManageQuestions={(quiz) => setManagingQuestionsQuiz(quiz)}
            onDeleteQuiz={handleDeleteQuiz}
            onOpenCreateModal={() => setIsCreateModalOpen(true)}
            onSeedDemoQuizzes={handleSeedDemoQuizzes}
            loading={loadingQuizzes}
          />
        ) : (
          <AttemptsView
            onTakeQuizPrompt={() => {
              setActiveTab('quizzes');
              setActiveQuizForTaking(null);
            }}
          />
        )}
      </main>

      {/* Modal: Create Quiz */}
      <QuizCreatorModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onQuizCreated={(newQuiz) => {
          showToast(`Quiz "${newQuiz.title}" created successfully!`);
          loadQuizzes(searchTerm);
          // Automatically offer to add questions
          setManagingQuestionsQuiz(newQuiz);
        }}
      />

      {/* Modal: Manage Questions */}
      <QuestionManagerModal
        isOpen={!!managingQuestionsQuiz}
        quiz={managingQuestionsQuiz}
        onClose={() => {
          setManagingQuestionsQuiz(null);
          loadQuizzes(searchTerm);
        }}
        onQuestionsUpdated={() => {
          loadQuizzes(searchTerm);
        }}
      />

      {/* Toast Notification */}
      {toastMessage && (
        <div className="toast-notice" id="app-toast-alert">
          <span>{toastMessage}</span>
        </div>
      )}
    </div>
  );
}
