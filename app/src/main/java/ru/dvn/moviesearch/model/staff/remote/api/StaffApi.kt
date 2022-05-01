package ru.dvn.moviesearch.model.staff.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.remote.list.StaffDto

interface StaffApi {
    @GET("api/v1/staff")
    fun getStaff(
        @Header("X-API-KEY") key: String,
        @Query("filmId") filmId: Long
    ): Call<List<StaffDto>>

    @GET("/api/v1/staff/{id}")
    fun getStaffDetail(
        @Header("X-API-KEY") key: String,
        @Path("id") personId: Long
    ): Call<StaffDetailsDto>
}