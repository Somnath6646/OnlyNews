package dev.somnath.onlynews.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.MainActivity
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentBookmarksBinding
import dev.somnath.onlynews.local.data.Bookmark
import dev.somnath.onlynews.ui.adapters.ArticleAdapter
import dev.somnath.onlynews.ui.adapters.BookmarksAdapter
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel
import dev.somnath.onlynews.utils.MyItemDetailsLookup
import dev.somnath.onlynews.utils.MyItemKeyProvider

@AndroidEntryPoint
class BookmarksFragment : BaseFragment<FragmentBookmarksBinding>(){

    private lateinit var adapter: BookmarksAdapter
    private var tracker: SelectionTracker<Long>? = null

    private val viewModel by activityViewModels<ArticlesViewModel>()

    override fun getFragmentView(): Int = R.layout.fragment_bookmarks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpRecyclerView()
    }

    fun setUpRecyclerView() {

        setUpAdapter()
        displayArticles()
    }


    private fun displayArticles() {
        viewModel.bookmarks.observe(viewLifecycleOwner, Observer { bookmarks: List<Bookmark> ->
           adapter.setBookmarkList(bookmarks)
        })


    }



    private fun setUpAdapter() {

        adapter = BookmarksAdapter({ bookmark: Bookmark, imageView: ImageView ->
            onClickBookmarks(bookmark, imageView)
        }
            ,
            {
                true
            })



        binding.bookmarksRecyclerView.adapter = adapter
        initTracker()
        adapter.tracker = tracker
        binding.bookmarksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }


    private fun initTracker(){
        tracker = SelectionTracker.Builder<Long>(

            "mySelection123",
            binding.bookmarksRecyclerView,
            MyItemKeyProvider(binding.bookmarksRecyclerView),
            MyItemDetailsLookup(binding.bookmarksRecyclerView),
            StorageStrategy.createLongStorage()

        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = tracker?.selection!!.size()
                    if (items > 0) {
                        showDeleteBtn(tracker?.selection!!)
                    }else{
                        disableDeleteBtn()
                    }
                }
            })
    }


    private fun disableDeleteBtn() {
        (requireActivity() as MainActivity).toStartTransition()
        binding.deleteCardContainer.visibility = View.GONE
    }

    private fun showDeleteBtn(selection: Selection<Long>) {

        (requireActivity() as MainActivity).toEndTransition()
        binding.deleteCardContainer.visibility = View.VISIBLE
        binding.deleteNumberText.text = selection.size().toString()
        binding.deleteCardContainer.setOnClickListener {
            val list = selection.map {
                viewModel.bookmarks.value!!.get(it.toInt())
            }

            deleteBookmarks(list)

            disableDeleteBtn()
            tracker?.clearSelection()
        }
    }


    private fun deleteBookmarks(bookmarks: List<Bookmark>) {

        showAlertDialog("Delete?",
            "Deleting will lead to permanent removal of these bookmarks",
            "Cancel",
            "Delete",
            { dialog: Dialog ->



                dialog.dismiss()

                viewModel.deleteABookmark(bookmarks)
                Snackbar.make(
                    binding.bookmarksFragment,
                    "Bookmark deleted successfully",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Undo") {
                        viewModel.addABookmark(bookmarks)
                    }.setActionTextColor(resources.getColor(R.color.colorPrimary)).show()




            }, { dialog: Dialog ->
                dialog.dismiss()
            })

    }

    private fun showAlertDialog(
        titleText: String,
        bodyText: String,
        negativeButtonText: String,
        positiveButtonText: String,
        positiveButtonAction: (Dialog) -> Unit,
        negativeButtonAction: (Dialog) -> Unit
    ) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialogue_delete)

        dialog.findViewById<TextView>(R.id.title_delete_dialog).setText(titleText)
        dialog.findViewById<TextView>(R.id.subTitle_delete_dialog).setText(bodyText)
        dialog.findViewById<TextView>(R.id.delete_button_dialog).setText(positiveButtonText)
        dialog.findViewById<TextView>(R.id.cancel_button_dialog).setText(negativeButtonText)

        dialog.findViewById<Button>(R.id.delete_button_dialog).setOnClickListener {
            positiveButtonAction(dialog)
        }

        dialog.findViewById<Button>(R.id.cancel_button_dialog).setOnClickListener {
            negativeButtonAction(dialog)
        }

        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }



    private fun onClickBookmarks(bookmark: Bookmark, imageView: ImageView){

        bookmark.article?.let { viewModel.selectItem(it) }
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToArticlesDetailFragment()
        findNavController().navigate(action)

    }
}