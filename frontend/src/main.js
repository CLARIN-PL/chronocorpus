// The Vue build version to load with the `import` command      'process.env.ABCD': JSON.stringify(process.env.ABCD),
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import BootstrapVue from 'bootstrap-vue'

import VueLocalStorage from 'vue-localstorage'
import { Nav, Form } from 'bootstrap-vue/es/components'
import { i18n } from '@/translations'

// import VueLayers from 'vuelayers'
// import 'vuelayers/lib/style.css'
import { Icon } from 'leaflet'
import 'leaflet/dist/leaflet.css'

import Vue2LeafletMarkerCluster from 'vue2-leaflet-markercluster'

// this part resolve an issue where the markers would not appear
delete Icon.Default.prototype._getIconUrl

Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
})

Vue.use(BootstrapVue)
Vue.use(Nav)
Vue.use(Form)
Vue.use(VueLocalStorage)
// Vue.use(VueLayers)
Vue.config.productionTip = false

Vue.component('v-marker-cluster', Vue2LeafletMarkerCluster)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  components: { App },
  template: '<App/>'
})
