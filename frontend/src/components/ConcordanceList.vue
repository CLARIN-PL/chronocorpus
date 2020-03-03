<template>
  <div class="concordance-row">
    <b-row :title="this.$t('concordance.document_info')"
           class="justify-content-md-center"
           @click="getTaskId(data.document_id)"
    >
      <b-col cols="5" class="concordance-right" >{{data.concordances[0].left}}</b-col>
      <b-col cols="2" class="concordance-center" >{{data.concordances[0].word}}</b-col>
      <b-col cols="5" class="concordance-left" >{{data.concordances[0].right}}</b-col>
    </b-row>
  </div>
</template>

<script>

import axios from 'axios'
import VueAxios from 'vue-axios'
import FadeTransition from '@/components/FadeTransition.vue'

export default {
  name: 'ConcordanceList',
  props: ['data', 'documentRequest'],
  data () {
    return {
      task: {
        id: null,
        status: '',
        result: '',
        finished: false
      },
      documents: []
    }
  },
  components: {axios, VueAxios, FadeTransition},
  methods: {
    getTaskId: async function (documentId) {
      try {
        let task = {
          corpus: 'chronopress',
          task_type: 'document',
          metadata_filter: [],
          query_parameters: [
            {
              name: 'document_id',
              value: documentId.toString()
            }
          ],
          response_parameters: []
        }
        console.log(JSON.stringify(task))
        const response = await axios.post(process.env.ROOT_API + 'startTask', task, {
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
        const response = await axios.get(process.env.ROOT_API + 'getStatus/' + taskId, {timeout: 1000})
        this.task.status = response.data.status
        console.log(this.task.status)
        if (this.task.status === 'DONE') {
          this.getResult(taskId)
        } else if (this.task.status === 'QUEUE') {
          console.log(this.task.status)
          timer += 100
          setTimeout(() => {
            this.checkStatus(taskId)
          }, timer)
        } else if (this.task.status === 'ERROR') {
          console.log(response)
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    getResult: async function (taskId) {
      try {
        const response = await axios.get(process.env.ROOT_API + 'getResult/' + taskId, {timeout: 5000})
        response.data.result.documents[0].text = this.colorizeDocumentText(response.data.result.documents[0].text)
        console.log('result: ', response.data.result)
        this.documentRequest(response)
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    },
    colorizeDocumentText (documentData) {
      let concordance = this.data.concordances[0].left + ' ' + this.data.concordances[0].word + ' ' + this.data.concordances[0].right
      let text = documentData.split(concordance)
      return [text[0], this.data.concordances[0].left, this.data.concordances[0].word, this.data.concordances[0].right, text[1]]
    }
  }
}
</script>

<style scoped>
  .concordance-row {
    border-bottom: 3px solid var(--dark_silver);
  }
  .concordance-row:hover{
    cursor: pointer;
    background-color: var(--warm_silver);
    /*box-shadow: 3 0 5px var(--violet_red) !important;*/
    border-bottom: 3px solid var(--violet_red);
    box-shadow: 0 -3px 0 0px var(--violet_red);
  }
  .concordance-right {
    text-align: right;
    /*font-size: 2vh;*/
    width: 50%;
    padding: 10px;
    color: var(--grey_blue);
  }
  .concordance-center {
    /*font-size: 2vh;*/
    margin: auto;
    color: var(--violet_red);
  }
  .concordance-left {
    text-align: left;
    /*font-size: 2vh;*/
    width: 50%;
    padding: 10px;
    color: var(--grey_blue);
  }
</style>
