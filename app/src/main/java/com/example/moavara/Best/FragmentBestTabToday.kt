package com.example.moavara.Best

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.*
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestTabTodayBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class FragmentBestTabToday(private val platform: String, private val pickItems: ArrayList<String>) :
    Fragment(), BestTodayListener {

    private var adapterToday: AdapterBestToday? = null

    private val items = ArrayList<BookListDataBest>()
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()
    var status = ""
    lateinit var root: View
    private var _binding: FragmentBestTabTodayBinding? = null
    private val binding get() = _binding!!
    private var obj = JSONObject()

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var userDao: DBUser? = null
    var bestDao: DBBest? = null
    var bestDaoBookCode: DBBestBookCode? = null
    var UserInfo : DataBaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestTabTodayBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
            Log.d("####", "${UserInfo}")
        }

        bestDao = Room.databaseBuilder(
            requireContext(),
            DBBest::class.java,
            "Today_${platform}_${UserInfo?.Genre}"
        ).allowMainThreadQueries().build()

        bestDaoBookCode = Room.databaseBuilder(
            requireContext(),
            DBBestBookCode::class.java,
            "Today_${platform}_${UserInfo?.Genre}_BookCode"
        ).allowMainThreadQueries().build()

        adapterToday = AdapterBestToday(items, bookCodeItems)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "작품을 불러오는 중..."
        binding.rviewBest.visibility = View.GONE

        //TODO:
