package com.example.moavara.Community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Retrofit.JoaraBoardResult
import com.example.moavara.Retrofit.RetrofitDataListener
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Search.CommunityBoard
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentCommunityTabBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FragmentBoard :
    Fragment() {

    private var adapterCommunity: AdapterCommunity? = null
    private val items = ArrayList<CommunityBoard?>()

    private var _binding: FragmentCommunityTabBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""
    var page = 1
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityTabBinding.inflate(inflater, container, false)
        val view = binding.root
        adapterCommunity = AdapterCommunity(items)
        firebaseAnalytics = Firebase.analytics

        binding.rview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rview.adapter = adapterCommunity

        getBoard(page)
        binding.rview.addOnScrollListener(recyclerViewScroll)

        return view
    }

    private fun getBoard(page : Int) {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["board"] = "free"
        param["page"] = page

        apiJoara.getBoardListJoa(
            param,
            object : RetrofitDataListener<JoaraBoardResult> {
                override fun onSuccess(it: JoaraBoardResult) {

                    if (it.status == "1" && it.boards != null) {
                        for (i in it.boards.indices) {
                            val date = it.boards[i].created
                            items.add(
                                CommunityBoard(
                                    it.boards[i].title,
                                    it.boards[i].nid,
                                    date.substring(4, 6) + "." + date.substring(6, 8),
                                )
                            )
                            adapterCommunity!!.notifyDataSetChanged()
                        }
                    }
                }
            })

        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val gaBundle = Bundle()
                gaBundle.putString("COMMUNITY_GO_DETAIL", "자유게시판")
                firebaseAnalytics.logEvent("COMMUNITY_FragmentBoard", gaBundle)

                val item: CommunityBoard? = adapterCommunity!!.getItem(position)
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://www.joara.com/freeboard/" + (item?.link ?: "")
                    )
                )
                startActivity(intent)
            }
        })

    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(!recyclerView.canScrollVertically(1)) {
                page++
                getBoard(page)
            }
        }
    }
}

