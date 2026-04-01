package com.example.api_mobile.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class LoginRequest(val usuario: String, val senha: String)
data class LoginResponse(val sucesso: Boolean, val mensagem: String, val id: Int? = null, val nome: String? = null, val usuario: String? = null)
data class CadastroRequest(val nome: String, val email: String, val usuario: String, val senha: String)
data class CadastroResponse(val sucesso: Boolean, val mensagem: String)

data class AdicionarCarrinhoRequest(val usuarioId: Int, val produtoId: Int)
data class AtualizarCarrinhoRequest(val usuarioId: Int, val produtoId: Int, val quantidade: Int)
data class CarrinhoItemResponse(val carrinhoId: Int, val produtoId: Int, val nome: String, val descricao: String, val preco: Double, val quantidade: Int)
data class ApiResponse(val sucesso: Boolean, val mensagem: String)

data class CriarPedidoRequest(val usuarioId: Int)
data class VendaItemResponse(
    val produtoId: Long,
    val nomeProduto: String,
    val quantidade: Int,
    val precoUnit: Double
)
data class PedidoResponse(
    val id: Long? = null,
    val usuarioId: Long? = null,
    val nomeUsuario: String? = null,
    val itens: List<VendaItemResponse>? = null,
    val total: Double? = null,
    val dataVenda: String? = null,
    val statusVenda: String? = null
)

data class ProdutoResponseItem(
    val id: Long,
    val nome: String,
    val descricao: String?,
    val categoria: String,
    val preco: Double,
    val quantidadeEstoque: Int,
    val imagemUrl: String?,
    val ativo: Boolean
)

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body r: LoginRequest): Response<LoginResponse>

    @POST("usuarios/cadastro")
    suspend fun cadastro(@Body r: CadastroRequest): Response<CadastroResponse>

    @GET("carrinho/{usuarioId}")
    suspend fun listarCarrinho(@Path("usuarioId") uid: Int): Response<List<CarrinhoItemResponse>>

    @POST("carrinho/adicionar")
    suspend fun adicionarAoCarrinho(@Body r: AdicionarCarrinhoRequest): Response<ApiResponse>

    @PUT("carrinho/atualizar")
    suspend fun atualizarCarrinho(@Body r: AtualizarCarrinhoRequest): Response<ApiResponse>

    @DELETE("carrinho/remover/{carrinhoId}")
    suspend fun removerDoCarrinho(@Path("carrinhoId") id: Int): Response<ApiResponse>

    @POST("vendas/finalizar")
    suspend fun criarPedido(@Body r: CriarPedidoRequest): Response<PedidoResponse>

    @GET("produtos/categoria/{categoria}")
    suspend fun listarPorCategoria(@Path("categoria") categoria: String): Response<List<ProdutoResponseItem>>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081/"
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}