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
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.ArrayList

class FragmentDC :
    Fragment() {

    private var adapterCommunity: AdapterCommunity? = null
    private val items = ArrayList<CommunityBoard?>()

    private var _binding: FragmentBestDetailTabsBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailTabsBinding.inflate(inflater, container, false)
        val view = binding.root
        adapterCommunity = AdapterCommunity(items)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterCommunity

        getBoard()

        return view
    }

    private fun getBoard() {

        Thread {
            val doc: Document = Jsoup.connect("https://gall.dcinside.com/mgallery/board/lists/?id=tgijjdd&s_type=search_subject_memo&s_keyword=%EC%A1%B0%EC%95%84%EB%9D%BC").post()
            val DC: Elements = doc.select(".ub-content")

            for (i in DC.indices) {

                val title = doc.select(".ub-content .gall_tit")[i].text()
                val date = doc.select(".ub-content .gall_date")[i].text()
                val link = doc.select(".gall_tit a")[i].absUrl("href")

                requireActivity().runOnUiThread {
                    items.add(
                        CommunityBoard(
                            title,
                            link,
                            date,
                        )
                    )
                    adapterCommunity!!.notifyDataSetChanged()
                }
            }
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