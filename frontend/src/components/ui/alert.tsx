import React from 'react';
import { cn } from '@/lib/utils';
import { AlertCircle, CheckCircle2, Info } from 'lucide-react';

interface AlertProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'danger' | 'success' | 'info';
  title?: string;
  message: string;
}

export function Alert({
  variant = 'danger',
  title,
  message,
  className,
  ...props
}: AlertProps) {
  const styles = {
    danger:
      'bg-rose-950/40 border-rose-800/60 text-rose-200 icon-rose-400',
    success:
      'bg-emerald-950/40 border-emerald-800/60 text-emerald-200 icon-emerald-400',
    info: 'bg-indigo-950/40 border-indigo-800/60 text-indigo-200 icon-indigo-400',
  };

  const icons = {
    danger: <AlertCircle className="w-5 h-5 text-rose-400 shrink-0" />,
    success: <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />,
    info: <Info className="w-5 h-5 text-indigo-400 shrink-0" />,
  };

  return (
    <div
      className={cn(
        'flex items-start gap-3 p-4 rounded-xl border backdrop-blur-md transition-all duration-200',
        styles[variant],
        className
      )}
      {...props}
    >
      {icons[variant]}
      <div className="text-sm leading-relaxed">
        {title && <h4 className="font-semibold mb-0.5">{title}</h4>}
        <p className="text-slate-300">{message}</p>
      </div>
    </div>
  );
}
