<template>
  <div class="app">
    <header>
      <h1>Logo</h1>
      <h2>Система анализа магазина 3D:</h2>
      <div class="user-icon">
        <img src="@/assets/account_circle.png" alt="User Icon" />
      </div>
    </header>

    <main>
      <div class="button-container-add">
        <button class="add-button" @click="togglePanel">
          <img src="@/assets/directions_walk.png" alt="icon1" class="btn-icon" />
          Добавить
      </button>
      </div>
      <div v-if="showPanel" class="add-panel">
          <p>Выберите тип:</p>
          <button class="panel-item">Элемент 1</button>
          <button class="panel-item">Элемент 2</button>
          <button class="panel-item">Элемент 3</button>
        </div>
      <div class="statistic">
        <button class="stat-button">
          <img src="@/assets/add_circle.png" alt="icon1" class="btn-icon" />
          Статистика
        </button>
      </div>
      <div class="model-viewer-container">
        <ModelViewer />
      </div>
    </main>
  </div>
</template>

<script>
import ModelViewer from './components/ModelViewer.vue'

export default {
  components: {
    ModelViewer
  },
  data() {
    return {
      showPanel: false
    }
  },
  methods: {
    togglePanel() {
      this.showPanel = !this.showPanel
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: Arial, sans-serif;
}
body {
  background-color: white;
}

.app {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  color: white;
}

header {
  position: fixed;
  width: 100%;
  height: 160px;
  z-index: 1000;
  left: 0;
  top: 0;
  background: linear-gradient(90deg, rgb(255, 140, 25), rgb(94, 0, 255) 100%);
  text-align: center;
  box-shadow: 0 20px 28px rgba(0, 0, 0, 0.3);
  color: #fff;
  padding-right: 20px; /* Отступ от правого края */
  display: flex;
  justify-content: space-between; /* Размещение элементов по бокам */
  align-items: center; /* Центрирование по вертикали */
}

header h1 {
  position: absolute;
  font-size: 3rem;
  width: 200px;
  height: 76.6px;
  top: 5px;
  left: -5px;
  font-family: Oswald;
  font-weight: bolder;
  font-size: 45px;
  line-height: 160%;
  letter-spacing: 0%;
}

header h2 {
  position: absolute;
  font-size: 1.5rem;
  font-weight: normal;
  width: 1000px;
  top: 75px;
  left: 50px;
  color: rgb(255, 255, 255);
  font-family: Oswald;
  font-size: 25px;
  font-weight: 600;
  line-height: 89px;
  letter-spacing: 0%;
  text-align: left;
}

.user-icon {
  position: absolute;
  right: 20px; /* Прижимает к правому краю */
  top: 20px; /* Можно регулировать по вертикали */
}

.user-icon img {
  width: 110px; /* Размер иконки */
  height: 110px; /* Размер иконки */
}

.add-checkbox {
  /* state-layer */
  position: static;
  width: 160px;
  height: 80px;
  /* Автолейаут */
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12;
  padding: 16px 20px 16px 16px;


  /* Inside Auto Layout */
  flex: none;
  order: 0;
  align-self: stretch;
  flex-grow: 1;
  margin: 0px 0px;
}
main section {
  margin-top: 400px;
}

main section h3 {
  margin-bottom: 10px;
  color: #444;
  border-bottom: 1px solid #eee;
  padding-bottom: 5px;
}

ul {
  padding-left: 20px;
}

.stat-button{
  /* state-layer */
  position: static;
  width: 160px;
  height: 60px;
  /* Автолейаут */
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 16px 20px 16px 16px;


  /* Дополнительные стили */
  background-color: rgb(255, 140, 25);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: rgb(255, 255, 255);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  font-family: Oswald;
  font-size: 20px;
  font-weight: 600;
  line-height: 36px;
  letter-spacing: 0%;
  text-align: left;
}

.stat-button:hover{
  background-color:  rgb(222, 117, 11);
}

.statistic{
  position: fixed;
  top: 200px;
  left: 40px;
}

.add-button {
  /* state-layer */
  position: static;
  width: 160px;
  height: 60px;
  /* Автолейаут */
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  padding: 16px 20px 16px 16px;

  /* Inside Auto Layout */
  flex: none;
  order: 0;
  align-self: stretch;
  flex-grow: 1;
  margin: 0px 0px;

  /* Дополнительные стили */
  background-color: #6200ee;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: rgb(255, 255, 255);
  font-family: Oswald;
  font-size: 20px;
  font-weight: 600;
  line-height: 36px;
  letter-spacing: 0%;
  text-align: left;
  
}

.add-button:hover {
  background-color: #3700b3;
}

.button-container-add {
  position: fixed;
  top: 200px;
  left: 220px;
}

.add-panel {
  margin-top: 10px;
  background-color: #f3f3f3;
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 25px;
  color: #333;
  position: absolute;  
  top: 290px; 
  left: 0;
  height: 500px;
  width: 380px; 
}
.panel-item {
  padding: 8px 12px;
  background-color: #6200ee;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.panel-item:hover {
  background-color: #3700b3;
}


</style>