// API client for QuizMaster Backend
const BASE_URL = '/api/v1';

async function handleResponse(response) {
  if (!response.ok) {
    let errorMessage = `HTTP Error ${response.status}`;
    try {
      const errorData = await response.json();
      if (errorData.message) {
        errorMessage = errorData.message;
      } else if (errorData.errors && typeof errorData.errors === 'object') {
        errorMessage = Object.values(errorData.errors).join(', ');
      }
    } catch {
      // Non-json error
    }
    throw new Error(errorMessage);
  }
  if (response.status === 204) {
    return null;
  }
  const json = await response.json();
  return json.data !== undefined ? json.data : json;
}

export const api = {
  // Health
  checkHealth: async () => {
    return handleResponse(await fetch(`${BASE_URL}/health`));
  },

  // Users
  getUsers: async () => {
    return handleResponse(await fetch(`${BASE_URL}/users`));
  },
  createUser: async (userData) => {
    return handleResponse(
      await fetch(`${BASE_URL}/users`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
      })
    );
  },

  // Quizzes
  getQuizzes: async (search = '') => {
    const url = search ? `${BASE_URL}/quizzes?search=${encodeURIComponent(search)}` : `${BASE_URL}/quizzes`;
    return handleResponse(await fetch(url));
  },
  getQuizById: async (id) => {
    return handleResponse(await fetch(`${BASE_URL}/quizzes/${id}`));
  },
  createQuiz: async (quizData) => {
    const payload = {
      title: quizData.title,
      description: quizData.description || '',
      timeLimitMinutes: Number(quizData.timeLimitMinutes || quizData.timeLimit || 10),
      passingScore: Number(quizData.passingScore || 70),
      createdById: quizData.createdById || 1,
      questions: quizData.questions || [],
    };
    return handleResponse(
      await fetch(`${BASE_URL}/quizzes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
    );
  },
  deleteQuiz: async (id) => {
    return handleResponse(
      await fetch(`${BASE_URL}/quizzes/${id}`, {
        method: 'DELETE',
      })
    );
  },

  // Questions
  getQuizQuestions: async (quizId) => {
    return handleResponse(await fetch(`${BASE_URL}/quizzes/${quizId}/questions`));
  },
  addQuestionToQuiz: async (quizId, questionData) => {
    const payload = {
      content: questionData.content || questionData.questionText,
      points: Number(questionData.points || 1),
      questionType: questionData.questionType || 'SINGLE_CHOICE',
      options: questionData.options,
    };
    return handleResponse(
      await fetch(`${BASE_URL}/quizzes/${quizId}/questions`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
    );
  },
  deleteQuestion: async (id) => {
    return handleResponse(
      await fetch(`${BASE_URL}/questions/${id}`, {
        method: 'DELETE',
      })
    );
  },

  // Attempts
  startAttempt: async (userId = 1, quizId) => {
    return handleResponse(
      await fetch(`${BASE_URL}/attempts/start?userId=${userId}&quizId=${quizId}`, {
        method: 'POST',
      })
    );
  },
  getAllAttempts: async () => {
    return handleResponse(await fetch(`${BASE_URL}/attempts`));
  },
  getUserAttempts: async (userId) => {
    return handleResponse(await fetch(`${BASE_URL}/attempts/user/${userId}`));
  },
  getQuizAttempts: async (quizId) => {
    return handleResponse(await fetch(`${BASE_URL}/attempts/quiz/${quizId}`));
  },
};
