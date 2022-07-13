package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.*
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import com.example.moavara.databinding.ItemBestDetailOtherBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*

class FragmentBestDetailBooks(private val platfrom: String, private val bookCode: String) :
    Fragment() {

    private var adapterBestOthers: AdapterBestComment? = null
    private val items = ArrayList<BookListDataBestToday?>()

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
        adapterBestOthers = AdapterBestComment(items)

        binding.rview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rview.adapter = adapterBestOthers

        if(platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium"){
            getOthersJoa()
        }  else if (platfrom == "Naver Today"  || platfrom == "Naver Challenge") {
            getCommentsNaverToday()
        }  else if (platfrom == "Ridi"){
            getCommentsRidi()
        } else if (platfrom == "Toksoda"){
            getBooksToksoda()
        }

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
                        for(i in data.bookLists.indices){
                            items.add(
                                BookListDataBestToday(
                                    data.bookLists[i].writerName,
                                    data.bookLists[i].subject,
                                    data.bookLists[i].bookImg,
                                    data.bookLists[i].bookCode,
                                    "줄거리 : " + data.bookLists[i].intro,
                                    "총 " + data.bookLists[i].cntChapter + " 화",
                                    "조회 수 : " + data.bookLists[i].cntPageRead,
                                    "선호작 수 : " + data.bookLists[i].cntFavorite,
                                    "추천 수 : " + data.bookLists[i].cntRecom,
                                    0,
                                    0,
                                    "",
                                    ""
                                )
                            )
                        }
                        adapterBestOthers!!.notifyDataSetChanged()
                    }
                }
            })
    }

    fun getCommentsNaverToday(){
        Thread {
            val doc: Document = Jsoup.connect(bookCode).get()
            val Naver: Elements = doc.select(".srch_cont .list_type2 li a")

            requireActivity().runOnUiThread {
                for (i in Naver.indices) {
                    items.add(
                        BookListDataBestToday(
                            Naver[i].select("strong").text(),
                            Naver[i].select("p .ellipsis").text(),
                            Naver.select("div img")[i].absUrl("src"),
                            Naver[i].absUrl("href"),
                            "작가 : ${Naver[0].select("strong").text()}",
                            "",
                            "",
                            "",
                            "",
                            0,
                            0,
                            "",
                            ""
                        )
                    )
                }
                adapterBestOthers!!.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getCommentsRidi() {
        Thread {
            val doc: Document = Jsoup.connect(bookCode).get()
            val other = doc.select(".book_macro_landscape")

            requireActivity().runOnUiThread {
                for (i in other.indices) {
                    items.add(
                        BookListDataBestToday(
                            doc.select(".author_name_wrapper h4 .lang_kor").text(),
                            doc.select(".title_text").text(),
                            "https:${doc.select(".thumbnail").attr("data-src")}",
                            "https://ridibooks.com/books${doc.select(".thumbnail_btn").attr("href")}",
                            doc.select(".book_count .count_num").text(),
                            doc.select(".RSGBookMetadata_StarRate .StarRate_ParticipantCount").text(),
                            doc.select(".book_metadata_wrapper .meta_description").text(),
                            doc.select(".rental_price_info").text(),
                            doc.select(".buy_price_info").text(),
                            0,
                            0,
                            "",
                            ""
                        )
                    )
                }
                adapterBestOthers!!.notifyDataSetChanged()
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
                            for (i in it.indices) {
                                items.add(
                                    BookListDataBestToday(
                                        it[i].AUTHOR,
                                        it[i].BOOK_NM,
                                        "https:${it[i].IMG_PATH}",
                                        it[i].BARCODE,
                                        it[i].INTRO,
                                        "장르 : ${it[i].LGCTGR_NM}",
                                        "유통사 : ${it[i].PUB_NM}",
                                        "키워드 : ${it[i].HASHTAG_NM}",
                                        "",
                                        0,
                                        0,
                                        "",
                                        ""
                                    )
                                )
                            }

                        }
                        adapterBestOthers!!.notifyDataSetChanged()
                    }
                }
            })
    }
}

class AdapterBestComment(items: List<BookListDataBestToday?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BookListDataBestToday?>? = items as ArrayList<BookListDataBestToday?>?

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

            val item = this.holder!![position]

            if (item != null) {
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .into(holder.binding.iview)

                holder.binding.tviewTitle.text = item.title
                holder.binding.tviewInfo1.text = item.info2
                holder.binding.tviewInfo2.text = item.info3
                holder.binding.tviewIntro.text = item.info1
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

    fun getItem(position: Int): BookListDataBestToday? {
        return holder!![position]
    }

}