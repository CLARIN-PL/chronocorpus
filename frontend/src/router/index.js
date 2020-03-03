import Vue from 'vue'

import Router from 'vue-router'
import Home from '@/views/Home'
import Concordance from '@/views/Concordance'
import About from '@/views/About'
import FrequencyLists from '@/views/FrequencyLists'
import Documents from '@/views/Documents'
import WordProfiles from '@/views/WordProfiles'
import QuantityAnalysis from '@/views/QuantityAnalysis'
import NamesMap from '@/views/NamesMap'
import TimeSeries from '@/views/TimeSeries'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
      props: true
    },
    {
      path: '/concordance/',
      name: 'Concordance',
      component: Concordance,
      props: true
    },
    {
      path: '/concordance/:concordanceWord',
      name: 'ConcordanceTrigger',
      component: Concordance,
      props: true
    },
    {
      path: '/concordance/:concordanceWord/:publicationYear',
      name: 'ConcordanceTime',
      component: Concordance,
      props: true
    },
    {
      path: '/about',
      name: 'About',
      component: About,
      props: true
    },
    {
      path: '/frequency_lists',
      name: 'FrequencyLists',
      component: FrequencyLists,
      props: true
    },
    {
      path: '/documents',
      name: 'Documents',
      component: Documents,
      props: true
    },
    {
      path: '/word_profiles',
      name: 'WordProfiles',
      component: WordProfiles,
      props: true
    },
    {
      path: '/quantity_analysis',
      name: 'QuantityAnalysis',
      component: QuantityAnalysis,
      props: true
    },
    {
      path: '/names_map',
      name: 'NamesMap',
      component: NamesMap,
      props: true
    },
    {
      path: '/time_series',
      name: 'TimeSeries',
      component: TimeSeries,
      props: true
    },
    {path: '*', redirect: '/'}
  ]
})
