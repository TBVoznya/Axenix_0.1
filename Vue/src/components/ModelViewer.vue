<template>
  <div class="model-viewer" ref="modelViewer">
    <!-- 3D модель будет отображаться здесь -->
  </div>

  <div v-if="showInfoPanel" class="info-panel">
    <p>Вы выбрали полку {{ selectedShelfName }}</p>
    <!-- Здесь можно потом сделать вывод ID, названия, содержимого и т.п. -->
  </div>
</template>

<script>
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import axios from 'axios';


export default {
  name: 'ModelViewer',
  data() {
    return {
      selectedShelfName: '',
      showInfoPanel: false, 
      selectedShelf: null  
    };
  },
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

    const highlightMaterial = new THREE.MeshBasicMaterial({
      color: 0xffff00,
      transparent: true,
      opacity: 0.3,
      side: THREE.DoubleSide,
    });

    const highlightGeometry = new THREE.PlaneGeometry(5, 5); // размер можно будет менять
    const highlightPlane = new THREE.Mesh(highlightGeometry, highlightMaterial);
    highlightPlane.rotation.x = -Math.PI / 2; // горизонтально (если над полкой сверху)
    highlightPlane.visible = false; // по умолчанию скрыт
    scene.add(highlightPlane);

    // Raycaster для отслеживания кликов
    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();
    const shelfModels = []; // Массив для хранения моделей

    let selectedShelf = null; // Полка, которую перемещаем
    let isDragging = false; // Флаг перемещения
    const plane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0); // Горизонтальная плоскость
    const planeIntersect = new THREE.Vector3(); // Точка пересечения с плоскостью
    let controlsEnabled = true;

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
        shelfModel.name = 'Полка 1';
        scene.add(shelfModel);
        shelfModels.push(shelfModel); // Добавляем в список
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
        shelfModel2.name = 'Полка 2';
        scene.add(shelfModel2);
        shelfModels.push(shelfModel2); // Добавляем в список
      },
      (xhr) => {
        console.log(`Загрузка второй полки: ${(xhr.loaded / xhr.total) * 100}% завершено`);
      },
      (error) => {
        console.error('Ошибка загрузки второй полки:', error);
      }
    );

    // Обработчик кликов
    const onMouseClick = (event) => {
      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(shelfModels, true); // true — проверка вложенных объектов

      if (intersects.length > 0) {
        let clickedObject = intersects[0].object;

        // Подняться до объекта из shelfModels
        let shelf = clickedObject;
        while (shelf.parent && !shelfModels.includes(shelf)) {
          shelf = shelf.parent;
        }

        // Подсветка
        const box = new THREE.Box3().setFromObject(shelf);
        const center = new THREE.Vector3();
        box.getCenter(center);

        highlightPlane.position.set(center.x, box.min.y + 5, center.z);
        highlightPlane.scale.set(
          (box.max.x - box.min.x) / 5,
          1,
          (box.max.z - box.min.z) / 5
        );
        highlightPlane.visible = true;
        highlightPlane.userData.target = shelf;

        // UI
        this.selectedShelfName = shelf.name || 'Без имени';
        this.showInfoPanel = true;
        this.selectedShelf = shelf;

      } else {
        highlightPlane.visible = false;
        this.showInfoPanel = false;
        this.selectedShelf = null;
        this.selectedShelfName = '';
      }
    };
    
    // Перемещение полок
    const onMouseDblClick = (event) => {
      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(shelfModels, true);

      if (intersects.length > 0) {
        selectedShelf = intersects[0].object.parent; // Получаем всю полку
        isDragging = true;
        controlsEnabled = false; // Отключаем управление камерой
        controls.enabled = false; // Замораживаем камеру
      }
    };

    const onMouseMove = (event) => {
      if (isDragging && selectedShelf) {
        // Скрываем highlightPlane во время перемещения
        highlightPlane.visible = false;

        mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
        mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

        raycaster.setFromCamera(mouse, camera);
        const plane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0);
        const planeIntersect = new THREE.Vector3();
        raycaster.ray.intersectPlane(plane, planeIntersect);

        if (planeIntersect) {
          selectedShelf.position.set(planeIntersect.x, selectedShelf.position.y, planeIntersect.z);
        }
      }
    };

    const onMouseUp = () => {
      if (isDragging) {
        isDragging = false;

        // Показываем highlightPlane после завершения перемещения
        if (selectedShelf) {
          highlightPlane.visible = true;
        }

        selectedShelf = null;
        controlsEnabled = true; // Включаем управление камерой
        controls.enabled = true; // Размораживаем камеру
      }
    };
    const onMouseWheel = (event) => {
      if (selectedShelf) {
        const rotationSpeed = 0.5; // Скорость вращения
        selectedShelf.rotation.z += event.deltaY * rotationSpeed * 0.01;
      }
    };

    // Слушаем событие клика
    window.addEventListener('click', onMouseClick, false);
    // Двойной клик
    window.addEventListener('dblclick', onMouseDblClick, false);
    window.addEventListener('mousemove', onMouseMove, false);
    window.addEventListener('mouseup', onMouseUp, false);
    window.addEventListener('wheel', onMouseWheel, false);

    // PROPS NPC
    const createNpc = (position, modelIndex = 0) => {
      const modelPath = `/Man_${modelIndex}.glb`;

      loader.load(modelPath, (gltf) => {
        const npc = gltf.scene;
        npc.scale.set(0.6, 0.6, 0.6);
        npc.position.copy(position);
        npc.rotation.set(0, Math.PI, 0);
        scene.add(npc);

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
            setTimeout(() => {
              currentTargetIndex = (currentTargetIndex + 1) % points.length;
              targetRotation = Math.atan2(direction.z, direction.x);
              startRotation = npc.rotation.y;
              lastTurnTime = Date.now();
              rotateNpc();
            }, Math.random() * 2000 + 1000);
          }

          requestAnimationFrame(moveNpc);
        };

        moveNpc();
      });
    };

    createNpc(new THREE.Vector3(-5, 0, 8), 0); // Man_0.glb
    createNpc(new THREE.Vector3(0, 0, 8), 1);  // Man_1.glb
    createNpc(new THREE.Vector3(5, 0, 8), 2);  // Man_2.glb

    // Загрузка покупателей с сервера и создание NPC
    axios.get('http://localhost:8080/api/customer')
      .then(response => {
        const customers = response.data;
        customers.forEach(c => {
          createNpc(new THREE.Vector3(c.x, c.y, c.z));
        });
      })
      .catch(error => {
        console.error("Ошибка при загрузке покупателей:", error);
      });

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
  position: fixed;
  width: 5.1%;
  height: 3.3%;
  left: 52.1%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
  text-align: center;
  font-family: Arial, sans-serif;
  font-size: 14px;
  color: #ffffff;
}

.info-panel {
  position: absolute;
  top: 1000px; /* чуть ниже модели */
  left: 435px;
  width: 1200px;
  height: 100px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 16px;
  border-radius: 8px;
  font-size: 16px;
  z-index: 10;
}
</style>