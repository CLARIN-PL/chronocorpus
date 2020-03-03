<script>
import { Pie } from 'vue-chartjs'

export default {
  extends: Pie,
  name: 'PieChart',
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
