<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">

      <router-link to="/names_map" tag="div" class="view-arrow-left">
        <img src="../assets/images/arrow_left.png"/>
      </router-link>
      <router-link to="/concordance" tag="div" class="view-arrow-right">
        <img src="../assets/images/arrow_right.png"/>
      </router-link>

      <h3>{{ $t('home.service6') }}</h3>
    </div>
    <div class="view-content">
      <div class="component-container">
        <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
          <div class="content-container">
            <b-form @submit="startTask">

              <b-form-group :label="this.$t('time_series.word')" label-for="timeSeriesOrth">
                <b-form-input class="form-input-default" ref="timeSeriesOrth" type="text"
                              :placeholder="this.$t('time_series.word_i')" required
                              v-model="orth" oninput="setCustomValidity('')"/>
              </b-form-group>
              <b-form-group :label="this.$t('time_series.part_of_speech')">
                <b-form-select selected="" required v-model="part_of_speech.selected"
                               :options="part_of_speech.options"/>
              </b-form-group>
              <b-form-group :label="this.$t('time_series.time_unit')">
                <b-form-select selected="" required v-model="time_unit.selected"
                               :options="time_unit.options"/>
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

              <b-button class="btn-block submit-button" type="submit" v-if="!show.loading">
                {{$t('quantity_analysis.submit')}}
              </b-button>
              <b-button class="btn-block submit-button" disabled v-if="show.loading">
                <b-spinner small type="grow"/>
                <span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
              </b-button>
            </b-form>
          </div>
        </div>
        <div class="text-center box-container-result" v-if="task.finished">
          <div class="content-container" ref="chartContainer">
            <div>
              <b-button class="btn filter-button" size="sm" @click="log = !log" style="margin: 3px; float: left"
                        :title="$t('scale.title')">
                {{$t('scale.'+log.toString())}}
              </b-button>
              <b-button v-on:click="export2image" type="button" size="sm" class="btn submit-button btn-secondary"
                        style="margin: 3px; float: right">
                {{$t('download')}} PNG
              </b-button>
              <vue-json-to-csv :json-data="json_data" :csv-title="orth" :separator="'\t'">
                <b-button type="button" size="sm" class="btn filter-button btn-secondary"
                          style="margin: 3px; float: right">
                  {{$t('download')}} TSV
                </b-button>
              </vue-json-to-csv>
              <b-button v-on:click="redrawChart()" type="button" size="sm" class="btn filter-button btn-secondary"
                        style="margin: 3px; float: right">
                ⟲
              </b-button>
            </div>
            <b-spinner v-if="show.loading" style="width: 3rem; height: 3rem;"/>
            <fade-transition>
              <line-chart :height="chartHeight" :chart-data="chart" :options="options" v-if="show.chart"
                          @on-receive="updatePoint"></line-chart>
            </fade-transition>
            <!--</b-card>-->
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
import LineChart from '@/components/LineChart'
import VueJsonToCsv from 'vue-json-to-csv'

