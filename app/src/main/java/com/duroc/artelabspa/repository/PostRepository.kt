package com.duroc.artelabspa.repository

import com.duroc.artelabspa.model.Post
import com.duroc.artelabspa.remote.RetrofitInstance

class PostRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}
