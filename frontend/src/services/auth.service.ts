import { apiClient } from './api-client';
import { API_ROUTES } from '@/lib/constants';
import { LoginInput } from '@/schemas/auth.schema';
import { TokenExchangeResponse } from '@/types/auth.types';

export const authService = {
  async login(credentials: LoginInput): Promise<TokenExchangeResponse> {
    const response = await apiClient.post<TokenExchangeResponse>(
      API_ROUTES.PUBLIC.LOGIN,
      credentials
    );
    return response.data;
  },
};
