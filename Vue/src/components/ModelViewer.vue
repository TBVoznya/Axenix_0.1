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
    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0xffffff);
    const camera = new THREE.PerspectiveCamera(50, 1200 / 800, 0.1, 1000);
    const renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(1200, 800);

    this.$refs.modelViewer.appendChild(renderer.domElement);

    const light = new THREE.HemisphereLight(0xffffff, 0x444444, 2);
    light.position.set(0, 300, 0);
    scene.add(light);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
    directionalLight.position.set(0, 200, 100).normalize();
    scene.add(directionalLight);

    // PROPS LOADING
    const loader = new GLTFLoader();
    loader.load(
      '/MainScene(Textured+floor).glb', 
      (gltf) => {
        const model = gltf.scene;
        model.scale.set(1, 1, 1);
        model.position.set(-5, 0, 0);
        scene.add(model);
      },
      (xhr) => {
        console.log(`Загрузка модели: ${(xhr.loaded / xhr.total) * 100}% завершено`);
      },
      (error) => {
        console.error('Ошибка загрузки модели:', error);
      }
    );

    loader.load(
      '/Shelter_Full(Textured+color).glb', 
      (gltf) => {
        const shelfModel = gltf.scene;
        shelfModel.scale.set(1, 1, 1); 
        shelfModel.position.set(10, 0, 4);
        shelfModel.rotation.set(0, 0, 0); 
        scene.add(shelfModel);
      },
      (xhr) => {
        console.log(`Загрузка полки: ${(xhr.loaded / xhr.total) * 100}% завершено`);
      },
      (error) => {
        console.error('Ошибка загрузки полки:', error);
      }
    );

    loader.load(
      '/Shelter_Empty(Textured+color).glb', 
      (gltf) => {
        const shelfModel2 = gltf.scene;
        shelfModel2.scale.set(1, 1, 1);
        shelfModel2.position.set(10, 0, 10);
        shelfModel2.rotation.set(0, 0, 0); 
        scene.add(shelfModel2);
      },
      (xhr) => {
        console.log(`Загрузка второй полки: ${(xhr.loaded / xhr.total) * 100}% завершено`);
      },
      (error) => {
        console.error('Ошибка загрузки второй полки:', error);
      }
    );

    // PROPS NPC
    const createNpc = (position) => {
      loader.load('/Man_0.glb', (gltf) => {
        const npc = gltf.scene;
        npc.scale.set(0.6, 0.6, 0.6);
        npc.position.copy(position);
        npc.rotation.set(0, Math.PI, 0); // Начальный поворот
        scene.add(npc);

        // Точки для цикличного движения
        const points = [
          new THREE.Vector3(-5, 0, 8),
          new THREE.Vector3(5, 0, 8),
          new THREE.Vector3(5, 0, 15)
        ];
        let currentTargetIndex = 0;
        const speed = 0.04;
        let lastTurnTime = Date.now();
        let startRotation = npc.rotation.y;
        let targetRotation = 0;

        // Функция плавного поворота
        const rotateNpc = () => {
          const deltaTime = Date.now() - lastTurnTime;
          const t = Math.min(deltaTime / 1000, 1);
          npc.rotation.y = THREE.MathUtils.lerp(startRotation, targetRotation, t);
          if (t < 1) {
            requestAnimationFrame(rotateNpc);
          }
        };

        const moveNpc = () => {
          const targetPosition = points[currentTargetIndex];
          const direction = new THREE.Vector3().subVectors(targetPosition, npc.position).normalize();
          npc.position.add(direction.multiplyScalar(speed));

          if (npc.position.distanceTo(targetPosition) < 0.1) {
            // Пауза перед движением в следующую точку (рандом от 1 до 3 секунд)
            setTimeout(() => {
              currentTargetIndex = (currentTargetIndex + 1) % points.length;
              targetRotation = Math.atan2(direction.z, direction.x);
              startRotation = npc.rotation.y;
              lastTurnTime = Date.now();
              rotateNpc();
            }, Math.random() * 2000 + 1000); // 1000 - 3000 миллисекунд
          }

          requestAnimationFrame(moveNpc);
        };

        moveNpc();
      });
    };

    createNpc(new THREE.Vector3(-5, 0, 8));
    createNpc(new THREE.Vector3(0, 0, 8));
    createNpc(new THREE.Vector3(5, 0, 8));


    camera.position.set(80, 32, 65);

    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true; 
    controls.dampingFactor = 0.08;
    controls.screenSpacePanning = false;
    controls.minDistance = 2; 
    controls.maxDistance = 35; 

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
width: 1200px;
height: 800px;
top: 180px;
left: 435px;
display: flex;
justify-content: center;
align-items: center;
background-color: #ffffff; 
border: 1px solid #ddd;
border-radius: 8px;
text-align: center;
font-family: Arial, sans-serif;
font-size: 14px;
color: #ffffff;
}
</style>
