package com.l1xly.notes.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.l1xly.notes.utils.getCurrentTime
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var lastDateEdit: Long = getCurrentTime()
) : Parcelable