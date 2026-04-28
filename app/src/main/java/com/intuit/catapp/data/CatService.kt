package com.intuit.catapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ICatService {

    @GET("breeds")
    suspend fun getBreeds(@Query("page") page: Int, @Query("limit") limit: Int): List<Breed>
}

/**
 * Find out more information on the Cat API by checking out the documentation here:
 * https://documenter.getpostman.com/view/5578104/RWgqUxxh#intro
 */
object CatService {

    private lateinit var service: ICatService

    fun init() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ICatService::class.java)
    }

    fun getService(): ICatService {
        return service
    }
}
