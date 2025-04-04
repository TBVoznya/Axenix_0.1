<template>
  <div class="app">
    <header>
      <h1>Logo</h1>
      <h2>Система анализа магазина 3D</h2>
    </header>

    <main>
      <div class="button-container-add">
        <button class="add-button">Добавить</button>
      </div>
      <div class="statistic">
        <button class="stat-button">Статистика</button>
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

.app {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

header {
  position: absolute;
  width: 1280px;
  height: 100px;
  left: 0;
  top: 0;
  background: linear-gradient(90deg, rgb(255, 140, 25), rgb(94, 0, 255) 100%);
  text-align: center;
  color: #fff;
}

header h1 {
  position: absolute;
  font-size: 2.5rem;
  width: 200px;
  height: 76.6px;
  top: 5px;
  left: -5px;
  font-family: Oswald;
  font-weight: 700;
  font-size: 45px;
  line-height: 160%;
  letter-spacing: 0%;
  
}

header h2 {
  position: absolute;
  font-size: 1.5rem;
  font-weight: normal;
  width: 1000px;
  top: 35px;
  left: 50px;
  color: rgb(255, 255, 255);
  font-family: Oswald;
  font-size: 25px;
  font-weight: 400;
  line-height: 89px;
  letter-spacing: 0%;
  text-align: left;
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
  width: 100px;
  height: 50px;
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
  font-family: Oswald;
  font-size: 14px;
  font-weight: 500;
  line-height: 36px;
  letter-spacing: 0%;
  text-align: left;
}

.stat-button:hover{
  background-color:  rgb(222, 117, 11);
}

.statistic{
  position: fixed;
  top: 120px;
  left: 40px;
}

.add-button {
  /* state-layer */
  position: static;
  width: 100px;
  height: 50px;
  /* Автолейаут */
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 12px;
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
  font-size: 14px;
  font-weight: 500;
  line-height: 36px;
  letter-spacing: 0%;
  text-align: left;
  
}

.add-button:hover {
  background-color: #3700b3;
}

.button-container-add {
  position: fixed;
  top: 120px;
  left: 150px;
}


</style>