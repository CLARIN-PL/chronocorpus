package pl.clarin.chronocorpus.timeseries.entity;

import java.util.Objects;

public class TimeSeriesRow {

    private int year;
    private int month;
    private int day;
    private int count;

    public TimeSeriesRow(int year, int month, int count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public TimeSeriesRow(int year, int month, int day, int count) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.count = count;
    }

    public String getKey(){
        if( day > 0){
            return day+"-"+month + "-" + year;
        } else if(month > 0) {
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSeriesRow that = (TimeSeriesRow) o;
        return year == that.year &&
                month == that.month &&
                day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
}
