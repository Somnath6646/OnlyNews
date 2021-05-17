package dev.somnath.onlynews.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentArticlesBinding
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.ui.adapters.ArticleAdapter
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel
import dev.somnath.onlynews.utils.Indicator
import dev.somnath.onlynews.utils.state.DataState



private const val ARG_OBJECT = "object"

@AndroidEntryPoint
class ArticlesFragment : BaseFragment<FragmentArticlesBinding>(){



    override fun getFragmentView(): Int = R.layout.fragment_articles

    private lateinit var adapter: ArticleAdapter
    private val viewModel by activityViewModels<ArticlesViewModel>()

    private lateinit var category: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf {
            it.containsKey(ARG_OBJECT) }?.apply {
            category = getString(ARG_OBJECT).toString()
            viewModel.category.value = getString(ARG_OBJECT)
            viewModel.getHeadlinesArticles()

            Log.i("MYTAG", "onViewCreated: ${viewModel.category.value}")
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setUpAdapter()

        Log.i("TAG", "onActivityCreated: called")

        viewModel.headlinesArticlesDataState.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (it.data.category.equals(category)){
                        it.data.articles?.let { it1 -> adapter.addArticles(it1) }
                    }
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
            viewModel.getHeadlinesArticles()
        }
    }

    private fun setUpAdapter() {

        adapter = ArticleAdapter { openDetails(it) }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun openDetails(article: Article) {
        viewModel.navigate.value = Indicator("headlinesfragment")
        viewModel.selectItem(article)
    }


}