package com.example.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.BestComment
import com.example.moavara.Util.Param
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import com.example.moavara.databinding.ItemBestDetailCommentBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class FragmentBestDetailComment(private val platfrom: String, private val bookCode: String) :
    Fragment() {

    private var adapterBestComment: AdapterBestOther? = null
    private val items = ArrayList<BestComment?>()

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
        adapterBestComment = AdapterBestOther(platfrom , items)

        binding.rview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rview.adapter = adapterBestComment

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "댓글이 없습니다."
        binding.rview.visibility = View.GONE

        if (platfrom == "Joara" || platfrom == "Joara_Nobless" || platfrom == "Joara_Premium") {
            getCommentsJoara()
        } else if (platfrom == "Kakao") {
            getCommentsKakao()
        } else if (platfrom == "Kakao_Stage") {
            getCommentsKakaoStage()
        } else if (platfrom == "OneStore") {
            getCommentsOneStory()
        } else if (platfrom == "Munpia") {
            getCommentsMunpia()
        }  else if (platfrom == "Toksoda"){
            getCommentsToksoda()
        }


        return view
    }

    private fun getCommentsJoara() {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["page"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "20"

        apiJoara.getBookCommentJoa(
            param,
            object : RetrofitDataListener<JoaraBestDetailCommentsResult> {
                override fun onSuccess(data: JoaraBestDetailCommentsResult) {
                    if (data.status == "1" && data.comments != null) {

                        if(data.comments.isNotEmpty()){
                            binding.blank.root.visibility = View.GONE
                            binding.rview.visibility = View.VISIBLE
                        }

                        for (i in data.comments.indices) {
                            items.add(
                                BestComment(
                                    data.comments[i].comment,
                                    data.comments[i].created,
                                )
                            )
                        }
                        adapterBestComment?.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun getCommentsKakao() {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["seriesid"] = bookCode
        param["orderby"] = 0
        param["page"] = 0
        param["singleid"] = 0

        apiKakao.postKakaoBookDetailComment(
            param,
            object : RetrofitDataListener<BestKakaoBookDetailComment> {
                override fun onSuccess(data: BestKakaoBookDetailComment) {

                    if(data.comment_list.isNotEmpty()){
                        binding.blank.root.visibility = View.GONE
                        binding.rview.visibility = View.VISIBLE
                    }

                    data.comment_list.let {
                        for (i in it.indices) {
                            items.add(
                                BestComment(
                                    it[i].comment,
                                    it[i].create_dt,
                                )
                            )
                        }
                        adapterBestComment?.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun getCommentsKakaoStage() {
        val apiKakaoStage = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["size"] = 20
        param["sort"] = "cacheField.likeCount,desc"
        param["sort"] = "id,desc"
        param["page"] = 0

        apiKakaoStage.getBestKakaoStageDetailComment(
            bookCode,
            "20",
            "cacheField.likeCount,desc",
            "id,desc",
            "0",
            object : RetrofitDataListener<KakaoStageBestBookCommentResult> {
                override fun onSuccess(data: KakaoStageBestBookCommentResult) {

                    if(data.content.isNotEmpty()){
                        binding.blank.root.visibility = View.GONE
                        binding.rview.visibility = View.VISIBLE
                    }

                    data.content.let {
                        for (i in it.indices) {
                            items.add(
                                BestComment(
                                    it[i].message,
                                    it[i].createdAt,
                                )
                            )
                        }
                        adapterBestComment!!.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun getCommentsOneStory() {
        val apiOnestory = RetrofitOnestore()
        val param: MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["offset"] = "1"
        param["count"] = "1"
        param["orderBy"] = "recommend"

        apiOnestory.getOneStoryBookDetailComment(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetailComment> {
                override fun onSuccess(data: OnestoreBookDetailComment) {

                    if(data.params?.commentList?.isNotEmpty() == true){
                        binding.blank.root.visibility = View.GONE
                        binding.rview.visibility = View.VISIBLE
                    }

                    data.params?.commentList.let {
                        if (it != null) {
                            for (i in it.indices) {
                                items.add(
                                    BestComment(
                                        it[i].commentDscr,
                                        it[i].regDate,
                                    )
                                )
                            }
                        }
                        adapterBestComment!!.notifyDataSetChanged()
                    }
                }
            })
    }

    fun getCommentsMunpia() {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            val it = doc.select(".review .article-inner")

            requireActivity().runOnUiThread {
                for (i in it.indices) {
                    items.add(
                        BestComment(
                            doc.select(".review .article-inner").get(i).text(),
                            doc.select(".review .writer-area .date-de").get(1).text()
                                .replace("· ", ""),
                        )
                    )
                }
                adapterBestComment!!.notifyDataSetChanged()
            }
        }.start()
    }

    fun getCommentsToksoda(){
        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()
        param["brcd"] = bookCode
        param["pageFuncName"] = "goCommentPage"
        param["page"] = "1"
        param["orderType"] = "DATE"
        param["_"] = "1657265744730"

        apiToksoda.getBestDetailComment(
            param,
            object : RetrofitDataListener<BestToksodaDetailCommentResult> {
                override fun onSuccess(data: BestToksodaDetailCommentResult) {

                    data.result?.commentList.let {
                        if (it != null) {
                            for (i in it.indices) {
                                items.add(
                                    BestComment(
                                        it[i].cmntCntts,
                                        it[i].rgstDtime,
                                    )
                                )
                            }
                        }
                        adapterBestComment!!.notifyDataSetChanged()
                    }
                }
            })
    }

}

class AdapterBestOther(private var platfrom : String, items: List<BestComment?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BestComment?>? = items as ArrayList<BestComment?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemBestDetailCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder!![position]

            if (item != null) {
                Glide.with(holder.itemView.context)
                    .load(R.mipmap.ic_launcher)
                    .circleCrop()
                    .into(holder.binding.iView)

                holder.binding.tviewTitle.text = item.comment

                if(platfrom == "Munpia"){
                    holder.binding.tviewDate.text = item.date
                } else {
                    holder.binding.tviewDate.text =
                        item.date.substring(4, 6) + "." + item.date.substring(6, 8)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            with(binding){
                iView.background = GradientDrawable().apply {
                    cornerRadius = 100f.dpToPx()
                }

                llayoutBody.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#0D0D0D"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 28f.dpToPx()
                }

                llayoutWrap.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos)
                    }
                }
            }
        }
    }

    fun getItem(position: Int): BestComment? {
        return holder!![position]
    }

}