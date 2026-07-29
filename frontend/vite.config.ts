import { defineConfig, loadEnv, type ProxyOptions } from 'vite';
import react from '@vitejs/plugin-react';
import type { IncomingMessage, ServerResponse } from 'http';
import type { ClientRequest } from 'http';
import type { Socket } from 'net';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const proxyTarget = env.VITE_API_PROXY_TARGET || 'http://localhost:8080';

  const proxyConfig: ProxyOptions = {
    target: proxyTarget,
    changeOrigin: true,
    headers: {
      'Accept': 'application/json, text/plain, */*',
    },
    configure: (proxy) => {
      proxy.on('error', (err: Error, _req: IncomingMessage, _res: ServerResponse | Socket) => {
        console.log('proxy error', err);
      });
      proxy.on('proxyReq', (proxyReq: ClientRequest, req: IncomingMessage, _res: ServerResponse) => {
        console.log('\n[proxy] Sending Request to the Target:', req.method, req.url);
        console.log('[proxy] Target:', proxyTarget + proxyReq.path);
      });
      proxy.on('proxyRes', (proxyRes: IncomingMessage, req: IncomingMessage, _res: ServerResponse) => {
        console.log('[proxy] Received Response from the Target:', proxyRes.statusCode, req.url);
      });
    },
  };

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/mappings': proxyConfig,
        '/policy': proxyConfig,
        '/validate': proxyConfig,
      },
    },
  };
});