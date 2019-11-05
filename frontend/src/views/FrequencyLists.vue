<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <div class="carousel">
        <router-link to="/concordance" tag="div" class="view-arrow-left">
          <img src="../assets/images/arrow_left.png"/>
        </router-link>
        <router-link to="/word_profiles" tag="div" class="view-arrow-right">
          <img src="../assets/images/arrow_right.png"/>
        </router-link>
      </div>
      <h3>{{ $t('home.service2') }} </h3>
    </div>

    <div class="view-content">
      <div class="component-container">
        <div v-bind:class="{ 'box-container-form': task.finished, 'box-container': !task.finished }">
          <div class="content-container">
            <b-form @submit="getTaskId">
              <b-form-group>
                <b-form-select class="form-input-select" selected="" id="countByBase" required v-model="count_by_base.selected"
                               :options="count_by_base.options"/>
              </b-form-group>

              <b-form-group :label="this.$t('concordance.howmany')">
                <b-input-group :prepend="limit.toString()" class="mt-3">
                  <b-form-input type="range" min="10" max="100" step="5" v-model="limit" @change="changeLimit"/>
                </b-input-group>
              </b-form-group>

              <b-button class="btn-block submit-button" type="submit" v-if="!show.loading">
                {{$t('frequency.submit')}}
              </b-button>
              <b-button class="btn-block submit-button" disabled v-if="show.loading">
                <b-spinner small type="grow"/>
                <span v-if="task.finished">{{$t('task.loading_data')}}</span><span v-if="!task.finished">{{$t('task.waiting_for_response')}}</span>
              </b-button>
            </b-form>
          </div>
        </div>
        <div class="box-container-result" v-if="task.finished">
          <div class="content-container">
            <b-card class="bv-example-row">
              <div class="content-pagination" v-if="frequency_list.length > 0">
                <vue-json-to-csv :json-data="json_data" :csv-title="csv_title">
                  <b-button type="button"  class="btn filter-button btn-secondary" style="float: right">
                    {{$t('download_csv')}}
                  </b-button>
                </vue-json-to-csv>
                <b-pagination :total-rows="frequency_list.length" :per-page="limit" @change="changePage" align="center"
                              v-if="frequency_list.length > 0"/>
              </div>
              <div class="content-list">
                <b-spinner v-if="show.loading" style="width: 3rem; height: 3rem;"/>
                <!--<b-row class="line_bottom justify-content-md-center" v-for="(item) in pages" :key="item.id">-->
                  <!--<b-col cols="7" style="text-align: left;  font-size: 12px">{{item.word}}</b-col>-->
                  <!--<b-col cols="3" style="text-align: right; font-size: 12px">{{partOfSpeech(item.part_of_speech)}}</b-col>-->
                  <!--&lt;!&ndash;<b-col cols="3" style="text-align: right; font-size: 12px">{{item.part_of_speech}}</b-col>&ndash;&gt;-->
                  <!--<b-col cols="2" style="margin: auto">{{item.count}}</b-col>-->
                <!--</b-row>-->

                <b-table v-if="task.finished" small striped hover fixed :items="frequency_list" :current-page="page" :per-page="limit" :filter="table_filter"  @filtered="onFiltered"></b-table>
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
        finished: false
      },
      show: {
        loading: false
      },
      frequency_list: [],
      limit: 50,
      skip: 0,
      page: 1
    }
  },
  components: {axios, FadeTransition, VueJsonToCsv},
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
        selected: 'false',
        options: [
          {value: 'true', text: this.$t('frequency.true')},
          {value: 'false', text: this.$t('frequency.false')}]
      }
    },
    pages: function () {
      try {
        let frequencyList = this.frequency_list
        let result = []
        for (var i = this.skip - this.limit; i < this.skip; i++) {
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
    getTaskId: async function (event) {
      event.preventDefault()
      this.task.finished = false
      this.show.loading = true
      this.frequency_list = []
      this.json_data = []
      try {
        const response = await axios.post(process.env.ROOT_API + 'startTask', {
          task_type: 'frequency',
          metadata_filter: [],
          query_parameters: [
            {
              name: 'count_by_base',
              value: this.count_by_base.selected
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
        this.checkStatus(this.task.id, 1000)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId, timer) {
      try {
        timer += 200
        console.log(timer)
        const response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 10000})
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
        const response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        this.changePage(1)
        this.skip = this.limit
        this.frequency_list = response.data.result.rows
        // for (let i = 0; i < 100; i++) {
        //   this.frequency_list[i] = response.data.result.rows[i]
        // }
        console.log(response)
        this.show.loading = false
        for (var x in this.frequency_list) {
          this.json_data.push({'word': this.frequency_list[x].word, 'count': this.frequency_list[x].count, 'part_of_speech': this.frequency_list[x].part_of_speech})
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
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    changeLimit: function (value) {
      try {
        this.limit = value
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
