<!--TODO error with undefinied pages-->
<template>
  <div class="component-container">
    <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
      <div class="content-container">
      <b-form class="concordance_form" @submit="startTask">

        <b-form-group :label="this.$t('concordance.word')" label-for="extendedWordInput">
          <b-form-input class="form-input-default" ref="extendedWordInput" type="text" :placeholder="this.$t('concordance.word_i')" required
                        v-model="form.word" oninput="setCustomValidity('')"/>
        </b-form-group>

        <b-form-group :label="this.$t('concordance.method')" label-for="methodInput">
          <b-form-select class="form-input-select" selected="" id="methodInput" required v-model="method.selected" :options="method.options"/>
        </b-form-group>

        <transition>
          <b-form-group>
            <b-button class="btn-block filter-button" @click="showFilters" v-if="!show.filters">
              {{$t('concordance.showfilters')}}
            </b-button>
            <b-button class="btn-block filter-button" @click="hideFilters" v-if="show.filters">
              {{$t('concordance.hidefilters')}}
            </b-button>
          </b-form-group>
        </transition>

        <div v-if="show.filters">
          <TaskFilter :output="setFilters"></TaskFilter>
        </div>
        <b-form-group :label="this.$t('concordance.howmany')">
          <b-input-group :prepend="limit.toString()" class="mt-3">
            <b-form-input type="range" min="10" max="100" step="5" v-model="limit" @change="changeLimit"/>
          </b-input-group>
        </b-form-group>
        <b-form-group>
        <b-button class="btn-block submit-button" type="submit" v-if="!show.loading">{{$t('concordance.submit')}}
          </b-button>
          <b-button class="btn-block submit-button" disabled  v-if="show.loading">
            <b-spinner small type="grow"/><span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
          </b-button>
        </b-form-group>
      </b-form>
    </div>
    </div>
    <div class="text-center box-container-result" v-if="task.finished">
      <div class="content-container" >
      <b-card class="bv-example-row" >

         <div class="content-pagination">
           <span v-if="!show.loading" class="rows-count">{{countMessage}}</span>
           <vue-json-to-csv :json-data="json_data" :csv-title="csv_title" :separator="'\t'">
             <b-button type="button"  class="btn filter-button btn-secondary" style="float: right">
               {{$t('download_csv')}}
             </b-button>
           </vue-json-to-csv>
          <b-pagination :total-rows="concordances.length" @change="changePage" align="center" :per-page="limit"
                        :current-page="page" v-model="page"
                        v-if="concordances.length > 0">
          </b-pagination>

         </div>
        <b-spinner v-if="show.loading"  style="width: 3rem; height: 3rem;"/>
        <div class="content-list">
          <div class="justify-content-md-center" v-for="(item) in pages" :key="item.id">
            <concordance-list :data="item" :document-request="getDocument"></concordance-list>
          </div>
        </div>
      </b-card>
      </div>
    </div>
    <b-modal ref="documentModal" size="xl" :title="this.$t('concordance.modal_title')"
             :cancel-title="this.$t('concordance.modal_cancel')" cancel-variant="none" ok-variant="dark submit-button">
      <b-container v-for="item in documentmodal.properties" :key="item.name" style="width: 50%; display: inline-block; float: left">
        <b-row class="mb-2">
          <b-col class="line_bottom" style="text-align: right" ><strong>{{$t(item.name)}}</strong></b-col>
          <b-col class="line_bottom" style="text-align: left" >{{item.value}}</b-col>
        </b-row>
      </b-container>
      <b-container style="width: 50%; display: inline-block; float: left">
        <b-row class="mb-2" v-if="documentmodal.data.proper_names">
          <b-col class="line_bottom" style="text-align: right" ><strong>{{$t('concordance.proper_names')}}</strong></b-col>
          <b-col class="line_bottom" style="text-align: left; overflow: auto; border-style: outset; border-color: #dee2e6" >
            <b-container style="width: 100%; height: 48px">
              <b-row v-if="item.geo_location" v-for="item in documentmodal.data.proper_names" :key="item.name" :title="item.geo_location.name + ' lon:' + item.geo_location.lon + ' lat:' + item.geo_location.lat">
                <b-col class="sm-8">
                  {{item.value}}
                </b-col>

              </b-row>
            </b-container>
          </b-col>
        </b-row>
      </b-container>
      <b-container class="card" style="padding: 20px">
        <b-row v-if="documentmodal.data.text">
          <b-col  style=" text-align: justify; text-justify: inter-word;">
            {{documentmodal.data.text[0]}}
            <span style="color: #8772c3; font-weight: bold">{{documentmodal.data.text[1]}}</span>
            <span style="color: #b14a89; font-weight: bold">{{documentmodal.data.text[2]}}</span>
            <span style="color: #8772c3; font-weight: bold">{{documentmodal.data.text[3]}}</span>
            {{documentmodal.data.text[4]}}
          </b-col>
        </b-row>
      </b-container>

    </b-modal>

  </div>
