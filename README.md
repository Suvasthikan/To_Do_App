# Todo Task Application

A simple to-do task management app built with React, Java Spring Boot, and MySQL.

## What This App Does

-  Create new tasks with title and description
-  View your tasks (shows 5 at a time)
-  Mark tasks as completed
-  Automatic updates when you complete tasks

## How to Run

### Step 1: Make sure Docker is running
Open Docker Desktop and wait for it to start.

### Step 2: Run the application
Open terminal/command prompt in this folder and run:

```bash
docker-compose up --build
```

Wait 2-3 minutes for everything to start.

### Step 3: Open the app
Go to: http://localhost:3000

## How to Use

1. **Add a task**: Type title and description, click "Add"
2. **Complete a task**: Click "Done" button next to any task
3. **View more tasks**: If you have more than 5 tasks, use Previous/Next buttons

## What's Included

- **Frontend**: React app (runs on port 3000)
- **Backend**: Java Spring Boot API (runs on port 8080)
- **Database**: MySQL database (runs on port 3307)

## How to Stop

```bash
docker-compose down
```

## Troubleshooting

**If the app shows "Cannot connect to server":**
1. Wait 2-3 minutes for everything to start
2. Check if Docker Desktop is running
3. Try: `docker-compose down` then `docker-compose up --build`

**If ports are busy:**
- Close other applications using ports 3000, 8080, or 3307

## Technical Details

- **Frontend**: React + TypeScript
- **Backend**: Java Spring Boot
- **Database**: MySQL
- **Containerization**: Docker

## Assignment Notes

This is a take-home assignment demonstrating:
- Full-stack development
- REST API design
- Database integration
- Modern UI/UX
- Containerization with Docker

---

**Need help?** Check Docker Desktop is running and try restarting with `docker-compose down` then `docker-compose up --build`