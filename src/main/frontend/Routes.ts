import React from 'react';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import { MainLayout } from './views/@layout'; 
import { HelloHillaView } from './views/@index'; 
import { LoginView } from './views/login'; 
import { NotFoundView } from './NotFound'; 

const routes: RouteObject[] = [
  {
    path: '/',
    element: React.createElement(MainLayout),
    children: [
      { path: 'hello', element: React.createElement(HelloHillaView) },
    ],
  },
  { path: '/login', element: React.createElement(LoginView) },
  { path: '*', element: React.createElement(NotFoundView) },
];

export const router = createBrowserRouter(routes);
