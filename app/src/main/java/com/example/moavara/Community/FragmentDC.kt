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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class FragmentDC : Fragment() {

    private var adapterCommunity: AdapterCommunity? = null
    private val items = ArrayList<CommunityBoard?>()

    private var _binding: FragmentCommunityTabBinding? = null
    private val binding get() = _binding!!

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

        getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=tgijjdd")
        getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=genrenovel")
        getBoard("https://gall.dcinside.com/mgallery/board/lists?id=lovestory")
        getBoard("https://gall.dcinside.com/board/lists?id=fantasy_new2")

        return view
    }

    private fun getBoard(Url : String) {

        Thread {
            val doc: Document = Jsoup.connect("${Url}&page=1&search_pos=&s_type=search_subject_memo&s_keyword=조아라").post()
            val DC: Elements = doc.select(".ub-content")
            requireActivity().runOnUiThread {
                try{
                    for (i in DC.indices) {
                        if(i > 0){
                            val title = doc.select(".ub-content .gall_tit")[i].text()
                            val date = doc.select(".ub-content .gall_date")[i].text()
                            val link = doc.select(".gall_tit a")[i].absUrl("href")

                            items.add(
                                CommunityBoard(
                                    title,
                                    link,
                                    date,
                                )
                            )


                        }
                    }
                    adapterCommunity?.notifyDataSetChanged()
                }catch (e : IllegalStateException){ }
            }
        }.start()


        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val gaBundle = Bundle()
                gaBundle.putString("COMMUNITY_GO_DETAIL", "DC")
                firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)

                val item: CommunityBoard? = adapterCommunity!!.getItem(position)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.link ?: ""))
                activity?.startActivity(intent)
            }
        })
    }
}