<template>
    <div style="width: 100%; background-color: var(--cold_silver);">
        <div class="view-title">
            <div class="carousel">
                <router-link class="view-arrow-left" tag="div" to="/concordance">
                    <img src="../assets/images/arrow_left.png"/>
                </router-link>
                <router-link class="view-arrow-right" tag="div" to="/word_profiles">
                    <img src="../assets/images/arrow_right.png"/>
                </router-link>
            </div>
            <h3>{{ $t('home.service2') }} </h3>
        </div>

        <div class="view-content">
            <div class="component-container">
                <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
                    <div class="content-container">
                        <b-form @submit="startTask">
                            <b-form-group>
                                <b-form-select :options="count_by_base.options" class="form-input-select"
                                               id="countByBase" required
                                               selected=""
                                               v-model="count_by_base.selected"/>
                            </b-form-group>
                            <b-form-group :label="this.$t('frequency.stop_list')">
                                <multiselect
                                        v-model="stop_list"
                                        :options="stop_list_default"
                                        :multiple="true"
                                        :close-on-select="false"
                                        deselect-label="☒"
                                        select-label="☐"
                                        selected-label="☑"
                                ></multiselect>
                            </b-form-group>

                            <b-form-group :label="this.$t('concordance.howmany')">
                                <b-input-group :prepend="limit.toString()" class="mt-3">
                                    <b-form-input @change="changeLimit" max="100" min="10" step="5" type="range"
                                                  v-model="limit"/>
                                </b-input-group>
                            </b-form-group>

                            <b-button class="btn-block submit-button" type="submit" v-if="!show.loading">
                                {{$t('frequency.submit')}}
                            </b-button>
                            <b-button class="btn-block submit-button" disabled v-if="show.loading">
                                <b-spinner small type="grow"/>
                                <span v-if="task.finished">{{$t('task.loading_data')}}</span><span
                                    v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
                            </b-button>
                        </b-form>
                    </div>
                </div>
                <div class="box-container-result" v-if="task.finished">
                    <div class="content-container">
                        <b-card class="bv-example-row">
                            <div class="content-pagination" v-if="frequency_list.length > 0">
                                <b-button @click="startDownload" class="btn filter-button btn-secondary"
                                          style="float: right"
                                          type="button"
                                          v-if="!download">{{$t('download_xlsx')}}
                                </b-button>
                                <b-button class="btn filter-button btn-secondary" disabled style="float: right"
                                          type="button"
                                          v-if="download">
                                    <b-spinner small type="grow"/>
                                    {{$t('wait_for_file')}}
                                </b-button>
                                <b-pagination :per-page="limit" :total-rows="number_of_pages * limit"
                                              @change="changePage"
                                              align="center" v-if="frequency_list.length > 0"
                                              v-model="page"/>
                                <!--<b-pagination-nav :link-gen="linkGen" :number-of-pages="number_of_pages" @change="changePage"></b-pagination-nav>-->
                            </div>
                            <div class="content-list">
                                <b-spinner style="width: 3rem; height: 3rem;" v-if="show.loading"/>
                                <b-row :key="item.id" class="line_bottom justify-content-md-center"
                                       v-for="(item) in frequency_list">
                                    <b-col cols="7" style="text-align: left;  font-size: 12px">{{item.word}}</b-col>
                                    <b-col cols="3" style="text-align: right; font-size: 12px">
                                        {{partOfSpeech(item.part_of_speech)}}
                                    </b-col>
                                    <!--<b-col cols="3" style="text-align: right; font-size: 12px">{{item.part_of_speech}}</b-col>-->
                                    <b-col cols="2" style="margin: auto">{{item.count}}</b-col>
                                </b-row>
                                <!--<b-table v-if="task.finished" small striped hover fixed :items="frequency_list" :current-page="page" :per-page="limit" :filter="table_filter"  @filtered="onFiltered"></b-table>-->
                            </div>
                        </b-card>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import FadeTransition from '@/components/FadeTransition.vue'
import axios from 'axios'
import VueJsonToCsv from 'vue-json-to-csv'
import Multiselect from 'vue-multiselect'

