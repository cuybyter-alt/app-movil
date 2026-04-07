package com.canchapp_kotlin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.ui.auth.AuthViewModel
import com.canchapp_kotlin.util.Resource

private val RGreenPrimary   = Color(0xFF4CAF50)
private val RGreenDark      = Color(0xFF388E3C)
private val RGreenLight     = Color(0xFF81C784)
private val RLinkGreen      = Color(0xFF2E7D32)
private val RCardBackground = Color(0xFFFAFAFA)
private val RSubtitleGray   = Color(0xFF757575)
private val RTextDark       = Color(0xFF1B1B1B)

@Composable
private fun regInputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor           = RTextDark,
    unfocusedTextColor         = RTextDark,
    focusedLabelColor          = RGreenPrimary,
    unfocusedLabelColor        = RSubtitleGray,
    focusedBorderColor         = RGreenPrimary,
    unfocusedBorderColor       = Color(0xFFBDBDBD),
    cursorColor                = RGreenPrimary,
    focusedTrailingIconColor   = RSubtitleGray,
    unfocusedTrailingIconColor = RSubtitleGray
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val registerState by authViewModel.registerState.collectAsState()

    var username         by remember { mutableStateOf("") }
    var email            by remember { mutableStateOf("") }
    var firstName        by remember { mutableStateOf("") }
    var lastName         by remember { mutableStateOf("") }
    var password         by remember { mutableStateOf("") }
    var confirmPassword  by remember { mutableStateOf("") }
    var passwordVisible  by remember { mutableStateOf(false) }
    var selectedRole     by remember { mutableStateOf("Player") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var localError       by remember { mutableStateOf<String?>(null) }

    val roles     = listOf("Player", "Owner")
    val roleLabel = mapOf("Player" to "Jugador", "Owner" to "Propietario de cancha")
    val isLoading    = registerState is Resource.Loading
    val isSuccess     = registerState is Resource.Success
    val successUser   = (registerState as? Resource.Success)?.data?.data

    val serverError  = (registerState as? Resource.Error)?.message
    val errorMessage = localError ?: serverError

    LaunchedEffect(registerState) {
        if (registerState is Resource.Success) {
            kotlinx.coroutines.delay(2000)
            authViewModel.resetRegisterState()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(RGreenLight, RGreenDark))),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .wrapContentHeight()
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = RCardBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cuypequeniologo),
                    contentDescription = "Logo CanchApp",
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(18.dp))
                )

                Text(
                    text = "Completa tu Registro",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RTextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                when {
                    isSuccess -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(text = "✓", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Cuenta creada exitosamente!",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                if (successUser != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Bienvenido/a, ${successUser.firstName} ${successUser.lastName}",
                                        color = Color.White.copy(alpha = 0.88f),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Redirigiendo al inicio de sesion...",
                                        color = Color.White.copy(alpha = 0.70f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                    isLoading -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = RGreenPrimary,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "Creando tu cuenta...",
                                    color = Color(0xFF2E7D32),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    errorMessage != null -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color(0xFFB71C1C),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; localError = null },
                    label = { Text("Usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = regInputColors()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; localError = null },
                    label = { Text("Correo electronico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = regInputColors()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it; localError = null },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = regInputColors()
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it; localError = null },
                        label = { Text("Apellido") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = regInputColors()
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; localError = null },
                    label = { Text("Contrasena") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = regInputColors(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility
                                              else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                val passwordsMatch = confirmPassword.isBlank() || password == confirmPassword
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = null },
                    label = { Text("Confirmar contrasena") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = !passwordsMatch,
                    colors = regInputColors(),
                    supportingText = {
                        if (!passwordsMatch) Text("Las contrasenas no coinciden", color = Color(0xFFB71C1C))
                    }
                )

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = roleLabel[selectedRole] ?: selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de cuenta") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = regInputColors()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(roleLabel[role] ?: role) },
                                onClick = { selectedRole = role; dropdownExpanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val allFieldsFilled = username.isNotBlank() && email.isNotBlank() &&
                        firstName.isNotBlank() && lastName.isNotBlank() &&
                        password.isNotBlank() && confirmPassword.isNotBlank()

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            localError = "Las contrasenas no coinciden"
                            return@Button
                        }
                        localError = null
                        authViewModel.resetRegisterState()
                        authViewModel.register(
                            email     = email.trim(),
                            firstName = firstName.trim(),
                            lastName  = lastName.trim(),
                            password  = password,
                            roleName  = selectedRole,
                            username  = username.trim()
                        )
                    },
                    enabled = !isLoading && allFieldsFilled,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RGreenPrimary)
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ya tienes cuenta? ", fontSize = 14.sp, color = RSubtitleGray)
                    TextButton(onClick = onLoginClick, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            text = "Inicia sesion",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RLinkGreen
                        )
                    }
                }
            }
        }
    }
}