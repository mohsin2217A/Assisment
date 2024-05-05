package com.app.assisment.data.remote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.assisment.common.Constants.UNIVERSITY_TABLE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = UNIVERSITY_TABLE)
@Parcelize
data class UniversityResponseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("alpha_two_code")
    val countryCode: String?,
    val country: String?,
    val name: String?,
    @SerializedName("state-province")
    val state: String?,
    @SerializedName("web_pages")
    val webPages: List<String>?
) : Parcelable