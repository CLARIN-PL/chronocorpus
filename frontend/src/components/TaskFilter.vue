<template>
  <div>
    <FilterDate :output="pushFilters"></FilterDate>
    <div class="justify-content-md-center" v-for="(item) in all_filters" :key="item.id" :class="item">
      <FilterProperty  :input="item" :output="pushFilters"></FilterProperty>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'
import FilterProperty from '@/components/FilterProperty'
import FilterDate from '@/components/FilterDate'

export default {
  name: 'TaskFilter',
  props: ['output'],
  data () {
    return {
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      all_filters: null,
      metadata_filters: []
    }
  },
  components: {FilterDate, FilterProperty, axios, VueAxios, FadeTransition},
  methods: {
    getTaskId: async function () {
      try {
        let response = await axios.post(process.env.ROOT_API + 'startTask', {
          task_type: 'dictionaries',
          metadata_filter: [],
          query_parameters: [
            {
              name: 'dictionaries',
              value: 'true'
            }
          ],
          response_parameters: []
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          crossDomain: true,
          timeout: 5000
        })
        this.task.id = response.data.id
        this.checkStatus(this.task.id, 100)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 100
        let response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 1000})
        this.task.status = response.data.status
        if (this.task.status === 'DONE') {
          this.getResult(taskId)
        } else if (this.task.status === 'QUEUE') {
          setTimeout(() => {
            this.checkStatus(taskId, timer)
          }, timer)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getResult: async function (taskId) {
      try {
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        let propertyNames = []
        response.data.result.dictionaries.property_names.forEach(function (value, i) {
          // two exceptions due to much data
          if (value !== 'article_title' && value !== 'authors' && value !== 'publication_day' && value !== 'publication_month' && value !== 'publication_year') {
            propertyNames.push(response.data.result.dictionaries.property_names[i])
          }
        })
        this.all_filters = propertyNames
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    pushFilters: function (request, input) {
      try {
        this.metadata_filters[input] = request
        this.output(this.metadata_filters, this.all_filters)
        console.log(this.metadata_filters)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    }
  },
  created () {
    if (this.all_filters === null) {
      this.getTaskId()
    }
  }
}
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
