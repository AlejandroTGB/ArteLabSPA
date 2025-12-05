package com.duroc.artelabspa.remote

import com.duroc.artelabspa.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}
