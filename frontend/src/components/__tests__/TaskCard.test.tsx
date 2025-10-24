import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import TaskCard from '../TaskCard';
import { Task } from '../../types/Task';

describe('TaskCard', () => {
  const mockTask: Task = {
    id: 1,
    title: 'Test Task',
    description: 'Test Description',
    completed: false,
    createdAt: new Date().toISOString(),
  };

  it('renders task information correctly', () => {
    const onComplete = vi.fn();
    render(<TaskCard task={mockTask} onComplete={onComplete} />);

    expect(screen.getByText('Test Task')).toBeInTheDocument();
    expect(screen.getByText('Test Description')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /done/i })).toBeInTheDocument();
  });

  it('calls onComplete when Done button is clicked', async () => {
    const onComplete = vi.fn();
    render(<TaskCard task={mockTask} onComplete={onComplete} />);

    const doneButton = screen.getByRole('button', { name: /done/i });
    fireEvent.click(doneButton);

    expect(onComplete).toHaveBeenCalledWith(mockTask.id);
  });

  it('disables button when completing', async () => {
    const onComplete = vi.fn(() => new Promise(resolve => setTimeout(resolve, 100)));
    render(<TaskCard task={mockTask} onComplete={onComplete} />);

    const doneButton = screen.getByRole('button', { name: /done/i });
    fireEvent.click(doneButton);

    expect(doneButton).toBeDisabled();
    expect(screen.getByText(/marking/i)).toBeInTheDocument();
  });
});



