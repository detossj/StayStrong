package com.deto.staystrong.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val sex: String?,
    val birth: String?,
    val weight: Float?,
    val height: Float?,
    val bio: String?,
    val ig: String?,
)