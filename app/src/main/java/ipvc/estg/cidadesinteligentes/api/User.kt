package ipvc.estg.cidadesinteligentes.api

import java.util.*

data class nota(
    val id:Int,
    val id_tipo_nota:tipo_notas,
    val id_utilizador:utilizador,
    val desc:String,
    val local:String,
    val data:Date
)
data class tipo_notas(
    val id:Int,
    val nome_nota:String
)

data class utilizador(
    val id:Int,
    val nome:String,
    val telemovel:Int,
    val password:String
)