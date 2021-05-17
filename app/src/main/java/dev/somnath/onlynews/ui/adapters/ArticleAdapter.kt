package dev.somnath.onlynews.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.ItemHeadlinesBinding
import dev.somnath.onlynews.models.Article


class ArticleAdapter(private val onclick: (Article) -> Unit): RecyclerView.Adapter<ArticleViewHolder>(){

    private var _articles: ArrayList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHeadlinesBinding>(inflater, R.layout.item_headlines, parent, false)
        return  ArticleViewHolder(binding, onclick)
    }

    override fun getItemCount(): Int = _articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
       holder.bind(_articles[position])
    }

    fun addArticles(articles: List<Article>){
        _articles.clear()
        _articles.addAll(articles)
        notifyDataSetChanged()
    }
}


class ArticleViewHolder(private val binding: ItemHeadlinesBinding, private val onclick: (Article) -> Unit): RecyclerView.ViewHolder(binding.root){

    fun bind(article: Article){



        if(!article.title.isNullOrEmpty()) {
            if (article.title!!.toLowerCase().contains("- ${article.source.name?.toLowerCase()}")) {
                article.title = article.title!!.substring(
                    0,
                    article.title!!.toLowerCase().indexOf("- ${article.source.name?.toLowerCase()}")
                )
            }
        }
        binding.articleSource.text = article.source.name
        binding.articleTitle.text = article.title
        if (article.urlToImage.isNullOrEmpty()) {
            binding.articleImage.setImageResource(R.drawable.sample_bg)
        } else {

            Picasso.get()
                .load(article.urlToImage)
                .fit()
                .centerCrop()
                .into(binding.articleImage)
        }

        binding.container.setOnClickListener {
            onclick(article)
        }

    }
}