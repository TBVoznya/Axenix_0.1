<template>
    <li :class="{ 'completed': checked }">
      <input 
        type="checkbox" 
        :checked="checked" 
        @change="$emit('update:checked', $event.target.checked)"
      />
      <span>{{ label }}</span>
    </li>
  </template>
  
  <script>
  export default {
    props: {
      checked: Boolean,
      label: String
    },
    emits: ['update:checked']
  }
  </script>
  
  <style scoped>
  li {
    list-style: none;
    padding: 5px 0;
    display: flex;
    align-items: center;
  }
  
  input[type="checkbox"] {
    margin-right: 10px;
  }
  
  .completed {
    color: #888;
    text-decoration: line-through;
  }
  </style>