<template>
  <div id="home">
    <concordance-request></concordance-request>
    <b-container id="services">
      <b-row>
        <b-col>
          <router-link :to="{name: 'Concordance'}">
          <b-button class="home_option" variant="outline-primary">{{$t("home.service1")}}</b-button>
          </router-link>
        </b-col>
        <b-col>
          <b-button disabled class="home_option" variant="outline-primary">{{$t("home.service2")}}</b-button>
        </b-col>
        <b-col>
          <b-button disabled class="home_option" variant="outline-primary">{{$t("home.service2")}}</b-button>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <b-button disabled class="home_option" variant="outline-primary">{{$t("home.service2")}}</b-button>
        </b-col>
        <b-col>
          <b-button disabled class="home_option" variant="outline-primary">{{$t("home.service3")}}</b-button>
        </b-col>
        <b-col>
          <b-button disabled class="home_option" variant="outline-primary">{{$t("home.service3")}}</b-button>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ConcordanceRequest from '@/components/ConcordanceRequest'
export default {
  name: 'Home',
  components: { ConcordanceRequest }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  #services{
    max-width: 40em;
    margin: 24px auto;
    position: relative;
  }
  .home_option{
    width: 10em;
    height: 10em;
    margin-bottom: 1vw;
    text-align: center;
  }
</style>
