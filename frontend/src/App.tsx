import { useState, useEffect } from 'react';
import { Task, CreateTaskRequest } from './types/Task';
import { taskApi } from './services/api';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import './App.css';

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [totalTasks, setTotalTasks] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [updating, setUpdating] = useState<boolean>(false);

  useEffect(() => {
    loadFirstPage();
  }, []);

  const loadFirstPage = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [tasksData, totalCount] = await Promise.all([
        taskApi.getFirstPageTasks(),
        taskApi.getIncompleteTaskCount()
      ]);
      
      setTasks(tasksData);
      setTotalTasks(totalCount);
      setCurrentPage(0);
    } catch (err: any) {
      if (err.code === 'ECONNREFUSED' || err.message.includes('Network Error')) {
        setError('Cannot connect to server. Please check if the backend is running on port 8080.');
      } else if (err.response?.status === 404) {
        setError('API endpoint not found. Please check the backend configuration.');
      } else if (err.response?.status >= 500) {
        setError('Server error. Please check the backend logs.');
      } else {
        setError(`Failed to load tasks: ${err.message || 'Unknown error'}`);
      }
    } finally {
      setLoading(false);
    }
  };

  const loadPage = async (page: number) => {
    try {
      setLoading(true);
      setError(null);

      const tasksData = await taskApi.getTasksPage(page);

      if (tasksData.content.length === 0 && page > 0) {
        const prevPageData = await taskApi.getTasksPage(page - 1);
        setTasks(prevPageData.content);
        setCurrentPage(page - 1);
      } else {
        setTasks(tasksData.content);
        setCurrentPage(page);
      }

      const totalCount = await taskApi.getIncompleteTaskCount();
      setTotalTasks(totalCount);

    } catch (err) {
      setError('Failed to load tasks. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (taskData: CreateTaskRequest) => {
    try {
      setError(null);
      await taskApi.createTask(taskData);
      await loadFirstPage();
    } catch (err) {
      setError('Failed to create task. Please try again.');
      throw err;
    }
  };

  const handleCompleteTask = async (taskId: number) => {
    try {
      setUpdating(true);
      setError(null);

      await taskApi.completeTask(taskId);

      const newTotal = await taskApi.getIncompleteTaskCount();
      setTotalTasks(newTotal);

      await loadPage(currentPage);

    } catch (err) {
      setError('Failed to complete task. Please try again.');
    } finally {
      setUpdating(false);
    }
  };

  const handlePageChange = (page: number) => {
    loadPage(page);
  };

  const totalPages = Math.ceil(totalTasks / 5);

  return (
    <div className="app">
      <div className="container">
        <div className="content">
          <TaskForm onSubmit={handleCreateTask} />
          
          <div className="task-list-container">
            <div className="task-list-header">
              <h2 className="task-list-title">
                Tasks ({totalTasks})
                {updating && <span className="updating-indicator">Updating...</span>}
              </h2>
              {totalPages > 1 && (
                <div className="pagination-info">
                  Page {currentPage + 1} of {totalPages}
                </div>
              )}
            </div>
            
            <div className="task-list-content">
              {error && <div className="error-message">{error}</div>}
              
              {loading ? (
                <div className="loading">Loading tasks...</div>
              ) : (
                <TaskList tasks={tasks} onComplete={handleCompleteTask} />
              )}
            </div>
            
            {totalPages > 1 && (
              <div className="pagination-controls">
                <button 
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                  className="pagination-btn"
                >
                  Previous
                </button>
                
                <span className="pagination-info">
                  {currentPage + 1} / {totalPages}
                </span>
                
                <button 
                  onClick={() => handlePageChange(currentPage + 1)}
                  disabled={currentPage >= totalPages - 1}
                  className="pagination-btn"
                >
                  Next
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;