//        if(bestDao?.bestDao()?.getAll()?.size == 0){
//            getBookListToday()
//        } else {
//            readJsonList()
//        }

        getBookListToday()

        adapterToday?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday?.getItem(position)

                val bundle = Bundle()
                bundle.putString("test_test", "FragmentBestTabToday")
                bundle.putString("test_test1", "FragmentBestTabToday")
                bundle.putString("test_test2", "FragmentBestTabToday")
                bundle.putString("test_test3", "FragmentBestTabToday")
                bundle.putString("test_test4", "FragmentBestTabToday")
                firebaseAnalytics.logEvent("test2", bundle)

                if(platform == "MrBlue"){
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.mrblue.com/novel/${item?.bookCode}")
                    )
                    context?.startActivity(intent)
                } else {
                    val mBottomDialogBest = BottomDialogBest(
                        requireContext(),
                        item,
                        platform,
                        position
                    )

                    fragmentManager?.let { mBottomDialogBest.show(it, null) }
                }
            }
        })

        return view
    }

    private fun getBookListToday() {

        bestDao?.bestDao()?.initAll()

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${platform}_${UserInfo?.Genre}.json")
        if (file.exists()) {
            file.delete()
        }

        val jsonArray = JSONArray()

        BestRef.getBestDataToday(platform, UserInfo?.Genre ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    binding.blank.root.visibility = View.GONE
                    binding.rviewBest.visibility = View.VISIBLE

                    for (postSnapshot in dataSnapshot.children) {
                        val jsonObject = JSONObject() //배열 내에 들어갈 json

                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)

                        if (group != null) {

                            jsonObject.put("writer", group.writer)
                            jsonObject.put("title", group.title)
                            jsonObject.put("bookImg", group.bookImg)
                            jsonObject.put("bookCode", group.bookCode)
                            jsonObject.put("info1", group.info1)
                            jsonObject.put("info2", group.info2)
                            jsonObject.put("info3", group.info3)
                            jsonObject.put("info4", group.info4)
                            jsonObject.put("info5", group.info5)
                            jsonObject.put("number", group.number)
                            jsonObject.put("date", group.date)
                            jsonObject.put("type", group.type)
                            jsonObject.put("memo", group.memo)

                            items.add(BookListDataBest(
                                group.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.info6,
                                group.number,
                                group.date,
                                group.type,
                                group.memo
                            ))

                            jsonArray.put(jsonObject)

                            bestDao?.bestDao()?.insert(
                                RoomBookListDataBest(
                                group.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.info6,
                                group.number,
                                group.date,
                                group.type,
                                group.memo
                            )
                            )
                        }
                    }

                    obj.putOpt("items", jsonArray)

                    getBestTodayList(items, true)


                    adapterToday?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    override fun getBestTodayList(items: ArrayList<BookListDataBest>, status: Boolean) {

        bestDaoBookCode?.bestDaoBookCode()?.initAll()

        BestRef.getBookCode(platform, UserInfo?.Genre ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val jsonArray = JSONArray()

                try {
                    for (bookCodeList in items) {

                        val items = dataSnapshot.child(bookCodeList.bookCode)

                        if (items.childrenCount > 1) {

                            val jsonObject = JSONObject()

                            val bookCodes = ArrayList<BookListDataBestAnalyze>()

                            for(item in items.children){

                                val group: BookListDataBestAnalyze? = item.getValue(BookListDataBestAnalyze::class.java)

                                if (group != null) {
                                    bookCodes.add(
                                        BookListDataBestAnalyze(
                                            group.info1,
                                            group.info2,
                                            group.info3,
                                            group.info4,
                                            group.number,
                                            group.numInfo1,
                                            group.numInfo2,
                                            group.numInfo3,
                                            group.numInfo4,
                                            group.date,

                                        )
                                    )
                                }
                            }

                            val lastItem = bookCodes[bookCodes.size - 1]
                            val moreLastItem = bookCodes[bookCodes.size - 2]

                            bookCodeItems.add(
                                BookListDataBestAnalyze(
                                    lastItem.info1,
                                    lastItem.info2,
                                    lastItem.info3,
                                    lastItem.info4,
                                    lastItem.number,
                                    lastItem.numInfo1,
                                    lastItem.numInfo2,
                                    lastItem.numInfo3,
                                    lastItem.numInfo4,
                                    lastItem.date,
                                    moreLastItem.number - lastItem.number,
                                    bookCodes.size
                                )
                            )

                            bestDaoBookCode?.bestDaoBookCode()?.insert(
                                RoomBookListDataBestAnalyze(
                                    lastItem.info1,
                                    lastItem.info2,
                                    lastItem.info3,
                                    lastItem.info4,
                                    lastItem.number,
                                    lastItem.numInfo1,
                                    lastItem.numInfo2,
                                    lastItem.numInfo3,
                                    lastItem.numInfo4,
                                    lastItem.date,
                                    moreLastItem.number - lastItem.number,
                                    bookCodes.size
                                )
                            )

                            jsonObject.put("info1", lastItem.info1)
                            jsonObject.put("info2", lastItem.info2)
                            jsonObject.put("info3", lastItem.info3)
                            jsonObject.put("number", lastItem.number)
                            jsonObject.put("date", lastItem.date)
                            jsonObject.put("numberDiff", moreLastItem.number - lastItem.number)
                            jsonObject.put("trophyCount", bookCodes.size)

                            jsonArray.put(jsonObject)

                        } else if (items.childrenCount.toInt() == 1) {

                            val jsonObject = JSONObject()

                            val group: BookListDataBestAnalyze? =
                                dataSnapshot.child(bookCodeList.bookCode).child(DBDate.DateMMDD())
                                    .getValue(BookListDataBestAnalyze::class.java)

                            if (group != null) {
                                bookCodeItems.add(
                                    BookListDataBestAnalyze(
                                        group.info1,
                                        group.info2,
                                        group.info3,
                                        group.info4,
                                        group.number,
                                        group.numInfo1,
                                        group.numInfo2,
                                        group.numInfo3,
                                        group.numInfo4,
                                        group.date,
                                        0,
                                        1
                                    )
                                )

                                jsonObject.put("info1", group.info1)
                                jsonObject.put("info2", group.info2)
                                jsonObject.put("info3", group.info3)
                                jsonObject.put("number", group.number)
                                jsonObject.put("date", group.date)
                                jsonObject.put("numberDiff", 0)
                                jsonObject.put("trophyCount", 1)

                                jsonArray.put(jsonObject)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                writeJsonList(obj.putOpt("itemStatus", jsonArray))

                binding.blank.root.visibility = View.GONE
                binding.rviewBest.visibility = View.VISIBLE

                adapterToday?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun writeJsonList(obj: JSONObject) {

        File("/storage/self/primary/MOAVARA").mkdir()

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${platform}_${UserInfo?.Genre}.json")

        try {

            if (!file.exists()) {
                file.createNewFile()
            }

            val bw = BufferedWriter(FileWriter(file, true))
            bw.write(obj.toString())
            bw.newLine()
            bw.close()
        } catch (e: IOException) {
            Log.i("저장오류", e.message.toString())
        }
    }

    private fun readJsonList() {
        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${platform}_${UserInfo?.Genre}.json")
        try {
            val reader = BufferedReader(FileReader(file))

            val buffer = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                buffer.append(line).append("\n")
                line = reader.readLine()
            }

            val jsonData = buffer.toString()

            val jsonObject = JSONObject(jsonData)
            val itemsFlag = jsonObject.getJSONArray("items")
            val itemStatusFlag = jsonObject.getJSONArray("itemStatus")

            for (i in 0 until itemsFlag.length()) {
                val jo = itemsFlag.getJSONObject(i)
                items.add(BookListDataBest(
                    jo.optString("writer"),
                    jo.optString("title"),
                    jo.optString("bookImg"),
                    jo.optString("bookCode"),
                    jo.optString("info1"),
                    jo.optString("info2"),
                    jo.optString("info3"),
                    jo.optString("info4"),
                    jo.optString("info5"),
                    jo.optString("info6"),
                    jo.optInt("number"),
                    jo.optString("date"),
                    jo.optString("type"),
                    jo.optString("memo"),
                ))
            }

            for (i in 0 until itemStatusFlag.length()) {
                val jo = itemStatusFlag.getJSONObject(i)
                bookCodeItems.add(BookListDataBestAnalyze(
                    jo.optString("info1"),
                    jo.optString("info2"),
                    jo.optString("info3"),
                    jo.optString("info4"),
                    jo.optInt("number"),
                    jo.optInt("numInfo1"),
                    jo.optInt("numInfo2"),
                    jo.optInt("numInfo3"),
                    jo.optInt("numInfo4"),
                    jo.optString("date"),
                    jo.optInt("numberDiff"),
                    jo.optInt("trophyCount"),
                ))
            }

            reader.close()
            binding.blank.root.visibility = View.GONE
            binding.rviewBest.visibility = View.VISIBLE
            adapterToday?.notifyDataSetChanged()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            getBookListToday()
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }
}