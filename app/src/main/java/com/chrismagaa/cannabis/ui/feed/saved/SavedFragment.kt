package com.chrismagaa.cannabis.ui.feed.saved

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chrismagaa.cannabis.databinding.FragmentSavedBinding
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.ui.details.DetailsActivity
import com.chrismagaa.cannabis.ui.feed.FeedViewModel
import com.chrismagaa.cannabis.ui.feed.PostAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {

    private var feed: List<Post>? = null
    private val vmFeed: FeedViewModel by viewModels()
    private lateinit var adapter: PostAdapter
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        initObservers()

    }

    private fun initObservers() {
        vmFeed.loadingSavedPosts.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.listSaved.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.listSaved.visibility = View.VISIBLE
            }
        }
        vmFeed.savedPosts.observe(viewLifecycleOwner) {
            feed = it
            adapter.setData(it)
        }
    }

    private fun setupAdapter() {
        adapter = PostAdapter ({
            goToDetailsActivity(it)
        }) {post, position ->
            vmFeed.updatePost(post)
            adapter.deleteItem(position)
        }

        binding.listSaved.adapter = adapter
        binding.listSaved.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onStart() {
        super.onStart()
        vmFeed.getSavedPosts()
    }

    private fun goToDetailsActivity(item: Post) {
        val intent = Intent(requireActivity(), DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_ITEM, item)
        }
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}