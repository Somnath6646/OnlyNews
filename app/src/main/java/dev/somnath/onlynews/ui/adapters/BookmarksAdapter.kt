package dev.somnath.onlynews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.ItemBookmarksBinding
import dev.somnath.onlynews.local.data.Bookmark

class BookmarksAdapter(
    private val clickListener: (Bookmark, ImageView) -> Unit,
    private val shouldShowCheckbox: () -> Boolean
) : RecyclerView.Adapter<BookmarksViewHolder>() {


    val articles: ArrayList<Bookmark> = ArrayList()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemBookmarksBinding>(
            inflater,
            R.layout.item_bookmarks,
            parent,
            false
        )
        return BookmarksViewHolder(binding)
    }

    

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int = articles.size


    fun setBookmarkList(bookmarks: List<Bookmark>) {
        this.articles.clear()
        this.articles.addAll(bookmarks)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        tracker?.let {
            holder.bind(articles[position], clickListener,  shouldShowCheckbox , it.isSelected(position.toLong()))
        }

    }
}

class BookmarksViewHolder(private val binding: ItemBookmarksBinding) :
    RecyclerView.ViewHolder(binding.root) {


    private val titleBookmark: TextView = binding.articleTitle
    
    private val source: TextView = binding.articleSource
    private val image: ImageView = binding.articleImage
    private val checkLayout: CardView = binding.checkLayout


    fun bind(
        bookmark: Bookmark,
        clickListener: (Bookmark, ImageView) -> Unit,
        shouldShowCheckbox: () -> Boolean,
        isSelected: Boolean = false
    ) {
        val article = bookmark.article
        if(article!=null){
        if (article.urlToImage.isNullOrEmpty()) {
            image.setImageResource(R.drawable.sample_bg)
        } else {

            Picasso.get()
                .load(article.urlToImage)
                .fit()
                .centerCrop()
                .into(image)
        }

        titleBookmark.text = article.title
        source.text = article.source.name


        binding.container.setOnClickListener {
            clickListener(bookmark, image)
        }

        if (isSelected && shouldShowCheckbox()){
            checkLayout.visibility = View.VISIBLE
        }else{
            checkLayout.visibility = View.GONE
        }
}


    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =

        object : ItemDetailsLookup.ItemDetails<Long>() {

            override fun getPosition(): Int = adapterPosition

            override fun getSelectionKey(): Long? = itemId

        }

}
