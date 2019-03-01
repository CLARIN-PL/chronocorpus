import Vue from 'vue'

import Router from 'vue-router'
import Home from '@/views/Home'
import Concordance from '@/views/Concordance'
import About from '@/views/About'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/concordance',
      name: 'Concordance',
      component: Concordance
    },
    {
      path: '/about',
      name: 'About',
      component: About
    }
  ]
})
