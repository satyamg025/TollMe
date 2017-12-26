package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 9/12/17.
 */

public class AllTollPOJO {
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("tollname")
    @Expose
    private String tollname;
    @SerializedName("tollplazaid")
    @Expose
    private Integer tollplazaid;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getTollname() {
        return tollname;
    }

    public void setTollname(String tollname) {
        this.tollname = tollname;
    }

    public Integer getTollplazaid() {
        return tollplazaid;
    }

    public void setTollplazaid(Integer tollplazaid) {
        this.tollplazaid = tollplazaid;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
