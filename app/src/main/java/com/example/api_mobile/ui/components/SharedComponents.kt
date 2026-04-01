package com.example.api_mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.mercadodani.ui.theme.*

@Composable
fun LogoIcon() {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Box(modifier = Modifier.size(12.dp).background(Color(0xFF7B8CDE), RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.size(12.dp).background(Color(0xFF4A6CF7), RoundedCornerShape(2.dp)))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Box(modifier = Modifier.size(12.dp).background(Color(0xFF4A6CF7), RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.size(12.dp).background(Color(0xFF7B8CDE), RoundedCornerShape(2.dp)))
        }
    }
}

@Composable
fun AppTopBar(showLogo: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLogo) LogoIcon() else Spacer(Modifier.size(28.dp))
        Spacer(Modifier.size(40.dp))
    }
}

@Composable
fun AppBottomNavigation(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    data class NavItem(val icon: ImageVector, val label: String)

    val items = listOf(
        NavItem(Icons.Default.ShoppingCart, "Carrinho"),
        NavItem(Icons.Default.Home, "Home"),
        NavItem(Icons.Default.Person, "Perfil"),
    )

    NavigationBar(
        containerColor = BottomNavBg,
        tonalElevation = 0.dp,
        modifier = Modifier.height(64.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    unselectedIconColor = MediumGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun ImagePlaceholder(color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(color))
}
