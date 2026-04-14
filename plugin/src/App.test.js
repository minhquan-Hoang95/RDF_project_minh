import { render, screen } from '@testing-library/react';
import App from './App';

test('renders login header', () => {
  render(<App />);
  const loginHeaders = screen.getAllByText(/Login/i);
  expect(loginHeaders.length).toBeGreaterThan(0);
});
