package ipvc.estg.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("/users/")
    fun getUsers(): Call<List<nota>>

    @GET("/users/{id}")
    fun getUser(@Path("id")id:Int): Call<utilizador>



}