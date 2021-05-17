package dev.somnath.onlynews.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.somnath.onlynews.ui.fragments.ArticlesFragment


private const val ARG_OBJECT = "object"

class ViewPagerAdapter(private val fragmentActivity: FragmentActivity, private val fragCategories: List<String>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 6




    override fun createFragment(position: Int): Fragment {

        val fragment: Fragment = ArticlesFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putString(ARG_OBJECT, fragCategories[position])
        }
        return fragment
    }


}


