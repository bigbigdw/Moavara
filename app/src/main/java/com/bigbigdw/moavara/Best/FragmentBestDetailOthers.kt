package com.bigbigdw.moavara.Best

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.Retrofit.*
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Util.Param
import com.bigbigdw.moavara.Util.applyingTextColor
import com.bigbigdw.moavara.databinding.FragmentBestDetailTabsBinding
import com.bigbigdw.moavara.databinding.ItemBestDetailOtherBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URL

class FragmentBestDetailBooks(private val platfrom: String, private val bookCode: String, private val bookWriter: String) :
    Fragment() {

    private var adapterBestOthers: AdapterBestOther? = null
    private val items = ArrayList<BookListDataBest?>()

    private var _binding: FragmentBestDetailTabsBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val crashlytics = Firebase.crashlytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailTabsBinding.inflate(inflater, container, false)
        val view = binding.root
        adapterBestOthers = AdapterBestOther(items)
        firebaseAnalytics = Firebase.analytics

        binding.rview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rview.adapter = adapterBestOthers

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "다른 작품이 없습니다."
        binding.rview.visibility = View.GONE

        if(platfrom == "Joara" || platfrom == "Joara_Nobless" || platfrom == "Joara_Premium"){
            getOthersJoa()
        }  else if (platfrom == "Naver_Today"  || platfrom == "Naver_Challenge") {
            getOtherNaverToday()
        }  else if (platfrom == "Ridi"){
            getOthersRidi()
        } else if (platfrom == "Toksoda"){
            getBooksToksoda()
        }

        adapterBestOthers?.setOnItemClickListener(object : AdapterBestOther.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterBestOthers?.getItem(position)

                if(item?.type == "MrBlue"){
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.mrblue.com${item.bookCode}")
                    )
                    requireContext().startActivity(intent)
                } else {

                    val bundle = Bundle()
                    bundle.putString("BEST_FROM", "Others")
                    firebaseAnalytics.logEvent("BEST_ActivityBestDetail", bundle)

                    crashlytics.setCustomKey("FragmentBestDetailBooks_BOOKCODE", item?.bookCode ?: "")
                    crashlytics.setCustomKey("FragmentBestDetailBooks_TITLE", item?.title ?: "")

                    val bookDetailIntent = Intent(requireContext(), ActivityBestDetail::class.java)
                    bookDetailIntent.putExtra("BookCode", item?.bookCode)
                    bookDetailIntent.putExtra("Type", item?.type)
                    bookDetailIntent.putExtra("POSITION", position)
                    bookDetailIntent.putExtra("HASDATA", true)
                    requireContext().startActivity(bookDetailIntent)
                }
            }
        })

        return view
    }

    private fun getOthersJoa() {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "25"

        apiJoara.getBookOtherJoa(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    if(data.bookLists != null){

                        if(data.bookLists.isNotEmpty()){
                            binding.blank.root.visibility = View.GONE
                            binding.rview.visibility = View.VISIBLE
                        }

                        for(i in data.bookLists.indices){
                            items.add(
                                BookListDataBest(
                                    data.bookLists[i].writerName,
                                    data.bookLists[i].subject,
                                    data.bookLists[i].bookImg.replace("http://", "https://"),
                                    data.bookLists[i].bookCode,
                                    data.bookLists[i].intro,
                                    "총 ${data.bookLists[i].cntChapter}x화",
                                    data.bookLists[i].cntPageRead,
                                    data.bookLists[i].cntFavorite,
                                    data.bookLists[i].cntRecom,
                                    "",
                                    0,
                                    "",
                                    platfrom
                                )
                            )
                        }
                        adapterBestOthers?.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun getOtherNaverToday(){
        Thread {
            val doc: Document = Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").get()
            val Naver: Elements = doc.select(".srch_cont .list_type2 li a")

            requireActivity().runOnUiThread {
                for (i in Naver.indices) {
                    items.add(
                        BookListDataBest(
                            Naver[i].select("strong").text(),
                            Naver[i].select("p .ellipsis").text(),
                            Naver.select("div img")[i].absUrl("src"),
                            Naver[i].absUrl("href"),
                            "작가 : ${Naver[0].select("strong").text()}",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            platfrom
                        )
                    )
                }

                if(items.isNotEmpty()){
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }

                adapterBestOthers?.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getOthersRidi() {
        Thread {

            val doc: Document = Jsoup.connect(bookWriter).get()

            val other = doc.select(".book_macro_landscape")

            requireActivity().runOnUiThread {
                for (i in other.indices) {

                    var img = other[i].select(".thumbnail").attr("data-src")

                    if(!img.contains("https://")){
                        img = "https:${img}"
                    }

                    val url = URL("https://ridibooks.com/books${other[i].select(".thumbnail_btn").attr("href")}")

                    items.add(
                        BookListDataBest(
                            other[i].select(".author_detail_link").text(),
                            other[i].select(".title_text").text(),
                            img,
                            url.path.replace("/books/books/",""),
                            other[i].select(".book_metadata_wrapper .meta_description").text(),
                            other[i].select(".genre").text(),
                            other[i].select(".StarRate_Score").text(),
                            other[i].select(".book_count .count_num").text(),
                            other[i].select(".StarRate_ParticipantCount").text(),
                            "",
                            0,
                            "",
                            platfrom
                        )
                    )
                }

                if(items.isNotEmpty()){
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }

                adapterBestOthers?.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getBooksToksoda() {
        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()

        param["srchwrd"] = (context as ActivityBestDetail).bookWriter
        param["keyword"] = (context as ActivityBestDetail).bookWriter
        param["pageSize"] = "20"
        param["pageIndex"] = "0"
        param["ageGrade"] = "0"
        param["lgctgrCd"] = ""
        param["mdctgrCd"] = ""
        param["searchType"] = "T"
        param["sortType"] = "W"
        param["prdtType"] = ""
        param["eventYn"] = "N"
        param["realSearchType"] = "N"
        param["_"] = "1657267049443"

        apiToksoda.getSearch(
            param,
            object : RetrofitDataListener<BestToksodaSearchResult> {
                override fun onSuccess(data: BestToksodaSearchResult) {

                    data.resultList.let {
                        if (it != null) {
                            if(it.isNotEmpty()){
                                binding.blank.root.visibility = View.GONE
                                binding.rview.visibility = View.VISIBLE
                            }

                            for (i in it.indices) {
                                items.add(
                                    BookListDataBest(
                                        it[i].AUTHOR,
                                        it[i].BOOK_NM,
                                        "https:${it[i].IMG_PATH}",
                                        it[i].BARCODE,
                                        it[i].INTRO,
                                        "장르 : ${it[i].LGCTGR_NM}",
                                        "유통사 : ${it[i].PUB_NM}",
                                        "키워드 : ${it[i].HASHTAG_NM}",
                                        "",
                                        "",
                                        0,
                                        "",
                                        platfrom
                                    )
                                )
                            }

                            if(items.isNotEmpty()){
                                binding.blank.root.visibility = View.GONE
                                binding.rview.visibility = View.VISIBLE
                            }

                        }
                        adapterBestOthers?.notifyDataSetChanged()
                    }
                }
            })
    }
}

class AdapterBestOther(items: List<BookListDataBest?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BookListDataBest?>? = items as ArrayList<BookListDataBest?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBestDetailOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder?.get(position)

            if (item != null) {
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .into(holder.binding.iView)

                with(holder.binding){

                    tviewTitle.text = item.title
                    tviewWriter.text = item.writer

                    if(item.type == "Joara" || item.type == "Joara_Nobless" || item.type == "Joara_Premium"){
                        val info2 = SpannableStringBuilder("쟝르 : ${item.info2}")
                        info2.applyingTextColor(
                            "쟝르 : ",
                            "#6E7686"
                        )

                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                        info3.applyingTextColor(
                            "조회 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("선호작 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder("추천 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "추천 수 : ",
                            "#6E7686"
                        )

                        tviewInfo.text = info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info4
                        tviewInfo3.text = info3
                        tviewIntro.text = item.info1
                    }  else if (item.type == "Naver_Today"  || item.type == "Naver_Challenge") {
                        tviewInfo.text = item.info2
                    }  else if (item.type == "Ridi"){

                        val info3 = SpannableStringBuilder("${item.info3}")
                        info3.applyingTextColor(
                            "점",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder(item.info4.replace("총", "총 :"))
                        info4.applyingTextColor(
                            "총 : ",
                            "#6E7686"
                        )
                        info4.applyingTextColor(
                            "권",
                            "#6E7686"
                        )
                        info4.applyingTextColor(
                            "화",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder("평가 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "평가 수 : ",
                            "#6E7686"
                        )
                        info5.applyingTextColor(
                            "명",
                            "#6E7686"
                        )

                        tviewInfo.text = item.info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info4
                        tviewInfo3.text = info3
                        tviewIntro.text = item.info1
                    } else if (item.type == "Toksoda"){
                        val info5 = SpannableStringBuilder("댓글 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "댓글 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("선호작 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info3 = SpannableStringBuilder("장르 : ${item.info3}")
                        info3.applyingTextColor(
                            "장르 : ",
                            "#6E7686"
                        )

                        tviewInfo.text = item.info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info4
                        tviewInfo3.text = info3
                        tviewIntro.text = item.info1
                    }

                    if (item.info1.isEmpty()) {
                        tviewIntro.visibility = View.GONE
                        tviewBar.visibility = View.GONE
                    } else {
                        tviewIntro.visibility = View.VISIBLE
                        tviewBar.visibility = View.VISIBLE
                    }

                    if (item.info2.isEmpty()) {
                        tviewInfo.visibility = View.GONE
                    } else {
                        tviewInfo.visibility = View.VISIBLE
                    }

                    if (item.info3.isEmpty()) {
                        tviewInfo3.visibility = View.GONE
                    } else {
                        tviewInfo3.visibility = View.VISIBLE
                    }

                    if (item.info4.isEmpty()) {
                        tviewInfo4.visibility = View.GONE
                    } else {
                        tviewInfo4.visibility = View.VISIBLE
                    }

                    if (item.info5.isEmpty()) {
                        tviewInfo5.visibility = View.GONE
                    } else {
                        tviewInfo5.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(it, pos)
                }
            }
        }
    }

    fun getItem(position: Int): BookListDataBest? {
        return holder?.get(position)
    }

}