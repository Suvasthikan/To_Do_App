import axios from 'axios';
import { Task, CreateTaskRequest } from '../types/Task';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export const taskApi = {
  getFirstPageTasks: async (): Promise<Task[]> => {
    const response = await api.get<Task[]>('/tasks/recent');
    return response.data;
  },

  getTasksPage: async (page: number): Promise<PaginatedResponse<Task>> => {
    const response = await api.get<PaginatedResponse<Task>>(`/tasks/page/${page}`);
    return response.data;
  },

  getAllTasks: async (): Promise<Task[]> => {
    const response = await api.get<Task[]>('/tasks');
    return response.data;
  },

  getIncompleteTaskCount: async (): Promise<number> => {
    const response = await api.get<number>('/tasks/count');
    return response.data;
  },

  createTask: async (task: CreateTaskRequest): Promise<Task> => {
    const response = await api.post<Task>('/tasks', task);
    return response.data;
  },

  completeTask: async (taskId: number): Promise<Task> => {
    const response = await api.put<Task>(`/tasks/${taskId}/complete`);
    return response.data;
  },

  deleteTask: async (taskId: number): Promise<void> => {
    await api.delete(`/tasks/${taskId}`);
  },
};

export default api;