export default {
  name: 'FrequencyLists',
  data () {
    return {
      json_data: [],
      csv_title: 'frequency_lists',
      table_filter: null,
      task: {
        id: null,
        status: '',
        result: '',
        finished: false,
        type: ''
      },
      show: {
        loading: false
      },
      frequency_list: [],
      limit: 50,
      skip: 0,
      page: 1,
      number_of_pages: 0,
      download: false,
      stop_list: [],
      stop_list_default: []
    }
  },
  components: {axios, FadeTransition, VueJsonToCsv, Multiselect},
  watch: {
    page: function (value) {
      this.page = value
      this.skip = this.limit * this.page
    },
    limit: function (value) {
      this.limit = value
      if (this.page > parseInt(this.frequency_list.length / this.limit) + 1) {
        this.page = parseInt(this.frequency_list.length / this.limit) + 1
      }
      this.skip = this.limit * this.page
    }
  },
  computed: {
    count_by_base: function () {
      return {
        selected: 'frequency_by_orth',
        options: [
          {value: 'frequency_by_base', text: this.$t('frequency.true')},
          {value: 'frequency_by_orth', text: this.$t('frequency.false')}]
      }
    },
    pages: function () {
      try {
        let frequencyList = this.frequency_list
        let result = []
        for (let i = this.skip - this.limit; i < this.skip; i++) {
          if (typeof frequencyList[i] !== 'undefined') {
            result.push(frequencyList[i])
          }
        }
        return result
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    }
  },
  methods: {
    startTask: async function (event) {
      event.preventDefault()
      if (this.count_by_base) {
        this.task.type = 'frequency_by_base'
      } else {
        this.task.type = 'frequency_by_orth'
      }
      this.frequency_list = []
      this.task.finished = false
      this.show.loading = true
      this.getPage()
    },
    getPage: async function () {
      try {
        console.log('task: ' + process.env.ROOT_API + 'getPagination/' + this.count_by_base.selected + '?page=' + (this.page - 1) + '&size=' + this.limit)
        let response = await axios.get(process.env.ROOT_API + 'getPagination/' + this.count_by_base.selected + '?page=' + (this.page - 1) + '&size=' + this.limit, {}, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 10000
        })
        this.task.status = response.data.status
        console.log('status => ' + this.task.status)
        if (this.task.status === 'DONE') {
          this.task.finished = true
          this.frequency_list = response.data.result.rows
          this.number_of_pages = response.data.result.numberOfPages
          this.show.loading = false
        } else if (this.task.status === 'QUEUE') {
          setTimeout(() => {
            this.getPage()
          }, 1000)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getTaskId: async function (event) {
      event.preventDefault()
      this.task.finished = false
      this.show.loading = true
      this.frequency_list = []
      this.json_data = []
      let stopList = ''
      this.stop_list.forEach(element => {
        stopList += element + ';'
      })
      let task = {
        corpus: document.querySelector('#methodInput').options[document.querySelector('#methodInput').selectedIndex].value,
        task_type: 'frequency',
        metadata_filter: [],
        query_parameters: [
          {
            name: 'count_by_base',
            value: this.count_by_base.selected
          },
          {
            name: 'stop_list',
            value: stopList
          }
        ],
        response_parameters: []
      }
      console.log('task: ' + JSON.stringify(task))
      try {
        let response = await axios.post(process.env.ROOT_API + 'startTask', task, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 5000
        })
        this.task.id = response.data.id
        this.checkStatus(this.task.id, 1000)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 200
        console.log(timer)
        let response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 10000})
        this.task.status = response.data.status
        console.log('status => ' + this.task.status)
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
        this.task.finished = true
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.changePage(1)
        this.skip = this.limit
        for (let i = 0; i < 1000; i++) {
          this.frequency_list[i] = response.data.result.rows[i]
        }
        this.show.loading = false
        for (let i = 1000; i < response.data.result.rows.length; i++) {
          this.frequency_list[i] = response.data.result.rows[i]
        }
        this.show.loading = false
        for (var x in this.frequency_list) {
          this.json_data.push({
            'word': this.frequency_list[x].word,
            'count': this.frequency_list[x].count,
            'part_of_speech': this.frequency_list[x].part_of_speech
          })
        }
        this.csv_title = this.count_by_base.selected === 'true' ? 'frequency_lists_words' : 'frequency_lists_lexemes'
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    },
    changePage: function (value) {
      try {
        this.page = value
        this.getPage()
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    changeLimit: function (value) {
      try {
        this.limit = value
        this.getPage()
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    partOfSpeech: function (value) {
      try {
        let number = parseInt(value)
        let result = ''
        switch (number) {
          case 1 :
            result = this.$t('verb')
            break
          case 2 :
            result = this.$t('noun')
            break
          case 3 :
            result = this.$t('adverb')
            break
          case 4 :
            result = this.$t('adjective')
            break
        }
        return result
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    onFiltered (filteredItems) {
      // Trigger pagination to update the number of buttons/pages due to filtering
      // this.totalRows = filteredItems.length
      this.page = 1
    },
    startDownload: async function (event) {
      event.preventDefault()
      this.download = true
      this.downloadXLSX()
    },
    downloadXLSX: async function () {
      try {
        axios({
          url: process.env.ROOT_API + 'getXLSX/' + this.count_by_base.selected,
          method: 'GET',
          responseType: 'blob'
        }).then((response) => {
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', this.count_by_base.selected + '.xlsx')
          document.body.appendChild(link)
          link.click()
          this.download = false
        })
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.download = false
      }
    },
    getStopList: async function () {
      try {
        let task = {
          corpus: document.querySelector('#methodInput').options[document.querySelector('#methodInput').selectedIndex].value,
          task_type: 'dictionaries',
          metadata_filter: [],
          query_parameters: [
            {
              name: 'dictionaries',
              value: 'true'
            }
          ],
          response_parameters: []
        }
        console.log('task: ' + JSON.stringify(task))
        let response = await axios.post(process.env.ROOT_API + 'startTask', task, {
          headers: {
            'Content-Type': 'application/json'
          },
          crossDomain: true,
          timeout: 5000
        })
        this.checkStopListStatus(response.data.id, 100)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStopListStatus: async function (taskId, timer) {
      try {
        timer += 200
        let response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 10000})
        if (response.data.status === 'DONE') {
          this.getStopListResult(taskId)
        } else if (response.data.status === 'QUEUE') {
          setTimeout(() => {
            this.checkStatus(taskId, timer)
          }, timer)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getStopListResult: async function (taskId) {
      try {
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.stop_list_default = response.data.result.dictionaries.default_stop_list
        this.stop_list = response.data.result.dictionaries.default_stop_list
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    }
  },
  mounted () {
    this.getStopList()
  }
}
</script>

<style scoped>
    .multiselect {
        max-width: 500px;
    }
    .line_bottom {
        border-bottom: 1px solid #c3c3c3;
    }

    .content-pagination {
        height: 50px;
        background-color: var(--dark_silver);
        padding: 6px;
    }

    .card.bv-example-row, .card-body {
        height: 100%;
    }

    .content-list {
        /*height: 80%;*/
        overflow-y: auto;
        overflow-x: hidden;
    }

    .box-container-result {
        overflow: inherit;
    }

    @media only screen and (min-width: 768px) {
        .content-list {
            height: calc(100% - 50px);
            overflow-y: auto;
            overflow-x: hidden;
        }
    }
</style>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
