# QuizMaster - Full-Stack Interactive Quiz Platform

A modern, responsive full-stack online quiz application built with **Spring Boot 3**, **Java 17**, and **React 19**.

---

## 🌟 Key Features

- ⏱️ **Interactive Quiz Taker**: Real-time countdown timer with warning states, progress tracking, and option selection.
- 🎉 **Instant Evaluation & Confetti**: Automatic score calculation, percentage evaluation, pass/fail badges, and celebratory fireworks.
- 🔍 **Dynamic Quiz Catalog**: Live keyword search across all quizzes with quick metrics (questions count, duration, passing score).
- 🛠️ **Quiz & Question Builder**: Create custom quizzes with multiple-choice questions, points, and dynamic options.
- 🏆 **Leaderboard**: Track attempt histories, scores, and timestamps in real time.
- ⚡ **Unified Deployment**: Packaged into a single container that runs both the React UI and Spring Boot REST APIs on one port.

---

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.3.4, Spring Data JPA, Hibernate, Java 17
- **Database**: H2 in-memory (zero-setup cloud fallback) / MySQL 8
- **Frontend**: React 19, Vite, Vanilla CSS design system, Lucide Icons, Canvas-Confetti
- **API Documentation**: OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
- **Containerization**: Multi-stage Docker build (`Dockerfile`)

---

## 🚀 How to Deploy 24/7 to Free Cloud (Render.com)

1. **Push to GitHub**:
   ```bash
   git init
   git add .
   git commit -m "Initial commit of QuizMaster application"
   git remote add origin https://github.com/<your-username>/quizmaster.git
   git branch -M main
   git push -u origin main
   ```

2. **Deploy on Render.com**:
   - Go to [dashboard.render.com](https://dashboard.render.com/) and sign in with your GitHub account.
   - Click **"New +"** $\to$ **"Web Service"**.
   - Select your `quizmaster` repository.
   - Render will detect the `Dockerfile` automatically.
   - Click **"Create Web Service"**.
   - Your full-stack app will be live 24/7 at `https://quizmaster-<hash>.onrender.com`!
