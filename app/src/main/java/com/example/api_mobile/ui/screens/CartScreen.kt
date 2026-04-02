package com.example.api_mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api_mobile.data.cart.CartItem
import com.example.api_mobile.data.cart.CartManager
import com.example.api_mobile.data.session.UserSession
import com.example.api_mobile.ui.components.AppBottomNavigation
import com.example.api_mobile.ui.components.AppTopBar
import com.example.api_mobile.ui.viewmodel.CartViewModel
import com.example.api_mobile.ui.viewmodel.CheckoutState
import com.example.mercadodani.ui.theme.*

@Composable
fun CartScreen(
    onBottomNavSelected: (Int) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    val items = CartManager.items
    val total = CartManager.totalPrice
    val checkoutState = cartViewModel.checkoutState

    if (checkoutState is CheckoutState.Success) {
        AlertDialog(
            onDismissRequest = { cartViewModel.resetCheckout() },
            confirmButton = {
                Button(onClick = { cartViewModel.resetCheckout() }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("OK", color = White)
                }
            },
            title = { Text("Pedido confirmado! 🎉", fontWeight = FontWeight.Bold) },
            text  = { Text(checkoutState.message) }
        )
    }

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomNavigation(selectedIndex = 0, onItemSelected = onBottomNavSelected) },
        containerColor = White
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text("Carrinho", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

            when {
                !UserSession.isLoggedIn -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                            Text("🔒", fontSize = 48.sp)
                            Spacer(Modifier.height(12.dp))
                            Text("Faça login para ver seu carrinho", fontSize = 16.sp, color = MediumGray, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = onNavigateToLogin, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                                Text("Fazer Login", color = White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                items.isEmpty() -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🛒", fontSize = 48.sp)
                            Spacer(Modifier.height(12.dp))
                            Text("Seu carrinho está vazio", fontSize = 16.sp, color = MediumGray)
                        }
                    }
                }

                else -> {
                    if (checkoutState is CheckoutState.Error) {
                        Text(checkoutState.message, color = Color(0xFFCC3333), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items, key = { it.id }) { CartItemRow(it) }
                    }

                    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp, color = White) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total", fontSize = 16.sp, color = MediumGray)
                                Text("R$ %.2f".format(total).replace(".", ","), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
                            }
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { cartViewModel.finalizarPedido() },
                                enabled = checkoutState !is CheckoutState.Loading,
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape = RoundedCornerShape(26.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                            ) {
                                if (checkoutState is CheckoutState.Loading) {
                                    CircularProgressIndicator(color = White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("Finalizar Pedido", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(LightGray).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(item.color))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            Text(item.description, fontSize = 12.sp, color = MediumGray)
            Text("R$ %.2f".format(item.price).replace(".", ","), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { CartManager.removeItem(item.id) }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Clear, "Remover", Modifier.size(18.dp), tint = DarkText)
            }
            Text("${item.quantity}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.widthIn(min = 24.dp))
            IconButton(onClick = { CartManager.addItem(item) }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Add, "Adicionar", Modifier.size(18.dp), tint = DarkText)
            }
            IconButton(onClick = { CartManager.deleteItem(item.id) }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, "Deletar", Modifier.size(18.dp), tint = Color(0xFFCC3333))
            }
        }
    }
}