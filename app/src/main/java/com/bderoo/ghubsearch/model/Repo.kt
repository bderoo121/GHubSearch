package com.bderoo.ghubsearch.model

data class Repo(
    val id: Int,
    val name: String,
    val full_name: String,
    val html_url: String,
    val description: String,
    val forks_count: Int,
    val stargazers_count: Int,
    val topics: List<String>,
)