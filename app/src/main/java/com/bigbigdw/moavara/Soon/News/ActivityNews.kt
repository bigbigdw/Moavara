package com.bigbigdw.moavara.Soon.News

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.ActivitySearch
import com.bigbigdw.moavara.Search.NewsBX
import com.bigbigdw.moavara.User.ActivityUser
import com.bigbigdw.moavara.databinding.ActivityNewsBinding
import com.bigbigdw.moavara.databinding.ItemNewsBinding
import java.util.*

class ActivityNews : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: AdapterNews
    private val items = ArrayList<NewsBX?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = AdapterNews(items)

        binding.rviewNews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rviewNews.adapter = adapter

        for(i in 1..10){
            items.add(
                NewsBX(
                    "2022. 4. 12",
                    "웹소설 리뷰, 예능, 셀럽",
                    "네이버시리즈, 웹소설 입덕 웹예능 ‘쓰리덕즈’ 7일 시작",
                    "https://zdnet.co.kr/view/?no=20220406091403",
                    "웹소설 대중화를 위해 업계 최초로 셀럽과 웹소설 리뷰 웹예능",
                    "대기업 자본에 의해 웹소설이 양지로 끌어올려지고 있습니다.\n" +
                            "카카오가 옷소매 붉은 끝동으로 아주 핫한 시점에 준호를 모델로 슈퍼 웹툰 프로젝트 광고를 제작한 건 다들 아실텐데요! \n" +
                            "준호가 원작의 대사를 읊거나하는 상황이 독자들의 궁금증을 활활 불태운듯 합니다. 영상은 공개 1시간 만에 100만 뷰, 웹소설과 웹툰은 누적 조회수 1억뷰를 달성했습니다. \n" +
                            "이에 질 수 없다! 네이버시리즈 또한 웹소설 리뷰를 주제로 한 웹 예능을 기획했습니다. \n" +
                            "웹소설의 대중화를 위해 업계 최초로 셀럽을 통해 웹소설을 대놓고 홍보하는 예능 프로그램입니다. \n" +
                            "\n" +
                            "둘의 공통점은 셀럽과 거대한 자본을 사용했다는 것 외에도, 요즘 사람들이 익숙한 영상 매체를 활용한다는 점이 있습니다.\n" +
                            "조아라 유튜브 개설에 대한 이야기가 한 번 씩 있었는데요.(신입 지원자들 이력서에도) 여러가지 현실적으로 힘든 점은 있겠지만, 고민해봐야할 부분인 것 같습니다.",
                )
            )
            adapter.notifyDataSetChanged()
        }


        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            val intent = Intent(this, ActivityNewsWrite::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.ActivitySearch -> {
                val intent = Intent(this, ActivitySearch::class.java)
                startActivity(intent)
            }
            R.id.ActivityUser -> {
                val intent = Intent(this, ActivityUser::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

class AdapterNews(items: List<NewsBX?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<NewsBX?>? = items as ArrayList<NewsBX?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder!![position]

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): NewsBX? {
        return holder!![position]
    }

}