package com.deto.staystrong.data.remote.services

import com.deto.staystrong.model.LoginRequest
import com.deto.staystrong.model.LoginResponse
import com.deto.staystrong.model.RegisterRequest
import com.deto.staystrong.model.RegisterResponse
import com.deto.staystrong.model.UpdateProfileResponse
import com.deto.staystrong.model.User
import com.deto.staystrong.model.UserUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("api/profile")
    suspend fun getUser(): User

    @POST("api/logout")
    suspend fun logout( @Header("Authorization") token: String)

    @PATCH("api/profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body updatedUser: UserUpdateRequest): UpdateProfileResponse
}



