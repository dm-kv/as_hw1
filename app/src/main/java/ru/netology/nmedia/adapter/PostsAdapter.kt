package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode

typealias LikeListener = (Post) -> Unit
typealias ShareListener = (Post) -> Unit

fun checkTheDigit(digit: Int,) = when(digit) {
    in 0..999 -> digit.toString()
    in 1000..9999 -> (digit.toDouble() / 1000).toBigDecimal().setScale(1, RoundingMode.DOWN).toString() + "K"
    in 10000..999999 -> (digit / 1000).toString() + "K"
    else -> (digit.toDouble() / 1000000).toBigDecimal().setScale(1, RoundingMode.DOWN).toString() + "M"
}

class PostsAdapter(private val likeListener: LikeListener, private val shareListener: ShareListener): ListAdapter<Post, PostViewHolder>(
    PostDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeListener, shareListener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }
}

class PostViewHolder(private val binding: CardPostBinding, private val likeListener: LikeListener, private val shareListener: ShareListener): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with (binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
            likeCount.text = checkTheDigit(post.likes)
            shareCount.text = checkTheDigit(post.shares)

            like.setOnClickListener {
                likeListener(post)
            }
            share.setOnClickListener {
                shareListener(post)
            }
        }
    }
}
object PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(p0: Post, p1: Post) = p0 ==p1
}


