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

              <b-form-group :label="this.$t('wordprofiles.context')">
                <b-form-select class="form-input-select" selected="" required v-model="part_of_speech.selected"
                               :options="part_of_speech.options"/>
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
                <vue-json-to-csv :json-data="json_data" :csv-title="csv_title">
                  <b-button type="button"  class="btn filter-button btn-secondary" style="float: right">
                    <!--{{$t('download')}} -->
                    ⇩ CSV
                  </b-button>
                </vue-json-to-csv>
                <b-pagination :total-rows="word_profiles.length" :per-page="limit" @change="changePage" align="center"
                              v-if="word_profiles.length > 0"/>
              </div>
              <div class="content-list">
              <b-row class="justify-content-md-center" v-for="(item) in pages" :key="item.id">
                <b-col col lg="2" class="line_bottom">{{item.profile}}</b-col>
                <b-col col lg="2" class="line_bottom">{{item.frequency}}</b-col>
              </b-row>
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
import TaskFilter from '@/components/TaskFilter'
import VueJsonToCsv from 'vue-json-to-csv'

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
        filters: false
      },
      word_profiles: [],
      limit: 50,
      skip: 0,
      page: 1,
      metadata_filters: []
    }
  },
  components: {TaskFilter, axios, FadeTransition, VueJsonToCsv},
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
    }
  },
  computed: {
    pages: function () {
      try {
        let wordProfiles = this.word_profiles
        let result = []
        for (var i = this.skip - this.limit; i < this.skip; i++) {
          if (typeof wordProfiles[i] !== 'undefined') {
            result.push(wordProfiles[i])
          }
        }
        return result
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
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
      this.json_data = []
      try {
        const response = await axios.post(process.env.ROOT_API + 'startTask', {
          task_type: 'word_profile',
          metadata_filter: this.metadata_filters,
          query_parameters: [
            {
              name: 'orth',
              value: this.form.orth
            },
            {
              name: 'part_of_speech',
              value: this.part_of_speech.selected
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
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
          timeout: 5000
        })
        this.task.id = response.data.id
        console.log(response)
        this.checkStatus(this.task.id, 200)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 100
        console.log(timer)
        const response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 1000})
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
        const response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.changePage(1)
        this.skip = this.limit
        this.csv_title = 'word_profiles_' + this.form.orth + '_L' + this.form.left_window_size + '_R' + this.form.right_window_size
        this.word_profiles = response.data.result.rows
        for (var x in this.word_profiles) {
          this.json_data.push({'frequency': this.word_profiles[x].frequency, 'profile': this.word_profiles[x].profile})
        }
        this.show.loading = false
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
