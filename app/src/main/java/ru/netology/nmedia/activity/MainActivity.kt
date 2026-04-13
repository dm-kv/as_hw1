package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import android.content.Intent
import  androidx.activity.result.launch
import android.net.Uri
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

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

        val viewModel: PostViewModel by viewModels()

        val editPostLauncher = registerForActivityResult(EditPostContract) { text ->
            text?.let {
                viewModel.saveContent(it)
            }
        }

        val newPostLauncher = registerForActivityResult(NewPostContract) {
            val result = it ?: return@registerForActivityResult
            viewModel.saveContent(result)
        }

        val adapter = PostsAdapter(
            object : PostListener {
                override fun onEdit(post: Post) {
                    editPostLauncher.launch(post.content)
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val chooser = Intent.createChooser(intent, getString(R.string.description_post_share))
                    startActivity(chooser)
                }

                override fun onVideo(post: Post) {
                    val intentVideo = Intent(Intent.ACTION_VIEW, post.video?.toUri())
                    startActivity(intentVideo)
                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) {posts ->
            adapter.submitList(posts)
        }

        binding.add.setOnClickListener {
            newPostLauncher.launch()
        }
    }
}
