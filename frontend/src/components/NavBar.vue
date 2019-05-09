<template>
  <div class="navbar-container">
    <b-navbar toggleable="lg">
      <div class="navbar-left">
      <b-navbar-brand href="#" @click="redirectUser('/')" >
        <!--<img src="../assets/images/logo.png" class="d-inline-block align-top" style="height: 30px; margin-left: 20%;"/>-->
        <img src="../assets/images/logo2.png" class="d-inline-block align-top brand-image"/>
      </b-navbar-brand>

      </div>

      <!--<b-nav-form class="navbar-form" v-on:submit.prevent="findConcordance">-->
        <!--<b-form-input :title="this.$t('navbar.form_tooltip')" class="mr-sm-2 nav-concordance-submit" data-tip="This is the text of the tooltip" :placeholder="this.$t('concordance.word_i')" required-->
                      <!--v-model="concordanceWord" ></b-form-input>-->
        <!--<b-button @submit="findConcordance" class="my-2 my-sm-0 nav-concordance-submit" type="submit" variant="primary">-->
        <!--{{$t('concordance.submit')}}-->
        <!--</b-button>-->
      <!--</b-nav-form>-->
      <!--<b-button block class=""></b-button>-->

        <!--<b-navbar-toggle target="nav-collapse"></b-navbar-toggle>-->

      <b-navbar-nav class="ml-auto navbar-small">
        <b-nav-form class="dropdown-button" v-if="this.$i18n.locale === 'pl'" @click.prevent="changeLanguage('en')">
          <b-button class="lang-button">
            <img src="../assets/ico/icon_pl.png" height="70%" width="100%">
          </b-button>
        </b-nav-form>
        <b-nav-form class="dropdown-button" v-if="this.$i18n.locale === 'en'" @click.prevent="changeLanguage('pl')">
          <b-button class="lang-button">
            <img src="../assets/ico/icon_en.png" height="70%" width="100%">
          </b-button>
        </b-nav-form>
      </b-navbar-nav>
        <!--<b-collapse id="nav-collapse" is-nav class="navbar-left">-->

          <b-navbar-nav class="ml-auto navbar-large">
            <b-nav-item @click="redirectUser('/')">
              {{ $t('navbar.home') }}
            </b-nav-item>
            <b-nav-item @click="redirectUser('/about')">
              {{ $t('navbar.about') }}
            </b-nav-item>

            <!-- Navbar dropdowns -->
            <b-nav-item-dropdown :text="this.$t('navbar.services')" right>
              <b-dropdown-item @click="redirectUser('/concordance')">
                <img src="../assets/images/concordance_image.png" class="navbar-icon"/>
                {{ $t('home.service1') }}
              </b-dropdown-item>
              <b-dropdown-item @click="redirectUser('/frequency_lists')">
                <img src="../assets/images/counter_image.png" class="navbar-icon"/>
                {{ $t('home.service2') }}
              </b-dropdown-item>
              <b-dropdown-item @click="redirectUser('/word_profiles')">
                <img src="../assets/images/profile_image.png" class="navbar-icon"/>
                {{ $t('home.service3') }}
              </b-dropdown-item>
              <b-dropdown-item @click="redirectUser('/quantity_analysis')">
                <img src="../assets/images/analysis_image.png" class="navbar-icon"/>
                {{ $t('home.service4') }}
              </b-dropdown-item>
              <b-dropdown-item @click="redirectUser('/names_map')">
                <img src="../assets/images/geonames_image.png" class="navbar-icon"/>
                {{ $t('home.service5') }}
              </b-dropdown-item>
              <b-dropdown-item @click="redirectUser('/time_series')">
                <img src="../assets/images/time_image.png" class="navbar-icon"/>
                {{ $t('home.service6') }}
              </b-dropdown-item>
            </b-nav-item-dropdown>
            <b-nav-form class="dropdown-button" v-if="this.$i18n.locale === 'pl'" @click.prevent="changeLanguage('en')">
              <b-button class="lang-button" >
                <img src="../assets/ico/icon_pl.png" height="70%" width="100%">
              </b-button>
            </b-nav-form>
            <b-nav-form class="dropdown-button" v-if="this.$i18n.locale === 'en'" @click.prevent="changeLanguage('pl')">
              <b-button class="lang-button">
                <img src="../assets/ico/icon_en.png" height="70%" width="100%">
              </b-button>
            </b-nav-form>
          </b-navbar-nav>
        <!--</b-collapse>-->
    </b-navbar>
  </div>
</template>

<script>
import BNavbar from 'bootstrap-vue/es/components/navbar/navbar'

export default {
  name: 'NavBar',
  components: {
    BNavbar
  },
  data () {
    return {
      langs: ['pl', 'en'],
      concordanceWord: undefined,
      changeLanguage: function (event) {
        this.$i18n.locale = event.toString()
        this.$forceUpdate()
      }
    }
  },
  methods: {
    redirectUser: function (path) {
      this.$router.push(path)
    },
    findConcordance: function () {
      // event.preventDefault()
      this.$router.push({name: 'ConcordanceTrigger', params: {concordanceWord: this.concordanceWord}})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .navbar-container {
    height: 66px;
    /*background-image: url('../assets/images/pattern.png') !important;*/
    /*background-color: var(--lavender);*/
  }
  .navbar-container .navbar{
    /*background: url('../assets/images/pattern.png') 10% !important;*/
    background-color: var(--cold_silver);
  }
  .navbar-brand {
    /*width: 400px;*/
    max-width: 50vw;
  }
  .brand-image {
    height: 40px;
    /*height: 40px;*/
    /*width: 300px;*/
    max-width: 60vw;
    margin-left: 30px;
  }
  .nav-concordance-submit {
    display: block;
    width: 100%;
    /*position: absolute;*/
    /*margin-right: 400px;*/
    /*margin-left: 400px;*/
    /*visibility: hidden;*/
  }
  .lang-button, .lang-button:active, .lang-button:focus {
    background-color: transparent !important;
    outline: 0;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
    border: none;
  }
  .lang-button:focus:active, .lang-button:hover{
    background-color: transparent !important;
  }
  .dropdown-item:active {
    background-color: var(--dark_silver);
  }
  .dropdown-item:hover {
    background-color: var(--dark_silver);
    color: var(--violet_red);
  }
  .navbar-icon {
    width: 20px;
  }
  .nav-item {
    max-height: 40px;
    display: block;
    text-align: center;
    width: auto;

  }
  .nav-link {
    max-height: 40px;
    text-align: center;
    display: block;
    width: auto;
  }
  .navbar-sufix {
    width: 450px;
  }
  .navbar-large{
    display: none;
  }
  .navbar-small{
    float: right;
  }
  @media only screen and (min-width: 1100px) {
    .navbar-large{
      display: inherit;
    }
    .navbar-small{
      display: none;
    }
  }
  /*@media only screen and (min-width: 768px) {*/
    /*.navbar-container {*/
      /*background-image: url('../assets/images/pattern.png') !important;*/
      /*!*padding: 10px;*!*/
    /*}*/
    /*@media only screen and (min-width: 1100px) {*/
        /*.navbar-container {*/
          /*!*height: 10vh;*!*/
          /*background-image: url('../assets/images/pattern.png') !important;*/
          /*!*padding: 10px;*!*/
        /*}*/
    /*}*/
  /*}*/
</style>
