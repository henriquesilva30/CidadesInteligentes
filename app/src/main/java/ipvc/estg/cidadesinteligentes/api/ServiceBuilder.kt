package ipvc.estg.cidadesinteligentes.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()


    val retrofit = Retrofit.Builder()
        .baseUrl("http://king-size-diagnosis.000webhostapp.com/myslim/index.php/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()


    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
