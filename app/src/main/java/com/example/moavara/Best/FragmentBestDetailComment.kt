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
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.BestComment
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import com.example.moavara.databinding.ItemBestDetailCommentBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap


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
        adapterBestComment = AdapterBestOther(items)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterBestComment

        Log.d("####", platfrom)

        if(platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium"){
            getCommentsJoara()
        } else if (platfrom == "Kakao"){
            getCommentsKakao()
        }


        return view
    }

    private fun getCommentsJoara() {
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["page"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "20"

        val call: Call<JoaraBestDetailCommentsResult> = RetrofitJoara.getBookCommentJoa(param)

        call.enqueue(object : Callback<JoaraBestDetailCommentsResult?> {
            override fun onResponse(
                call: Call<JoaraBestDetailCommentsResult?>,
                response: Response<JoaraBestDetailCommentsResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->

                        if(it.status == "1" && it.comments != null){
                            for(i in it.comments.indices){
                                items.add(
                                    BestComment(
                                        it.comments[i].comment,
                                        it.comments[i].created,
                                    )
                                )
                                adapterBestComment!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestDetailCommentsResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getCommentsKakao() {
        val apiKakao = RetrofitKaKao()
        val param : MutableMap<String?, Any> = HashMap()

        param["seriesid"] = bookCode
        param["orderby"] = 0
        param["page"] = 0
        param["singleid"] = 0

        apiKakao.postKakaoBookDetailComment(
            param,
            object : RetrofitDataListener<BestKakaoBookDetailComment> {
                override fun onSuccess(data: BestKakaoBookDetailComment) {

                    data.comment_list.let {
                        for(i in it.indices){
                            items.add(
                                BestComment(
                                    it[i].comment,
                                    it[i].create_dt,
                                )
                            )
                            adapterBestComment!!.notifyDataSetChanged()
                        }
                    }
                }
            })


        val call: Call<JoaraBestDetailCommentsResult> = RetrofitJoara.getBookCommentJoa(param)

        call.enqueue(object : Callback<JoaraBestDetailCommentsResult?> {
            override fun onResponse(
                call: Call<JoaraBestDetailCommentsResult?>,
                response: Response<JoaraBestDetailCommentsResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->

                        if(it.status == "1" && it.comments != null){
                            for(i in it.comments.indices){
                                items.add(
                                    BestComment(
                                        it.comments[i].comment,
                                        it.comments[i].created,
                                    )
                                )
                                adapterBestComment!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestDetailCommentsResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }
}

class AdapterBestOther(items: List<BestComment?>?) :
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
        val view = ItemBestDetailCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                holder.binding.tviewDate.text = item.date.substring(4, 6) + "." + item.date.substring(6, 8)
            }

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailCommentBinding) :
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

    fun getItem(position: Int): BestComment? {
        return holder!![position]
    }

}