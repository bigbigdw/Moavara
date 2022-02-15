package com.example.moavara.ETC

interface API {
    companion object {
        const val BEST_BOOK_KAKAO = "/api/v2/store/day_of_week_top/list"
        const val BEST_BOOK_JOA = "v1/best/book.joa"
        const val EVENT_JOA = "v1/banner/home_banner.joa"
        const val EVENT_DETAIL_JOA = "v1/board/event_detail.joa"
        const val NOTICE_DETAIL_JOA = "v1/board/notice_detail.joa"

        const val USER_TOKEN_CHECK_JOA = "v1/user/token_check.joa"
        const val USER_AUTH_JOA = "v1/user/auth.joa"
        const val USER_DEAUTH_JOA = "v1/user/deauth.joa"
        const val INFO_INDEX_JOA = "api/info/index.joa"
    }
}