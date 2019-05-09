<script>
// import Bar from 'vue-chartjs'
import { mixins, Bar } from 'vue-chartjs'
const { reactiveProp } = mixins

export default {
  extends: Bar,
  mixins: [reactiveProp],
  name: 'QuantityAnalysisChart',
  props: {
    chartdata: {
      type: Object,
      default: null
    }
  },
  data: function () {
    return {
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            },
            gridLines: {
              display: true
            },
            type: 'logarithmic'
          }],
          xAxes: [{
            ticks: {
              beginAtZero: true
            },
            gridLines: {
              display: false
            }
          }]
        },
        legend: {
          display: false
        },
        tooltips: {
          enabled: true,
          mode: 'single',
          callbacks: {
            label: function (tooltipItems, data) {
              return tooltipItems.yLabel
            }
          }
        },
        responsive: true,
        maintainAspectRatio: false,
        height: 200
      }
    }
  },
  mounted () {
    this.renderChart(this.chartData, this.options)
  }
}

</script>

<style scoped>
</style>
