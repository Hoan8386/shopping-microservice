export const API_GATEWAY_URL =
  process.env.NEXT_PUBLIC_API_GATEWAY_URL || 'http://localhost:8080';

export const API_ROUTES = {
  PUBLIC: {
    LOGIN: '/api/v1/public/login',
  },
  USERS: {
    PROFILE: '/api/v1/users/profile',
  },
  PRODUCTS: {
    LIST: '/api/v1/product',
  },
} as const;

export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'shopping_access_token',
  ID_TOKEN: 'shopping_id_token',
  USER_DATA: 'shopping_user_data',
} as const;
