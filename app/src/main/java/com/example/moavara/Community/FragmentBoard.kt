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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Best.AdapterBestToday
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.R
import com.example.moavara.Retrofit.JoaraBoardResult
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.CommunityBoard
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentBestDetailTabsBinding
import com.example.moavara.databinding.ItemCommunityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentBoard :
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
        val param = Param.getItemAPI(context)
        param["board"] = "free"

        val call: Call<JoaraBoardResult> = RetrofitJoara.getBoardListJoa(param)

        call.enqueue(object : Callback<JoaraBoardResult?> {
            override fun onResponse(
                call: Call<JoaraBoardResult?>,
                response: Response<JoaraBoardResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->

                        if (it.status == "1" && it.boards != null) {
                            for (i in it.boards.indices) {
                                val date = it.boards[i].created
                                items.add(
                                    CommunityBoard(
                                        it.boards[i].title,
                                        it.boards[i].nid,
                                        date.substring(4, 6) + "." + date.substring(6, 8),
                                    )
                                )
                                adapterCommunity!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBoardResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapterCommunity?.setOnItemClickListener(object : AdapterCommunity.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: CommunityBoard? = adapterCommunity!!.getItem(position)
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://www.joara.com/freeboard/" + (item?.nid
                            ?: "")
                    )
                )
                startActivity(intent)
            }
        })

    }
}
