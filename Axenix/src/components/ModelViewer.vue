<template>
    <div class="model-viewer" ref="modelViewer">
      <!-- 3D модель будет отображаться здесь -->
    </div>
  </template>
  
  <script>
  import * as THREE from 'three';
  import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
  import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
  
  export default {
    name: 'ModelViewer',
    mounted() {
      // Инициализация сцены, камеры и рендера
      const scene = new THREE.Scene();
      scene.background = new THREE.Color(0xffffff); // Голубой фон
      const camera = new THREE.PerspectiveCamera(75, 805.7 / 451.7, 0.1, 1000);
      const renderer = new THREE.WebGLRenderer({ antialias: true });
      renderer.setSize(805.7, 451.7);
  
      // Добавление рендера в DOM
      this.$refs.modelViewer.appendChild(renderer.domElement);
  
      // Настройка освещения
      const light = new THREE.HemisphereLight(0xffffff, 0x444444, 1);
      light.position.set(0, 200, 0);
      scene.add(light);
  
      const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
      directionalLight.position.set(0, 200, 100).normalize();
      scene.add(directionalLight);
  
      // Загрузка модели GLTF
      const loader = new GLTFLoader();
      loader.load(
        '/Test_Map_V0.glb', // Укажите путь к вашей модели
        (gltf) => {
          const model = gltf.scene;
          model.scale.set(1, 1, 1); // Масштабирование модели
          model.position.set(0, 0, 0); // Позиция модели
          scene.add(model);
        },
        (xhr) => {
          console.log(`Загрузка модели: ${(xhr.loaded / xhr.total) * 100}% завершено`);
        },
        (error) => {
          console.error('Ошибка загрузки модели:', error);
        }
      );
  
      // Позиционирование камеры
      camera.position.z = 5;
  
      // Добавление OrbitControls для управления мышью
      const controls = new OrbitControls(camera, renderer.domElement);
      controls.enableDamping = true; // Добавление плавности
      controls.dampingFactor = 0.05;
      controls.screenSpacePanning = false;
      controls.minDistance = 1; // Минимальное расстояние камеры
      controls.maxDistance = 20; // Максимальное расстояние камеры
  
      // Анимация
      const animate = () => {
        requestAnimationFrame(animate);
        controls.update(); // Обновление контролов
        renderer.render(scene, camera);
      };
  
      animate();
    }
  };
  </script>
  
  <style>
.model-viewer {
  position: absolute;
  width: 805.7px;
  height: 451.7px;
  top: 120px;
  left: 435px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff; /* Изменено на белый */
  border: 1px solid #ddd;
  border-radius: 8px;
  text-align: center;
  font-family: Arial, sans-serif;
  font-size: 14px;
  color: #ffffff;
}
  </style>