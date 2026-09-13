package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald

@Composable
fun CartScreen(
    cartItems: List<CartItemEntity>,
    appliedCoupon: CouponEntity?,
    couponMessage: String?,
    isCheckingOut: Boolean,
    lastCreatedOrder: OrderEntity?,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onApplyCoupon: (String, Double) -> Unit,
    onRemoveCoupon: () -> Unit,
    onExecuteCheckout: (List<CartItemEntity>, Double, Double, Double, Double, String) -> Unit,
    onGoToPurchases: () -> Unit,
    modifier: Modifier = Modifier
) {
    var couponInput by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe / Paytm)") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val discount = if (appliedCoupon != null) {
        if (appliedCoupon.discountPercentage > 0) {
            (subtotal * appliedCoupon.discountPercentage) / 100.0
        } else {
            appliedCoupon.flatDiscount.coerceAtMost(subtotal)
        }
    } else 0.0

    val taxable = (subtotal - discount).coerceAtLeast(0.0)
    val tax = taxable * 0.18 // 18% GST standard in tech services/digital
    val total = taxable + tax

    // Show order success dialog if order is ready
    if (showSuccessDialog && lastCreatedOrder != null) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TechEmerald)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Payment Successful!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Thank you for trusting Growth Up Tech. Your order has been verified and processed securely.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Order ID: ${lastCreatedOrder.orderId}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Txn: ${lastCreatedOrder.transactionId}", fontSize = 11.sp, color = TechCyan, fontFamily = FontFamily.Monospace)
                            Text(text = "Amount: ₹${lastCreatedOrder.totalAmount.toInt()}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text(text = "Status: VERIFIED & ACTIVE", fontWeight = FontWeight.Bold, color = TechEmerald, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "All purchased courses and digital templates are unlocked and ready in your account.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onGoToPurchases()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                    modifier = Modifier.testTag("dialog_access_purchases_btn")
                ) {
                    Text("Access Courses & Downloads")
                }
            }
        )
    }

    if (cartItems.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Explore our courses, templates and developer kits to get started.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Shopping Cart (${cartItems.size} items)",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Cart items
        items(cartItems) { item ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("cart_item_${item.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TechBadge(text = item.itemType, color = if (item.itemType == "COURSE") TechCyan else TechEmerald)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2
                        )
                        Text(
                            text = "₹${item.price.toInt()} each",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onRemoveItem(item.id) },
                            modifier = Modifier.testTag("remove_cart_item_${item.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFFF5F56))
                        }
                    }
                }
            }
        }

        // Coupon Section
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Apply Coupon Code",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = couponInput,
                            onValueChange = { couponInput = it },
                            placeholder = { Text("GROWTH50 or WELCOME20") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("coupon_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (couponInput.isNotBlank()) {
                                    onApplyCoupon(couponInput, subtotal)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                            modifier = Modifier.testTag("apply_coupon_btn")
                        ) {
                            Text("Apply")
                        }
                    }

                    if (couponMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = couponMessage,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (appliedCoupon != null) TechEmerald else Color(0xFFFF5F56),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    if (appliedCoupon != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap here to remove coupon",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.clickable { onRemoveCoupon() }
                        )
                    }
                }
            }
        }

        // Payment Method Selection
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    PaymentOptionRow(
                        title = "UPI (Google Pay / PhonePe / Paytm)",
                        icon = Icons.Default.QrCode,
                        selected = selectedPaymentMethod.startsWith("UPI"),
                        onSelect = { selectedPaymentMethod = "UPI (Google Pay / PhonePe / Paytm)" }
                    )

                    PaymentOptionRow(
                        title = "Debit / Credit Card (Visa, RuPay, MC)",
                        icon = Icons.Default.CreditCard,
                        selected = selectedPaymentMethod.startsWith("Debit"),
                        onSelect = { selectedPaymentMethod = "Debit / Credit Card (Visa, RuPay, MC)" }
                    )

                    PaymentOptionRow(
                        title = "Net Banking (SBI, HDFC, ICICI, Axis)",
                        icon = Icons.Default.AccountBalance,
                        selected = selectedPaymentMethod.startsWith("Net"),
                        onSelect = { selectedPaymentMethod = "Net Banking (SBI, HDFC, ICICI, Axis)" }
                    )
                }
            }
        }

        // Order Summary
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Subtotal", style = MaterialTheme.typography.bodySmall)
                        Text(text = "₹${subtotal.toInt()}", style = MaterialTheme.typography.bodySmall)
                    }

                    if (discount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Coupon Discount (${appliedCoupon?.code})", style = MaterialTheme.typography.bodySmall.copy(color = TechEmerald))
                            Text(text = "-₹${discount.toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = TechEmerald, fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "GST (18%)", style = MaterialTheme.typography.bodySmall)
                        Text(text = "₹${tax.toInt()}", style = MaterialTheme.typography.bodySmall)
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Total Payable",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "₹${total.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = TechEmerald
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onExecuteCheckout(cartItems, subtotal, discount, tax, total, selectedPaymentMethod)
                            showSuccessDialog = true
                        },
                        enabled = !isCheckingOut,
                        colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("pay_securely_btn")
                    ) {
                        if (isCheckingOut) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Securing Transaction...")
                        } else {
                            Icon(Icons.Default.Payments, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Pay Securely ₹${total.toInt()}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = TechCyan)
        )
        Icon(imageVector = icon, contentDescription = null, tint = TechCyan, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
