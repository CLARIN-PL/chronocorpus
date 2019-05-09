<template>
  <l-popup>
    <table aria-busy="false" aria-colcount="4" class="table b-table">
      <thead class="">
      <tr>
        <th scope="col" aria-colindex="1" class="">{{$t('names_map.popup.full_name')}}</th>
        <th scope="col" aria-colindex="2" class="">{{$t('names_map.popup.type')}}</th>
        <th scope="col" aria-colindex="3" class="">{{$t('names_map.popup.frequency')}}</th>
        <th scope="col" aria-colindex="3" class="">{{$t('names_map.popup.concordance')}}</th>
      </tr>
      </thead>
      <tbody class="">
      <tr class="">
        <td class="">{{input.fullName}}</td>
        <td  class="">{{input.type}}</td>
        <td  class="">{{input.frequency}}</td>
        <td  class="">
          <router-link v-for="word in input.name" :key="word.id" target="_blank" :to="{ name: 'ConcordanceTrigger', params: {concordanceWord: word}}">"{{word}}"<br></router-link>
        </td>
      </tr>
      </tbody>
    </table>
  </l-popup>
</template>

<script>
import { LPopup } from 'vue2-leaflet'

export default {
  name: 'NamesMapPopup',
  props: ['input'],
  components: { LPopup }
}
</script>

<style>
.popup-x {
  margin: 10px
}
.leaflet-popup-content {
  width: auto !important;
  height: auto !important;;
}
</style>
