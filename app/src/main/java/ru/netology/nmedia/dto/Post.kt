package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 9999,
    var shares: Int = 999,
    var likedByMe: Boolean = false
)

