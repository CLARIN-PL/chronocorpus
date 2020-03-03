<template>
<div>
</div>
</template>

<script>

import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'

export default {
  name: 'Documents',
  data () {
    return {
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      documents: []
    }
  },
  components: {axios, VueAxios, FadeTransition},
  // TODO todo
  methods: {
    startTask: async function () {
      console.log()
      try {
        let response = await axios.post(process.env.ROOT_API + 'startTask', {
          task_type: 'dictionaries',
          metadata_filter: [],
          query_parameters: [{
            name: 'property_value_list',
            value: 'journal_title'
          }],
          response_parameters: []
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 5000
        })
        this.task.id = response.data.id
        console.log(response)
        this.checkStatus(this.task.id)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId) {
      try {
        let response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 5000})
        this.task.status = response.data.status
        console.log('task status => ' + this.task.status)
        if (this.task.status === 'DONE') {
          this.getResult(taskId)
        } else if (this.task.status === 'QUEUE') {
          console.log(this.task.status)
          setTimeout(() => {
            this.checkStatus(taskId)
          }, 300)
        } else if (this.task.status === 'ERROR') {
          console.log(response)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getResult: async function (taskId) {
      try {
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        // this.task.finished = true
        this.documents = response.data.result.documents
        console.log(response)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    }
  },
  mounted () {
    this.startTask()
  }
}
</script>

<style scoped>

  .concordance-row :hover {
    cursor: pointer;
    color: #007bff;
  }
</style>
