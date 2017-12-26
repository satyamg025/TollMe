package com.teamerp.ipechackathon.ipechackathon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 4/11/17.
 */

public interface TollTaxRequest {
    @GET("tollTax.php")
    Call<TollTaxPOJO> call(@Query("from") String from,@Query("to") String to,@Query("route") String route);
}
