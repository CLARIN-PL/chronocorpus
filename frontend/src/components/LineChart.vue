<script>
import { Line } from 'vue-chartjs'

export default {
  extends: Line,
  name: 'LineChart',
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
    this.options.onClick = this.handle
    this.renderChart(this.chartData, this.options)
    this.$data._chart.update()
  },
  watch: {
    chartData () {
      this.$data._chart.update()
    }
  },
  methods: {
    handle (point, event) {
      try {
        const item = event[0]
        if (typeof item !== 'undefined') {
          let pointLabel = this._data._chart.data.labels[item._index] + ''
          let canSplit = function () {
            return (pointLabel || '').split('-').length > 1
          }
          let result = []
          if (canSplit === true) {
            result = pointLabel.split('-')
          } else {
            result = this._data._chart.data.labels[item._index] + ''
          }
          this.$emit('on-receive', {
            // index: item._index,
            year: result
            // value: this.values[item._index]
          })
        }
      } catch (e) {
        console.log(Object.keys(e), e.message)
      }
    }
  }
}

</script>

<style scoped>
</style>
