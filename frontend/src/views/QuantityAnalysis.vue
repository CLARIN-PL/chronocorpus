<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <div class="carousel">
        <router-link to="/word_profiles" tag="div" class="view-arrow-left">
          <img src="../assets/images/arrow_left.png"/>
        </router-link>
        <router-link to="/names_map" tag="div" class="view-arrow-right">
          <img src="../assets/images/arrow_right.png"/>
        </router-link>
      </div>
      <h3>{{ $t('home.service4') }}</h3>
    </div>
    <div class="view-content">
      <div class="component-container">
        <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
          <div class="content-container">
            <b-form @submit="startTask">
              <b-form-group :label="this.$t('quantity_analysis.calculation_type')">
                <b-form-select selected="" required v-model="calculation_type.selected"
                               :options="calculation_type.options" @change="onTypeChange"/>
              </b-form-group>
              <b-form-group   v-if="is_average" :label="this.$t('quantity_analysis.calculation_object')">
                <b-form-select selected="" required v-model="calculation_object.selected"
                               :options="calculation_object.options" @change="onObjectChange"/>
              </b-form-group>

              <b-form-group v-if="is_average" :label="this.$t('quantity_analysis.calculation_unit')">
                <b-form-select selected="" required v-model="calculation_unit.selected"
                               :options="calculation_unit.options"/>
              </b-form-group>
              <b-form-group
                :label="this.$t('quantity_analysis.parts_of_speech')">
                  <b-button class="trigger-button" :pressed.sync="adjective"><span class="trigger-button-ico"></span> {{$t('adjective')}}</b-button>
                  <b-button class="trigger-button" :pressed.sync="noun" ><span class="trigger-button-ico"></span>{{$t('noun')}}</b-button>
                  <b-button class="trigger-button" :pressed.sync="verb" ><span class="trigger-button-ico"></span>{{$t('verb')}}</b-button>
                  <b-button class="trigger-button" :pressed.sync="adverb"><span class="trigger-button-ico"></span>{{$t('adverb')}}</b-button>
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

              <b-button class="btn-block submit-button" type="submit" variant="primary" v-if="!show.loading">
                {{$t('quantity_analysis.submit')}}
              </b-button>
              <b-button class="btn-block submit-button" disabled variant="primary" v-if="show.loading">
                <b-spinner small type="grow"/>
                <span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
              </b-button>
            </b-form>
          </div>
        </div>
        <div class="text-center box-container-result" v-if="task.finished">
          <div class="content-container" ref="chartContainer">
            <div v-on:resize="redrawChart()">
              <div>
                <b-collapse id="collapse-1" class="mt-2">
                  <b-card class="mb-2">
                    <div class=""></div>
                    <b-container v-for="item in table" :key="item.key" class="info-card" style="">
                      <b-row class="mb-0  line_bottom">
                        <b-col style="text-align: right" ><strong>{{$t('quantity_analysis.' + item.key)}}</strong></b-col>
                        <b-col style="text-align: left" >{{ isNaN(item.value) ? $t('quantity_analysis.' + item.value) : item.value.toFixed(8)}}</b-col>
                      </b-row>
                    </b-container>
                  </b-card>
                </b-collapse>
              </div>

              <b-button v-b-toggle.collapse-1 type="button" size="sm" class="btn submit-button btn-secondary" style="margin: 3px; float: left">
                {{$t('quantity_analysis.more_info')}}
              </b-button>

                <b-button class="btn filter-button" size="sm"  @click="log = !log" style="margin: 3px; float: left" :title="$t('scale.title')">
                  {{$t('scale.'+log.toString())}}
                </b-button>

              <b-button v-on:click="export2image" type="button" size="sm" class="btn submit-button btn-secondary" style="margin: 3px; float: right">
                {{$t('download')}} PNG
              </b-button>
              <vue-json-to-csv :json-data="json_data" :csv-title="csv_title" :separator="'\t'">
                <b-button type="button" size="sm" class="btn filter-button btn-secondary" style="margin: 3px; float: right">
                  {{$t('download')}} TSV
                </b-button>
              </vue-json-to-csv>
              <b-button v-on:click="redrawChart()" type="button" size="sm" class="btn filter-button btn-secondary" style="margin: 3px; float: right">
                ⟲
              </b-button>

            </div>
              <b-spinner v-if="show.loading" style="width: 3rem; height: 3rem;"/>
            <fade-transition>
              <bar-chart :height="chartHeight" :chart-data="chart" :options="options" v-if="show.chart"></bar-chart>
            </fade-transition>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import FadeTransition from '@/components/FadeTransition.vue'
