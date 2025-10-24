import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import TaskList from '../TaskList';
import { Task } from '../../types/Task';

describe('TaskList', () => {
  const mockTasks: Task[] = [
    {
      id: 1,
      title: 'Task 1',
      description: 'Description 1',
      completed: false,
      createdAt: new Date().toISOString(),
    },
    {
      id: 2,
      title: 'Task 2',
      description: 'Description 2',
      completed: false,
      createdAt: new Date().toISOString(),
    },
  ];

  it('renders list of tasks', () => {
    const onComplete = vi.fn();
    render(<TaskList tasks={mockTasks} onComplete={onComplete} />);

    expect(screen.getByText('Task 1')).toBeInTheDocument();
    expect(screen.getByText('Task 2')).toBeInTheDocument();
    expect(screen.getByText('Description 1')).toBeInTheDocument();
    expect(screen.getByText('Description 2')).toBeInTheDocument();
  });

  it('renders empty state when no tasks', () => {
    const onComplete = vi.fn();
    render(<TaskList tasks={[]} onComplete={onComplete} />);

    expect(screen.getByText(/no tasks yet/i)).toBeInTheDocument();
    expect(screen.getByText(/create your first task/i)).toBeInTheDocument();
  });
});



