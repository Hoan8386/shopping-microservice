export interface TokenExchangeResponse {
  access_token: string;
  expires_in?: string | number;
  refresh_expires_in?: string | number;
  token_type: string;
  id_token?: string;
  scope?: string;
}

export interface UserSession {
  username: string;
  sub?: string;
  roles?: string[];
  accessToken: string;
}

export interface AuthState {
  user: UserSession | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}
