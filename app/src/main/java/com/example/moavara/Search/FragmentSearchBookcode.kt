package com.example.moavara.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moavara.Retrofit.JoaraBestDetailResult
import com.example.moavara.Retrofit.RetrofitDataListener
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentSearchBookcodeBinding

class FragmentSearchBookcode(private var platform: String) : Fragment() {

    private var _binding: FragmentSearchBookcodeBinding? = null
    private val binding get() = _binding!!
    var bookCode = ""
    lateinit var searchUnit: Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBookcodeBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding){

            Log.d("@@@@", platform)

            etextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    bookCode = s.toString()
                }
            })

            if(platform == "Joara"){
                tviewSearch.text = "https://www.joara.com/book/"
//                searchUnit = setLayoutJoara()
            }

            btnSearch.setOnClickListener {
                setLayoutJoara()
            }


        }

        return view
    }

    fun setLayoutJoara(){
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(requireContext())
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "0"

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(it: JoaraBestDetailResult) {

                    with(binding){
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE
                        if(it.status == "1" && it.book != null){
                            Glide.with(requireContext())
                                .load(it.book.bookImg)
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = it.book.subject
                            searchResult.tviewWriter.text = it.book.writerName

                            searchResult.tviewInfo1.text = "총 " + it.book.cntChapter + " 화"
                            searchResult.tviewInfo2.text =  "선호작 수 : " + it.book.cntFavorite
                            searchResult.tviewInfo3.text =  "조회 수 : " + it.book.cntPageRead
                            searchResult.tviewInfo4.text =  "추천 수 : " + it.book.cntRecom

                        }
                    }
                }
            })
    }




}