</template>

<script>

import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'
import ConcordanceList from '@/components/ConcordanceList'
import TaskFilter from '@/components/TaskFilter'
import FilterDate from '@/components/FilterDate'
import VueJsonToCsv from 'vue-json-to-csv'

export default {
  name: 'ConcordanceRequest',
  props: {
    executeOnMount: {
    },
    concordanceWord: {
      type: String
    },
    publicationYear: {
      type: String
    }
  },
  data () {
    return {
      json_data: [],
      csv_title: 'concordance',
      form: {
        word: '',
        corpus: {
          selected: 'chronopress',
          options: [{value: 'chronopress', text: 'chronopress'}]
        }
      },
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      show: {
        loading: false,
        filters: false
      },
      concordances: [],
      limit: 50,
      skip: 0,
      page: 1,
      documentmodal: {
        data: '',
        properties: []
      },
      dupa: [],
      metadata_filters: []
    }
  },
  components: {FilterDate, TaskFilter, ConcordanceList, axios, VueAxios, FadeTransition, VueJsonToCsv},
  watch: {
    page: function (value) {
      this.page = value
      this.skip = this.limit * this.page
    },
    limit: function (value) {
      this.limit = value
      if (this.page > parseInt(this.concordances.length / this.limit) + 1) {
        this.page = parseInt(this.concordances.length / this.limit) + 1
      }
      this.skip = this.limit * this.page
    }
  },
  computed: {
    pages: function () {
      try {
        let concordances = this.concordances
        let result = []
        for (var i = this.skip - this.limit; i < this.skip; i++) {
          if (typeof concordances[i] !== 'undefined') {
            result.push(concordances[i])
          }
        }
        return result
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    method () {
      return {
        selected: 'base',
        options:
            [
              {value: 'base', text: this.$t('concordance.base')},
              {value: 'orth', text: this.$t('concordance.orth')}
            ]
      }
    },
    countMessage () {
      let result = ''
      if (this.concordances.length === 0 || this.concordances.length > 5) {
        result = this.$t('concordance.found3')
      } else if (this.concordances.length === 1) {
        result = this.$t('concordance.found1')
      } else if (this.concordances.length > 1 && this.concordances.length < 5) {
        result = this.$t('concordance.found2')
      }
      return this.concordances.length + ' ' + result
    }
  },
  methods: {
    showFilters: function () {
      this.show.filters = true
    },
    hideFilters: function () {
      this.show.filters = false
      this.metadata_filters = []
    },
    setFilters: function (metadata, all) {
      this.metadata_filters = []
      for (let i = 0; i < all.length; i++) {
        if (typeof metadata[all[i]] !== 'undefined' && metadata[all[i]] !== '') {
          this.metadata_filters.push({
            name: all[i],
            value: metadata[all[i]]
          })
        }
      }
      if (typeof metadata['publication_year'] !== 'undefined' && metadata['publication_year'] !== '') {
        this.metadata_filters.push({
          name: 'publication_year',
          value: metadata['publication_year']
        })
      }
    },
    startTask: async function (event) {
      if (!this.executeOnMount) {
        event.preventDefault()
      }
      this.task.finished = false
      this.show.loading = true
      this.concordances = []
      this.json_data = []
      let task = {
        corpus: 'chronopress',
        task_type: 'concordance',
        metadata_filter: this.metadata_filters,
        query_parameters: [
          {
            name: this.method.selected,
            value: this.form.word
          }
        ],
        response_parameters: []
      }
      console.log('task: ' + JSON.stringify(task))
      try {
        let response = await axios.post(process.env.ROOT_API + 'startTask', task, {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
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
        console.log('task status => ' + this.task.status)
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
        console.log('result: ', response.data.result)
        this.changePage(1)
        this.skip = this.limit
        this.concordances = response.data.result.rows
        this.csv_title = this.form.word + '_' + this.method.selected + '_' + this.task.id
        this.show.loading = false
        for (var x in this.concordances) {
          this.json_data.push({'left': this.concordances[x].concordances[0].left, 'word': this.concordances[x].concordances[0].word, 'right': this.concordances[x].concordances[0].right})
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    },
    changePage: function (value) {
      try {
        this.page = value
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    changeLimit: function (value) {
      try {
        this.changePage(1)
        this.limit = value
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getDocument: function (request) {
      try {
        this.documentmodal.data = request.data.result.documents[0]
        this.documentmodal.properties = this.serializeProperties(request.data.result.documents[0].metadata.properties)
        this.$refs.documentModal.show()
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    serializeProperties: function (properties) {
      let time = {year: null, month: null, day: null}
      let result = []
      properties.forEach(function (p) {
        if (p.name.split('_')[0] === 'publication') {
          time[p.name.split('_')[1]] = p.value
        } else if (p.name === 'article_title' || p.name === 'journal_title' || p.name === 'authors') {
          p.value = p.value.charAt(0).toUpperCase() + p.value.slice(1)
          result.push({'name': p.name, 'value': p.value})
        } else {
          p.value = p.value.charAt(0).toLowerCase() + p.value.slice(1)
          result.push({'name': p.name, 'value': p.value})
        }
      })
      let newDate = new Date(time.year, time.month - 1, time.day)
      var dd = String(newDate.getDate()).padStart(2, '0')
      var mm = String(newDate.getMonth() + 1).padStart(2, '0')
      var yyyy = newDate.getFullYear()
      result.push({'name': 'publication_date', 'value': mm + '-' + dd + '-' + yyyy})
      // result.push({'name': '  ', 'value': '  '})
      return result
    }
  },
  mounted () {
    try {
      if (this.executeOnMount === true && typeof this.concordanceWord !== 'undefined') {
        this.form.word = this.concordanceWord
        if ( typeof this.publicationYear !== 'undefined') {
          this.metadata_filters.push({
            name: 'publication_year',
            value: this.publicationYear
          })
        }
        this.startTask()
      }
    } catch (e) {
      console.log(Object.keys(e), e.message)
    }
  }
}
</script>

<style scoped>
  #form_container {
    max-width: 40em;
    margin: 1vw auto;
    position: relative;
  }
  .content-pagination {
    height:50px;
    background-color: var(--dark_silver);
    padding: 6px;
  }
  .content-list {
    height: calc(100% - 50px) !important;
    overflow-y: auto;
    overflow-x: hidden;
    /*margin-top: -50px;*/
  }
  .card.bv-example-row, .card-body {
    height: 100%;
  }
  .box-container-result {
    overflow: inherit;
  }
  ul.pagination {
    /*width: 100px;*/
  }
  .rows-count {
    background-color: var(--violet_red);
    border-radius: 5px;
    float: left;
    padding: 8px 8px;
    color: white;
    height: 36px;
  }
  @media only screen and (min-width: 768px) {
    .content-list {
      height: 80%;
      overflow-y: auto;
      overflow-x: hidden;
    }
  }
</style>
