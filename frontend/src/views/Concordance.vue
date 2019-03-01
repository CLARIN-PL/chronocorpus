<template>
  <div>
    <b-card>
    <concordance-request extended-form="true"></concordance-request>
    </b-card>
  </div>
</template>

<script>

import ConcordanceRequest from '@/components/ConcordanceRequest'
export default {
  name: 'Concordance',
  components: {ConcordanceRequest}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
