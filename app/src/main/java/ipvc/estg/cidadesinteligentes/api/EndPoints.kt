package ipvc.estg.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface EndPoints {

    @GET("api/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<user>

    @GET("api/users/")
    fun getUsers(): Call<List<user>>

    @GET("api/markers/")
    fun getPontos(): Call<List<nota>>

    @FormUrlEncoded
    @POST("api/login")
    fun postLog(@Field("telemovel") telemovel: String, @Field("password") password: String): Call<List<OutputPost>>

}