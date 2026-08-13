'use client';

import React from 'react';
import { motion } from 'motion/react';
import { ShoppingBag, Server, Shield, Cpu, Layers } from 'lucide-react';

export function AuthBrandShowcase() {
  const services = [
    { name: 'API Gateway', status: 'Active (Port 8080)', icon: Server },
    { name: 'User Service', status: 'Keycloak Auth Provider', icon: Shield },
    { name: 'Product Service', status: 'Catalog & Inventory', icon: ShoppingBag },
    { name: 'Order & Cart Service', status: 'Event-driven Messaging', icon: Cpu },
  ];

  return (
    <div className="relative hidden md:flex flex-col justify-between h-full p-12 overflow-hidden bg-slate-900 border-l border-slate-800">
      {/* Background Mesh Gradients */}
      <div className="absolute top-0 right-0 w-96 h-96 bg-indigo-600/15 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-0 left-0 w-96 h-96 bg-violet-600/10 rounded-full blur-3xl pointer-events-none" />

      {/* Decorative Grid Pattern */}
      <div className="absolute inset-0 bg-[linear-gradient(to_right,#1e293b_1px,transparent_1px),linear-gradient(to_bottom,#1e293b_1px,transparent_1px)] bg-[size:4rem_4rem] [mask-image:radial-gradient(ellipse_60%_50%_at_50%_50%,#000_70%,transparent_100%)] opacity-20 pointer-events-none" />

      {/* Brand Header */}
      <div className="relative z-10">
        <div className="flex items-center gap-3">
          <div className="flex items-center justify-center w-10 h-10 rounded-xl bg-indigo-600 text-white shadow-lg shadow-indigo-500/30">
            <Layers className="w-5 h-5" />
          </div>
          <div>
            <span className="text-xl font-bold text-white tracking-wide">
              SHOPPING
            </span>
            <span className="text-xs ml-2 px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 font-mono">
              MICROSERVICES
            </span>
          </div>
        </div>
      </div>

      {/* Center Showcase Content */}
      <motion.div
        initial={{ opacity: 0, scale: 0.96 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.6, delay: 0.2 }}
        className="relative z-10 my-auto"
      >
        <h2 className="text-3xl font-extrabold text-white leading-tight mb-4 max-w-md">
          Kiến trúc Microservices hiện đại qua Cổng API Gateway
        </h2>
        <p className="text-slate-400 text-sm leading-relaxed mb-8 max-w-md">
          Hệ thống thương mại điện tử đồng bộ dữ liệu thời gian thực, quản lý xác thực tập trung và đảm bảo hiệu năng cao.
        </p>

        {/* Microservice Cards Grid */}
        <div className="grid grid-cols-2 gap-3 max-w-md">
          {services.map((item, idx) => {
            const Icon = item.icon;
            return (
              <div
                key={idx}
                className="p-3.5 rounded-xl border border-slate-800 bg-slate-950/50 backdrop-blur-md hover:border-indigo-500/50 transition-colors group"
              >
                <div className="flex items-center gap-2 mb-1.5">
                  <Icon className="w-4 h-4 text-indigo-400 group-hover:scale-110 transition-transform" />
                  <span className="text-xs font-semibold text-slate-200">
                    {item.name}
                  </span>
                </div>
                <span className="text-[11px] text-slate-500 block truncate">
                  {item.status}
                </span>
              </div>
            );
          })}
        </div>
      </motion.div>

      {/* Footer info */}
      <div className="relative z-10 flex items-center justify-between text-xs text-slate-500 pt-6 border-t border-slate-800/60">
        <span>© 2026 Shopping Microservices Platform</span>
        <span className="font-mono">v1.0.0</span>
      </div>
    </div>
  );
}
