package com.bderoo.ghubsearch.util

import com.bderoo.ghubsearch.model.Repo

fun createRepo(
    id: Int = 0,
    name: String = "Name",
    fullName: String = "FullName",
    url: String = "http://test.com",
    description: String = "Description",
    forks: Int = 0,
    stars: Int = 0,
    topics: List<String> = emptyList(),
): Repo = Repo(id, name, fullName, url, description, forks, stars, topics)