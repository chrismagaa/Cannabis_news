package com.chrismagaa.cannabis.ui.feed.news

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chrismagaa.cannabis.R
import com.chrismagaa.cannabis.databinding.FragmentFeedBinding
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.ui.details.DetailsActivity
import com.chrismagaa.cannabis.ui.feed.FeedViewModel
import com.chrismagaa.cannabis.ui.feed.PostAdapter
import com.chrismagaa.cannabis.ui.settings.SettingsActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val vmFeed: FeedViewModel by activityViewModels()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PostAdapter
    private var feed: List<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setuAdapter()
        initObservers()

    }

    private fun initObservers() {
        vmFeed.loadingNewPosts.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBarFeed.visibility = View.VISIBLE
                binding.list.visibility = View.GONE
            } else {
                binding.progressBarFeed.visibility = View.GONE
                binding.list.visibility = View.VISIBLE
            }
        }
        vmFeed.newPosts.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                feed = it
                adapter.setData(it)
            }
        }
    }

    private fun setuAdapter() {
        adapter = PostAdapter ({
            goToDetailsActivity(it)
        }) {post, position ->
            vmFeed.updatePost(post)
            adapter.updateItem(position)
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refresh -> {
                        vmFeed.getNewPosts()
                        true
                    }
                    R.id.action_settings -> {
                        goToSettingsActivity()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun goToSettingsActivity(){
        val intent = Intent(requireContext(), SettingsActivity::class.java).apply {
           putExtra(SettingsActivity.EXTRA_MODE_THEME, vmFeed.modeTheme.value)
            putExtra(SettingsActivity.EXTRA_FEEDS_ACTIVATED, Gson().toJson(vmFeed.getActivatedRssHashMap()))
        }
        startActivity(intent)
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