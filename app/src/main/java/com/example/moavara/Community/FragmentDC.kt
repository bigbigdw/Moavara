package com.example.moavara.Community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

class FragmentDC(
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

        getBoard(page)

        return view
    }

    private fun getBoard(page : Int) {

        Thread {
            val doc: Document = Jsoup.connect("${Url}&page=${page}&search_pos=&s_type=search_subject_memo&s_keyword=조아라").post()
            val DC: Elements = doc.select(".ub-content")

            for (i in DC.indices) {
                if(i > 0){
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
            }
        }.start()


        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: CommunityBoard? = adapterCommunity!!.getItem(position)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.link ?: ""))
                activity?.startActivity(intent)
            }
        })

    }
}