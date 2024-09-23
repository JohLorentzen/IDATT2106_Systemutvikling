<template>
  <div :class="isMobile ? 'fixed inset-x-0 bottom-0 z-50' : 'fixed inset-x-0 top-0 z-40'">
    <div class="bg-white shadow-lg">
      <div
        class="max-w-7xl mx-auto py-4 sm:px-10 flex md:justify-start justify-around items-center sm:gap-6 navbar"
      >
        <router-link to="/" v-if="displayLogo">
          <img src="@/assets/logo.svg" class="h-12 w-auto" />
        </router-link>
        <router-link to="/" class="items-center flex md:flex-row flex-col gap-2 router-group">
          <FontAwesomeIcon icon="wave-square" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Sparesti</p>
        </router-link>
        <router-link
          to="/budget"
          class="items-center flex md:flex-row order-first md:order-none flex-col gap-2 router-group"
        >
          <FontAwesomeIcon icon="sack-dollar" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Budsjett</p>
        </router-link>
        <router-link
          to="/challenges"
          class="flex items-center md:flex-row flex-col gap-2 router-group"
        >
          <FontAwesomeIcon icon="trophy" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Utfordringer</p>
        </router-link>
        <router-link
          to="/savingGoals"
          class="flex items-center md:flex-row flex-col gap-2 router-group"
        >
          <FontAwesomeIcon icon="bullseye" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Sparemål</p>
        </router-link>
        <router-link
          v-show="!isMobile"
          to="/explore"
          class="items-center flex text-center gap-2 ml-auto router-group"
        >
          <FontAwesomeIcon icon="graduation-cap" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Tips</p>
        </router-link>
        <router-link
          to="/profile"
          class="items-center flex md:flex-row flex-col gap-2 router-group profile"
        >
          <FontAwesomeIcon icon="user" class="sm:fa-xl fa-lg" />
          <p class="text-s mt-1">Profil</p>
        </router-link>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { ref, onMounted, onUnmounted } from 'vue'

const displayLogo = ref(window.innerWidth > 840)
const isMobile = ref(window.innerWidth < 768)
function handleResize() {
  isMobile.value = window.innerWidth < 768
  displayLogo.value = window.innerWidth > 840
}
onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
a {
  transition: 200ms;
  color: rgb(107 114 128);
}
a:hover,
a.activePage {
  color: #ea6f6f;
  fill: #ea6f6f;
  stroke: #ea6f6f;
}

@media (max-width: 768px) {
  .router-group {
    min-width: 70px;
    gap: 0.2rem;
  }

  .profile {
    margin-left: 0;
  }

  p {
    font-size: 0.8rem;
  }
}
</style>
