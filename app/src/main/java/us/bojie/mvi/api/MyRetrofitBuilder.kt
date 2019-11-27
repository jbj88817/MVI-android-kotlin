package us.bojie.mvi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import us.bojie.mvi.util.LiveDataCallAdapterFactory

object MyRetrofitBuilder {
    const val BASE_URL = "https://open-api.xyz/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }
}

