package com.deto.staystrong.model

data class UserUpdateRequest(
    val name: String?,
    val sex: String?,
    val birth: String?,
    val weight: Float?,
    val height: Float?,
    val bio: String?,
    val ig: String?
)