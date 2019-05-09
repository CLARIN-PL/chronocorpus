<template>
<div id="copyright">
  <div style="height: 20px; font-size: 16px">
    {{$t('footer.copyright')}}
  </div>
  <div id="logo-bar">
    <img src="../assets/images/logo_UWr.svg" class="d-inline-block align-top"/>
    <img v-if="this.$i18n.locale === 'pl'" src="../assets/images/pwr_PL.png" class="d-inline-block align-top" style="padding: 18px"/>
    <img v-if="this.$i18n.locale === 'en'" src="../assets/images/pwr_EN.png" class="d-inline-block align-top" style="padding: 18px"/>
    <img src="../assets/images/clarin-pl_logo_jasnetlo_hor_pl.png" class="d-inline-block align-top" style="padding: 10px 5px"/>
    <img src="../assets/images/logo_cl.png" class="d-inline-block align-top"/>
  </div>
  <!--<img src="../assets/images/logo_clarin.png" class="d-inline-block align-top"/>-->

</div>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
#copyright {
  text-align: center;
  font-size: 1.5vh;
  color: var(--grey_blue);
  /*background-color: var(--lavender);*/
  clear: both;
  position: relative;
  height: 100px;
  /*margin-top: -100px;*/
}
#logo-bar{
  height: 80px;
  /*background-color: var(--violet_red);*/
}
#logo-bar img {
    height:100%;
    padding: 5px;
  }
@media only screen and (min-width: 768px) {
  #copyright {
    transform: translateY(100px);
  }
}
</style>
