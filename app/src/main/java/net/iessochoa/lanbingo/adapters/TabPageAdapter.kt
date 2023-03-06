package net.iessochoa.lanbingo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.iessochoa.lanbingo.fragments.ConnectionFragment
import net.iessochoa.lanbingo.fragments.PlayFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayFragment()
            1 -> ConnectionFragment()
            else -> PlayFragment()
        }
    }
}