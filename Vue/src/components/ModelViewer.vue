<template>
  <div class="model-viewer" ref="modelViewer">
    <!-- 3D модель -->
  </div>

  <div v-if="showInfoPanel" class="info-panel" :style="{ height: panelHeight }">
    <p>Вы выбрали полку {{ selectedShelfName }}</p>

    <!-- Форма для добавления продукта -->
    <div class="add-product-form">
      <input 
        type="text" 
        v-model="newProduct.name" 
        placeholder="Введите название продукта" 
      />
      <input 
        type="number" 
        v-model.number="newProduct.quantity" 
        placeholder="Введите количество" 
        min="1" 
      />
      <button @click="addProduct">Добавить продукт</button>
    </div>

    <!-- Таблица продуктов -->
    <table>
      <thead>
        <tr>
          <th>Название продукта</th>
          <th>Количество</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(product, index) in currentShelfProducts" :key="index">
          <td>{{ product.name }}</td>
          <td>{{ product.quantity }}</td>
          <td>
            <button @click="removeProduct(index)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Управление NPC -->
    <div class="npc-controls">
      <button @click="startNpcSpawning">Запустить NPC</button>
      <button @click="stopNpcSpawning">Остановить NPC</button>
    </div>
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
      selectedShelf: null,
      shelfProducts: {},
      currentShelfProducts: [],
      newProduct: { name: '', quantity: 1 },
      npcList: [],
      spawnInterval: null,
      spawnDelay: 5000,
      storeEntrance: new THREE.Vector3(-12, 0, 6),
      waitingSpot: new THREE.Vector3(-2, 0, -4),
      scene: null,
      loader: null
    };
  },
  computed: {
    panelHeight() {
      const baseHeight = 300;
      const rowHeight = 40;
      const maxHeight = 400;
      const calculatedHeight = baseHeight + this.currentShelfProducts.length * rowHeight;
      return Math.min(calculatedHeight, maxHeight) + 'px';
    }
  },
  methods: {
    addProduct() {
      if (this.newProduct.name.trim() && this.newProduct.quantity > 0) {
        this.currentShelfProducts.push({ 
          name: this.newProduct.name.trim(), 
          quantity: this.newProduct.quantity 
        });
        this.newProduct.name = '';
        this.newProduct.quantity = 1;
      } else {
        alert('Введите корректные данные для продукта!');
      }
    },
    removeProduct(index) {
      this.currentShelfProducts.splice(index, 1);
    },
    createNpc(shelfPosition, modelIndex = 0) {
  const modelPath = `/Man_${modelIndex}.glb`;
  
  this.loader.load(modelPath, (gltf) => {
    const npc = gltf.scene;
    npc.scale.set(0.5, 0.5, 0.5);
    npc.position.copy(this.storeEntrance);
    npc.rotation.set(0, Math.PI, 0);
    this.scene.add(npc);
    this.npcList.push(npc);

    const route = [
      shelfPosition,    // 1. Идем к полке
      this.waitingSpot,  // 2. Затем к точке ожидания
      this.storeEntrance // 3. Возвращаемся ко входу (для удаления)
    ];

    let currentTargetIndex = 0;
    const speed = 0.04;
    let isWaiting = false;
    let animationId = null;

    const moveNpc = () => {
      if (isWaiting) {
        animationId = requestAnimationFrame(moveNpc);
        return;
      }

      const targetPosition = route[currentTargetIndex];
      const direction = new THREE.Vector3().subVectors(targetPosition, npc.position).normalize();
      
      // Поворачиваем NPC в направлении движения
      npc.rotation.y = Math.atan2(direction.x, direction.z);
      
      // Двигаем NPC
      npc.position.add(direction.multiplyScalar(speed));

      // Проверяем достижение цели
      if (npc.position.distanceTo(targetPosition) < 0.5) {
        // Если это последняя точка - удаляем NPC
        if (currentTargetIndex === route.length - 1) {
          this.removeNpc(npc);
          if (animationId) cancelAnimationFrame(animationId);
          return;
        }
        
        // Ожидание 3-4 секунды
        isWaiting = true;
        setTimeout(() => {
          isWaiting = false;
          currentTargetIndex++;
        }, 3000 + Math.random() * 1000);
      }

      animationId = requestAnimationFrame(moveNpc);
    };

    moveNpc();
  });
},
    removeNpc(npc) {
      this.scene.remove(npc);
      this.npcList = this.npcList.filter(item => item !== npc);
    },
    startNpcSpawning() {
      const shelfPositions = [
        new THREE.Vector3(10, 0, 4),
        new THREE.Vector3(10, 0, 10),
      ];

      this.spawnInterval = setInterval(() => {
        const randomShelf = shelfPositions[Math.floor(Math.random() * shelfPositions.length)];
        const randomModel = Math.floor(Math.random() * 3);
        this.createNpc(randomShelf, randomModel);
      }, this.spawnDelay);
    },
    stopNpcSpawning() {
      if (this.spawnInterval) {
        clearInterval(this.spawnInterval);
        this.spawnInterval = null;
      }
    }
  },
  mounted() {
    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0xffffff);
    const camera = new THREE.PerspectiveCamera(50, 1200 / 800, 0.1, 1000);
    const renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(1200, 800);

    this.$refs.modelViewer.appendChild(renderer.domElement);

    this.scene = scene;
    this.loader = new GLTFLoader();

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

    const highlightGeometry = new THREE.PlaneGeometry(5, 5);
    const highlightPlane = new THREE.Mesh(highlightGeometry, highlightMaterial);
    highlightPlane.rotation.x = -Math.PI / 2;
    highlightPlane.visible = false;
    scene.add(highlightPlane);

    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();
    const shelfModels = [];

    let selectedShelf = null;
    let isDragging = false;
    const plane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0);
    const planeIntersect = new THREE.Vector3();
    let controlsEnabled = true;

    // Загрузка моделей
    this.loader.load('/MainScene(Textured+floor).glb', (gltf) => {
      const model = gltf.scene;
      model.scale.set(1, 1, 1);
      model.position.set(-5, 0, 0);
      scene.add(model);
    });

    this.loader.load('/Shelter_Full(Textured+color).glb', (gltf) => {
      const shelfModel = gltf.scene;
      shelfModel.scale.set(1, 1, 1);
      shelfModel.position.set(10, 0, 4);
      shelfModel.name = 'Полка 1';
      scene.add(shelfModel);
      shelfModels.push(shelfModel);
    });

    this.loader.load('/Shelter_Empty(Textured+color).glb', (gltf) => {
      const shelfModel2 = gltf.scene;
      shelfModel2.scale.set(1, 1, 1);
      shelfModel2.position.set(10, 0, 10);
      shelfModel2.name = 'Полка 2';
      scene.add(shelfModel2);
      shelfModels.push(shelfModel2);
    });

    // Обработчики событий
    const onMouseClick = (event) => {
      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(shelfModels, true);

      if (intersects.length > 0) {
        let clickedObject = intersects[0].object;
        let shelf = clickedObject;
        while (shelf.parent && !shelfModels.includes(shelf)) {
          shelf = shelf.parent;
        }

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
        const shelfName = shelf.name || 'Без имени';
        this.selectedShelfName = shelfName;
        this.showInfoPanel = true;
        this.selectedShelf = shelf;

        if (!this.shelfProducts[shelfName]) {
          this.shelfProducts[shelfName] = [];
        }
        this.currentShelfProducts = this.shelfProducts[shelfName];
      } 
    };

    const onMouseDblClick = (event) => {
      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(shelfModels, true);

      if (intersects.length > 0) {
        selectedShelf = intersects[0].object.parent;
        isDragging = true;
        controlsEnabled = false;
        controls.enabled = false;
      }
    };

    const onMouseMove = (event) => {
      if (isDragging && selectedShelf) {
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
        if (selectedShelf) {
          highlightPlane.visible = true;
        }
        selectedShelf = null;
        controlsEnabled = true;
        controls.enabled = true;
      }
    };

    const onMouseWheel = (event) => {
      if (selectedShelf) {
        const rotationSpeed = 0.5;
        selectedShelf.rotation.z += event.deltaY * rotationSpeed * 0.01;
      }
    };

    window.addEventListener('click', onMouseClick, false);
    window.addEventListener('dblclick', onMouseDblClick, false);
    window.addEventListener('mousemove', onMouseMove, false);
    window.addEventListener('mouseup', onMouseUp, false);
    window.addEventListener('wheel', onMouseWheel, false);

    camera.position.set(70, 34, 80);

    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.08;
    controls.screenSpacePanning = false;
    controls.minDistance = 2;
    controls.maxDistance = 40;

    // Запуск спавна NPC
    this.startNpcSpawning();

    // Анимация
    const animate = () => {
      requestAnimationFrame(animate);
      controls.update();
      renderer.render(scene, camera);
    };

    animate();

    // Очистка при уничтожении компонента
    this.$once('hook:beforeDestroy', () => {
      this.stopNpcSpawning();
      window.removeEventListener('click', onMouseClick);
      window.removeEventListener('dblclick', onMouseDblClick);
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup', onMouseUp);
      window.removeEventListener('wheel', onMouseWheel);
    });
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
  top: 60%;
  left: 1%;
  width: 450px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 16px;
  border-radius: 8px;
  font-size: 16px;
  z-index: 10;
  overflow-y: auto;
  transition: height 0.3s ease;
}

.info-panel th, .info-panel td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.info-panel th {
  background-color: #444;
  color: white;
}

.info-panel input[type="number"] {
  width: 60px;
}

.add-product-form {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}

.add-product-form input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.add-product-form button {
  padding: 8px 16px;
  font-size: 14px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-product-form button:hover {
  background-color: #0056b3;
}

.npc-controls {
  margin-top: 16px;
  display: flex;
  gap: 8px;
}

.npc-controls button {
  padding: 8px 16px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.npc-controls button:hover {
  background-color: #218838;
}
</style>