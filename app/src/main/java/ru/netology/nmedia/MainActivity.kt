package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    fun checkTheDigit1(digit: Int): String {
        if (digit in 1000..9999) {
            val d = digit.toDouble() / 1000
            return d.toString().format("%1f", d) + "K"
        } else if (digit in 10000..999999) {
            val d = digit.toDouble() / 1000
            return d.toString() + "K"
        } else if (digit >= 1000000) {
            val d = digit.toDouble() / 1000000
            return d.toString().format("%1f", d) + "M"
        } else return digit.toString()
    }
    
    fun checkTheDigit(digit: Int, d: Double = digit.toDouble()) = when(digit) {
        in 0..999 -> digit.toString()
        in 1000..9999 -> (d / 1000).toString().format("%1f", d) + "K"
        in 10000..999999 -> (d / 1000).toString() + "K"
        else -> (d / 1000000).toString().format("%1f", d) + "M"
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

        val post = Post(1,
            "Нетология. Университет интернет-профессий будущего",
            "21 мая в 18:36",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        )
        with(binding){
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = post.likes.toString()
            if (post.likedByMe) {
                like.setImageResource((R.drawable.ic_liked_24))
            }
            like.setOnClickListener {
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likeCount.text = checkTheDigit(post.likes)
            }
            shareCount.text = post.shares.toString()
            share.setOnClickListener {
                post.shares++
                shareCount.text = checkTheDigit(post.shares)
            }
        }
    }
}