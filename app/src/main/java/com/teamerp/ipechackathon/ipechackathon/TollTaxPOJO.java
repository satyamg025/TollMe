package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 4/11/17.
 */

public class TollTaxPOJO {
    @SerializedName("toll_taxes")
    @Expose
    private List<String> tollTaxes = null;
    @SerializedName("toll_name")
    @Expose
    private List<String> tollName = null;
    @SerializedName("toll_id")
    @Expose
    private List<String> toll_id = null;

    public List<String> getTollTaxes() {
        return tollTaxes;
    }

    public void setTollTaxes(List<String> tollTaxes) {
        this.tollTaxes = tollTaxes;
    }

    public List<String> getTollName() {
        return tollName;
    }

    public void setTollName(List<String> tollName) {
        this.tollName = tollName;
    }

    public List<String> getToll_id(){
        return toll_id;
    }
}
