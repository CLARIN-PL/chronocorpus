<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <div class="carousel">
        <router-link to="/time_series" tag="div" class="view-arrow-left">
          <img src="../assets/images/arrow_left.png"/>
        </router-link>
        <router-link to="/frequency_lists" tag="div" class="view-arrow-right">
          <img src="../assets/images/arrow_right.png"/>
        </router-link>
      </div>
      <h3>
        {{ $t('home.service1') }}
      </h3>
    </div>
    <div class="view-content">
      <concordance-request
        v-if="isMounted"
        :concordance-word="concordanceWord"
        :execute-on-mount="executeOnMount"
        :predefined-filers="predefinedFilters"
      ></concordance-request>
    </div>
  </div>
</template>

<script>

import ConcordanceRequest from '@/components/ConcordanceRequest'
export default {
  name: 'Concordance',
  props: {
    concordanceWord: {
      type: String
    },
    predefinedFilters: {
      type: Array
    }
  },
  components: {ConcordanceRequest},
  data () {
    return {
      executeOnMount: false,
      isMounted: false
    }
  },
  mounted () {
    if (typeof this.concordanceWord !== 'undefined') {
      this.executeOnMount = true
    } else {
      this.executeOnMount = false
    }
    // console.log('executeOnMount con :' + this.executeOnMount)
    // console.log(this.concordanceWord)
    // console.log(this.predefinedFilters)
    this.isMounted = true
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
