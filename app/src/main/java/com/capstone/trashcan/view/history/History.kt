package com.capstone.trashcan.view.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val result: String,
    val date: String,
    val photo: Int
): Parcelable