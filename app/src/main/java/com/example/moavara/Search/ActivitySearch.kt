package com.example.moavara.Search

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.commit
import com.example.moavara.Best.FragmentBestDetailBooks
import com.example.moavara.R
import com.example.moavara.databinding.ActivitySearchBinding
import com.google.android.material.tabs.TabLayout

class ActivitySearch : AppCompatActivity() {

    private lateinit var mFragmentSearch: FragmentSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        mFragmentSearch = FragmentSearch()
        supportFragmentManager.commit {
            replace(R.id.viewFragment, mFragmentSearch)
        }
    }

}