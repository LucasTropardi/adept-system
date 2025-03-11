import React from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { AuthProvider } from 'Frontend/util/auth';
import { router } from './Routes';

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}

const outlet = document.getElementById('outlet');
if (outlet) {
  const root = createRoot(outlet);
  root.render(<App />);
}
