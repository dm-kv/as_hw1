package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import java.util.Optional.empty


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
        val adapter = PostsAdapter(
            object : PostListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) {posts ->
            adapter.submitList(posts)
        }

        binding.save.setOnClickListener {
            val content = binding.content.text?.toString().orEmpty()
            if (content.isBlank()) {
                Toast.makeText(
                    this,
                    R.string.content_is_blank_error,
                    Toast.LENGTH_SHORT,
                )
                    .show()
                return@setOnClickListener
            }

            viewModel.saveContent(content)
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
        }

        binding.cancel.setOnClickListener {
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
            binding.group.visibility = View.GONE
            viewModel.toDoEmpty()
        }

        viewModel.edited.observe(this) { edited ->
            if (edited.id != 0L) {
                with (binding.content) {
                    AndroidUtils.showKeyboard(this)
                    setText("")
                    append(edited.content)
                    binding.group.visibility = View.VISIBLE
                    binding.cancelEdit.text = "Message editor"
                }
            } else {
                binding.group.visibility = View.GONE

            }
        }
    }
}
