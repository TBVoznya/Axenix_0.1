import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0',  // Слушаем все интерфейсы
    port: 5173,        // Фиксированный порт
    strictPort: true,   // Запрещаем автоматический выбор порта
    hmr: {
      clientPort: 5173, // Порт для горячей перезагрузки
      protocol: 'ws'    // Протокол WebSocket
    },
    watch: {
      usePolling: true  // Для работы в Docker контейнере
    }
  },
  plugins: [
    vue(),             // Плагин Vue
    vueDevTools()      // Интеграция Vue DevTools
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))  // Алиас для импортов
    }
  },
  optimizeDeps: {
    exclude: ['vite-plugin-vue-devtools']  // Исключаем из оптимизации
  }
})