package pl.clarin.chronocorpus.timeseries.entity;

public class TimeSeriesRow {

    private int year;
    private int month;
    private int count;

    public TimeSeriesRow(int year, int month, int count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }
    public String getKey(){
        if(month > 0) {
            return month + "-" + year;
        } else{
            return ""+year;
        }
    }
    public void addCount(int cnt){
        this.count = this.count + cnt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSeriesRow)) return false;

        TimeSeriesRow that = (TimeSeriesRow) o;

        if (year != that.year) return false;
        return month == that.month;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }
}
