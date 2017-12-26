package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 8/12/17.
 */

public class OTPPOJO {

    @SerializedName("error")
    @Expose
    public Boolean error=false;
    @SerializedName("msg")
    @Expose
    public String message=null;
    @SerializedName("token")
    @Expose
    public String token=null;

    public Boolean getError(){
        return this.error;
    }

    public String getMessage(){
        return this.token;
    }
}
