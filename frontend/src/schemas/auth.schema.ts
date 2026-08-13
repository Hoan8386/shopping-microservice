import { z } from 'zod';

export const loginSchema = z.object({
  username: z
    .string()
    .min(1, { message: 'Tên đăng nhập không được để trống' })
    .min(3, { message: 'Tên đăng nhập phải có ít nhất 3 ký tự' }),
  password: z
    .string()
    .min(1, { message: 'Mật khẩu không được để trống' })
    .min(4, { message: 'Mật khẩu phải có ít nhất 4 ký tự' }),
});

export type LoginInput = z.infer<typeof loginSchema>;
