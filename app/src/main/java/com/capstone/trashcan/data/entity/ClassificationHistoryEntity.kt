package com.capstone.trashcan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification_history")
data class ClassificationHistoryEntity(
    @field:ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "sub_category")
    val subCategory: String? = null,

    @field:ColumnInfo(name = "main_category")
    val mainCategory: String? = null,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

//    @field:ColumnInfo(name = "recommendations")
//    val recommendations: List<String?>? = null,

    @field:ColumnInfo(name = "recommendations")
    val recommendations: List<String?>? = null,

    @field:ColumnInfo(name = "date")
    val date: String? = null,

    @field:ColumnInfo(name = "photo")
    var photo: String? = null,
)
