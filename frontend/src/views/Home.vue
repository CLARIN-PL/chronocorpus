<template>
  <div style="width: 100%;">
    <div class="home-title">
      <h3>{{ $t('home.title') }}</h3>
    </div>
    <div class="home-content">

      <b-container style="width: 100%; height:100%" class="wrapper">

        <div id="card1">
          <div class="home-card home-card-margin">
            <router-link :to="{name: 'Concordance'}">
              <div class="home-card-row"><img src="../assets/images/concordance_image.png" class="home-card-image"/>
              </div>
              <div class="home-card-title"><h4>{{$t('home.service1')}}</h4></div>
            </router-link>
          </div>
        </div>
        <div id="card2">
          <div class="home-card home-card-margin">
            <router-link :to="{name: 'FrequencyLists'}">
              <div class="home-card-row"><img src="../assets/images/counter_image.png" class="home-card-image"/></div>
              <div class="home-card-title"><h4>{{$t('home.service2')}}</h4></div>
            </router-link>
          </div>
        </div>
        <div id="card3">
          <div class="home-card">
            <router-link :to="{name: 'WordProfiles'}">
              <div class="home-card-row"><img src="../assets/images/profile_image.png" class="home-card-image"/></div>
              <div class="home-card-title"><h4>{{$t('home.service3')}}</h4></div>
            </router-link>
          </div>
        </div>
        <div id="card4">
          <div class="home-card home-card-margin">
            <router-link :to="{name: 'QuantityAnalysis'}">
              <div class="home-card-row"><img src="../assets/images/analysis_image.png" class="home-card-image"/>
              </div>
              <div class="home-card-title"><h4>{{$t('home.service4')}}</h4></div>
            </router-link>
          </div>
        </div>
        <div id="card5">
          <div class="home-card home-card-margin">
            <router-link :to="{name: 'NamesMap'}" class="">
              <div class="home-card-row"><img src="../assets/images/geonames_image.png" class="home-card-image"/>
              </div>
              <div class="home-card-title"><h4>{{$t('home.service5')}}</h4></div>
            </router-link>
          </div>
        </div>
        <div id="card6">
          <div class="home-card">
            <router-link :to="{name: 'TimeSeries'}">
              <div class="home-card-row"><img src="../assets/images/time_image.png" class="home-card-image"/></div>
              <div class="home-card-title"><h4>{{$t('home.service6')}}</h4></div>
            </router-link>
          </div>
        </div>

      </b-container>

    </div>
  </div>
</template>

<script>

export default {
  name: 'Home'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  #copyright {
    background-color: var(--cold_silver) !important;
  }
  .home-title {
    justify-content: center;
    height: 66px;
    border-bottom-style: solid;
    border-top-style: solid;
    border-color: var(--dark_silver);
    border-width: 3px;
    background-color: var(--cold_silver);
  }
  .home-title h3 {
    text-align: center;
    text-transform: uppercase;
    color: var(--grey_blue);
    height: 66px;
    font-size: 18px;
    padding-top: 20px;
  }
  .home-content {
    /*background: url('../assets/images/pattern.png') 0% 20% repeat;*/
    padding: 20px;
    min-height: 100%;
  }
  .home-card{
    width: 100%;
    height: 30vh;
    margin: 2vh auto;
    background-color: var(--warm_silver);
    border-style: solid;
    border-color: var(--dark_silver);
    border-radius: 1vh;
  }
  .home-card:after {
    content: "";
    display: block;
    /*padding-bottom: 100%;*/
  }
  .home-card-row{
    width: 100%;
    height: 60%;
    text-align: center;
  }
  .home-card-title{
    width: 100%;
    height: 40%;
    text-align: center;
  }
  .home-card h4 {
    width: auto;
    position: relative;
    top: 50%;
    transform: translateY(-50%);
    text-align: center;
    text-transform: uppercase;
    color: var(--grey_blue);
    font-size: 2vh;
  }
  .home-card-image {
    margin-top:1%;
    height: 70%;
    width: 50%;
    position: relative;
    top: 50%;
    transform: translateY(-50%);
    text-align: center;
    text-transform: uppercase;
    color: var(--grey_blue);
    object-fit: contain;
  }
  .home-cards-content {
    /*min-width: 90vw;*/
    margin: 0 1vw;
    justify-content: center;
    display: flex;                  /* establish flex container */
    flex-direction: row;            /* default value; can be omitted */
    flex-wrap: nowrap;              /* default value; can be omitted */
    justify-content: space-between; /* switched from default (flex-start, see below) */
  }
  .home-card:hover {
    /*transform: translateX(-5px);*/
    transition: all 0.3s ease 0s;
    border-color: var(--violet_red);
    background-color: var(--warm_silver);
  }
  .home-card:hover a{
    text-decoration: inherit !important;
  }

  #card1 {grid-area: card1; position: relative}
  #card2 {grid-area: card2; position: relative}
  #card3 {grid-area: card3; position: relative}
  #card4 {grid-area: card4; position: relative}
  #card5 {grid-area: card5; position: relative}
  #card6 {grid-area: card6; position: relative}
  .wrapper {
    height: 100%;
    display: grid;
    /*grid-gap: 30px;*/
    grid-template-areas:
      "card1"
      "card2"
      "card3"
      "card4"
      "card5"
      "card6"
  ;
  }

  @media only screen and (min-width: 520px) {
    .home-title h3 {
      font-size: 36px;
      padding-top: 9px;
    }
  }
  @media only screen and (min-width: 768px) {
    .wrapper {
      height: 100%;
      display: grid;
      /*grid-gap: 30px;*/
      grid-template-areas:
        "card1 card2 card3"
        "card4 card5 card6"
    ;
    }
    .home-content {
      padding: inherit;
      min-height: inherit;
      height: calc(100% - 66px) !important;
      overflow: hidden;
      border-color: var(--dark_silver);
    }
      .home-card-margin {
        /*margin-right: 5%;*/
      }
    .home-card{
      display: block;
      height: calc(25vmin - 27px);
      width: calc(25vmin - 27px);
      /*max-height: 50%;*/
      /*background: #ca0164;*/
      margin: 0;
      background-color: var(--cold_silver);
      border-style: solid;
      border-color: var(--dark_silver);
      border-radius: 1vh;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%)
    }
    .home-card:hover {
      transition: all 0.3s ease 0s;
      border-color: var(--violet_red);
      background-color: var(--warm_silver);
      /*top: 50%;*/
      /*left: 50%;*/
      transform: translate(-50%, Calc(-50% - 5px))
    }
    .home-card-image {
      margin-top: 5%;
      /*height:70%*/
    }
    li a {
      text-decoration: none;
    }
    .container{
      max-width: 70%;
    }
    .container {
      width: 100%;
    }

  }
  /*@media only screen and (min-width: 1100px) {*/
    /*.home-card {*/
      /*display: block;*/
      /*height: calc(25vmin);*/
      /*width: calc(25vmin);*/
    /*}*/
  /*}*/
</style>
