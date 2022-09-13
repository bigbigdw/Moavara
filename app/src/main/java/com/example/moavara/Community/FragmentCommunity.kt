package com.example.moavara.Community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.moavara.R
import com.example.moavara.databinding.FragmentCommunityBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FragmentCommunity : Fragment() {

    private lateinit var fragmentBoard: FragmentBoard
    private lateinit var fragmentDC: FragmentDC

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    var pos = 0
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentBestTab = binding.tabs
        firebaseAnalytics = Firebase.analytics

        fragmentBoard = FragmentBoard()
        childFragmentManager.commit {
            replace(R.id.llayoutWrap, fragmentBoard)
        }

        val gaBundle = Bundle()
        gaBundle.putString("COMMUNITY_TAB", "자유게시판")
        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("자유게시판"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("DC 장르 소설"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("DC 웹소설 연재"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("DC 로맨스 소설"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("DC 판타지"))

        fragmentBestTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        val gaBundle = Bundle()
                        gaBundle.putString("COMMUNITY_TAB", "자유게시판")
                        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                        fragmentBoard = FragmentBoard()
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, fragmentBoard)
                        }
                    }
                    1->{
                        val gaBundle = Bundle()
                        gaBundle.putString("COMMUNITY_TAB", "DC 장르 소설")
                        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                        fragmentDC = FragmentDC("https://gall.dcinside.com/mgallery/board/lists/?id=tgijjdd")
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, fragmentDC)
                        }
                    }
                    2->{
                        val gaBundle = Bundle()
                        gaBundle.putString("COMMUNITY_TAB", "DC 웹소설 연재")
                        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                        fragmentDC = FragmentDC("https://gall.dcinside.com/mgallery/board/lists/?id=genrenovel")
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, fragmentDC)
                        }
                    }
                    3->{
                        val gaBundle = Bundle()
                        gaBundle.putString("COMMUNITY_TAB", "DC 로맨스 소설")
                        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                        fragmentDC = FragmentDC("https://gall.dcinside.com/mgallery/board/lists?id=lovestory")
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, fragmentDC)
                        }
                    }
                    4->{
                        val gaBundle = Bundle()
                        gaBundle.putString("COMMUNITY_TAB", "DC 판타지")
                        firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                        fragmentDC = FragmentDC("https://gall.dcinside.com/board/lists?id=fantasy_new2")
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, fragmentDC)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }
}