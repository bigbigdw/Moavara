package com.example.moavara.Search

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentSearchBookcodeBinding
import org.jsoup.HttpStatusException
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

            if (platform == "Joara") {
                tviewSearch.text = "https://www.joara.com/book/1452405"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Kakao") {
                tviewSearch.text = "https://page.kakao.com/home?seriesId=57530778"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Kakao_Stage") {
                tviewSearch.text = "https://pagestage.kakao.com/novels/74312919"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Naver") {
                tviewSearch.text = "https://novel.naver.com/webnovel/list?novelId=252934"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Naver_Challenge") {
                tviewSearch.text = "https://novel.naver.com/challenge/list?novelId=75595"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Naver_Today") {
                tviewSearch.text = "https://novel.naver.com/best/list?novelId=268129"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Ridi") {
                tviewSearch.text = "https://ridibooks.com/books/425295076"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "OneStore") {
                tviewSearch.text = "https://onestory.co.kr/detail/H042820022"
                sview.inputType = InputType.TYPE_CLASS_TEXT
            } else if (platform == "Munpia") {
                tviewSearch.text = "https://novel.munpia.com/284801"
                sview.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (platform == "Toksoda") {
                tviewSearch.text =
                    "https://www.tocsoda.co.kr/product/productView?brcd=76M2207187389"
                sview.inputType = InputType.TYPE_CLASS_TEXT
            }

            binding.sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    bookCode = query.toString()

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
                    } else if (platform == "OneStore") {
                        setLayoutOneStore()
                    } else if (platform == "Toksoda") {
                        setLayoutToksoda()
                    }
                    return false

                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
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
                                .load(data.book.bookImg.replace("http://","https://"))
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = data.book.subject
                            searchResult.tviewWriter.text = data.book.writerName

                            searchResult.tviewInfo.text = "총 ${data.book.cntChapter}화"

                            searchResult.tviewInfo1.text =
                                BestRef.decimalToString(data.book.cntPageRead.toInt())
                            searchResult.tviewInfo2.text =
                                BestRef.decimalToString(data.book.cntFavorite.toInt())
                            searchResult.tviewInfo3.text = BestRef.decimalToString(data.book.cntRecom.toInt())
                            searchResult.tviewInfo4.text = BestRef.decimalToString(data.book.cntTotalComment.toInt())
                        } else {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
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

                        if(data.home != null){
                            Glide.with(requireContext())
                                .load("https://dn-img-page.kakao.com/download/resource?kid=${data.home.land_thumbnail_url}")
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = data.home.title
                            searchResult.tviewWriter.text = data.home.author_name

                            searchResult.tviewInfo.text = "총 ${data.home.open_counts}화"

                            searchResult.tviewInfo1.text = BestRef.decimalToString(data.home.page_rating_count.toInt())
                            searchResult.tviewInfo2.text = BestRef.decimalToString(data.home.page_rating_summary.replace(".0","").toInt())
                            searchResult.tviewInfo3.text = BestRef.decimalToString(data.home.read_count.toInt())
                            searchResult.tviewInfo4.text = BestRef.decimalToString(data.home.page_comment_count.toInt())
                        } else {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
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
                       var da = data.title


                        if(data != null){
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE

                            Glide.with(requireContext())
                                .load(data.thumbnail.url)
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = data.title
                            searchResult.tviewWriter.text = data.nickname.name

                            searchResult.tviewInfo.text = "총 ${data.publishedEpisodeCount}화"
                            searchResult.tviewInfo1.text = BestRef.decimalToString(data.favoriteCount.toInt())
                            searchResult.tviewInfo2.text = BestRef.decimalToString(data.viewCount.toInt())
                            searchResult.tviewInfo3.text = BestRef.decimalToString(data.visitorCount.toInt())
                            searchResult.tviewInfo4.text = BestRef.decimalToString(data.episodeLikeCount.toInt())
                        } else {
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE

                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }

            })
    }

    fun setLayoutNaverToday() {
        Thread {
            var doc: Document? = null

            try{
                when (platform) {
                    "Naver" -> {
                        doc = Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}")
                            .post()
                    }
                    "Naver_Today" -> {
                        doc = Jsoup.connect("https://novel.naver.com/best/list?novelId=${bookCode}").post()
                    }
                    "Naver_Challenge" -> {
                        doc = Jsoup.connect("https://novel.naver.com/challenge/list?novelId=${bookCode}")
                            .post()
                    }
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

                            searchResult.tviewInfo.text =
                                "장르 : ${doc.select(".info_book .genre").text()}"

                            searchResult.tviewInfo1.text = doc.select(".info_book .like").text().replace("관심", "").replace("명", "")
                            searchResult.tviewInfo2.text = doc.select(".grade_area em").text()

                            searchResult.iviewInfo3.setImageResource(R.mipmap.ic_launcher)
                            searchResult.tviewInfo3.text = doc.select(".info_book .download").text().replace("다운로드", "")

                            searchResult.llayoutTab4.visibility = View.GONE
                            searchResult.viewTab4.visibility = View.GONE

                            if(doc.select(".book_title").text().isEmpty()){
                                binding.blank.root.visibility = View.VISIBLE
                                binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                                binding.searchResult.root.visibility = View.GONE
                                binding.llayoutBtn.visibility = View.GONE
                            }
                        }
                    }

                }
            } catch (e : HttpStatusException){

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE

                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
                }
            }

        }.start()
    }

    fun setLayoutRidi() {
        Thread {
            try{
                val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

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

                        searchResult.tviewInfo.text =
                            doc.select(".header_info_wrap .info_category_wrap").text()

                        searchResult.tviewInfo1.text =
                            doc.select(".header_info_wrap .StarRate_Score").text()
                        searchResult.tviewInfo2.text =
                            doc.select(".header_info_wrap .StarRate_ParticipantCount").text()

                        searchResult.llayoutTab3.visibility = View.GONE
                        searchResult.llayoutTab4.visibility = View.GONE
                        searchResult.viewTab4.visibility = View.GONE
                        searchResult.viewTab3.visibility = View.GONE

                        searchResult.tviewInfo3.text =
                            doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

                    }
                }
            }catch (e : HttpStatusException){

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE

                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
                }
            }
        }.start()
    }

    fun setLayoutMunpia() {
        Thread {
            try{
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

                        searchResult.tviewInfo.text = doc.select(".meta-path strong").text()

                        searchResult.tviewInfo1.text =
                            doc.select(".meta-etc dd").next().next()[1].text()
                        searchResult.tviewInfo2.text =
                            doc.select(".meta-etc dd").next().next()[2].text()

                        searchResult.llayoutTab3.visibility = View.GONE
                        searchResult.llayoutTab4.visibility = View.GONE
                        searchResult.viewTab4.visibility = View.GONE
                        searchResult.viewTab3.visibility = View.GONE
                    }
                }
            }catch (e : HttpStatusException){

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE

                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
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

                            searchResult.tviewInfo.text = "장르 :  ${it?.lgctgrNm}"

                            searchResult.tviewInfo1.text = it?.inqrCnt
                            searchResult.tviewInfo2.text = it?.goodCnt
                            searchResult.tviewInfo3.text = it?.intrstCnt

                            searchResult.llayoutTab4.visibility = View.GONE
                            searchResult.viewTab4.visibility = View.GONE

                        }
                    }
                }
            })
    }

    fun setLayoutOneStore(){
        val apiOnestory = RetrofitOnestore()
        val param : MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getOneStoreDetail(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    with(binding){
                        data.params.let {
                            Glide.with(requireContext())
                                .load(it?.orgFilePos)
                                .into(searchResult.iviewBookCover)

                            searchResult.tviewTitle.text = it?.prodNm ?: ""
                            searchResult.tviewWriter.text = it?.artistNm

                            searchResult.tviewInfo.text = it?.menuNm

                            searchResult.tviewInfo1.text = it?.ratingAvgScore
                            searchResult.tviewInfo2.text = it?.favoriteCount
                            searchResult.tviewInfo3.text = it?.pageViewTotal
                            searchResult.tviewInfo4.text = it?.commentCount

                        }
                    }
                }
            })
    }
}