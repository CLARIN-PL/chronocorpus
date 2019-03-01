<template>
  <div>
    <b-navbar id="navbar" type="light" variant="light">
      <b-navbar-nav>
        <router-link tag="span" :to="{name: 'Home'}">
          <b-nav-item>
            ChronoCorpus
          </b-nav-item>
        </router-link>
      </b-navbar-nav>
      <b-navbar-nav class="ml-auto">
        <router-link tag="span" :to="{name: 'Home'}">
          <b-nav-item>
            {{ $t('navbar.home') }}
          </b-nav-item>
        </router-link>
        <router-link tag="span" :to="{name: 'About'}">
          <b-nav-item>
            {{ $t('navbar.about') }}
          </b-nav-item>
        </router-link>

        <!-- Navbar dropdowns -->
        <b-nav-item-dropdown :text="this.$t('navbar.services')" right>
          <b-dropdown-item>
            <router-link tag="span" :to="{name: 'Concordance'}">
              {{ $t('navbar.concordance') }}
            </router-link>
          </b-dropdown-item>
        </b-nav-item-dropdown>
        <b-button variant="light" v-if="this.$i18n.locale === 'pl'" @click="changeLanguage('en')">
          <img src="../assets/ico/icon_pl.png" height="17" width="23">
        </b-button>
        <b-button variant="light" v-if="this.$i18n.locale === 'en'" @click="changeLanguage('pl')">
          <img src="../assets/ico/icon_en.png" height="17" width="23">
        </b-button>
      </b-navbar-nav>
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
      changeLanguage: function (event) {
        this.$i18n.locale = event.toString()
        this.$forceUpdate()
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  #navbar {
    max-height: 12vh;
  }
</style>
