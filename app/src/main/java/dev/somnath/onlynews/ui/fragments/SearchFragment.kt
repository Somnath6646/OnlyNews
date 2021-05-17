package dev.somnath.onlynews.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentSearchBinding
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.ui.adapters.ArticleAdapter
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel
import dev.somnath.onlynews.utils.state.DataState

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(){


    private val viewModel by activityViewModels<ArticlesViewModel>()

    private lateinit var adapter: ArticleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()

    }

    override fun getFragmentView(): Int  = R.layout.fragment_search

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel

        binding.lifecycleOwner = requireActivity()

        setUpAdapter()

        binding.btnSearch.setOnClickListener{
            val query = binding.searchTextField.text.toString()
            if(query.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Type something to search", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.getSearchedArticles()
            }
        }

        viewModel.searchedArticlesDataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchIcon.visibility = View.VISIBLE
                    it.data?.let { it1 -> adapter.addArticles(it1) }
                }
                is DataState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchIcon.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), it.exception, Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchIcon.visibility = View.GONE
                }
            }
        })





    }

    private fun setUpAdapter() {

        adapter = ArticleAdapter { openDetails(it) }
        binding.searchRecyclerView.adapter = adapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun openDetails(article: Article){
        viewModel.selectItem(article)
        val action = SearchFragmentDirections.actionSearchFragmentToArticlesDetailFragment()
        findNavController().navigate(action)
    }
}