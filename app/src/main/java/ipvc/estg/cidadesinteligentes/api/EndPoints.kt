package ipvc.estg.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface EndPoints {

    @GET("api/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @GET("api/users/")
    fun getUsers(): Call<List<User>>

    @GET("api/markers/")
    fun getPontos(): Call<List<Markers>>

    @FormUrlEncoded
    @POST("api/login")
    fun postLog(@Field("telemovel") telemovel: String, @Field("password") password: String): Call<List<User>>

}