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
        <button class="panel-item" @click="addCustomer">Покупатель</button>
        <button class="panel-item" @click="addShelf">Полка</button>
        <button class="panel-item" @click="addProduct">Товар</button>
      </div>
      <div class="statistic">
        <button class="stat-button" @click="toggleStatistics">
          <img src="@/assets/add_circle.png" alt="icon1" class="btn-icon" />
          Статистика
        </button>
      </div>

      <!-- Таблица статистики -->
      <div v-if="showStatistics" class="statistics-table">
        <table>
          <thead>
            <tr>
              <th>Параметр</th>
              <th>Значение</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Покупателей за день:</td>
              <td>{{ statistics.customersPerDay }}</td>
            </tr>
            <tr>
              <td>Пиковый Трафик:</td>
              <td>{{ statistics.peakTraffic }}</td>
            </tr>
            <tr>
              <td>Кол-во поломок:</td>
              <td>{{ statistics.breakdowns }}</td>
            </tr>
            <tr>
              <td>Кол-во проданных товаров:</td>
              <td>{{ statistics.soldProducts }}</td>
            </tr>
            <tr>
              <td>Эффективность акций:</td>
              <td>{{ statistics.promotionEfficiency }}</td>
            </tr>
            <tr>
              <td>Удовлетворённость:</td>
              <td>{{ statistics.satisfaction }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="model-viewer-container">
        <ModelViewer :storeData="storeData" :newCustomers="newCustomers" :newShelves="newShelves" :newProducts="newProducts"/>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'
import ModelViewer from './components/ModelViewer.vue'

export default {
  components: {
    ModelViewer
  },
  data() {
  return {
    storeData: null,
    showPanel: false,
    showStatistics: false, // Флаг для отображения таблицы статистики
    newCustomers: [],
    newShelves: [],
    newProducts: [],
    statistics: {
      customersPerDay: 0,
      peakTraffic: 0,
      breakdowns: 0,
      soldProducts: 0,
      promotionEfficiency: '0%',
      satisfaction: '0%'
    }
  };
},
  methods: {
    togglePanel() {
      this.showPanel = !this.showPanel
    },
    toggleStatistics() {
      this.showStatistics = !this.showStatistics;
      if (this.showStatistics) {
        this.getStatistics();
      }
    },
    addCustomer() {
      const newCustomer = { 
        name: "Новый покупатель", 
        profile: "любит скидки",
        position: { x: 0, y: 0, z: 0 } 
      }
      axios.post('/api/customer', newCustomer)
        .then(response => {
          console.log("Покупатель добавлен", response)
          this.newCustomers.push(response.data)
          this.showPanel = false
        })
        .catch(error => {
          console.error("Ошибка добавления покупателя", error)
        })
    },
    addShelf() {
      const newShelf = {
        type: "Стандартная",
        position: { x: 0, y: 0, z: 0 },
        capacity: 100
      }
      axios.post('/api/shelf', newShelf)
        .then(response => {
          console.log("Полка добавлена", response)
          this.newShelves.push(response.data)
          this.showPanel = false
        })
        .catch(error => {
          console.error("Ошибка добавления полки", error)
        })
    },
    addProduct() {
      const newProduct = {
        name: "Новый товар",
        category: "Разное",
        price: 100
      }
      axios.post('/api/product', newProduct)
        .then(response => {
          console.log("Товар добавлен", response)
          this.newProducts.push(response.data)
          this.showPanel = false
        })
        .catch(error => {
          console.error("Ошибка добавления товара", error)
        })
    },
    getStatistics() {
      axios.get('/api/statistics')
        .then(response => {
          console.log("Статистика:", response.data)
        })
        .catch(error => {
          console.error("Ошибка получения статистики", error)
        })
    }
  },
  mounted() {
    axios.get('/api/store')
      .then(response => {
        this.storeData = response.data
      })
      .catch(error => {
        console.error('Ошибка получения данных магазина', error)
      })
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
  padding-right: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  right: 20px;
  top: 20px;
}

.user-icon img {
  width: 110px;
  height: 110px;
}

main section {
  margin-top: 400px;
}

.stat-button{
  position: static;
  width: 160px;
  height: 60px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 16px 20px 16px 16px;
  background-color: rgb(255, 140, 25);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  font-family: Oswald;
  font-size: 20px;
  font-weight: 600;
  line-height: 36px;
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
  position: static;
  width: 160px;
  height: 60px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  padding: 16px 20px 16px 16px;
  background-color: #6200ee;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-family: Oswald;
  font-size: 20px;
  font-weight: 600;
  line-height: 36px;
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
  height: auto;
  width: 380px; 
  z-index: 1001;
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

.model-viewer-container {
  margin-top: 180px;
}

.statistics-table {
  position: absolute;
  top: 30%;
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

.statistics-table table {
  width: 100%;
  border-collapse: collapse;
}

.statistics-table th, .statistics-table td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.statistics-table th {
  background-color: #007bdf;
  color: white;
} 

.statistics-table td {
  background-color: #fff;
}
</style>