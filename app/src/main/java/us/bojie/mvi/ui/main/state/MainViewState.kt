package com.codingwithmitch.mviexample.ui.main.state

import us.bojie.mvi.model.BlogPost
import us.bojie.mvi.model.User

data class MainViewState(

    var blogPosts: List<BlogPost>? = null,
    var user: User? = null

)