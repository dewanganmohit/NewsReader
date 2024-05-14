package com.myapp.application.newsreader.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.application.newsreader.util.DateUtil
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Parcelable {
    val formattedArticleDatetime : String get() {
        publishedAt?.let {
            return DateUtil.changeDateFormat(publishedAt)
        }
        return ""
    }
}