package ipvc.estg.cidadesinteligentes.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("api/user/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @GET("api/users/")
    fun getUsers(): Call<List<User>>

    @GET("api/markers/")
    fun getPontos(): Call<List<Markers>>

    @GET("api/marker/{id}")
    fun getPonto(
        @Path("id") id: Int
    ): Call<Markers>

    @DELETE("api/marker/{id}")
    fun deletePonto(
        @Path("id") id: Int
    ): Call<Markers>

    @Multipart
    @POST("api/marker")
    fun addPonto(
        @Part("id_tipo_nota") id_tipo_nota: Int,
        @Part("id_utilizador") id_utilizador: Int?,
        @Part("descricao") descricao: String,
        @Part("lat") lat: Double?,
        @Part("lng") lng: Double?,
        @Part("img") img: String?
    ): Call<Markers>

    @FormUrlEncoded
    @POST("api/login")
    fun postLog(@Field("telemovel") telemovel: String, @Field("password") password: String): Call<List<User>>

}