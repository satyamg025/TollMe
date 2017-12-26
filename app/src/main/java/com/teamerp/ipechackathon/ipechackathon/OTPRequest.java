package com.teamerp.ipechackathon.ipechackathon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 8/12/17.
 */

public interface OTPRequest {
    @GET("login/check_otp/")
    Call<OTPPOJO> call(@Query("otp") String otp);
}