export default {
  name: 'TimeSeries',
  components: {LineChart, TaskFilter, axios, FadeTransition, VueJsonToCsv},
  data () {
    return {
      json_data: [],
      orth: '',
      concordance_word: '',
      task: {
        id: null,
        status: '',
        result: '',
        finished: false,
        unit: 'year'
      },
      show: {
        loading: false,
        filters: false,
        chart: false
      },
      time_series: [],
      metadata_filters: [],
      log: false,
      chart: {},
      pointData: {},
      chartHeight: 470
    }
  },
  computed: {
    part_of_speech () {
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
    time_unit () {
      return {
        selected: 'year',
        options:
            [
              {value: 'month', text: this.$t('month')},
              {value: 'year', text: this.$t('year')},
              {value: 'day', text: this.$t('day')}
            ]
      }
    },
    options () {
      return {
        title: {
          display: true,
          text: this.$t('home.service6')
        },
        scales: {
          yAxes: [{
            type: this.log ? 'logarithmic' : 'linear',
            scaleLabel: {
              display: true,
              labelString: this.$t('time_series.frequency')
            },
            ticks: {
              display: true
            },
            gridLines: {
              display: true
            }
            // type: 'logarithmic'
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString: this.$t('time_series.time_unit' + this.time_unit.selected.toString())
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
    options: function () {
      this.redrawChart()
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
      event.preventDefault()
      this.concordance_word = this.orth
      this.task.finished = false
      this.show.loading = true
      this.show.chart = false
      this.json_data = []
      this.time_series = {}
      this.chart = {
        labels: [],
        datasets: [
          {
            // backgroundColor: '#b14a89',
            borderColor: '#b14a89',
            backgroundColor: 'rgba(177, 74, 137, 0.1)',
            pointBackgroundColor: '#b14a89',
            borderWidth: 1,
            data: []
          }]
      }
      try {
        let response = await axios.post(process.env.ROOT_API + 'startTask', {
          corpus: document.querySelector('#methodInput').options[document.querySelector('#methodInput').selectedIndex].value,
          task_type: 'time_series',
          metadata_filter: this.metadata_filters,
          query_parameters: [
            {
              name: 'base',
              value: this.orth
            },
            {
              name: 'part_of_speech',
              value: this.part_of_speech.selected
            },
            {
              name: 'time_unit',
              value: this.time_unit.selected
            }
          ],
          response_parameters: []
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 5000
        })
        this.task.id = response.data.id
        // console.log(response)
        this.checkStatus(this.task.id, 200)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 100
        console.log(timer)
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
        console.log(response)
        var formattedResponse = []
        response.data.result.rows.forEach((item) => {
          let rowJson = JSON.stringify(item)
          let row = JSON.parse(rowJson)
          formattedResponse.push(row)
        })
        console.log(formattedResponse)
        this.mapChartData(formattedResponse[0].series)
        this.resizeChart()
        this.show.loading = false
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    },
    mapChartData: function (chartData) {
      try {
        let years = this.getYears(chartData)
        for (let y = years[0]; y <= years[1]; y++) {
          if (this.time_unit.selected === 'year') {
            this.time_series[y] = {'value': 0, 'label': y}
          } else if (this.time_unit.selected === 'month') {
            this.time_series[y] = {
              1: {'value': 0, 'label': '1-' + y},
              2: {'value': 0, 'label': '2-' + y},
              3: {'value': 0, 'label': '3-' + y},
              4: {'value': 0, 'label': '4-' + y},
              5: {'value': 0, 'label': '5-' + y},
              6: {'value': 0, 'label': '6-' + y},
              7: {'value': 0, 'label': '7-' + y},
              8: {'value': 0, 'label': '8-' + y},
              9: {'value': 0, 'label': '9-' + y},
              10: {'value': 0, 'label': '10-' + y},
              11: {'value': 0, 'label': '11-' + y},
              12: {'value': 0, 'label': '12-' + y}
            }
          }
        }
        for (let i = 0; i <= chartData.length; i++) {
          for (var key in chartData[i]) {
            if (this.time_unit.selected === 'year') {
              this.time_series[key].value = chartData[i][key]
            } else if (this.time_unit.selected === 'month') {
              let date = key
              let splittedDate = date.split('-')
              this.time_series[splittedDate[1]][splittedDate[0]].value = chartData[i][key]
            }
          }
        }
        for (var x in this.time_series) {
          if (this.time_unit.selected === 'year') {
            this.chart.datasets[0].data.push(this.time_series[x].value)
            this.chart.labels.push(this.time_series[x].label)
            this.json_data.push({'year': x, 'frequency': this.time_series[x].value})
          } else if (this.time_unit.selected === 'month') {
            for (let i = 1; i <= 12; i++) {
              this.chart.datasets[0].data.push(this.time_series[x][i].value)
              this.chart.labels.push(this.time_series[x][i].label)
              this.json_data.push({'year': x, 'month': i, 'frequency': this.time_series[x][i].value})
            }
          }
        }
        this.show.chart = true
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getYears: function (data) {
      try {
        let years = []
        for (let i = 0; i <= data.length; i++) {
          for (var key in data[i]) {
            if (this.time_unit.selected === 'year') {
              years.push(parseInt(key))
            } else if (this.time_unit.selected === 'month') {
              let fullDate = key
              let splittedDate = fullDate.split('-')
              years.push(parseInt(splittedDate[1]))
            }
          }
        }
        return [Math.min.apply(null, years), Math.max.apply(null, years)]
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    export2image: function (event) {
      event.preventDefault()
      let canvas = document.getElementById('line-chart').toDataURL('image/png')
      let link = document.createElement('a')
      link.download = 'time_seres_chart'
      link.href = canvas
      console.log(canvas)
      console.log(link)
      var mouseEvent = new MouseEvent('click')
      link.dispatchEvent(mouseEvent)
    },
    updatePoint (data) {
      this.pointData = data
      let routeData = this.$router.resolve({
        name: 'ConcordanceTime',
        params:
            {
              concordanceWord: this.concordance_word,
              publicationYear: data.year

            }
      })
      window.open(routeData.href, '_blank')
    },
    resizeChart: function () {
      if (window.innerWidth > 768) {
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
    handleResize (event) {
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

</style>
