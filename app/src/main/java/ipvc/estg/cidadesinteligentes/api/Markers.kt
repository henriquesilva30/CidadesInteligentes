package ipvc.estg.cidadesinteligentes.api

import java.util.*

data class Markers(
    val id:Int,
    val id_tipo_nota: Int,
    val id_utilizador: Int,
    val descricao:String,
    val local:String,
    val data: Date,
    val lat: String,
    val lng: String
)


