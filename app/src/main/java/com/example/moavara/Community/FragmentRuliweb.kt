package com.example.moavara.Community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Search.CommunityBoard
import com.example.moavara.databinding.FragmentCommunityTabBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class FragmentRuliweb(
    private var Url : String
) :
    Fragment() {

    private var adapterCommunity: AdapterCommunity? = null
    private val items = ArrayList<CommunityBoard?>()

    private var _binding: FragmentCommunityTabBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityTabBinding.inflate(inflater, container, false)
        val view = binding.root
        adapterCommunity = AdapterCommunity(items)

        binding.rview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rview.adapter = adapterCommunity

        getBoard()

        return view
    }

    private fun getBoard() {

        Thread {
            val doc: Document = Jsoup.connect("https://bbs.ruliweb.com/community/board/300143?search_type=subject_content&search_key=%EC%A1%B0%EC%95%84%EB%9D%BC").post()
            val Ruliweb: Elements = doc.select(".list_inner")

            Log.d("####", "${Ruliweb}")

//            for (i in Ruliweb.indices) {
//                if(i > 0){
//                    val title = doc.select(".ub-content .gall_tit")[i].text()
//                    val date = doc.select(".ub-content .gall_date")[i].text()
//                    val link = doc.select(".gall_tit a")[i].absUrl("href")
//
//                    requireActivity().runOnUiThread {
//                        items.add(
//                            CommunityBoard(
//                                title,
//                                link,
//                                date,
//                            )
//                        )
//                        adapterCommunity!!.notifyDataSetChanged()
//                    }
//                }
//            }
        }.start()


        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: CommunityBoard? = adapterCommunity!!.getItem(position)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.nid ?: ""))
                activity?.startActivity(intent)
            }
        })

    }
}