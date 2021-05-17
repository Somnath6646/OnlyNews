package dev.somnath.onlynews.ui.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.MainActivity
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentArticlesDetailBinding
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel

@AndroidEntryPoint
class ArticlesDetailFragment : BaseFragment<FragmentArticlesDetailBinding>() {


    private val viewModel: ArticlesViewModel by activityViewModels<ArticlesViewModel>()


    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy: ")

        (requireHost() as MainActivity).toStartTransition()

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        (requireHost() as MainActivity).toEndTransition()


        viewModel.selectedArticle.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                setUpViews(it)
            }
        })

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bookmarksBtn.setOnClickListener {
            val article: Article? = viewModel.selectedArticle.value
            if (article != null)
                viewModel.addABookmark(article = article)
        }

    }

    private fun setUpViews(article: Article) {

        if(article!=null){
        if (!article.title.isNullOrEmpty() && (article.source.name != null)    ) {
            if (article.title!!.toLowerCase().contains("- ${article.source.name.toLowerCase()}")) {
                article.title = article.title!!.substring(
                    0,
                    article.title!!.toLowerCase().indexOf("- ${article.source.name.toLowerCase()}")
                )
            }
        }
        binding.articleSource.text = article.source.name
        binding.articleTitle.text = article.title


        if (article.content != null && article.description != null) {
            if (article.content!!.length > article.description.length)
                binding.articleDescription.text = article.content
            else
                binding.articleDescription.text = article.description
        }

            binding.articleDescription.append("\n View full news at: ${article.url}")
        if (article.urlToImage.isNullOrEmpty()) {
            binding.articleImage.setImageResource(R.drawable.sample_bg)
        } else {

            Picasso.get()
                .load(article.urlToImage)
                .fit()
                .centerCrop()
                .into(binding.articleImage)
        }

        if(article.source.name != null)
        binding.articleSourceLogo.text = article.source.name[0].toString()
}

    }

    override fun getFragmentView(): Int = R.layout.fragment_articles_detail

}