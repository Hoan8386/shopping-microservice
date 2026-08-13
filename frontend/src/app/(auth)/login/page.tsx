import React from 'react';
import { LoginForm } from '@/components/auth/login-form';
import { AuthBrandShowcase } from '@/components/auth/auth-brand-showcase';
import { Navbar } from '@/components/layout/navbar';

export default function LoginPage() {
  return (
    <div className="min-h-screen flex flex-col bg-slate-950 text-slate-100">
      <Navbar />

      <main className="flex-1 grid grid-cols-1 md:grid-cols-2">
        {/* Left Side: Form Container */}
        <div className="flex items-center justify-center p-6 sm:p-12 md:p-16">
          <LoginForm />
        </div>

        {/* Right Side: Visual Showcase */}
        <AuthBrandShowcase />
      </main>
    </div>
  );
}
