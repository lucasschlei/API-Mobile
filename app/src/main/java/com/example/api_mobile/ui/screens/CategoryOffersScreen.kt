package com.example.api_mobile.ui.screens

import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mercadodani.data.cart.CartItem
import com.example.mercadodani.data.network.RetrofitClient
import com.example.mercadodani.data.session.UserSession
import com.example.mercadodani.ui.components.AppBottomNavigation
import com.example.mercadodani.ui.components.AppTopBar
import com.example.mercadodani.ui.theme.*
import com.example.mercadodani.ui.viewmodel.CartViewModel

fun categoryColor(categoria: String): Color = when (categoria) {
    "Alim. Basicos" -> Color(0xFFD4A96A)
    "Bebidas"       -> Color(0xFF4A90D9)
    "Carnes"        -> Color(0xFFB85C5C)
    "Hortifruti"    -> Color(0xFF6DBE6D)
    "Laticinios"    -> Color(0xFFF0E68C)
    "Limpeza"       -> Color(0xFF5BA3D4)
    "Higiene"       -> Color(0xFFDDA0DD)
    "Padaria"       -> Color(0xFFD4A96A)
    "Congelados"    -> Color(0xFF89B4D4)
    else            -> Color(0xFFCCCCCC)
}

@Composable
fun CategoryOffersScreen(
    categoryName: String = "Bebidas",
    dbCategoryName: String = categoryName,
    onBack: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    var products by remember { mutableStateOf<List<com.example.mercadodani.data.network.ProdutoResponseItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showLoginDialog by remember { mutableStateOf(false) }

    LaunchedEffect(dbCategoryName) {
        isLoading = true
        try {
            val response = RetrofitClient.api.listarPorCategoria(dbCategoryName)
            if (response.isSuccessful) {
                products = (response.body() ?: emptyList()).take(6)
            } else {
                errorMsg = "Erro ao carregar produtos"
            }
        } catch (e: Exception) {
            errorMsg = "Sem conexão com o servidor"
        }
        isLoading = false
    }

    if (showLoginDialog) {
        LoginRequiredDialog(
            onLogin   = { showLoginDialog = false; onNavigateToLogin() },
            onDismiss = { showLoginDialog = false }
        )
    }

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomNavigation(selectedIndex = 1, onItemSelected = onBottomNavSelected) },
        containerColor = White
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text = "Ofertas de $categoryName",
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                }
                errorMsg != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMsg!!, color = MediumGray, textAlign = TextAlign.Center)
                    }
                }
                products.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum produto encontrado", color = MediumGray)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            ApiProductCard(
                                product   = product,
                                color     = categoryColor(dbCategoryName),
                                cartViewModel = cartViewModel,
                                onNeedLogin   = { showLoginDialog = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApiProductCard(
    product: com.example.mercadodani.data.network.ProdutoResponseItem,
    color: Color,
    cartViewModel: CartViewModel = viewModel(),
    onNeedLogin: () -> Unit = {}
) {
    var added by remember { mutableStateOf(false) }
    Column {
        if (!product.imagemUrl.isNullOrBlank()) {
            AsyncImage(
                model = product.imagemUrl,
                contentDescription = product.nome,
                modifier = Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(12.dp)).background(color))
        }
        Spacer(Modifier.height(6.dp))
        Text("R$ %.2f".format(product.preco).replace(".", ","), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text(product.nome, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkText, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(product.descricao ?: "", fontSize = 11.sp, color = MediumGray)
        Spacer(Modifier.height(6.dp))
        Button(
            onClick = {
                if (!UserSession.isLoggedIn) { onNeedLogin() }
                else {
                    cartViewModel.adicionarAoCarrinho(
                        CartItem(product.id.toInt(), product.nome, product.descricao ?: "", product.preco, color)
                    )
                    added = true
                }
            },
            modifier = Modifier.fillMaxWidth().height(34.dp),
            shape = RoundedCornerShape(17.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (added) Color(0xFF22C55E) else PrimaryBlue)
        ) {
            Icon(Icons.Default.Add, null, Modifier.size(14.dp), tint = White)
            Spacer(Modifier.width(4.dp))
            Text(if (added) "Adicionado!" else "Adicionar", fontSize = 12.sp, color = White)
        }
    }
}