'use client';

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useRouter } from 'next/navigation';
import { loginSchema, LoginInput } from '@/schemas/auth.schema';
import { authService } from '@/services/auth.service';
import { useAuth } from '@/hooks/use-auth';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert } from '@/components/ui/alert';
import { PasswordInput } from '@/components/auth/password-input';
import { User, ArrowRight, ShieldCheck } from 'lucide-react';
import { motion } from 'motion/react';

export function LoginForm() {
  const router = useRouter();
  const { setSessionData } = useAuth();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginInput>({
    resolver: zodResolver(loginSchema),
    mode: 'onTouched',
  });

  const onSubmit = async (data: LoginInput) => {
    setIsLoading(true);
    setErrorMessage(null);

    try {
      const response = await authService.login(data);
      setSessionData(response, data.username);
      router.push('/dashboard');
    } catch (error: any) {
      if (error.response) {
        if (error.response.status === 401) {
          setErrorMessage('Tên đăng nhập hoặc mật khẩu không chính xác.');
        } else if (error.response.status === 403) {
          setErrorMessage('Tài khoản của bạn đã bị khóa hoặc không có quyền truy cập.');
        } else {
          setErrorMessage(
            error.response.data?.message ||
              'Kết nối API Gateway thất bại. Vui lòng kiểm tra lại dịch vụ.'
          );
        }
      } else if (error.request) {
        setErrorMessage(
          'Không thể kết nối đến API Gateway (http://localhost:8080). Hãy đảm bảo Backend Service đã khởi chạy.'
        );
      } else {
        setErrorMessage('Đã xảy ra lỗi không xác định. Vui lòng thử lại.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 15 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4, ease: 'easeOut' }}
      className="w-full max-w-md mx-auto"
    >
      <div className="mb-8 text-center md:text-left">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold uppercase tracking-wider mb-4">
          <ShieldCheck className="w-3.5 h-3.5" />
          <span>API Gateway Secure Auth</span>
        </div>
        <h1 className="text-3xl font-extrabold tracking-tight text-white mb-2">
          Đăng nhập Hệ thống
        </h1>
        <p className="text-slate-400 text-sm">
          Truy cập nền tảng Shopping Microservice qua cổng API Gateway.
        </p>
      </div>

      {errorMessage && (
        <Alert
          variant="danger"
          title="Đăng nhập thất bại"
          message={errorMessage}
          className="mb-6"
        />
      )}

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
        <div>
          <Label htmlFor="username" required>
            Tên đăng nhập / Username
          </Label>
          <Input
            id="username"
            placeholder="Nhập tên đăng nhập..."
            leftIcon={<User className="w-4 h-4" />}
            error={errors.username?.message}
            {...register('username')}
          />
        </div>

        <div>
          <div className="flex items-center justify-between mb-1">
            <Label htmlFor="password" required>
              Mật khẩu / Password
            </Label>
            <a
              href="#"
              className="text-xs text-indigo-400 hover:text-indigo-300 font-medium transition-colors"
            >
              Quên mật khẩu?
            </a>
          </div>
          <PasswordInput
            id="password"
            placeholder="Nhập mật khẩu..."
            error={errors.password?.message}
            {...register('password')}
          />
        </div>

        <Button
          type="submit"
          variant="primary"
          size="lg"
          className="w-full mt-2"
          isLoading={isLoading}
        >
          <span>Đăng nhập ngay</span>
          <ArrowRight className="w-4 h-4 ml-2" />
        </Button>
      </form>

      <div className="mt-8 pt-6 border-t border-slate-800/80 text-center text-xs text-slate-500">
        <p>
          Hệ thống được bảo vệ bởi Spring Gateway & Keycloak OAuth2 Realm.
        </p>
      </div>
    </motion.div>
  );
}
