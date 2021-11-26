package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.viewpager.databinding.ActivityCheckRoleBinding
import com.google.android.material.tabs.TabLayoutMediator

class CheckRoleActivity : AppCompatActivity() {
    val binding by lazy { ActivityCheckRoleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fragmentList = listOf(FragmentA(), Info(), FragmentC())
        val adapter = FragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager.adapter = adapter
        val tabTitles = listOf<String>("A", "B", "C")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        val message = intent.getStringExtra("userType")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}