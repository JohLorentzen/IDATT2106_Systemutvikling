<template>
  <div class="min-h-screen flex flex-col items-center bg-backgroundImage bg-scroll bg-cover bg-no-repeat bg-center">
    <h1 class="mt-28 font-bold text-3xl text-primary-text mb-8 tracking-wider">Mine Sparemål</h1>
      <div
        @click="available.length < 3 ? openModal(true) : null">
      <button
          :disabled="available.length >= 3"
          :class="{ 'opacity-50 cursor-not-allowed': available.length >= 3 }"
          class="bg-white text-primary-text px-6 py-2 transition-colors duration-150 rounded-full flex items-center justify-center hover:bg-gray-200"
          style="box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.3)"
      >
          {{ available.length < 3 ? text.addNewGoal : text.maxGoalsReached }}<span class="text-4xl ml-3"> + </span>
      </button>
    </div>
    <div class="flex justify-center gap-4 my-4 mt-16 text-lg">
      <button
          :class="activeTab === 'active' ? 'text-primaryBtn border-b-4 border-primaryBtn border-hoverBtn' : 'text-primary-text hover:text-hoverBtn'"
          @click="switchTab({tab : 'active'})"
      >
        {{ text.activeGoals }}
      </button>
      <button
          :class="activeTab === 'completed' ? 'text-primaryBtn border-b-4 border-primaryBtn border-hoverBtn ml-3' : 'ml-3 text-primary-text hover:text-hoverBtn'"
          @click="switchTab({tab : 'completed'})"
      >
        {{ text.completedGoals }}
      </button>
    </div>
    <div class="goals-container max-w-lg flex w-full flex-col items-center ">

      <div class="w-full max-w-sm items-center gap-4 flex-wrap ml-5 mr-5 p-3 mb-20" v-if="activeTab === 'active'">
        <div class="text-s text-gray-500 text-center font-bold">
        {{ available.length }}/3 {{ text.activeGoalsCountPrompt }}
        </div>
        <SavingGoalOverview
      v-for="(g, index) in available"
          :key="index"
          :goal="g"
          :isCompleted="false"
          :is-goal-overview-page="true"
          :class="{
            'border-4 border-green-500': activeGoal && activeGoal.id === g.id,
            'border-transparent': !(activeGoal && activeGoal.id === g.id)
          }"
          @openModal="isNewGoal = false; isModalOpen = true; goal = $event"
        >

          <template v-slot:header>
          <div v-if="g.active" class="text-center underline">
            {{ text.activeStatusHeader }}
          </div>
        </template>
        </SavingGoalOverview>
      </div>
      <div class="w-full  flex justify-center gap-4 flex-wrap ml-5 mr-5 p-4" v-if="activeTab.valueOf() === 'completed'">
        <SavingGoalOverview
              v-for="(g, index) in completedGoals"
              :key="index"
              :goal="g"
              :isCompleted="true"
        />
        <p
          v-if="completedGoals.length == 0"
          class="text-center mx-4 font-semibold py-6 px-4 w-full bg-white max-w-xl rounded-lg shadow-md"
        >
          {{ text.noArchivedGoalsMessage }}
        </p>
      </div>
    </div>
    <div class="flex flex-col items-center justify-center w-full max-w-3xl" v-if="activeTab === 'active'">
      </div>
    <EditSavingGoalModal :isModalOpen="isModalOpen" :isNewGoal="isNewGoal" :goal="goal" @close="isModalOpen = false"
                         @fetchGoals="fetchGoals"/>
  </div>
</template>
<script setup lang="ts">
import {ref, onMounted} from 'vue'
import SavingGoalOverview from '@/components/SavingGoalOverview.vue'
import text from '@/assets/text.no.json'
import api from "@/services/savingPath/";
import EditSavingGoalModal from '@/components/EditSavingGoalModal.vue'
import type {SavingsGoal} from "@/services/savingPath/types";
import {SavingsGoalImpl} from "@/services/savingPath/types";

const activeTab = ref<'active' | 'completed'>('active');
const available = ref<SavingsGoal[]>([])
const isModalOpen = ref(false)
const isNewGoal = ref(false)
const goal = ref<SavingsGoal>(new SavingsGoalImpl());
const completedGoals = ref<SavingsGoal[]>([])
const activeGoal = ref<SavingsGoal | undefined>(undefined);

/**
 * Fetches the list of incomplete saving goals from the API.
 * Filters out the goals where savedAmount is greater than or equal to goalAmount.
 * sets the first goal as active if there are no active goals.
 * Updates the activeGoals ref with the filtered list of goals.
 *
 * @async
 * @function
 * @throws Will throw an error if the API call fails.
 */
const fetchGoals = async () => {
  try {
    const response = await api.getIncompleteSavingsGoals()
    if (response.status === 204) {
      available.value = []
      return;
    }
    available.value = response.data
    .filter(goal => goal?.savedAmount < goal?.goalAmount)
    .sort((a, b) => {
      if(a?.id === activeGoal.value?.id) {
        return 1
      }else if (b?.id === activeGoal.value?.id) {
        return -1
      }else {
        return 0
      }
    });
    // Find the active goal and set activeGoal to it
    activeGoal.value = available.value.find(goal => goal?.active);
  } catch (error) {
    console.error(error)
  }
}
/**
 * Fetches the list of completed saving goals from the API.
 */
const fetchArchivedGoals = async () => {
  try {
    const response = await api.getArchivedSavingsGoals()
    
    if (response.status === 204) {
      // display no archived goals
      console.log("No goals found")
      return;
    }
    completedGoals.value = [...response.data].map((goal) => {
      return {
        ...goal,
        isTargetReached: goal.savedAmount >= goal.goalAmount,
        dateAchieved: goal.savedAmount >= goal.goalAmount ? new Date() : goal.dateAchieved
      };
    });

    // sorting the completed goals so that the ones with a later dateAchieved are shown first
    completedGoals.value.sort((a, b) => {
  return (b.dateAchieved?.getTime() || 0) - (a.dateAchieved?.getTime() || 0);
    });
  } catch (error) {
    console.error("No archived goals found..")
  }
}
const switchTab = ({tab}: { tab: "active" | "completed" }) => {
  activeTab.value = tab
  if (tab === 'completed') {
    fetchArchivedGoals()
  } else {
    fetchGoals()
  }
}

function openModal(newGoal = false) {
  isNewGoal.value = newGoal
  goal.value = new SavingsGoalImpl()
  isModalOpen.value = true
}

onMounted(() => {
  fetchGoals();
  fetchArchivedGoals()
})
</script>
