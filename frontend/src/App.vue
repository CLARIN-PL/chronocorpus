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
    document.title = 'ChronoCorpus'
  },
  mounted () {
  }
}
</script>

<style>
  :root {
    --cold_silver: #f3f5fc;
    --warm_silver: #f8f6fd;
    --dark_silver: #c0bcd5;
    --violet_red: #b14a89;
    --lavender: #8772c3;
    --grey_blue: #506487;
  }

  html {
    height: 100%;
  }

  body {
    background-image: url('assets/images/pattern.png');
    height: 100%;
    margin: 0;
    padding: 0;
  }

  #app {
    height: 100%;
    /*margin-bottom: -100px;*/
  }

  #view-container {
    /*height: 100%;*/
    display: flex;
    display: -webkit-flex; /* Safari */
    -webkit-flex-direction: row-reverse; /* Safari 6.1+ */
    flex-direction: row;
    justify-content: center;
    overflow: visible;
    border-bottom-style: solid;
    border-color: var(--dark_silver);
  }

  .component-container {
    padding: 10px;
    height: 100%;
    width: 100%;
    text-align: center;
  }

  .box-container {
    width: 100%;
    margin: auto;
    display: flex;
    padding: 1vw;

  }

  .box-container-form {
    width: 100%;
    float: left;
    margin: auto;
    display: flex;
  }

  .box-container-result {
    width: 100%;
    float: left;
    margin: auto;
    display: flex;
  }

  .push {
    height: 50px
  }

  .content-container {
    height: 100%;
    width: 100%;
    padding: 1vw;
    right: 0;
    left: 0;
    justify-content: center;
    /*margin-bottom: 10%;*/
  }

  .carousel {
    height: 66px;
    width: 100%;
    position: absolute;
    z-index: +1;
    display: inline;
    margin-top: -3px;
  }

  .view-title {
    height: 66px;
    background: url('assets/images/pattern.png');
    border-bottom-style: solid;
    border-top-style: solid;
    border-color: var(--dark_silver);
    border-width: 3px;
    width: 100%;
  }

  .view-content {
    height: 100%;
    margin-top: -66px;
    padding-top: 66px;
  }

  .view-title h3 {
    /*position: relative;*/
    /*top: 50%;*/
    /*transform: translateY(-50%);*/
    text-align: center;
    text-transform: uppercase;
    color: var(--grey_blue);
    height: 66px;
    font-size: 18px;
    padding-top: 20px;
  }

  .view-title a:hover {
    color: var(--lavender);
    transition: all 0.3s ease 0s;
  }

  label, legend {
    color: var(--grey_blue);
    text-transform: uppercase;
  }

  :focus {
    outline: none !important;
  }

  .submit-button, .submit-button:focus {
    background-color: var(--violet_red) !important;
    outline: 0;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
    border: none;
    text-transform: uppercase;
  }

  .submit-button:focus:active, .submit-button:active:focus, .submit-button:focus.active, .submit-button:active.focus, .submit-button:hover {
    background-color: #b62b80 !important;
  }

  .submit-button:disabled {
    background-color: var(--violet_red) !important;
  }

  .submit-button:active {
    background-color: #974171 !important;
    box-shadow: 0 0 5px var(--violet_red) !important;
    border: 1px solid var(--violet_red) !important;
  }

  .filter-button, .filter-button:focus {
    background-color: var(--grey_blue) !important;
    outline: 0;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
    border: none;
    text-transform: uppercase;
  }

  .filter-button:focus:active, .filter-button:active:focus, .filter-button:focus.active, .filter-button:active.focus, .filter-button:hover {
    background-color: #3b5787 !important;
  }

  .filter-button:disabled {
    background-color: var(--grey_blue) !important;
  }

  .filter-button:active {
    background-color: #445674 !important;
    box-shadow: 0 0 5px var(--violet_red) !important;
    border: 1px solid var(--violet_red) !important;
  }

  .trigger-button, .trigger-button:focus {
    width: 100%;
    background-color: var(--warm_silver) !important;
    outline: 0;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
    border: 2px solid var(--grey_blue) !important;
    text-transform: uppercase;
    color: var(--grey_blue) !important;
    text-decoration: line-through;
    margin: 2px;
  }

  .trigger-button:hover {
    background-color: var(--grey_blue) !important;
    color: var(--warm_silver) !important;
    text-decoration: line-through;
  }

  .trigger-button.active:hover {
    background-color: var(--grey_blue) !important;
    color: var(--warm_silver) !important;
    text-decoration: inherit;
  }

  .trigger-button:disabled {
    background-color: var(--grey_blue) !important;
    /*color: var(--warm_silver) !important;*/
    text-decoration: inherit;
    font-weight: 900 !important;
  }

  .trigger-button.active {
    background-color: var(--lavender) !important;
    color: var(--warm_silver) !important;
    text-decoration: inherit;
  }

  .trigger-button.active:focus {
    background-color: var(--lavender) !important;
    color: var(--warm_silver) !important;
    text-decoration: inherit;
  }

  input[type=text].form-input-default:focus, textarea:focus, select:focus, input[type=range]:focus {
    box-shadow: 0 0 5px var(--violet_red) !important;
    border: 1px solid var(--violet_red) !important;
  }

  .form-input-default {
    /*height: 30px;*/
  }

  .input-group-prepend::before {
    background-color: var(--violet_red);
  }

  .nav-link {
    text-align: center;
    text-transform: uppercase;
  }

  .dropdown-item {
    text-align: center;
  }

  .dropdown-button {
    justify-content: center;
  }

  .btn-none {
    visibility: hidden;
  }

  .modal-header {
    background-color: var(--grey_blue);
    text-transform: uppercase;
    color: var(--warm_silver)
  }

  li.page-item a {
    background-color: var(--warm_silver);
    color: var(--grey_blue);
  }

  li.page-item:hover a {
    background-color: var(--warm_silver);
    color: var(--violet_red);
  }

  li.page-item.active a {
    background-color: var(--grey_blue) !important;
    color: var(--warm_silver);
    box-shadow: 0 0 0 var(--grey_blue) !important;
    border: 1px solid var(--grey_blue) !important;
  }

  li.page-item:active a {
    background-color: var(--grey_blue);
    color: var(--warm_silver);
    box-shadow: 0 0 0 var(--grey_blue) !important;
    border: 1px solid var(--grey_blue) !important;
  }

  li.page-item:focus a {
  }

  /*.page-item li {*/
  /*background-color: #b21f2d !important;*/
  /*}*/
  input[title]:hover:after {
    background-color: transparent !important;
  }

  .view-arrow-left{
    width: 66px;
    height: 66px;
    float:left;
    position: relative;
    z-index: auto;
    transform: translateY(-3px);
  }
  .view-arrow-right{
    width: 66px;
    height: 66px;
    right: 0;
    position: relative;
    z-index: auto;
    float:right;
    transform: translateY(-3px);
  }
  .view-arrow-left img{
    position: absolute;
    width: 80%;
    height: 80%;
    margin: 10%;
    opacity: 0.2;
    filter: alpha(opacity=20);
  }
  .view-arrow-right img{
    position: absolute;
    width: 80%;
    height: 80%;
    margin: 10%;
    opacity: 0.2;
    filter: alpha(opacity=20);
  }
  .view-arrow-right:hover img {
    opacity: 0.8;
    filter: alpha(opacity=80);
    width: 100%;
    height: 100%;
    margin: 0;
    right: 0;
    transition: all 0.3s ease 0s;
  }
  .view-arrow-left:hover img {
    opacity: 0.8;
    filter: alpha(opacity=80);
    width: 100%;
    height: 100%;
    margin: 0;
    left: 0;
    transition: all 0.3s ease 0s;
  }
  span.multiselect__tag {
    background-color: var(--lavender);
  }
  span.multiselect__tag-icon {
    background-color: var(--lavender);
  }
  i.multiselect__tag-icon:hover {
    background-color: var(--violet_red);
    box-shadow: 0 0 5px var(--violet_red);
    /*border: 1px solid var(--grey_blue) !important;*/
  }
  .multiselect__option--highlight{
    /*background-color: var(--lavender) !important;*/
  }
  .multiselect__active {
    box-shadow: 0 0 0 var(--grey_blue) !important;
    border: 0px solid var(--grey_blue) !important;
  }
  .multiselect__option {
    /*background-color: var(--violet_red) !important;*/
  }
  .multiselect__element{
    /*background-color: red !important;*/
  }
  .multiselect__option--highlight {
    background: var(--grey_blue) !important;
    outline: none;
    color: white;
  }
  .multiselect__option--highlight:after {
    content: attr(data-select);
    background: var(--grey_blue) !important;
    color: white;
  }
  .multiselect__option--selected {
    background: #F3F3F3;
    color: #35495E;
    font-weight: bold;
  }
  .multiselect__option--selected:after {
    content: attr(data-selected);
    color: silver;
  }
  .multiselect__option--selected.multiselect__option--highlight {
    background: var(--violet_red) !important;
    color: #fff;
  }
  .multiselect__option--selected.multiselect__option--highlight:after {
    background: var(--violet_red) !important;
    content: attr(data-deselect);
    color: #fff;
  }
  @media only screen and (min-width: 520px) {
    .view-title h3 {
      font-size: 36px;
      padding-top: 9px;
    }
  }
  @media only screen and (min-width: 768px) {
    #view-container {
      min-height: inherit;
      height: calc(100% - 44px) !important;
      display: flex;
      display: -webkit-flex; /* Safari */
      -webkit-flex-direction: row-reverse; /* Safari 6.1+ */
      flex-direction: row;
      justify-content: center;
      overflow: visible;
      /*background-color: var(--cold_silver);*/
      border-bottom-style: solid;
      border-color: var(--dark_silver);
      margin-bottom: -100px;
      margin-top: -132px;
      padding-top: 132px;
      /*margin-top: -56px;*/
      /*transform: translateY(56px);*/
    }

    .component-container {
      /*height: 100%;*/
      width: 100%;
      text-align: center;
    }

    .box-container {
      overflow: auto;
      width: auto;
      min-width: 500px;
      height: 100% !important;
      display: inline-block;
      margin: auto;
    }

    .box-container-form {
      overflow: auto;
      width: 30%;
      height: 100% !important;
      display: inline-block;
    }

    .box-container-result {
      overflow: auto;
      width: 70%;
      height: 100% !important;
      display: inline-block;
    }

    .content-container {
      /*overflow: auto;*/
      width: 100%;
      height: 100% !important;
      margin-bottom: inherit;
    }

    .form-input-default {
      height: 30px;
    }

    /* Special styling for WebKit/Blink */
    input[type=range]::-webkit-slider-thumb {
      -webkit-appearance: none;
      height: 16px;
      width: 16px;
      background: var(--grey_blue) !important;
      cursor: pointer;
      /*margin-top: -14px; !* You need to specify a margin in Chrome, but in Firefox and IE it is automatic *!*/
    }

    /* All the same stuff for Firefox */
    input[type=range]::-moz-range-thumb {
      height: 16px;
      width: 16px;
      background: var(--grey_blue) !important;
      cursor: pointer;
    }

    /* All the same stuff for IE */
    input[type=range]::-ms-thumb {
      height: 16px;
      width: 16px;
      background: var(--lavender) !important;
      cursor: pointer;
    }

    input[type=range], .input-group-prepend {
      height: 30px !important;
    }

    select {
      height: 30px !important;
      padding: 2px 6px !important;
    }

    .trigger-button {
      width: 45% !important;
    }

    .dropdown-item {
      text-align: left;
    }
  }
</style>
