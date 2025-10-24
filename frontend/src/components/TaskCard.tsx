import { useState } from 'react';
import { Task } from '../types/Task';
import './TaskCard.css';

interface TaskCardProps {
  task: Task;
  onComplete: (taskId: number) => void;
}

function TaskCard({ task, onComplete }: TaskCardProps) {
  const [completing, setCompleting] = useState(false);

  const handleComplete = async () => {
    try {
      setCompleting(true);
      await onComplete(task.id);
    } catch (err) {
      setCompleting(false);
    }
  };

  return (
    <div className="task-card">
      <div className="task-content">
        <h3 className="task-title">{task.title}</h3>
        <p className="task-description">{task.description}</p>
      </div>
      
      <button
        className="done-button"
        onClick={handleComplete}
        disabled={completing}
      >
        {completing ? 'Completing...' : '✓ Done'}
      </button>
    </div>
  );
}

export default TaskCard;



