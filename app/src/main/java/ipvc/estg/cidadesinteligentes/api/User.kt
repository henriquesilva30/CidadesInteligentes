package ipvc.estg.cidadesinteligentes.api

import java.util.*

data class user(
    val id:Int,
    val nome:String,
    val telemovel:Int,
    val password:String
)
data class tipo_nota(
    val id:Int,
    val nome_nota:String
)

data class nota(
    val id:Int,
    val id_tipo_nota:tipo_nota,
    val id_utilizador:user,
    val desc:String,
    val local:String,
    val data:Date
)