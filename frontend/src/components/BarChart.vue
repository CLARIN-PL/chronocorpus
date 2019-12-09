<script>
import { Bar } from 'vue-chartjs'

export default {
  extends: Bar,
  name: 'BarChart',
  props: {
    chartData: {
      type: Object,
      default: null
    },
    options: {
      type: Object,
      default: null
    }
  },
  data: function () {
    return {

    }
  },
  mounted () {
    this.renderChart(this.chartData, this.options)
    this.$data._chart.update()
  },
  watch: {
    chartData () {
      this.$data._chart.update()
    }
  }
}

</script>

<style scoped>
</style>
