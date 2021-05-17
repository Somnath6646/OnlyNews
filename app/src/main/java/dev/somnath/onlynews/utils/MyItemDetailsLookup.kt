package dev.somnath.onlynews.utils



import android.view.MotionEvent

import androidx.recyclerview.selection.ItemDetailsLookup

import androidx.recyclerview.widget.RecyclerView
import dev.somnath.onlynews.ui.adapters.BookmarksViewHolder


class MyItemDetailsLookup(private val recyclerView: RecyclerView) :

    ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {

        val view = recyclerView.findChildViewUnder(event.x, event.y)

        if (view != null) {

            return (recyclerView.getChildViewHolder(view) as BookmarksViewHolder)

                .getItemDetails()

        }

        return null

    }

}