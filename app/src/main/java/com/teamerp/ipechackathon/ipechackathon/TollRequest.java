package com.teamerp.ipechackathon.ipechackathon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 9/12/17.
 */

public interface TollRequest {
    @GET("toll_info/toll_data/")
    Call<TollPOJO> call();
}
