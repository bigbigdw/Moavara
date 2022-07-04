package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.Retrofit.JoaraBestListResult
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Param
import com.example.moavara.Util.calculateNum
import com.example.moavara.Util.miningValue
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import com.example.moavara.databinding.ItemBestDetailOtherBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

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

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterBestOthers

        if(platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium"){
            getOthersJoa()
        }  else if (platfrom == "Naver Today"  || platfrom == "Naver Challenge") {
            getCommentsNaverToday()
        }

        return view
    }

    private fun getOthersJoa() {
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "25"

        val call: Call<JoaraBestListResult> = RetrofitJoara.getBookOtherJoa(param)

        call.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->

                        if(it.bookLists != null){
                            for(i in it.bookLists.indices){
                                items.add(
                                    BookListDataBestToday(
                                        it.bookLists[i].writerName,
                                        it.bookLists[i].subject,
                                        it.bookLists[i].bookImg,
                                        it.bookLists[i].bookCode,
                                        "줄거리 : " + it.bookLists[i].intro,
                                        "총 " + it.bookLists[i].cntChapter + " 화",
                                        "조회 수 : " + it.bookLists[i].cntPageRead,
                                        "선호작 수 : " + it.bookLists[i].cntFavorite,
                                        "추천 수 : " + it.bookLists[i].cntRecom,
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
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
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
                    .circleCrop()
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