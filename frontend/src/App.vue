<template>
  <div id="app">
    <NavBar/>
    <div id="view-container">
      <fade-transition mode="out-in">
      <router-view/>
      </fade-transition>
    </div>
    <Footer/>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'
import FadeTransition from '@/components/FadeTransition'
import Footer from '@/components/Footer'

export default {
  name: 'App',
  components: {
    NavBar,
    Footer,
    FadeTransition
  },
  created () {
    document.title = 'Chronocorpus'
  }
}
</script>

<style>
  #view-container {
    padding: 3vw;
    mix-height:100vh;
    position:relative;
  }
</style>
