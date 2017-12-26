package com.teamerp.ipechackathon.ipechackathon;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyam on 9/12/17.
 */

public interface ProfileRequest {
    @GET("profile")
    Call<ProfilePOJO> call();
}
