<template>
  <div class="navbar-container">
    <b-navbar toggleable="lg" class="wrapper">

      <!--<div class="navbar-left">-->
      <b-navbar-brand id="nav1" href="#" @click="redirectUser('/')" >
        <!--<img src="../assets/images/logo.png" class="d-inline-block align-top" style="height: 30px; margin-left: 20%;"/>-->
        <img src="../assets/images/logo2.png" class="d-inline-block align-top brand-image"/>
<!--        <b-form-group :label="this.$t('concordance.method')" label-for="methodInput">-->
      </b-navbar-brand>
      <b-form-select title="wybierz korpus z którego będą pobierane dane" class="corpora" selected="" id="methodInput" required v-model="corpus.selected" :options="corpus.options"/>

      <!--</div>-->

      <b-nav-form id="nav2" v-if="!this.$route.name.includes('Concordance')" class="navbar-form" v-on:submit.prevent="findConcordance">
        <b-form-input :title="this.$t('navbar.form_tooltip')" class="mr-sm-2 nav-concordance-submit" data-tip="This is the text of the tooltip" :placeholder="this.$t('concordance.word_i')" required
                      v-model="concordanceWord" ></b-form-input>
      </b-nav-form>

      <!--<b-button block class=""></b-button>-->

        <!--<b-navbar-toggle target="nav-collapse"></b-navbar-toggle>-->

      <b-navbar-nav id="nav4" class="ml-auto navbar-small">
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

          <b-navbar-nav  id="nav3" class="ml-auto navbar-large">
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
import {BNavbar} from 'bootstrap-vue'

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
      },
      corpus: {
        selected: 'monco',
        options:
                  [
                    {value: 'monco', text: 'corona'},
                    {value: 'chronocorpus', text: 'chronocorpus'}
                  ]
      }

    }
  },
  methods: {
    redirectUser: function (path) {
      this.$router.push(path)
    },
    findConcordance: function () {
      event.preventDefault()
      this.$router.push({name: 'ConcordanceTrigger', params: {concordanceWord: this.concordanceWord}})
      this.$router.reload()
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .wrapper {
    height: 100%;
    width: 100%;
    display: grid;
    grid-gap: 30px;
    grid-template-columns:1fr 1fr;
    grid-template-areas:
      "nav1 methodInput nav4"
  ;
  }
  #nav1 {
    grid-area: nav1;
    position: relative}
  #nav2 {
    grid-area: nav2;
    position: relative;
    display: none}
  #nav3 {
    grid-area: nav3;
    position: relative;
    display: -webkit-box;      /* OLD - iOS 6-, Safari 3.1-6 */
    display: -moz-box;         /* OLD - Firefox 19- (buggy but mostly works) */
    display: -ms-flexbox;      /* TWEENER - IE 10 */
    display: -webkit-flex;     /* NEW - Chrome */
    display: flex;             /* NEW, Spec - Opera 12.1, Firefox 20+ */
    flex-direction: row;
    display:none;
  }
  #nav4 {
    grid-area: nav4;
    position: relative
  }
  methodInput {
    grid-area: methodInput;
    position: relative;
  }
  .corpora {
    width: 80%;
  }
  .navbar-container {
    height: 66px;
  }
  .navbar-container .navbar{
    background-color: var(--cold_silver);
  }

  .brand-image {
    height: 42px;
    /*max-width: 60vw;*/
    margin-left: 30px;
  }
  .nav-concordance-submit {
    display: block;
    width: 100%;
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
  /*.nav-item {*/
    /*max-height: 40px;*/
    /*display: block;*/
    /*text-align: center;*/
    /*width: auto;*/
  /*}*/
  li.nav-item {
    /*max-height: 40px;*/
    /*display: block;*/
    /*text-align: center;*/
    /*width: auto;*/
  }
  .nav-link {
    max-height: 40px;
    text-align: center;
    /*display: block;*/
    width: auto;
    display: inline-block;
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
  @media only screen and (min-width: 1020px) {
    .wrapper {
      grid-template-columns: 1fr 1fr 1fr;
      grid-template-areas: "nav1 methodInput nav3";
    }
    #nav3 {
      grid-area: nav3;
      position: relative;
      display: -webkit-box;      /* OLD - iOS 6-, Safari 3.1-6 */
      display: -moz-box;         /* OLD - Firefox 19- (buggy but mostly works) */
      display: -ms-flexbox;      /* TWEENER - IE 10 */
      display: -webkit-flex;     /* NEW - Chrome */
      display: flex;             /* NEW, Spec - Opera 12.1, Firefox 20+ */
      flex-direction: row
    }
    #nav4 {
      display: none;
    }
  }
  @media only screen and (min-width: 1300px) {
    .wrapper {
      grid-template-columns:1fr 1fr 1fr 1fr 1fr 1fr;
      grid-template-areas:
        "nav1 methodInput nav2 nav2 nav3 nav3"
    ;
    }
    #nav2 {
      grid-area: nav2;
      position: relative;
      display: inline
    }
    #nav3 {
      grid-area: nav3;
      position: relative;
      display: -webkit-box;      /* OLD - iOS 6-, Safari 3.1-6 */
      display: -moz-box;         /* OLD - Firefox 19- (buggy but mostly works) */
      display: -ms-flexbox;      /* TWEENER - IE 10 */
      display: -webkit-flex;     /* NEW - Chrome */
      display: flex;             /* NEW, Spec - Opera 12.1, Firefox 20+ */
      /*flex-direction: row*/
    }
    .nav-concordance-submit {
        width: 100% !important;
    }
  }
</style>
