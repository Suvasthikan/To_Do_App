import { useState, FormEvent } from 'react';
import { CreateTaskRequest } from '../types/Task';
import './TaskForm.css';

interface TaskFormProps {
  onSubmit: (task: CreateTaskRequest) => Promise<void>;
}

/**
 * TaskForm component for creating new tasks
 */
function TaskForm({ onSubmit }: TaskFormProps) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [submitting, setSubmitting] = useState(false);

  /**
   * Handle form submission
   */
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    
    if (!title.trim() || !description.trim()) {
      return;
    }

    try {
      setSubmitting(true);
      await onSubmit({ title, description });
      // Clear form on success
      setTitle('');
      setDescription('');
    } catch (err) {
      // Error is handled by parent component
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="task-form-container">
      <h1 className="form-title">Add a Task</h1>
      
      <form onSubmit={handleSubmit} className="task-form">
        <div className="form-group">
          <label htmlFor="title" className="form-label">
            Title
          </label>
          <input
            type="text"
            id="title"
            className="form-input"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter task title"
            maxLength={255}
            required
            disabled={submitting}
          />
        </div>

        <div className="form-group">
          <label htmlFor="description" className="form-label">
            Description
          </label>
          <textarea
            id="description"
            className="form-textarea"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter task description"
            rows={4}
            maxLength={1000}
            required
            disabled={submitting}
          />
        </div>

        <button
          type="submit"
          className="submit-button"
          disabled={submitting || !title.trim() || !description.trim()}
        >
          {submitting ? 'Adding...' : 'Add'}
        </button>
      </form>
    </div>
  );
}

export default TaskForm;



