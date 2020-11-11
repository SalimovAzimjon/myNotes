package uz.azim.mynote.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "notes")
@Parcelize
data class Note(
    var title: String,
    var description: String,
    val createdDate: String,
    var isFinished:Boolean,
    val imageUrl: ArrayList<String>? = null,
    var updateDate: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) : Parcelable