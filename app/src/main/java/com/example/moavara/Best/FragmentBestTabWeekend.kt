package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.synnapps.carouselview.ViewListener
import org.json.JSONObject

class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    lateinit var root: View
    var genre = ""

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    var arrayCarousel: MutableList<JSONObject> = java.util.ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        return view
    }


    fun initCarousel(){
        with(binding){
            carousel.removeAllViews()
            arrayCarousel = ArrayList()
        }
    }

    private val viewListenerBest =
        ViewListener { position ->
            val customView: View = layoutInflater.inflate(R.layout.item_best_weekend_carousel, null)

            val image: ImageView = customView.findViewById(R.id.Img_BookBest)
            val bestRankImage: ImageView = customView.findViewById(R.id.BestRankImg)
            val title: TextView = customView.findViewById(R.id.Text_Title_Best)
            val writer: TextView = customView.findViewById(R.id.Text_Writer_Best)
            val intro: TextView = customView.findViewById(R.id.Text_Intro_Best)
            val bestViewCount: TextView = customView.findViewById(R.id.Text_BestViewed)
            val bestFav: TextView = customView.findViewById(R.id.Text_BestFav)
            val bestRecommend: TextView = customView.findViewById(R.id.Text_BestRecommend)
            val bookCode: TextView = customView.findViewById(R.id.BookCodeText)
            val topText: TextView = customView.findViewById(R.id.TopText)
            val category: TextView = customView.findViewById(R.id.Category)
            val textCntChapter: TextView = customView.findViewById(R.id.Text_CntChapter)
            val bookLabel: LinearLayout = customView.findViewById(R.id.BookLabel)

            Glide.with(requireContext()).load(arrayCarousel[position].getString("bookImg"))
                .into(image)

            when (position) {
                0 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_1)
                }
                1 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_2)
                }
                2 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_3)
                }
                3 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_4)
                }
                4 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_5)
                }
                5 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_6)
                }
                6 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_7)
                }
                7 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_8)
                }
                8 -> {
                    bestRankImage.setImageResource(R.drawable.icon_best_9)
                }
                else -> {
                    Log.d("bestRankImage","NO_IMAGE")
                }
            }

            title.text = arrayCarousel[position].getString("subject")
            writer.text = arrayCarousel[position].getString("writerName")
            intro.text = arrayCarousel[position].getString("intro")
            bestViewCount.text = arrayCarousel[position].getString("cntPageRead")
            bestFav.text = arrayCarousel[position].getString("cntFavorite")
            bestRecommend.text = arrayCarousel[position].getString("cntRecom")
            bookCode.text = arrayCarousel[position].getString("bookCode")
            category.text = arrayCarousel[position].getString("categoryKoName")
            textCntChapter.text = arrayCarousel[position].getString("cntChapter")

            if (arrayCarousel[position].getString("isNobless") == "TRUE" && arrayCarousel[position].getString("isAdult") == "FALSE") {
               Log.d("!!!!", "1")
            } else if (arrayCarousel[position].getString("isPremium") == "TRUE" && arrayCarousel[position].getString("isAdult") == "FALSE") {
                Log.d("!!!!", "2")
            } else if (arrayCarousel[position].getString("isFinish") == "TRUE" && arrayCarousel[position].getString("isAdult") == "FALSE") {
                Log.d("!!!!", "3")
            } else if (arrayCarousel[position].getString("isNobless") == "TRUE" && arrayCarousel[position].getString("isAdult") == "TRUE") {
                Log.d("!!!!", "4")
            } else if (arrayCarousel[position].getString("isPremium") == "TRUE" && arrayCarousel[position].getString("isAdult") == "TRUE") {
                Log.d("!!!!", "5")
            } else if (arrayCarousel[position].getString("isFinish") == "TRUE" && arrayCarousel[position].getString("isAdult") == "TRUE") {
                Log.d("!!!!", "6")
            }


            binding.carousel.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            customView
        }
}