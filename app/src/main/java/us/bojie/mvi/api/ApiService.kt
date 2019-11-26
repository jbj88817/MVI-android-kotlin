package us.bojie.mvi.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Path
import us.bojie.mvi.model.BlogPost
import us.bojie.mvi.model.User
import us.bojie.mvi.util.GenericApiResponse

interface ApiService {
    @GET("placeholder/blogs")
    fun getBlogPosts(): LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ): LiveData<GenericApiResponse<User>>
}