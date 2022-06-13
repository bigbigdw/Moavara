package com.example.moavara.News

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.Main.mRootRef
import com.example.moavara.Pick.ActivityPick
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.Search.NewsBX
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ActivityNewsBinding
import com.example.moavara.databinding.ActivityNewsWriteBinding
import com.example.moavara.databinding.ItemNewsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ActivityNewsWrite : AppCompatActivity() {

    private lateinit var binding: ActivityNewsWriteBinding
    var NewsBX = FirebaseDatabase.getInstance().reference.child("NewsBX")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        binding.llayoutBtn.setOnClickListener { view ->
            Log.d("###", "HIHI")

            NewsBX.child("2").setValue(
                NewsBX(
                    DBDate.DateMMDD(),
                    "",
                    "광주대학교 문예창작과 주최 제2회 전국 고교생 웹소설 공모전 시상식 성료",
                    "http://www.lecturernews.com/news/articleView.html?idxno=87513",
                    "예능에서도 그렇고 10대의 가능성! (예: 고등래퍼, 스걸파)이 주목을 받는다. 조아라에서도 새로운 공모전 주체를 10대로 잡아서.. 가볍게 한 번 해보는건 어떨까? 자유주제나.. 아니면 지금 청춘이니까.. 실감나는 청춘물을...\n" +
                            "아무튼 필력이 성인에 비해 어떨진 모르겠지만, 일단 예비작가들이면서 사용자들을 데려올 수 있다는게 큰 장점아닐까!",
                    "제 2회 전국 고교생 웹소설 공모전 시상식이 1월 17일 개최.\n" +
                            "광주대학교 x 뉴스페이퍼 키다리스튜디오 공동 주최\n" +
                            "장원 수상은 박찬주 학생의 `나를 죽인 악녀에게 빙의했다`"
                )
            )
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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