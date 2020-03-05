<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <div class="carousel">
        <router-link to="/quantity_analysis" tag="div" class="view-arrow-left">
          <img src="../assets/images/arrow_left.png"/>
        </router-link>
        <router-link to="/time_series" tag="div" class="view-arrow-right">
          <img src="../assets/images/arrow_right.png"/>
        </router-link>
      </div>
      <h3>{{ $t('home.service5') }}</h3>
    </div>
    <div class="view-content">
  <div class="component-container">
    <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
      <div class="content-container">
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
        <b-button @click="startTask" class="btn-block  submit-button" type="submit" variant="primary" v-if="!show.loading">{{$t('names_map.submit')}}
        </b-button>
        <b-button class="btn-block submit-button" disabled variant="primary" v-if="show.loading">
          <b-spinner small type="grow"/><span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
        </b-button>
      </div>

    </div>
    <div class="box-container-result" v-if="task.finished">
      <div class="content-container">
        <b-button v-for="(item) in letters" :key=item v-on:click="changeLetter(item)" type="button" size="sm" :ref="'letter_' + item" :id="'letter_' + item" :class="'btn filter-button btn-secondary' + (item === 'A' ? ' active' : '')" style="margin: 3px; float: right">
          {{item}}
        </b-button>
        <l-map v-if="task.finished" style="min-width: 100%; border-radius: 10px;" :zoom="zoom" :center="center" >
          <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
          <v-marker-cluster>
            <l-marker v-for="(item) in map_data_table" v-if="item !== null" :key="item.id" :lat-lng="item.lanLon" :icon="item.icon">
              <names-map-popup :input="item"></names-map-popup>
            </l-marker>
          </v-marker-cluster>
        </l-map>
      </div>
    </div>
  </div>
    </div>
  </div>
</template><img src="../assets/images/logo.png" height="331" width="746"/>

<script>

import { LMap, LTileLayer, LMarker } from 'vue2-leaflet'
import * as L from 'leaflet'
import axios from 'axios'
import 'leaflet.markercluster'
import NamesMapPopup from '@/components/NamesMapPopup'
import TaskFilter from '@/components/TaskFilter'

// const markerHtmlStyles = `
//   background-color: #8772c3;
//   width: 2rem;
//   height: 2rem;
//   display: block;
//   left: -1.0rem;
//   top: -1.0rem;
//   position: relative;
//   border-radius: 3rem 3rem 0;
//   transform: rotate(45deg);
//   border: 2px solid #FFFFFF`

export default {
  name: 'NamesMap',
  components: { NamesMapPopup, LMap, LTileLayer, LMarker, TaskFilter },
  data () {
    return {
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      show: {
        loading: false,
        filters: true
      },
      letter: 'A',
      letters: [],
      coordinates: {
        A: [],
        Ą: [],
        B: [],
        C: [],
        Ć: [],
        D: [],
        E: [],
        Ę: [],
        F: [],
        G: [],
        H: [],
        I: [],
        J: [],
        K: [],
        L: [],
        Ł: [],
        M: [],
        N: [],
        Ń: [],
        O: [],
        Ó: [],
        P: [],
        R: [],
        S: [],
        Ś: [],
        T: [],
        U: [],
        W: [],
        Y: [],
        Z: [],
        Ź: [],
        Ż: []
      },
      zoom: 13,
      center: L.latLng(52.237049, 21.017532),
      url: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
      attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      marker: L.latLng(47.413220, -1.219482),
      circle: {
        center: [52.237049, 21.017532],
        radius: 6,
        color: 'red'
      },
      metadata_filters: []
    }
  },
  computed: {
    map_data_table () {
      try {
        let newTable = this.coordinates[this.letter]
        return newTable
      } catch (e) {
        console.log(Object.keys(e), e.message)
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
    startTask: async function () {
      this.show.loading = true
      this.coordinates = {
        A: [],
        Ą: [],
        B: [],
        C: [],
        Ć: [],
        D: [],
        E: [],
        Ę: [],
        F: [],
        G: [],
        H: [],
        I: [],
        J: [],
        K: [],
        L: [],
        Ł: [],
        M: [],
        N: [],
        Ń: [],
        O: [],
        Ó: [],
        P: [],
        R: [],
        S: [],
        Ś: [],
        T: [],
        U: [],
        W: [],
        Y: [],
        Z: [],
        Ź: [],
        Ż: []}
      let task = {
        task_type: 'geo_proper_names',
        metadata_filter: this.metadata_filters,
        query_parameters: [],
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
        let letters = new Set()
        let response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.points = response.data.result.rows
        for (let i = 0; i < this.points.length; i++) {
        // for (let i = 0; i < 2000; i++) {
          let lon = this.points[i].lon
          let lan = this.points[i].lan
          let name = this.points[i].name
          let type = this.points[i].type
          let frequency = this.points[i].frequency
          let letter = name.charAt(0).toUpperCase() + ''
          for (var key in this.coordinates) {
            if (letter === key) {
              letters.add(key)
              this.coordinates[key].push({
                lanLon: [parseFloat(lan.toPrecision(2)), parseFloat(lon.toPrecision(2))],
                fullName: name,
                name: this.splitWords(name),
                type: type,
                frequency: frequency,
                icon: L.divIcon({
                  iconAnchor: [0, 24],
                  labelAnchor: [-6, 0],
                  popupAnchor: [0, -36],
                  html: `<span style="${this.statePointIcon(frequency)}" />`
                })
              })
            }
          }
        }
        this.letters = Array.from(letters)
        this.letters.sort()
        this.letters.reverse()
        this.task.finished = true
        this.show.loading = false
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    splitWords (words) {
      let result = []
      let s = words
      s = s.replace(/(^\s*)|(\s*$)/gi, '')
      s = s.replace(/[ ]{2,}/gi, ' ')
      s = s.replace(/\n /, '\n')
      result = s.split(' ')
      return result
    },
    statePointIcon (frequency) {
      let red = 0
      let green = 255
      let blue = 0
      if (frequency >= 1020) {
        frequency = 1020
      }

      for (let i = 0; i < frequency; i++) {
        if (i < 256) {
          red++
        } else if (i > 255 && i < 511) {
          green--
        } else if (i > 510 && i < 766) {
          blue++
        } else if (i > 765) {
          red--
        }
      }
      var color = `rgb(` + red + `,` + green + `,` + blue + `)`
      return `background-color: ` + color + `;
              width: 30px;
              height: 30px;
              display: block;
              left: -15px;
              top: -15px;
              position: relative;
              border-radius: 45px 45px 0;
              transform: rotate(45deg);
              border: 2px solid #FFFFFF`
    },
    changeLetter (letter) {
      let prevButton = this.$refs['letter_' + this.letter][0]
      let nextButton = this.$refs['letter_' + letter][0]
      prevButton.classList.remove('active')
      nextButton.classList.add('active')
      this.letter = letter
    }
  }
}
</script>

<style scoped>

.filter-button.active {
  background-color: var(--violet_red) !important;
}
</style>
