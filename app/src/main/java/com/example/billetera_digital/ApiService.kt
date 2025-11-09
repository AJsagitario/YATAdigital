package com.example.billetera_digital

import com.example.billetera_digital.model.request
import com.example.billetera_digital.model.TransferenciaRequest
import com.example.billetera_digital.model.TransferenciaResponse
import com.example.billetera_digital.model.UsuarioDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT



interface ApiService {
    @GET("api/auth/login/contacto/{numero}/{pin}")
    fun loginPorContactoYPin(
        @Path("numero") numero: String,
        @Path("pin") pin: String
    ): Call<UsuarioDto>

    // registro
    @POST("api/usuarios")
    fun crearUsuario(
        @Body body: request
    ): Call<UsuarioDto>

    @PUT("api/usuarios/transferir")
    fun transferir(
        @Body req: TransferenciaRequest
    ): retrofit2.Call<TransferenciaResponse>

    @GET("api/usuarios")
    fun listarUsuarios(): Call<List<UsuarioDto>>
}