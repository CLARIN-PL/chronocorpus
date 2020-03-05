<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <div class="carousel">
        <router-link to="/frequency_lists" tag="div" class="view-arrow-left">
          <img src="../assets/images/arrow_left.png"/>
        </router-link>
        <router-link to="/quantity_analysis" tag="div" class="view-arrow-right">
          <img src="../assets/images/arrow_right.png"/>
        </router-link>
      </div>
      <h3>
      {{ $t('home.service3') }}
      </h3>
    </div>
    <div class="view-content">
      <div class="component-container">
        <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
          <div class="content-container">
            <b-form @submit="getTaskId">

              <b-form-group :label="this.$t('wordprofiles.word')">
                <b-form-input class="form-input-default" type="text" :placeholder="this.$t('concordance.word_i')" required
                              v-model="form.orth" oninput="setCustomValidity('')"/>
              </b-form-group>

              <b-form-group :label="this.$t('wordprofiles.word_part')">
                <b-form-select selected="" required v-model="word_part_of_speech.selected"
                               :options="word_part_of_speech.options"/>
              </b-form-group>

              <b-container style="padding: 0 !important">
                <b-row >
                  <b-col>
                    <b-form-group :label="this.$t('wordprofiles.left')" >
                      <b-form-select class="form-input-select" selected="1" required v-model="form.left_window_size"
                                     :options="[0,1,2,3,4,5]"/>
                    </b-form-group>
                  </b-col>
                  <b-col>
                    <b-form-group :label="this.$t('wordprofiles.right')">
                      <b-form-select class="form-input-select" selected="1" required v-model="form.right_window_size"
                                     :options="[0,1,2,3,4,5]"/>
                    </b-form-group>
                  </b-col>
                </b-row>
              </b-container>
              <!--<b-form-group-->
                <!--:label="this.$t('wordprofiles.context')">-->
                <!--<b-button class="trigger-button" :pressed.sync="adjective"><span class="trigger-button-ico"></span> {{$t('adjective')}}</b-button>-->
                <!--<b-button class="trigger-button" :pressed.sync="noun" ><span class="trigger-button-ico"></span>{{$t('noun')}}</b-button>-->
                <!--<b-button class="trigger-button" :pressed.sync="verb" ><span class="trigger-button-ico"></span>{{$t('verb')}}</b-button>-->
                <!--<b-button class="trigger-button" :pressed.sync="adverb"><span class="trigger-button-ico"></span>{{$t('adverb')}}</b-button>-->
              <!--</b-form-group>-->

              <b-form-group :label="this.$t('wordprofiles.context')">
                <b-form-select selected="" required v-model="context_part_of_speech.selected"
                               :options="context_part_of_speech.options"/>
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
                <b-button class="btn-block submit-button" type="submit" variant="primary" v-if="!show.loading">
                  {{$t('wordprofiles.submit')}}
                </b-button>
                <b-button class="btn-block submit-button" disabled variant="primary" v-if="show.loading">
                  <b-spinner small type="grow"/>
                  <span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
                </b-button>
              </b-form-group>
            </b-form>
          </div>
        </div>
        <div class="box-container-result" v-if="task.finished">
          <div class="content-container">
            <b-card class="bv-example-row" v-if="task.finished">
              <b-spinner v-if="show.loading" style="width: 3rem; height: 3rem;"/>
              <div class="content-pagination" v-if="word_profiles.length > 0">
                <vue-json-to-csv :json-data="json_data" :csv-title="csv_title" :separator="'\t'">
                  <b-button type="button"  class="btn filter-button btn-secondary" style="float: right">
                    {{$t('download_csv')}}
                  </b-button>
                </vue-json-to-csv>
                <b-pagination :total-rows="word_profiles.length" :per-page="limit" @change="changePage" align="center"
                              v-if="word_profiles.length > 0"/>
              </div>

                <div class="content-list col-5 card-img-left" v-if="chart.datasets[0].data.length > 1">
                  <pie-chart v-if="show.chart" :chart-data="chart"></pie-chart>
                </div>
                <div class="content-list col-5 card-img-left" v-if="!(chart.datasets[0].data.length > 1) && task.finished">
                  {{$t('nodata')}}
                </div>
              <div class="content-list col-7">
              <b-row class="justify-content-md" v-for="(item) in pages" :key="item.id">
                <b-col col lg="2" class="line_bottom">{{item.collocate}}</b-col>
                <b-col col lg="6" class="line_bottom">{{item.matching.split(',').join(', ')}}</b-col>
                <b-col col lg="2" class="line_bottom">{{item.frequency}}</b-col>
                <b-col col lg="2" class="line_bottom">{{item.percentage.toFixed(3)}}</b-col>

              </b-row>
              </div>
            </b-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>R

