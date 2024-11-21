package com.ruufilms.beans;

import java.io.Serializable;

public class TvSeriesBean implements Serializable {
    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }

    public String getSeries_category() {
        return series_category;
    }

    public void setSeries_category(String series_category) {
        this.series_category = series_category;
    }

    private String series_name;
    private String series_id;
    private String series_category;


}
