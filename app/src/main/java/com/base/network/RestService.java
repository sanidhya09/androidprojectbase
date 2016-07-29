package com.base.network;

import com.base.models.Contributor;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RestService {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
        @Path("owner") String owner,
        @Path("repo") String repo);

//    @Multipart
//    @POST("machinetest/product/save_product")
//    Call<SaveProduct> uploadImages(@Part("id") RequestBody category_id, @PartMap Map<String, RequestBody> Files);
  }