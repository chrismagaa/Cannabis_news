package com.chrismagaa.cannabis.ui.details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import coil.load
import com.chrismagaa.cannabis.R
import com.chrismagaa.cannabis.databinding.ActivityDetailsBinding
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.utils.fromHTMLToSpanned
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ITEM = "extra_item"
    }

    private var item: Post? = null
    private val vmDetails: DetailsViewModel by viewModels()


    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setupToolbar()

        if (intent.hasExtra(EXTRA_ITEM)){
            item = intent.getParcelableExtra<Post>(EXTRA_ITEM)
            vmDetails.setIsFavorite(item?.isFavorite ?: false)
            updateView()
        }

        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarDetails.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun updateView() {
        val urlPicture = item?.getPictureURLFromDescription()
        if(urlPicture?.isNotBlank() == true){
            binding.cvPicture.visibility = View.VISIBLE
            binding.ivPictureDetail.load(urlPicture) {
                crossfade(true)
            }
        }else{
            binding.cvPicture.visibility = View.GONE
        }
        binding.tvTitleBlog.text = item?.titleBlog?.fromHTMLToSpanned()?: ""
        binding.tvTitleDetail.text = item?.title?.fromHTMLToSpanned()?: ""
        binding.tvDetails.text = item?.description?.fromHTMLToSpanned()?: ""
        binding.tvDateDetails.text = item?.getDateFormatted()?:""

        vmDetails.isFavorite.observe(this) {
            if (it) {
                binding.btnMark.setImageResource(R.drawable.ic_baseline_turned_in_24)
            } else {
                binding.btnMark.setImageResource(R.drawable.ic_outline_bookmark_add_24)
            }
        }

        binding.btnMark.setOnClickListener {
            item?.let { post -> vmDetails.updatePost(post) }
            vmDetails.isFavorite.value?.let { value -> vmDetails.setIsFavorite(value.not()) }
        }


        binding.cvFullStory.setOnClickListener {
            viewFullStory()
        }

    }

    private fun viewFullStory() {
        item?.link?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
    }
}