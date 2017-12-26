package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 9/12/17.
 */

public class TollPOJO {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("AllTolls")
    @Expose
    private List<AllTollPOJO> allTolls = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AllTollPOJO> getAllTolls() {
        return allTolls;
    }

    public void setAllTolls(List<AllTollPOJO> allTolls) {
        this.allTolls = allTolls;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
