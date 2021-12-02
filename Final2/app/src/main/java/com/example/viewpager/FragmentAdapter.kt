package com.example.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
	var fragmentList = listOf<Fragment>()
	var id = "No id"

	override fun getItemCount(): Int {
		return fragmentList.size
	}

	override fun createFragment(position: Int): Fragment {
		var bundle = Bundle()
		bundle.putString("id",id)
		if( position == 0){fragmentList[0].arguments =bundle
			return fragmentList[0]}
		if (position == 1) {
			fragmentList[1].arguments = bundle
			return fragmentList[1]
		}
		if (position ==2){fragmentList[2].arguments =bundle
			return fragmentList[2]
		}

		return fragmentList.get(position)
	}
}