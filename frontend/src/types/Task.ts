/**
 * Task interface representing a to-do task
 */
export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt: string;
}

/**
 * Request interface for creating a new task
 */
export interface CreateTaskRequest {
  title: string;
  description: string;
}



