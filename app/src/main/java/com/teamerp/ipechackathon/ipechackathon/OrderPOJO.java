package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 6/11/17.
 */

public class OrderPOJO {
    @SerializedName("url")
    @Expose
    public String url=null;

    public String getUrl(){
        return this.url;
    }
}
