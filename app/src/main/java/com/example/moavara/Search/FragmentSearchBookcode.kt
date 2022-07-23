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
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentSearchBookcodeBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class FragmentSearchBookcode(private var platform: String = "Joara") : Fragment() {

    private var _binding: FragmentSearchBookcodeBinding? = null
    private val binding get() = _binding!!
    var bookCode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBookcodeBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {

            etextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    bookCode = s.toString()
                }
            })

            if (platform == "Joara") {
                tviewSearch.text = "https://www.joara.com/book/1452405"
            } else if (platform == "Kakao") {
                tviewSearch.text = "https://page.kakao.com/home?seriesId=57530778"
            } else if (platform == "Kakao_Stage") {
                tviewSearch.text = "https://page.kakao.com/home?seriesId=49902709"
            } else if (platform == "Naver") {
                tviewSearch.text = "https://novel.naver.com/webnovel/list?novelId=252934"
            } else if (platform == "Naver_Challenge") {
                tviewSearch.text = "https://novel.naver.com/challenge/list?novelId=75595"
            } else if (platform == "Naver_Today") {
                tviewSearch.text = "https://novel.naver.com/best/list?novelId=268129"
            } else if (platform == "Ridi") {
                tviewSearch.text = "https://ridibooks.com/books/425295076"
            } else if (platform == "Munpia") {
                tviewSearch.text = "https://novel.munpia.com/284801"
            } else if (platform == "Toksoda") {
                tviewSearch.text =
                    "https://www.tocsoda.co.kr/product/productView?brcd=76M2207187389"
            }

            btnSearch.setOnClickListener {
                if (platform == "Joara") {
                    setLayoutJoara()
                } else if (platform == "Kakao") {
                    setLayoutKaKao()
                } else if (platform == "Kakao_Stage") {
                    setLayoutKaKaoStage()
                } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
                    setLayoutNaverToday()
                } else if (platform == "Ridi") {
                    setLayoutRidi()
                } else if (platform == "Munpia") {
                    setLayoutMunpia()
                } else if (platform == "Toksoda") {
                    setLayoutToksoda()
                }
            }
        }

        return view
    }

    fun setLayoutJoara() {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(requireContext())
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "0"

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    with(binding) {
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        if (data.status == "1" && data.book != null) {
                            Glide.with(requireContext())
                                .load(data.book.bookImg)
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = data.book.subject
                            searchResult.tviewWriter.text = data.book.writerName

                            searchResult.tviewInfo1.text = "총 " + data.book.cntChapter + " 화"
                            searchResult.tviewInfo2.text = "선호작 수 : " + data.book.cntFavorite
                            searchResult.tviewInfo3.text = "조회 수 : " + data.book.cntPageRead
                            searchResult.tviewInfo4.text = "추천 수 : " + data.book.cntRecom

                        }
                    }
                }
            })
    }

    fun setLayoutKaKao() {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        param["seriesid"] = bookCode

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {

                    with(binding) {
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        data.home?.let { it ->
                            Glide.with(requireContext())
                                .load("https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}")
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = it.title
                            searchResult.tviewWriter.text = it.author_name

                            searchResult.tviewInfo1.text = "총 ${it.open_counts}화"
                            searchResult.tviewInfo2.text = "장르 : ${it.sub_category}"
                            searchResult.tviewInfo3.text = "조회 수 : ${it.read_count}"
                            searchResult.tviewInfo4.text = "댓글 수 : ${it.page_comment_count}"

                        }
                    }
                }
            })
    }

    fun setLayoutKaKaoStage() {
        val apiKakaoStage = RetrofitKaKao()

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    with(binding) {
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        data.let {
                            Glide.with(requireContext())
                                .load(data.thumbnail.url)
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = it.title
                            searchResult.tviewWriter.text = it.nickname.name

                            searchResult.tviewInfo1.text = "총 ${it.publishedEpisodeCount}화"
                            searchResult.tviewInfo2.text = "선호작 수 : ${it.favoriteCount}"
                            searchResult.tviewInfo3.text = "조회 수 : ${it.viewCount}"
                            searchResult.tviewInfo4.text = "방문 수 : ${it.visitorCount}"
                        }
                    }
                }
            })
    }

    fun setLayoutNaverToday() {
        Thread {
            var doc: Document? = null

            if (platform == "Naver") {
                doc = Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}")
                    .post()
            } else if (platform == "Naver_Today") {
                doc = Jsoup.connect("https://novel.naver.com/best/list?novelId=${bookCode}").post()
            } else if (platform == "Naver_Challenge") {
                doc = Jsoup.connect("https://novel.naver.com/challenge/list?novelId=${bookCode}")
                    .post()
            }

            requireActivity().runOnUiThread {
                with(binding) {
                    llayoutSearch.visibility = View.GONE
                    llayoutResult.visibility = View.VISIBLE

                    if (doc != null) {
                        Glide.with(requireContext())
                            .load(doc.select(".section_area_info .pic img").attr("src"))
                            .into(searchResult.iviewBookCover)

                        searchResult.tviewTitle.text = doc.select(".book_title").text()
                        searchResult.tviewWriter.text = doc.select(".writer").text()

                        searchResult.tviewInfo1.text = doc.select(".info_book .like").text()
                        searchResult.tviewInfo2.text = doc.select(".info_book .download").text()
                        searchResult.tviewInfo3.text = doc.select(".info_book .publish").text()
                        searchResult.tviewInfo4.text =
                            "장르 : ${doc.select(".info_book .genre").text()}"
                    }
                }

            }
        }.start()
    }

    fun setLayoutRidi() {
        Thread {
            val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

            Log.d("####", "https://ridibooks.com/books/${bookCode}")

            requireActivity().runOnUiThread {
                with(binding) {
                    llayoutSearch.visibility = View.GONE
                    llayoutResult.visibility = View.VISIBLE

                    Glide.with(requireContext())
                        .load("https:${doc.select(".thumbnail_image img").attr("src")}")
                        .into(searchResult.iviewBookCover)

                    searchResult.tviewTitle.text =
                        doc.select(".header_info_wrap .info_title_wrap h3").text()
                    searchResult.tviewWriter.text =
                        doc.select(".metadata_writer .author_detail_link").text()

                    searchResult.tviewInfo1.text =
                        doc.select(".header_info_wrap .info_category_wrap").text()
                    searchResult.tviewInfo2.text =
                        "평점 : ${doc.select(".header_info_wrap .StarRate_Score").text()}"
                    searchResult.tviewInfo3.text =
                        doc.select(".header_info_wrap .StarRate_ParticipantCount").text()
                    searchResult.tviewInfo4.text =
                        doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

                }
            }
        }.start()
    }

    fun setLayoutMunpia() {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            requireActivity().runOnUiThread {
                with(binding) {
                    llayoutSearch.visibility = View.GONE
                    llayoutResult.visibility = View.VISIBLE

                    Glide.with(requireContext())
                        .load("https:${doc.select(".cover-box img").attr("src")}")
                        .into(searchResult.iviewBookCover)

                    searchResult.tviewTitle.text = doc.select(".detail-box h2 a").text()
                        .replace(doc.select(".detail-box h2 a span").text() + " ", "")
                    searchResult.tviewWriter.text = doc.select(".member-trigger strong").text()

                    searchResult.tviewInfo1.text = doc.select(".meta-path strong").text()
                    searchResult.tviewInfo2.text =
                        "조회 수 : ${doc.select(".meta-etc dd").next().next().get(1).text()}"
                    searchResult.tviewInfo3.text =
                        "추천 수 : ${doc.select(".meta-etc dd").next().next().get(2).text()}"
                    searchResult.tviewInfo4.text = doc.select(".meta-etc dt").next().get(2).text()

                }
            }
        }.start()
    }

    fun setLayoutToksoda() {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["brcd"] = bookCode
        param["_"] = "1657265744728"

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    with(binding) {
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        data.result.let {
                            Glide.with(requireContext())
                                .load("https:${it?.imgPath}")
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = it?.wrknm ?: ""
                            searchResult.tviewWriter.text = it?.athrnm ?: ""

                            searchResult.tviewInfo1.text = "장르 :  ${it?.lgctgrNm}"
                            searchResult.tviewInfo2.text = "구독 수 : ${it?.inqrCnt}"
                            searchResult.tviewInfo3.text = "관심 : ${it?.intrstCnt}"
                            searchResult.tviewInfo4.text = "선호작 수 : ${it?.goodCnt}"

                        }
                    }
                }
            })
    }
}