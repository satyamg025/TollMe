package com.teamerp.ipechackathon.ipechackathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 8/12/17.
 */

public class LoginPOJO {
    @SerializedName("error")
    @Expose
    public Boolean error=false;
    @SerializedName("msg")
    @Expose
    public String message=null;

    public Boolean getError(){
        return this.error;
    }

    public String getMessage(){
        return this.message;
    }
}
