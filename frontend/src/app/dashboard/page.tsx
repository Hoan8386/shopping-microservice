'use client';

import React, { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/use-auth';
import { Button } from '@/components/ui/button';
import { Navbar } from '@/components/layout/navbar';
import { ShieldCheck, LogOut, User, Key, Database } from 'lucide-react';
import { motion } from 'motion/react';

export default function DashboardPage() {
  const router = useRouter();
  const { user, isAuthenticated, isLoading, logout } = useAuth();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push('/login');
    }
  }, [isLoading, isAuthenticated, router]);

  if (isLoading || !isAuthenticated || !user) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center text-slate-400">
        <div className="flex items-center gap-3">
          <div className="w-5 h-5 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin" />
          <span>Đang kiểm tra phiên đăng nhập...</span>
        </div>
      </div>
    );
  }

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col">
      <Navbar />

      <main className="flex-1 max-w-5xl w-full mx-auto p-6 sm:p-8">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          {/* Header Banner */}
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-6 rounded-2xl border border-indigo-500/30 bg-indigo-950/20 backdrop-blur-xl">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-xl bg-indigo-600 text-white flex items-center justify-center font-bold text-xl shadow-lg shadow-indigo-600/30">
                {user.username.charAt(0).toUpperCase()}
              </div>
              <div>
                <div className="flex items-center gap-2 mb-1">
                  <h1 className="text-xl font-bold text-white">
                    Xin chào, {user.username}!
                  </h1>
                  <span className="px-2 py-0.5 rounded text-[10px] font-semibold bg-emerald-500/20 text-emerald-300 border border-emerald-500/30">
                    AUTHENTICATED
                  </span>
                </div>
                <p className="text-xs text-slate-400">
                  Phiên đăng nhập thành công qua Spring Gateway & User Service API.
                </p>
              </div>
            </div>

            <Button variant="danger" size="md" onClick={handleLogout}>
              <LogOut className="w-4 h-4 mr-2" />
              Đăng xuất
            </Button>
          </div>

          {/* Cards Grid */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="p-5 rounded-xl border border-slate-800 bg-slate-900/60 backdrop-blur-md">
              <div className="flex items-center gap-3 text-indigo-400 mb-3">
                <User className="w-5 h-5" />
                <h3 className="font-semibold text-sm text-slate-200">Tài khoản</h3>
              </div>
              <p className="text-xs text-slate-400 mb-1">Username:</p>
              <p className="text-sm font-mono font-medium text-slate-100">{user.username}</p>
            </div>

            <div className="p-5 rounded-xl border border-slate-800 bg-slate-900/60 backdrop-blur-md">
              <div className="flex items-center gap-3 text-emerald-400 mb-3">
                <ShieldCheck className="w-5 h-5" />
                <h3 className="font-semibold text-sm text-slate-200">Quyền hạn (Roles)</h3>
              </div>
              <p className="text-xs text-slate-400 mb-1">Assigned Roles:</p>
              <div className="flex flex-wrap gap-1">
                {user.roles && user.roles.length > 0 ? (
                  user.roles.map((r, i) => (
                    <span
                      key={i}
                      className="px-2 py-0.5 rounded bg-slate-800 text-[11px] font-mono text-slate-300 border border-slate-700"
                    >
                      {r}
                    </span>
                  ))
                ) : (
                  <span className="text-xs text-slate-500 font-mono">User Role Standard</span>
                )}
              </div>
            </div>

            <div className="p-5 rounded-xl border border-slate-800 bg-slate-900/60 backdrop-blur-md">
              <div className="flex items-center gap-3 text-violet-400 mb-3">
                <Database className="w-5 h-5" />
                <h3 className="font-semibold text-sm text-slate-200">Subject ID</h3>
              </div>
              <p className="text-xs text-slate-400 mb-1">Keycloak UUID:</p>
              <p className="text-xs font-mono text-slate-300 truncate">
                {user.sub || 'Keycloak Identity Claims'}
              </p>
            </div>
          </div>

          {/* Access Token Viewer */}
          <div className="p-6 rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-md">
            <div className="flex items-center gap-2 text-slate-200 font-semibold text-sm mb-3">
              <Key className="w-4 h-4 text-indigo-400" />
              <span>Access Token (JWT)</span>
            </div>
            <div className="p-3 rounded-lg bg-slate-950 border border-slate-800 text-[11px] font-mono text-slate-400 break-all leading-relaxed max-h-32 overflow-y-auto">
              {user.accessToken}
            </div>
          </div>
        </motion.div>
      </main>
    </div>
  );
}
