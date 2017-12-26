package com.teamerp.ipechackathon.ipechackathon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 6/11/17.
 */

public interface OrderRequest {
    @GET("toll_info/qr_generate/")
    Call<OrderPOJO> call(@Query("tid") String toll_id,@Query("amount") String amount,@Query("type") String type,@Query("route") String route);
}
