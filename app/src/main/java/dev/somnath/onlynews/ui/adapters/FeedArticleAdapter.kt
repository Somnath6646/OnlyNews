package dev.somnath.onlynews.ui.adapters

import android.animation.Animator
import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.ItemFeedBinding
import dev.somnath.onlynews.models.Article


class FeedArticleAdapter(private val onClick: (Article, MaterialCardView) -> Unit, ) : RecyclerView.Adapter<FeedArticleViewHolder>(){

    private var _articles: ArrayList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFeedBinding>(inflater, R.layout.item_feed, parent, false)
        return FeedArticleViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int = _articles.size

    override fun onBindViewHolder(holder: FeedArticleViewHolder, position: Int) {
        holder.bind(_articles[position])
    }

    fun addArticles(articles: List<Article>){
        _articles.clear()
        _articles.addAll(articles)
        notifyDataSetChanged()
    }
}

private const val TAG = "FeedArticleAdapter"

class FeedArticleViewHolder(private val binding: ItemFeedBinding, private val onClick: (Article, MaterialCardView) -> Unit): RecyclerView.ViewHolder(binding.root){



    @SuppressLint("ClickableViewAccessibility")
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
        binding.articleDetail.text = article.description
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
            onClick(article, binding.container)
        }



        /*binding.container.setOnGenericMotionListener(object : View.OnGenericMotionListener{
            override fun onGenericMotion(v: View?, event: MotionEvent?): Boolean
            {

                if (true) {
                    if(true)

                        v?.apply {
                            animate()
                                .scaleX(0.95f)
                                .scaleY(0.95f)
                                .setDuration(100)
                                .setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationRepeat(animation: Animator?) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {
                                        v?.apply {
                                            animate()
                                                .scaleX(1f)
                                                .scaleY(1f)

                                                .start()
                                        }
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {

                                    }

                                    override fun onAnimationStart(animation: Animator?) {

                                    }
                                }).start()
                        }
                }

                return false
            }
        })

        binding.container.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean){

                Log.i(TAG, "onHover: aya hai")


            }

        })*/
    }

}