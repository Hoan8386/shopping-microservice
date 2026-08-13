'use client';

import React, { createContext, useState, useEffect, useCallback } from 'react';
import { STORAGE_KEYS } from '@/lib/constants';
import { parseJwt } from '@/lib/utils';
import { AuthState, TokenExchangeResponse, UserSession } from '@/types/auth.types';

interface AuthContextType extends AuthState {
  setSessionData: (response: TokenExchangeResponse, usernameFallback: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AuthState>({
    user: null,
    isAuthenticated: false,
    isLoading: true,
  });

  useEffect(() => {
    const token = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
    if (token) {
      const decoded = parseJwt(token);
      if (decoded && decoded.exp * 1000 > Date.now()) {
        setState({
          user: {
            username: decoded.preferred_username || decoded.sub || 'User',
            sub: decoded.sub,
            roles: decoded.realm_access?.roles || [],
            accessToken: token,
          },
          isAuthenticated: true,
          isLoading: false,
        });
        return;
      }
    }
    setState({ user: null, isAuthenticated: false, isLoading: false });
  }, []);

  const setSessionData = useCallback(
    (response: TokenExchangeResponse, usernameFallback: string) => {
      const token = response.access_token;
      localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, token);
      if (response.id_token) {
        localStorage.setItem(STORAGE_KEYS.ID_TOKEN, response.id_token);
      }

      const decoded = parseJwt(token);
      const username = decoded?.preferred_username || decoded?.sub || usernameFallback;

      const user: UserSession = {
        username,
        sub: decoded?.sub,
        roles: decoded?.realm_access?.roles || [],
        accessToken: token,
      };

      setState({
        user,
        isAuthenticated: true,
        isLoading: false,
      });
    },
    []
  );

  const logout = useCallback(() => {
    localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.ID_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.USER_DATA);
    setState({
      user: null,
      isAuthenticated: false,
      isLoading: false,
    });
  }, []);

  return (
    <AuthContext.Provider
      value={{
        ...state,
        setSessionData,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
