package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    fun checkTheDigit(digit: Int,) = when(digit) {
        in 0..999 -> digit.toString()
        in 1000..9999 -> (digit.toDouble() / 1000).toBigDecimal().setScale(1, RoundingMode.DOWN).toString() + "K"
        in 10000..999999 -> (digit / 1000).toString() + "K"
        else -> (digit.toDouble() / 1000000).toBigDecimal().setScale(1, RoundingMode.DOWN).toString() + "M"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) {post ->
            with(binding){
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likeCount.text = checkTheDigit(post.likes)
                shareCount.text = checkTheDigit(post.shares)
            }

        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener {
            viewModel.share()
        }
    }
}
