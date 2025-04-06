<template>
  <div class="model-viewer" ref="modelViewer">
    <!-- 3D модель -->
  </div>

  <div v-if="showInfoPanel" class="info-panel" :style="{ height: panelHeight }">
    <button class="close-button" @click="closeInfoPanel">Закрыть</button>
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
      <input 
        type="number" 
        v-model.number="newProduct.price" 
        placeholder="Введите цену" 
        min="0" 
        step="0.01" 
      />
      <button @click="addProduct">Добавить продукт</button>
    </div>

    <!-- Таблица продуктов -->
    <table>
      <thead class="table">
        <tr>
          <th>Название продукта</th>
          <th>Количество</th>
          <th>Цена</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(product, index) in currentShelfProducts" :key="index">
          <td>{{ product.name }}</td>
          <td>{{ product.quantity }}</td>
          <td>{{ product.price.toFixed(2) }}</td>
          <td>
            <button @click="removeProduct(index)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
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
      newProduct: { name: '', quantity: 1, price: 0.0 },
      npcList: [],
      spawnInterval: null,
      spawnDelay: 5000,
      storeEntrance: new THREE.Vector3(-12, 0, 6),
      waitingSpot: new THREE.Vector3(2, 0, -2),
      scene: null,
      loader: null,
      shelfModels: [],
      lastClickTime: 0,
      isDragging: false,
      controls: null
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
        if (this.newProduct.name.trim() && this.newProduct.quantity > 0 && this.newProduct.price >= 0) {
        this.currentShelfProducts.push({ 
          name: this.newProduct.name.trim(), 
          quantity: this.newProduct.quantity,
          price: this.newProduct.price
        });
        this.newProduct.name = '';
        this.newProduct.quantity = 1;
        this.newProduct.price = 0.0;

        // Обновляем модель полки
        this.updateShelfModel(this.selectedShelfName);
      } else {
        alert('Введите корректные данные для продукта!');
      }
    },
    removeProduct(index) {
      this.currentShelfProducts.splice(index, 1);
          // Обновляем модель полки
      this.updateShelfModel(this.selectedShelfName);
    },
    closeInfoPanel() {
      this.showInfoPanel = false;
    },
    createNpc(shelfPosition, modelIndex = 0) {
      const modelPath = `/Man_${modelIndex}.glb`;
      
      this.loader.load(modelPath, (gltf) => {
        
        const npc = gltf.scene;
        this.$parent.incrementCustomerCounter();
        this.$parent.peakTrafficCounter();

        npc.scale.set(0.5, 0.5, 0.5);
        npc.position.copy(this.storeEntrance);
        npc.rotation.set(0, Math.PI, 0);
        this.scene.add(npc);
        this.npcList.push(npc);

        const route = [
          shelfPosition,    
          this.waitingSpot,
          this.storeEntrance 
        ];

        let currentTargetIndex = 0;
        const speed = 0.05;
        let isWaiting = false;
        let animationId = null;

        const moveNpc = () => {
          if (isWaiting) {
            animationId = requestAnimationFrame(moveNpc);
            return;
          }

          const targetPosition = route[currentTargetIndex];
          const direction = new THREE.Vector3().subVectors(targetPosition, npc.position).normalize();
          
          npc.rotation.y = Math.atan2(direction.x, direction.z);
          npc.position.add(direction.multiplyScalar(speed));

          if (npc.position.distanceTo(targetPosition) < 0.5) {
            if (currentTargetIndex === route.length - 1) {
              this.removeNpc(npc);
              if (animationId) cancelAnimationFrame(animationId);
              return;
            }
            
            isWaiting = true;
            setTimeout(() => {
              isWaiting = false;
              currentTargetIndex++;
            }, 3000 + Math.random() * 1000);
          }

          animationId = requestAnimationFrame(moveNpc);
        };

        moveNpc();
      }, undefined, (error) => {
        console.error('Ошибка загрузки модели NPC:', error);
      });
    },
    removeNpc(npc) {
      this.scene.remove(npc);
      this.npcList = this.npcList.filter(item => item !== npc);
    },
    startNpcSpawning() {
      const shelfPositions = this.shelfModels.map(shelf => {
        const box = new THREE.Box3().setFromObject(shelf);
        const center = new THREE.Vector3();
        box.getCenter(center);
        return new THREE.Vector3(center.x, 0, center.z);
      });

      if (shelfPositions.length === 0) {
        console.warn('Нет полок для посещения NPC');
        return;
      }

      for (let i = 0; i < 2; i++) {
        const randomShelf = shelfPositions[Math.floor(Math.random() * shelfPositions.length)];
        const randomModel = Math.floor(Math.random() * 3);
        this.createNpc(randomShelf, randomModel);
      }

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
    },
    resetShelfHighlight(shelf) {
      shelf.traverse((child) => {
        if (child.isMesh && child.material.emissive) {
          child.material.emissive.setHex(0x000000);
          child.material.emissiveIntensity = 0;
        }
      });
    },
    updateShelfModel(shelfName) {
      const shelf = this.shelfModels.find(s => s.name === shelfName);
      if (!shelf) return;

      const hasProducts = this.shelfProducts[shelfName] && this.shelfProducts[shelfName].length > 0;
      const modelPath = hasProducts 
        ? '/Shelter_Full(Textured+color).glb' 
        : '/Shelter_Empty(Textured+color).glb';

      // Удаляем текущую модель полки
      this.scene.remove(shelf);
      this.shelfModels = this.shelfModels.filter(s => s !== shelf);

      // Загружаем новую модель
      this.loader.load(modelPath, (gltf) => {
        const newShelfModel = gltf.scene;
        newShelfModel.scale.set(1, 1, 1);
        newShelfModel.position.copy(shelf.position);
        newShelfModel.name = shelfName;
        newShelfModel.userData.id = shelf.userData.id;

        this.scene.add(newShelfModel);
        this.shelfModels.push(newShelfModel);
      });
    },
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
    raycaster.params.Points.threshold = 0.1;
    raycaster.params.Line.threshold = 0.1;
    const mouse = new THREE.Vector2();

    let selectedShelf = null;
    const plane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0);
    const planeIntersect = new THREE.Vector3();

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
      shelfModel.userData.id = 'shelf-1';
      scene.add(shelfModel);
      this.shelfModels.push(shelfModel);
      this.startNpcSpawning();
      
    // Устанавливаем начальную модель
    this.updateShelfModel('Полка 1');
    });

    this.loader.load('/Shelter_Empty(Textured+color).glb', (gltf) => {
    const shelfModel2 = gltf.scene;
    shelfModel2.scale.set(1, 1, 1);
    shelfModel2.position.set(10, 0, 10);
    shelfModel2.name = 'Полка 2';
    shelfModel2.userData.id = 'shelf-2';
    this.scene.add(shelfModel2);
    this.shelfModels.push(shelfModel2);

    // Устанавливаем начальную модель
    this.updateShelfModel('Полка 2');
  });
    // Обработчики событий
    const onMouseClick = (event) => {
      const currentTime = new Date().getTime();
      const isDoubleClick = (currentTime - this.lastClickTime) < 300;
      this.lastClickTime = currentTime;

      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);
      const intersects = raycaster.intersectObjects(this.shelfModels, true);

      if (intersects.length > 0) {
        let clickedObject = intersects[0].object;
        let shelf = clickedObject;
        while (shelf.parent && !this.shelfModels.includes(shelf)) {
          shelf = shelf.parent;
        }

        if (isDoubleClick) {
          // Двойной клик - начало перемещения
          if (selectedShelf) this.resetShelfHighlight(selectedShelf);
          selectedShelf = shelf;
          this.isDragging = true;
          this.controls.enabled = false;
          highlightPlane.visible = false;
          
          // Подсветка выбранной полки
          shelf.traverse((child) => {
            if (child.isMesh) {
              child.material.emissive = new THREE.Color(0xffff00);
              child.material.emissiveIntensity = 0.5;
            }
          });
          return;
        }

        // Одинарный клик - подсветка и панель
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

    const onMouseMove = (event) => {
      if (this.isDragging && selectedShelf) {
        mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
        mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

        raycaster.setFromCamera(mouse, camera);
        raycaster.ray.intersectPlane(plane, planeIntersect);

        if (planeIntersect) {
          // Ограничение области перемещения
          const x = Math.max(-20, Math.min(20, planeIntersect.x));
          const z = Math.max(-20, Math.min(20, planeIntersect.z));
          selectedShelf.position.set(x, selectedShelf.position.y, z);
        }
      }
    };

    const onMouseUp = () => {
      if (this.isDragging) {
        this.isDragging = false;
        this.controls.enabled = true;
        if (selectedShelf) {
          this.resetShelfHighlight(selectedShelf);
          highlightPlane.visible = true;
        }
      }
    };

    const onKeyDown = (event) => {
      if (event.key === 'Escape' && this.isDragging) {
        this.isDragging = false;
        this.controls.enabled = true;
        if (selectedShelf) {
          this.resetShelfHighlight(selectedShelf);
          highlightPlane.visible = true;
        }
      }
    };

    window.addEventListener('click', onMouseClick, false);
    window.addEventListener('mousemove', onMouseMove, false);
    window.addEventListener('mouseup', onMouseUp, false);
    window.addEventListener('keydown', onKeyDown, false);

    camera.position.set(80, 32, 65);

    this.controls = new OrbitControls(camera, renderer.domElement);
    this.controls.enableDamping = true;
    this.controls.dampingFactor = 0.08;
    this.controls.screenSpacePanning = false;
    this.controls.minDistance = 2;
    this.controls.maxDistance = 35;

    axios.get('http://localhost:8080/api/customer')
      .then(response => {
        const customers = response.data;
        customers.forEach(c => {
          this.createNpc(new THREE.Vector3(c.x, c.y, c.z));
        });
      })
      .catch(error => {
        console.error("Ошибка при загрузке покупателей:", error);
      });

    const animate = () => {
      requestAnimationFrame(animate);
      this.controls.update();
      renderer.render(scene, camera);
    };

    animate();

    this.$once('hook:beforeDestroy', () => {
      this.stopNpcSpawning();
      window.removeEventListener('click', onMouseClick);
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup', onMouseUp);
      window.removeEventListener('keydown', onKeyDown);
    });
  }
};
</script>

<style>
/* Ваши стили остаются без изменений */
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
  width: 475px;
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  font-family: Arial, sans-serif;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: black;
}

.info-panel th, .info-panel td {
  border: 1px solid #000000;
  padding: 8px;
  text-align: left;
}

.info-panel th {
  background-color: #ffffff;
    color: rgb(0, 0, 0);
}

.info-panel input[type="number"] {
  width: 60px;
}

.add-product-form {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.add-product-form input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #000000;
  border-radius: 4px;
  width: 100px;
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

.close-button {
  position: absolute;
  top: 27.3px;
  right: 1.3px;
  background-color: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  cursor: pointer;
  font-size: 14px;
}

.close-button:hover {
  background-color: #cc0000;
}
</style>