<script>
import FadeTransition from '@/components/FadeTransition.vue'
import axios from 'axios'
import TaskFilter from '@/components/TaskFilter'
import VueJsonToCsv from 'vue-json-to-csv'
import PieChart from '../components/PieChart'

export default {
  name: 'WordProfiles',
  data () {
    return {
      json_data: [],
      csv_title: 'word_profiles',
      form: {
        orth: 'Polska',
        left_window_size: 1,
        right_window_size: 1
      },
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      show: {
        loading: false,
        filters: false,
        chart: false
      },
      chart:
        {
          label: 'a',
          labels: [],
          datasets: [
            {
              backgroundColor: ['#f44336', '#2196f3', '#ffc107', '#4caf50', '#7e57c2', '#8d6e63', '#ec407a', '#3f51b5', '#7cb342', '#c7c7c7'],
              data: []
            }
          ]
        },
      word_profiles: [],
      adjective: false,
      noun: true,
      verb: false,
      adverb: false,
      limit: 50,
      skip: 0,
      page: 1,
      metadata_filters: []
    }
  },
  components: {TaskFilter, axios, FadeTransition, VueJsonToCsv, PieChart},
  watch: {
    page: function (value) {
      this.page = value
      this.skip = this.limit * this.page
    },
    limit: function (value) {
      this.limit = value
      if (this.page > parseInt(this.word_profiles.length / this.limit) + 1) {
        this.page = parseInt(this.word_profiles.length / this.limit) + 1
      }
      this.skip = this.limit * this.page
    },
    adjective: function () {
      if (this.noun === false && this.verb === false && this.adverb === false) {
        if (this.adjective === false) {
          this.adjective = true
          alert(this.$t('quantity_analysis.alert'))
        }
      }
    },
    noun: function () {
      if (this.adjective === false && this.verb === false && this.adverb === false) {
        if (this.noun === false) {
          this.noun = true
          alert(this.$t('quantity_analysis.alert'))
        }
      }
    },
    verb: function () {
      if (this.noun === false && this.adjective === false && this.adverb === false) {
        if (this.verb === false) {
          this.verb = true
          alert(this.$t('quantity_analysis.alert'))
        }
      }
    },
    adverb: function () {
      if (this.noun === false && this.verb === false && this.adjective === false) {
        if (this.adverb === false) {
          this.adverb = true
          alert(this.$t('quantity_analysis.alert'))
        }
      }
    }
  },
  computed: {
    pages: function () {
      try {
        let result = []
        for (var i = this.skip - this.limit; i < this.skip; i++) {
          if (typeof this.word_profiles[i] !== 'undefined') {
            result.push(this.word_profiles[i])
          }
        }
        return result
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    word_part_of_speech () {
      return {
        selected: '2',
        options:
                [
                  {value: '1', text: this.$t('verb')},
                  {value: '2', text: this.$t('noun')},
                  {value: '3', text: this.$t('adverb')},
                  {value: '4', text: this.$t('adjective')}
                ]
      }
    },
    context_part_of_speech () {
      return {
        selected: '1',
        options:
                [
                  {value: '1', text: this.$t('verb')},
                  {value: '2', text: this.$t('noun')},
                  {value: '3', text: this.$t('adverb')},
                  {value: '4', text: this.$t('adjective')}
                ]
      }
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
    getTaskId: async function (event) {
      event.preventDefault()
      this.task.finished = false
      this.show.loading = true
      this.word_profiles = []
      this.show.chart = false
      this.json_data = []
      let task = {
        task_type: 'word_profile',
        metadata_filter: this.metadata_filters,
        query_parameters: [
          {
            name: 'orth',
            value: this.form.orth.toString()
          },
          {
            name: 'part_of_speech',
            value: this.word_part_of_speech.selected.toString()
          },
          {
            name: 'window_item_part_of_speech',
            value: this.context_part_of_speech.selected.toString()
            // value: this.part_of_speech.toString()
          },
          {
            name: 'left_window_size',
            value: this.form.left_window_size.toString()
          },
          {
            name: 'right_window_size',
            value: this.form.right_window_size.toString()
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
        this.checkStatus(response.data.id, 200)
      } catch (e) {
        this.task.finished = false
        this.show.loading = true
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 100
        let response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 1000})
        this.task.status = response.data.status
        console.log('status => ' + this.task.status)
        if (this.task.status === 'DONE') {
          this.getResult(taskId)
        } else if (this.task.status === 'QUEUE') {
          if (timer <= 5000) {
            setTimeout(() => {
              this.checkStatus(taskId, timer)
            }, timer)
          } else {
            this.task.finished = false
            this.show.loading = false
          }
        } else if (this.task.status === 'ERROR') {
          this.task.finished = false
          this.show.loading = false
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getResult: async function (taskId) {
      try {
        this.task.finished = true
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        console.log(process.env.ROOT_API + 'getResult/' + taskId)
        this.changePage(1)
        this.skip = this.limit
        this.csv_title = 'word_profiles_' + this.form.orth + '_L' + this.form.left_window_size + '_R' + this.form.right_window_size
        this.word_profiles = response.data.result.rows

        for (var x in response.data.result.rows) {
          this.json_data.push({
            'collocate': x.collocate,
            'matching': x.matching,
            'frequency': x.frequency,
            'percentage': x.percentage
          })
        }
        this.mapChartData(response.data.result.rows)
        this.show.loading = false
        setTimeout(() => {
          this.show.chart = true
        }, 110)
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
    mapChartData: function (data) {
      console.log(data)
      this.chart =
              {
                label: 'a',
                labels: [],
                datasets: [
                  {
                    backgroundColor: ['#f44336', '#2196f3', '#ffc107', '#4caf50', '#7e57c2', '#8d6e63', '#ec407a', '#3f51b5', '#7cb342', '#c7c7c7'],
                    data: []
                  }
                ]
              }
      // let left = 100
      let fields = (data.length >= 10) ? 10 : data.length

      this.chart.labels = []
      this.chart.datasets[0].data = []

      for (let i = 0; i < fields; i++) {
        this.chart.labels[i] = '"' + data[i].collocate + '" (' + data[i].percentage.toFixed(3) + '%)'
        this.chart.datasets[0].data[i] = data[i].percentage
      }
      this.show.chart = true
    }
  }
}
</script>
  <style scoped>
    .line_bottom {
      border-bottom: 1px solid #c3c3c3;
    }
   .content-pagination {
     height:50px;
     background-color: var(--dark_silver);
     padding: 6px;
   }
    .card.bv-example-row, .card-body {
      height: 100%;
    }
  .content-list {
    margin: auto;
    float: left;
    height: calc(100% - 30px);
    overflow-y: auto;
    overflow-x: hidden;
  }
  .box-container-result {
    overflow: inherit;
  }
  @media only screen and (min-width: 768px) {

  }
</style>
