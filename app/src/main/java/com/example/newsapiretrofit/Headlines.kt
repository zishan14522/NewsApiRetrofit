package com.example.newsapiretrofit

data class Headlines(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)