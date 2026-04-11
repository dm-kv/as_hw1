package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import androidx.activity.result.contract.ActivityResultContract

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val intent = Intent().putExtra(Intent.EXTRA_TEXT, text)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}
object NewPostContract: ActivityResultContract<Unit, String?>() {

    override fun createIntent(
        context: Context,
        input: Unit
    ) = Intent (context, NewPostActivity::class.java)

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) =
        if (resultCode == Activity.RESULT_OK) {
        intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}

object EditPostContract : ActivityResultContract<String?, String?>() {

    override fun createIntent(
        context: Context,
        input: String?
    ) = Intent(context, NewPostActivity::class.java).apply {
            putExtra(Intent.EXTRA_TEXT, input)
        }
    
    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}

