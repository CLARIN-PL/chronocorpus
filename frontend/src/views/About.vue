<template>
  <div style="width: 100%; background-color: var(--cold_silver);">
    <div class="view-title">
      <h3>{{ $t('about.project.header') }}</h3>
    </div>
    <div class="view-content">
      <div id="about">
        <div class="about-fit">
          <div class="about-content">{{$t('about.project.content')}}</div>
          <div class="about-title">
            <h2>{{$t('about.tools.header')}}</h2>
          </div>
          <div class="about-content">{{$t('about.tools.content')}}</div>
          <div class="about-title">
            <h2>{{$t('about.corpus.header')}}</h2>
          </div>
          <div class="about-content">{{$t('about.corpus.content')}}</div>
          <div class="about-title">
            <h2>{{$t('about.team.header')}}</h2>
          </div>
          <div class="about-content">{{$t('about.team.content')}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'About',
  props: ['corpus']
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h2 {
    /*margin-top: 10px;*/
    color: #3d5c99;
  }
  #about {
    height: 100%;
    overflow: auto;
    text-align: justify;
    padding: 20px;
  }
  .about-title {
    margin-top: 2rem;
    height: 2rem;
    justify-content: center;
    /*border-bottom-style: solid;*/
    /*border-top-style: solid;*/
    /*border-color: var(--grey_blue);*/
    background-color: var(--violet_red);
  }
  .about-title h2 {
    height: auto;
    position: relative;
    top: 50%;
    transform: translateY(-50%);
    text-align: center;
    text-transform: uppercase;
    color: var(--warm_silver);
    font-size: 1.5rem;
  }
  .about-content {
    padding-top: 10px ;
  }
  @media only screen and (min-width: 768px) {
    .about-fit {
      width:700px;
      margin: auto;
    }
  }
</style>
