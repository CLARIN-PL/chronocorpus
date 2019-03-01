<template>
  <div>
    <div id="form_container">
      <b-form class="concordance_form" @submit="getTaskId" v-if="!extendedForm">
        <b-input-group :prepend="this.$t('concordance.word')" class="mt-3">
          <b-form-input type="text" :placeholder="this.$t('concordance.word_i')" required
                        v-model="form.word"/>
          <b-input-group-append>
            <b-button type="submit" variant="primary" v-if="!show.loading">{{$t('concordance.submit')}}</b-button>
            <b-button disabled variant="primary" v-if="show.loading">
              <b-spinner small type="grow"/>
              {{$t('concordance.loading')}}
            </b-button>
          </b-input-group-append>
        </b-input-group>
      </b-form>
      <b-form class="concordance_form" @submit="getTaskId" v-if="extendedForm">
        <b-form-group :label="this.$t('concordance.word')" label-for="extendedWordInput">
          <b-form-input ref="extendedWordInput" type="text" :placeholder="this.$t('concordance.word_i')" required
                        v-model="form.word"  oninput="setCustomValidity('')"/>
        </b-form-group>

        <b-form-group :label="this.$t('concordance.corpus')" label-for="corpusInput">
          <b-form-select id="corpusInput" required v-model="form.corpus.selected" :options="form.corpus.options"/>
        </b-form-group>

        <b-form-group :label="this.$t('concordance.method')" label-for="methodInput">
          <b-form-select selected="" id="methodInput" required v-model="form.method.selected"
                         :options="form.method.options"/>
        </b-form-group>

        <b-form-group :label="this.$t('concordance.howmany')" label-for="methodInput">
          <b-input-group :prepend="limit.toString()" class="mt-3">
            <b-form-input type="range" min="10" max="100" v-model="limit"/>
          </b-input-group>
        </b-form-group>

        <b-button class="btn-block" type="submit" variant="primary" v-if="!show.loading">{{$t('concordance.submit')}}
        </b-button>
        <b-button class="btn-block" disabled variant="primary" v-if="show.loading">
          <b-spinner small type="grow"/>
        </b-button>
      </b-form>
    </div>
    <div class="text-center" style="width: 100%">

        <b-card class="bv-example-row" v-if="task.finished">
          Found {{concordances.length}} concordances
          <fade-transition mode="out-in">
            <b-pagination :total-rows="concordances.length" :per-page="limit" @change="changePage" align="center"
                          v-if="concordances.length > 0"/>
          </fade-transition>
          <b-row class="line_bottom justify-content-md-center" v-for="(item) in pages" :key="item.id">
            <b-col cols="5" style="text-align: right; font-size: 12px">{{item.left}}</b-col>
            <b-col cols="2" style="margin: auto">{{item.word}}</b-col>
            <b-col cols="5" style="text-align: left;  font-size: 12px">{{item.right}}</b-col>
          </b-row>
        </b-card>
      <fade-transition mode="out-in">
        <b-pagination :total-rows="concordances.length" :per-page="limit" @change="changePage" align="center"
                      v-if="concordances.length > 0"/>
      </fade-transition>
    </div>
  </div>
</template>

<script>

import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'

export default {
  name: 'ConcordanceRequest',
  props: {
    extendedForm: false
  },
  data () {
    return {
      form: {
        word: '',
        corpus: {
          selected: 'chronopress',
          options: [{value: 'chronopress', text: 'chronopress'}]
        },
        method: {
          selected: 'base',
          options: [
            {value: 'base', text: this.$t('concordance.base')},
            {value: 'orth', text: this.$t('concordance.orth')}]
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
        form: true
      },
      concordances: [],
      limit: 50,
      skip: 0
    }
  },
  components: {axios, VueAxios, FadeTransition},
  computed: {
    pages () {
      axios.interceptors.request.use(request => {
        console.log('Starting Request', request)
        return request
      })
      return this.concordances.slice(this.skip, this.skip + this.limit)
    }
  },
  methods: {
    getTaskId: async function () {
      this.show.loading = true
      this.show.form = false
      try {
        const response = await axios.post('http://10.17.55.2:8080/startTask', {
          corpus: this.form.corpus.selected,
          task_type: 'concordance',
          metadata_filter: [],
          params: [
            {
              name: this.form.method.selected,
              value: this.form.word
            }
          ]
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Referer': 'http://10.17.55.2:8080'
          }
        })
        this.task.id = response.data.id
        this.checkStatus(this.task.id)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    checkStatus: async function (taskId) {
      try {
        const response = await axios.get('http://10.17.55.2:8080/getStatus/' + taskId)
        this.task.status = response.data.status
        console.log('task status => ' + this.task.status)
        if (this.task.status === 'DONE') {
          this.getConcordances(taskId)
        } else if (this.task.status === 'QUEUE') {
          console.log(this.task.status)
          // setTimeout(() => {
          //   this.checkStatus(taskId)
          // }, 300)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getConcordances: async function (taskId) {
      try {
        const response = await axios.get('http://10.17.55.2:8080/getResult/' + taskId)
        this.show.loading = false
        console.log('hide loading gif')
        this.task.finished = true
        console.log('show result container')
        this.concordances = response.data.result.rows
        console.log('insert data to container')
      } catch (e) {
        console.log(Object.keys(e), e.message)
        this.show.loading = false
      }
    },
    changePage: function (page) {
      this.skip = this.limit * page
    }
  }
  // mounted: function () {
  //   if (this.extendedForm) {
  //     this.$refs['extendedWordInput'].setCustomValidity(this.$t('concordance.warning'))
  //   } else {
  //     this.$refs['WordInput'].setCustomValidity(this.$t('concordance.warning'))
  //   }
  // }
}
</script>

<style scoped>
  #form_container {
    max-width: 40em;
    margin: 1vw auto;
    position: relative;
  }

  .line_bottom {
    border-bottom: 1px solid #c3c3c3;
  }
</style>
