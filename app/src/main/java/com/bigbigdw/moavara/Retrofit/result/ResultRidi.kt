package com.bigbigdw.moavara.Retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RidiBestResult {
    @SerializedName("pageProps")
    @Expose
    val pageProps: RidiPageProps? = null
}

class RidiPageProps {
    @SerializedName("dehydratedState")
    @Expose
    val dehydratedState: RidiDehydratedState? = null
}

class RidiDehydratedState {
    @SerializedName("queries")
    @Expose
    val queries: List<RidiQueries>? = null
}

class RidiQueries {
    @SerializedName("state")
    @Expose
    val state: RidiState? = null
}

class RidiState{
    @SerializedName("data")
    @Expose
    val data: RidiData? = null
}

class RidiData{
    @SerializedName("bestsellers")
    @Expose
    val bestsellers: RidiBestsellers? = null
}

class RidiBestsellers{
    @SerializedName("items")
    @Expose
    val items: List<RidiBestItems>? = null
}

class RidiBestItemsBooks{
    @SerializedName("authors")
    @Expose
    val authors: List<RidiBestItemsAuthors>? = null

    @SerializedName("categories")
    @Expose
    val categories : List<RidiBestItemsCategories>? = null

    @SerializedName("id")
    @Expose
    val id = ""

    @SerializedName("introduction")
    @Expose
    val introduction : RidiBestIntroduction? = null

    @SerializedName("ratings")
    @Expose
    val ratings : List<RidiBestItemsRating>? = null

    @SerializedName("series")
    @Expose
    val series : RidiBestItemSeries? = null
}

class RidiBestItems{
    @SerializedName("book")
    @Expose
    val book: RidiBestItemsBooks? = null

}

class RidiBestItemSeries{
    @SerializedName("title")
    @Expose
    val title = ""

    @SerializedName("totalEpisodeCount")
    @Expose
    val totalEpisodeCount = "0"

    @SerializedName("thumbnail")
    @Expose
    val thumbnail : RidiBestItemThumbnail? = null
}

class RidiBestItemThumbnail{
    @SerializedName("large")
    @Expose
    val large = ""
}

class RidiBestItemsRating{
    @SerializedName("count")
    @Expose
    val count = ""

    @SerializedName("rating")
    @Expose
    val rating = ""
}

class RidiBestIntroduction{
    @SerializedName("description")
    @Expose
    val description = ""
}

class RidiBestItemsCategories{
    @SerializedName("name")
    @Expose
    val name = ""
}

class RidiBestItemsAuthors{
    @SerializedName("id")
    @Expose
    val id = ""

    @SerializedName("name")
    @Expose
    val name = ""
}