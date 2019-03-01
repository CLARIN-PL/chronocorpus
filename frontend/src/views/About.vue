<template>
  <div id="about">
    <div class="about_header">
      <h2>{{$t('about.project.header')}}</h2>
    </div>
    <span>{{$t('about.project.content')}}</span>
    <h3>{{$t('about.tools.header')}}</h3>
    <span>{{$t('about.tools.content')}}</span>
    <h3>{{$t('about.corpus.header')}}</h3>
    <span>{{$t('about.corpus.content')}}</span>
    <h3>{{$t('about.team.header')}}</h3>
    <span>{{$t('about.team.content')}}</span>
  </div>
</template>

<script>
export default {
  name: 'About'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .about_header {
    border-bottom-width: 2px;
    border-bottom-style: solid;
    border-bottom-color: slateblue;
    padding-bottom: 10px;
    margin-bottom: 10px;
  }
  h2, h3 {
    margin-top: 10px;
    color: slateblue;
  }
</style>
