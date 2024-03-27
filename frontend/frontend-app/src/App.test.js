import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';
import axios from 'axios';

// Mock axios to prevent actual API calls
jest.mock('axios');

describe('App Component', () => {
  test('renders without crashing', () => {
    render(<App />);
    expect(screen.getByText(/Create New Asset/i)).toBeInTheDocument();
  });

  test('updates form state on input change', async () => {
    render(<App />);
    const titleInput = screen.getByLabelText(/Title:/i);
    userEvent.type(titleInput, 'New Asset');
    expect(titleInput).toHaveValue('New Asset');

    const descriptionTextarea = screen.getByLabelText(/Description:/i);
    userEvent.type(descriptionTextarea, 'An amazing new asset');
    expect(descriptionTextarea).toHaveValue('An amazing new asset');

    const typeSelect = screen.getByLabelText(/Type:/i);
    userEvent.selectOptions(typeSelect, 'Software Module');
    expect(typeSelect).toHaveValue('Software Module');
  });

  test('submits form and resets', async () => {
    axios.post.mockResolvedValue({ data: 'Asset created successfully!' });

    render(<App />);
    const titleInput = screen.getByLabelText(/Title:/i);
    userEvent.type(titleInput, 'Test Asset');

    const descriptionTextarea = screen.getByLabelText(/Description:/i);
    userEvent.type(descriptionTextarea, 'A test asset description');

    const typeSelect = screen.getByLabelText(/Type:/i);
    userEvent.selectOptions(typeSelect, 'Software Module');

    const submitButton = screen.getByRole('button', { name: /Create Asset/i });
    userEvent.click(submitButton);

    await waitFor(() => expect(axios.post).toHaveBeenCalled());
    expect(titleInput).toHaveValue('');
    expect(descriptionTextarea).toHaveValue('');
    expect(typeSelect).toHaveValue('');
  });
});
