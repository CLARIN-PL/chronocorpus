<template>
<div>
  <b-form-group size="sm" :label="this.$t(this.input)">
    <!--<b-form-select size="sm" selected="" v-model="selected" :options="this.options" v-on:change="onChange">-->
      <!--<option value="" v-if="selected">{{$t('undo_selection')}}</option>-->
    <!--</b-form-select>-->
    <multiselect
      v-model="selected"
      :options="this.options"
      :multiple="true"
      @input="onChange"
      :close-on-select="false"
      deselect-label="☒"
      select-label="☐"
      selected-label="☑"
    >
    </multiselect>
  </b-form-group>
</div>
</template>

<script>
import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'
import Multiselect from 'vue-multiselect'

export default {
  name: 'FilterDate',
  props: ['output'],
  data () {
    return {
      input: 'publication_year',
      filter_name: '',
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      selected: '',
      property_value_list: []
    }
  },
  components: {axios, VueAxios, FadeTransition, Multiselect},
  computed: {
    options: function () {
      let data = []
      if (this.filter_name === 'exposition') {
        data = [
          {value: '1', text: this.$t('exp.first')},
          {value: '2', text: this.$t('exp.middle')},
          {value: '3', text: this.$t('exp.last')}
        ]
      } else {
        data = this.property_value_list
      }
      return data
    }
  },
  methods: {
    getTaskId: async function (prop) {
      try {
        this.filter_name = prop
        const response = await axios.post(process.env.ROOT_API + 'startTask', {
          task_type: 'dictionaries',
          metadata_filter: [],
          query_parameters: [
            {
              name: 'property_value_list',
              value: prop
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
        const response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 1000})
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
        const response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.property_value_list = response.data.result[this.input]
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    onChange: function () {
      try {
        // console.log(this.selected)
        let years = ''
        for (let i = 0; i < this.selected.length; i++) {
          years += this.selected[i]
          if (i !== this.selected.length - 1) {
            years += ';'
          }
        }
        // console.log(years)
        this.output(years, this.input)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    }
  },
  mounted () {
    this.getTaskId(this.input)
  }
}
</script>
<style scoped>

</style>
