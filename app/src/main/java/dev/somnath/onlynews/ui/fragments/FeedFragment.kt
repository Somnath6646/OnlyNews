package dev.somnath.onlynews.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.MainActivity
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentFeedBinding
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.ui.adapters.FeedArticleAdapter
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel
import dev.somnath.onlynews.utils.Indicator
import dev.somnath.onlynews.utils.state.DataState

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>() {

    private lateinit var adapter: FeedArticleAdapter

    private val viewModel by activityViewModels<ArticlesViewModel>()

    override fun getFragmentView(): Int = R.layout.fragment_feed


    private fun setUpAdapter() {
        adapter = FeedArticleAdapter { article: Article, materialCardView: MaterialCardView ->
            onClick(article, materialCardView)
        }
        binding.feeedRecyclerView.adapter = adapter
        binding.feeedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setUpAdapter()

        viewModel.feedArticlesDataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    it.data?.let { it1 -> adapter.addArticles(it1) }
                }
                is DataState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(requireContext(), it.exception, Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getFeedArticles()
        }

    }

    private fun onClick(article: Article, cardView: MaterialCardView) {

        val action  = FeedFragmentDirections.actionFeedFragmentToArticlesDetailFragment()
        findNavController().navigate(action)
        viewModel.selectItem(article)
    }

}