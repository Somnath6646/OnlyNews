package dev.somnath.onlynews.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.R
import dev.somnath.onlynews.databinding.FragmentHeadlinesBinding
import dev.somnath.onlynews.ui.adapters.ViewPagerAdapter
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel

@AndroidEntryPoint
class HeadlinesFragment : BaseFragment<FragmentHeadlinesBinding>(){


    override fun getFragmentView(): Int = R.layout.fragment_headlines

    private val viewModel by activityViewModels<ArticlesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getHeadlinesArticles()
        setUpAdapter()
        setUpTabLayout()

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                if(it!=null){
                if(it.toLowerCase() == "headlinesfragment"){
                    val action = HeadlinesFragmentDirections.actionHeadlinesFragmentToArticlesDetailFragment()
                    findNavController().navigate(action)
                }
                }
            }
        })

    }



    private lateinit var adapter: ViewPagerAdapter
    private fun setUpAdapter() {

        adapter = createPagerAdapter()
        binding.pager.adapter = adapter
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



    }


    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.customView = when (position) {
                    0 -> addCustomView(
                        "Technology", 14f,
                        Color.WHITE
                    )
                    1 -> addCustomView("Business")
                    2 -> addCustomView("Health")
                    3 -> addCustomView("Science")
                    4 -> addCustomView("Entertainment")
                    5 -> addCustomView("Sports")
                    else -> addCustomView("null")
                }


            }).attach()



        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

                tab?.view?.children?.forEach {
                    if (it is LinearLayout) {
                        val view1 = it.getChildAt(0)

                        if (view1 is CardView)
                            view1.setCardBackgroundColor(resources.getColor(R.color.cardColor))

                        val view2 = view1.findViewById<TextView>(R.id.title)

                        if (view2 is TextView) {
                            view2.post {
                                view2.setTextSize(14f)
                                view2.setTextColor(resources.getColor(R.color.textColor))
                            }
                        }
                    }
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {


                tab?.view?.children?.forEach {
                    if (it is LinearLayout) {
                        val view1 = it.getChildAt(0)



                        if (view1 is CardView)
                            view1.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))

                        val view2 = view1.findViewById<TextView>(R.id.title)


                        if (view2 is TextView) {
                            view2.post {

                                    view2.setTextSize(14f)
                                view2.setTextColor(Color.WHITE)
                            }
                        }
                    }
                }

            }

        })

    }


    @SuppressLint("ResourceAsColor")
    fun addCustomView(
        title: String,
        size: Float = 14f,
        color: Int = R.color.textColor
    ): View {
        val view = layoutInflater.inflate(R.layout.item_tab, binding.tabLayout, false)
        val titleTextView = view.findViewById<TextView>(R.id.title)
        titleTextView.apply {
            text = title


            if (color == Color.WHITE) {
                (view.findViewById<CardView>(R.id.cardView)).setCardBackgroundColor(
                    resources.getColor(
                        R.color.colorPrimary
                    )
                )
                setTextColor(color)
            } else {
                setTextColor(resources.getColor(color))
            }

            textSize = size
        }

        return view
    }


    private fun getTitle(position: Int): String {
        return when (position) {
            0 -> "Top Headlines"
            1 -> "General"
            2 -> "Technology"
            3 -> "Business"
            4 -> "Health"
            5 -> "Science"
            6 -> "Entertainment"
            7 -> "Sports"
            else -> "null"
        }
    }

    private fun createPagerAdapter(): ViewPagerAdapter =
        ViewPagerAdapter(requireActivity(), listOf("Technology",  "Business", "Health", "Science", "Entertainment", "Sports"))
}