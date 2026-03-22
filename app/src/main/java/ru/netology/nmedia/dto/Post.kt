package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var shares: Int = 0,
    var likedByMe: Boolean = false,
    var likeCount: Int = 0,
    var shareCount: Int = 0
)

