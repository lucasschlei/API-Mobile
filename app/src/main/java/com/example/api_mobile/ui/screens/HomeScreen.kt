package com.example.api_mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api_mobile.data.cart.CartItem
import com.example.api_mobile.data.cart.CartManager
import com.example.api_mobile.data.session.UserSession
import com.example.api_mobile.ui.components.AppBottomNavigation
import com.example.api_mobile.ui.components.AppTopBar
import com.example.api_mobile.ui.viewmodel.CartViewModel
import com.example.mercadodani.ui.theme.*

data class Category(val name: String, val dbName: String, val color: Color)
data class PromoProduct(val id: Int, val timeRange: String, val name: String, val description: String, val price: Double, val color: Color)

@Composable
fun LoginRequiredDialog(onLogin: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = White)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🔒", fontSize = 40.sp)
                Text("Faça login para continuar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkText, textAlign = TextAlign.Center)
                Text("Para adicionar produtos ao carrinho você precisa estar logado.", fontSize = 14.sp, color = MediumGray, textAlign = TextAlign.Center)
                Spacer(Modifier.height(4.dp))
                Button(onClick = onLogin, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("Fazer Login", fontSize = 15.sp, color = White, fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(24.dp)) {
                    Text("Cancelar", fontSize = 15.sp, color = DarkText)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onCategoryClick: (Category) -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    val categories = listOf(
        Category("Alim. Básicos", "Alim. Basicos", Color(0xFFD4A96A)),
        Category("Bebidas",       "Bebidas",        Color(0xFF4A90D9)),
        Category("Carnes",        "Carnes",         Color(0xFFB85C5C)),
        Category("Hortifruti",    "Hortifruti",     Color(0xFF6DBE6D)),
        Category("Laticínios",    "Laticinios",     Color(0xFFF0E68C)),
        Category("Limpeza",       "Limpeza",        Color(0xFF5BA3D4)),
        Category("Higiene",       "Higiene",        Color(0xFFDDA0DD)),
        Category("Padaria",       "Padaria",        Color(0xFFD4A96A)),
        Category("Congelados",    "Congelados",     Color(0xFF89B4D4)),
    )

    val promos = listOf(
        PromoProduct(11, "De 14h às 18h", "Coca-Cola",      "Garrafa 2L",  15.0, Color(0xFFCC3333)),
        PromoProduct(1,  "De 19h às 21h", "Arroz Tio João", "Pacote 1kg",  35.0, Color(0xFFDDDDDD)),
        PromoProduct(31, "De 19h às 21h", "Peito de Frango","1kg",         14.9, Color(0xFFB85C5C)),
    )

    val cartCount = CartManager.totalItems
    var showLoginDialog by remember { mutableStateOf(false) }

    if (showLoginDialog) {
        LoginRequiredDialog(onLogin = { showLoginDialog = false; onNavigateToLogin() }, onDismiss = { showLoginDialog = false })
    }

    Scaffold(
        topBar = {
            Box {
                AppTopBar()
                if (cartCount > 0) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 14.dp, top = 10.dp).size(18.dp).background(Color(0xFFCC3333), CircleShape), contentAlignment = Alignment.Center) {
                        Text("$cartCount", fontSize = 10.sp, color = White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        bottomBar = { AppBottomNavigation(selectedIndex = 1, onItemSelected = onBottomNavSelected) },
        containerColor = White
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())) {
            SectionTitle("Categorias")
            categories.chunked(3).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { cat -> CategoryCard(cat, Modifier.weight(1f)) { onCategoryClick(cat) } }
                    repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            Spacer(Modifier.height(16.dp))
            SectionTitle("Promo do Dia")
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                items(promos) { promo -> PromoCard(promo, cartViewModel = cartViewModel, onNeedLogin = { showLoginDialog = true }) }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Spacer(Modifier.height(4.dp))
        Box(Modifier.width(40.dp).height(3.dp).background(PrimaryBlue, RoundedCornerShape(2.dp)))
    }
}

@Composable
fun CategoryCard(category: Category, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(modifier = modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(10.dp)).background(category.color))
        Spacer(Modifier.height(4.dp))
        Text(category.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = DarkText, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun PromoCard(promo: PromoProduct, cartViewModel: CartViewModel = viewModel(), onNeedLogin: () -> Unit = {}) {
    var added by remember { mutableStateOf(false) }
    Column(modifier = Modifier.width(160.dp)) {
        Text(promo.timeRange, fontSize = 11.sp, color = MediumGray, modifier = Modifier.padding(bottom = 4.dp))
        Box(Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)).background(promo.color))
        Spacer(Modifier.height(6.dp))
        Text("R$ %.2f".format(promo.price).replace(".", ","), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text(promo.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkText)
        Text(promo.description, fontSize = 11.sp, color = MediumGray)
        Spacer(Modifier.height(6.dp))
        Button(
            onClick = {
                if (!UserSession.isLoggedIn) { onNeedLogin() }
                else {
                    cartViewModel.adicionarAoCarrinho(CartItem(promo.id, promo.name, promo.description, promo.price, promo.color))
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