import axios from 'axios'
import TaskFilter from '@/components/TaskFilter'
import BarChart from '../components/BarChart'
import VueJsonToCsv from 'vue-json-to-csv'
import { ResizeObserver } from 'vue-resize'

export default {
  name: 'QuantityAnalysis',
  components: {BarChart, TaskFilter, axios, FadeTransition, VueJsonToCsv, ResizeObserver},
  data () {
    return {
      json_data: [],
      csv_title: 'quantity_analysis',
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
      quantity_analysis: [],
      adjective: true,
      noun: true,
      verb: true,
      adverb: true,
      log: false,
      metadata_filters: [],
      word_was_chosen: true,
      is_average: true,
      table: [],
      chart: {},
      chartHeight: 470
    }
  },
  computed: {
    calculation_object () {
      return {
        selected: 'word',
        options:
          [
            {value: 'word', text: this.$t('quantity_analysis.word')},
            {value: 'sentence', text: this.$t('quantity_analysis.sentence')}
          ]
      }
    },
    calculation_type () {
      if (this.word_was_chosen) {
        return this.calculation_type_word
      } else {
        return this.calculation_type_sentence
      }
    },
    calculation_unit () {
      if (this.word_was_chosen) {
        return this.calculation_unit_word
      } else {
        return this.calculation_unit_sentence
      }
    },
    calculation_type_word () {
      return {
        selected: 'average',
        options:
          [
            {value: 'average', text: this.$t('quantity_analysis.average')},
            {value: 'zipf_histogram', text: this.$t('quantity_analysis.zipf_histogram')}
          ]
      }
    },
    calculation_type_sentence () {
      return {
        selected: 'average',
        options:
          [
            {value: 'average', text: this.$t('quantity_analysis.average')}
          ]
      }
    },
    calculation_unit_word () {
      return {
        selected: 'letter',
        options:
          [
            {value: 'letter', text: this.$t('quantity_analysis.letter')},
            {value: 'syllable', text: this.$t('quantity_analysis.syllable')},
            {value: 'phoneme', text: this.$t('quantity_analysis.phoneme')}
          ]
      }
    },
    calculation_unit_sentence () {
      return {
        selected: 'word',
        options:
          [
            {value: 'word', text: this.$t('quantity_analysis.word')},
            {value: 'letter', text: this.$t('quantity_analysis.letter')},
            {value: 'syllable', text: this.$t('quantity_analysis.syllable')},
            {value: 'phoneme', text: this.$t('quantity_analysis.phoneme')}
          ]
      }
    },
    part_of_speech () {
      let partOfSpeech = ''
      if (this.adjective) {
        partOfSpeech += '1'
      }
      if (this.noun) {
        if (partOfSpeech !== '') {
          partOfSpeech += ';'
        }
        partOfSpeech += '2'
      }
      if (this.verb) {
        if (partOfSpeech !== '') {
          partOfSpeech += ';'
        }
        partOfSpeech += '3'
      }
      if (this.adverb) {
        if (partOfSpeech !== '') {
          partOfSpeech += ';'
        }
        partOfSpeech += '4'
      }
      return partOfSpeech
    },
    options () {
      return {
        title: {
          display: true,
          text: this.$t('home.service4')
        },
        scales: {
          yAxes: [{
            type: this.log ? 'logarithmic' : 'linear',
            scaleLabel: {
              display: true,
              labelString: this.$t('quantity_analysis.y_label')
            },
            ticks: {
              beginAtZero: true
            },
            gridLines: {
              display: true
            }
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString:
                this.$t('quantity_analysis.x_prefix') +
                this.$t('quantity_analysis.' + this.calculation_unit.selected) +
                this.$t('quantity_analysis.x_sufix') +
                this.$t('quantity_analysis.' + this.calculation_object.selected) + ')'
            },
            ticks: {
              beginAtZero: true
            },
            gridLines: {
              display: false
            }
          }]
        },
        legend: {
          display: false
        },
        tooltips: {
          enabled: true,
          mode: 'single',
          callbacks: {
            label: function (tooltipItems, data) {
              return tooltipItems.yLabel
            }
          }
        },
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },
  watch: {
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
    },
    options: function () {
      this.redrawChart()
    }
  },
  methods: {
    onObjectChange: function (value) {
      if (value === 'word') {
        this.word_was_chosen = true
      } else if (value === 'sentence') {
        this.word_was_chosen = false
      }
    },
    onTypeChange (value) {
      this.is_average = value === 'average'
    },
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
      event.preventDefault()
      this.task.finished = false
      this.show.loading = true
      this.show.chart = false
      this.json_data = []
      this.quantity_analysis = []
      this.csv_title = this.calculation_unit.selected + 's_in_' + this.calculation_object.selected
      this.chart = {
        labels: [],
        datasets: [
          {
            backgroundColor: '#b14a89',
            pointBackgroundColor: 'white',
            borderWidth: 1,
            data: []
          }],
        legend: { // hides the legend
          display: false
        },
        scales: { // hides the y axis
          yAxes: [{
            type: 'logarithmic'
          }]
        }}
      try {
        let task = {
          corpus: document.querySelector('#methodInput').options[document.querySelector('#methodInput').selectedIndex].value,
          task_type: 'quantity_analysis',
          metadata_filter: this.metadata_filters,
          query_parameters: [
            {
              name: 'calculation_object',
              value: this.calculation_object.selected
            },
            {
              name: 'calculation_type',
              value: this.calculation_type.selected
            },
            {
              name: 'calculation_unit',
              value: this.calculation_unit.selected
            },
            {
              name: ' parts_of_speech',
              value: this.part_of_speech
            }
          ],
          response_parameters: []
        }
        console.log('task: ' + JSON.stringify(task))
        let response = await axios.post(process.env.ROOT_API + 'startTask', task, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 5000
        })
        this.task.id = response.data.id
        this.checkStatus(this.task.id, 200)
      } catch (e) {
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
          setTimeout(() => {
            this.checkStatus(taskId, timer)
          }, timer)
        } else if (this.task.status === 'ERROR') {
          this.task.finished = true
          this.task.loading = false
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getResult: async function (taskId) {
      try {
        this.task.finished = true
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        console.log('result:', response.data.result)
        this.quantity_analysis = response.data.result.rows[0]
        this.mapData(this.quantity_analysis)
        this.mapChartData(this.quantity_analysis.chart)
        this.resizeChart()
        this.show.loading = false
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    },
    mapData: function (data) {
      this.table = []
      try {
        for (let key in data) {
          if (key !== 'chart') {
            this.table.push({key: key, value: data[key]})
          }
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    mapChartData: function (chartData) {
      try {
        for (let i = 0; i <= chartData.length; i++) {
          for (var key in chartData[i]) {
            // console.log(i, key, chartData[i][key])
            this.chart.datasets[0].data.push(chartData[i][key])
            this.chart.labels.push(key)
            this.json_data.push({[this.csv_title]: key, 'quantity': chartData[i][key]})
          }
        }
        this.show.chart = true
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    resizeChart: function () {
      if (window.innerWidth > 768 && this.$refs.chartContainer) {
        this.chartHeight = Math.ceil(this.$refs.chartContainer.clientHeight * 0.8)
      } else {
        this.chartHeight = Math.ceil(470)
      }
    },
    redrawChart: function () {
      this.show.chart = false
      this.resizeChart()
      setTimeout(() => {
        this.show.chart = true
      }, 0)
    },
    export2image: function (event) {
      event.preventDefault()
      let canvas = document.getElementById('bar-chart').toDataURL('image/png')
      let link = document.createElement('a')
      link.download = this.csv_title + '_chart'
      link.href = canvas
      var mouseEvent = new MouseEvent('click')
      link.dispatchEvent(mouseEvent)
    },
    handleResize: function (event) {
      if (this.task.finished) {
        this.redrawChart()
      }
    }
  },
  created: function () {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy: function () {
    window.removeEventListener('resize', this.handleResize)
  }
}
</script>

<style scoped>
  #form_container {
    max-width: 40em;
    margin: 1vw auto;
    position: relative;
  }

  .line_bottom {
    border-width: 1px;
    border-style: solid;
    border-image:
      linear-gradient(
        to left,
        rgba(0, 0, 0, 0),
        var(--dark_silver),
        rgba(0, 0, 0, 0)
      )1 0;
  }

  .info-card {
    width: 50%;
    display: inline-block;
    float: left;
    font-size: 0.8rem;
    color: var(--grey_blue);
  }

</style>
