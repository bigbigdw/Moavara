package com.example.moavara.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DataEvent (
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var genre: String = "",
    var type: String = "",
    var memo: String = ""
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class BookListDataBestToday (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var number: Int = 0,
    var numberDiff: Int = 0,
    var date: String = "",
    var type: String = "",
    var status: String = "",
    var trophyCount: Int = 0,
    var memo: String = "",
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

data class BookListDataBest (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var number: Int = 0,
    var date: String = "",
    var type: String = "",
    var memo: String = "",
)

//class BookListDataBest(
//    var writer: String = "",
//    var title: String = "",
//    var bookImg: String = "",
//    var bookCode: String = "",
//    var info1: String = "",
//    var info2: String = "",
//    var info3: String = "",
//    var info4: String = "",
//    var info5: String = "",
//    var number: Int = 0,
//    var numberDiff: Int = 0,
//    var date: String = "",
//    var type: String = "",
//    var status: String = "",
//    var trophyCount: Int = 0,
//    var data: ArrayList<BookListDataBestAnalyze>? = null,
//    var memo: String = "",
//) : Parcelable {
//
//    constructor(parcel: Parcel) : this() {
//
//        writer = parcel.readString().toString()
//        title = parcel.readString().toString()
//        bookImg = parcel.readString().toString()
//        bookCode = parcel.readString().toString()
//        info1 = parcel.readString().toString()
//        info2 = parcel.readString().toString()
//        info3 = parcel.readString().toString()
//        info4 = parcel.readString().toString()
//        info5 = parcel.readString().toString()
//        number = parcel.readInt()
//        numberDiff = parcel.readInt()
//        date = parcel.readString().toString()
//        type = parcel.readString().toString()
//        status = parcel.readString().toString()
//        trophyCount = parcel.readInt()
//        data = parcel.readArrayList(BookListDataBestAnalyze::class.java.classLoader)  as ArrayList<BookListDataBestAnalyze>
//        memo = parcel.readString().toString()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(writer)
//        parcel.writeString(title)
//        parcel.writeString(bookImg)
//        parcel.writeString(bookCode)
//        parcel.writeString(info1)
//        parcel.writeString(info2)
//        parcel.writeString(info3)
//        parcel.writeString(info4)
//        parcel.writeString(info5)
//        parcel.writeInt(number)
//        parcel.writeInt(numberDiff)
//        parcel.writeString(info5)
//        parcel.writeString(type)
//        parcel.writeString(status)
//        parcel.writeInt(trophyCount)
//        parcel.writeInt(trophyCount)
//        parcel.writeList(data)
//        parcel.writeString(memo)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<BookListDataBest> {
//        override fun createFromParcel(parcel: Parcel): BookListDataBest {
//            return BookListDataBest(parcel)
//        }
//
//        override fun newArray(size: Int): Array<BookListDataBest?> {
//            return arrayOfNulls(size)
//        }
//    }
//}

data class BookListDataBestAnalyze (
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var number: Int = 0,
    var date: String = "",
    var numberDiff: Int = 0,
    var trophyCount: Int = 0,
)

data class TrophyInfo (
    var month: Int = 0,
    var week: Int = 0,
    var date: Int = 0,
)

@Entity
data class DataBestMonth (
    var writer: String ? = null,
    var title: String ? = null,
    var bookImg: String ? = null,
    var intro: String ? = null,
    var bookCode: String ? = null,
    var cntChapter: String ? = null,
    var cntPageRead: String ? = null,
    var cntFavorite: String ? = null,
    var cntRecom: String ? = null,
    var number: Int ? = null,
    var date: String ? = null,
    var type: String ? = null,
    var week: String ? = null,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}