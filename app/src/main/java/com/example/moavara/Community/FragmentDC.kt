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

        getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=tgijjdd", "tgijjdd")
        getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=genrenovel", "genrenovel")
        getBoard("https://gall.dcinside.com/mgallery/board/lists?id=lovestory", "lovestory")
        getBoard("https://gall.dcinside.com/board/lists?id=fantasy_new2","fantasy_new2")


        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val gaBundle = Bundle()
                gaBundle.putString("COMMUNITY_GO_DETAIL", "DC")
                firebaseAnalytics.logEvent("COMMUNITY_FragmentCommunity", gaBundle)
                val item: CommunityBoard? = adapterCommunity?.getItem(position)

                Log.d("####", "${item?.link} ${item?.title}")

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.link ?: ""))
                activity?.startActivity(intent)
            }
        })

        return view
    }

    private fun getBoard(Url: String, ID : String) {

        Thread {
            val doc: Document = Jsoup.connect("${Url}&s_type=search_subject_memo&s_keyword=.EC.A1.B0.EC.95.84.EB.9D.BC").post()
            val DC: Elements = doc.select(".ub-content")

                try {

                    for (i in DC.indices) {
                        val title = DC.select(".gall_tit")[i].text()
                        val date = DC.select(".gall_date")[i].text()
                        val link = "https://gall.dcinside.com/mgallery/board/view/?id=${ID}&no=${DC[i].absUrl("data-no").replace("https://gall.dcinside.com/board/","")}"

                        if (doc.select(".gall_tit a")[i].absUrl("href").contains("https://gall.dcinside.com/mgallery/board/view/?id=")) {

                            requireActivity().runOnUiThread {
                                items.add(
                                    CommunityBoard(
                                        title,
                                        link,
                                        date,
                                    )
                                )

                                adapterCommunity?.notifyDataSetChanged()
                            }
                        }
                    }


                } catch (e: IllegalStateException) {
                }

        }.start()
    }
}