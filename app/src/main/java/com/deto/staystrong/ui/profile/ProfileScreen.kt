package com.deto.staystrong.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.deto.staystrong.Login
import com.deto.staystrong.model.UserUpdateRequest
import com.deto.staystrong.ui.AppViewModelProvider
import com.deto.staystrong.ui.auth.AuthUiState
import com.deto.staystrong.ui.auth.AuthViewModel
import com.deto.staystrong.ui.components.CustomBottomAppBar
import com.deto.staystrong.ui.components.CustomCircularProgressIndicator
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import com.deto.staystrong.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val authState = viewModel.authState
    val context = LocalContext.current
    var isEditable by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Hombre", "Mujer")
    var editableProfile by remember { mutableStateOf(EditableProfile()) }


    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }
    LaunchedEffect(authState) {
        if (authState is AuthUiState.Idle) {
            navController.navigate(Login) {
                popUpTo("profile") { inclusive = true }
            }
        }
    }



    if (viewModel.userData != null && editableProfile.name.isBlank()) {
        val user = viewModel.userData!!
        editableProfile = EditableProfile(
            name = user.name ?: "",
            birth = user.birth ?: "",
            weight = user.weight?.toString() ?: "",
            height = user.height?.toString() ?: "",
            bio = user.bio ?: "",
            ig = user.ig ?: "",
            sex = when (user.sex) {
                "male", "hombre", "Hombre" -> "Hombre"
                "female", "mujer", "Mujer" -> "Mujer"
                else -> "Otro"
            }
        )
    }

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(navController)
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.profile_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    TextButton(
                        onClick = { authViewModel.logout() },
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.logout_24px),
                            contentDescription = "Cerrar sesión",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.profile_logout),
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }


                if (authState is AuthUiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomCircularProgressIndicator("perfil")
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        CampoEditable(stringResource(R.string.profile_label_nombre), editableProfile.name, { editableProfile = editableProfile.copy(name = it) }, isEditable, Modifier.weight(1f), 1)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = formatDateToDisplay(editableProfile.birth),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.profile_label_fecha)) },
                            colors = customTextFieldColors(),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (isEditable) {
                            IconButton(
                                onClick = {
                                    val calendar = Calendar.getInstance()
                                    val year = calendar.get(Calendar.YEAR)
                                    val month = calendar.get(Calendar.MONTH)
                                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                                    android.app.DatePickerDialog(
                                        context,
                                        { _, y, m, d ->
                                            editableProfile = editableProfile.copy(
                                                birth = "%04d-%02d-%02d".format(y, m + 1, d)
                                            )
                                        },
                                        year, month, day
                                    ).show()
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE91E63))
                            ) {
                                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha", tint = Color.White)
                            }
                        } else {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Bloqueado", tint = Color.Gray, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.weight(1f)) {
                            ExposedDropdownMenuBox(
                                expanded = expanded && isEditable,
                                onExpandedChange = {
                                    if (isEditable) expanded = !expanded
                                }
                            ) {
                                OutlinedTextField(
                                    value = editableProfile.sex,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = isEditable,
                                    label = { Text(stringResource(R.string.profile_label_sexo)) },
                                    trailingIcon = {
                                        if (!isEditable) {
                                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Bloqueado", tint = Color.Gray)
                                        }
                                    },
                                    colors = customTextFieldColors(),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded && isEditable,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    options.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                editableProfile = editableProfile.copy(sex = selectionOption)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        CampoEditable(stringResource(R.string.profile_label_peso), editableProfile.weight, { editableProfile = editableProfile.copy(weight = it) }, isEditable, Modifier.weight(1f), 1)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    CampoEditable(stringResource(R.string.profile_label_altura), editableProfile.height, { editableProfile = editableProfile.copy(height = it) }, isEditable, maxLine = 1)

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(stringResource(R.string.profile_subtitle), color = Color.White, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    CampoEditable(stringResource(R.string.profile_label_bio), editableProfile.bio, { editableProfile = editableProfile.copy(bio = it) }, isEditable, maxLine = 5)
                    Spacer(modifier = Modifier.height(8.dp))
                    CampoEditable(stringResource(R.string.profile_label_instagram), editableProfile.ig, { editableProfile = editableProfile.copy(ig = it) }, isEditable, maxLine = 1)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isEditable) {
                                val updateRequest = UserUpdateRequest(
                                    name = editableProfile.name.takeIf { it.isNotBlank() },
                                    sex = when (editableProfile.sex) {
                                        "Hombre" -> "male"
                                        "Mujer" -> "female"
                                        else -> "other"
                                    },
                                    birth = editableProfile.birth.takeIf { it.isNotBlank() },
                                    weight = editableProfile.weight.toFloatOrNull(),
                                    height = editableProfile.height.toFloatOrNull(),
                                    bio = editableProfile.bio.takeIf { it.isNotBlank() },
                                    ig = editableProfile.ig.takeIf { it.isNotBlank() }
                                )
                                viewModel.updateProfile(updateRequest)
                            }
                            isEditable = !isEditable
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = if (isEditable) stringResource(R.string.profile_text_button_isEditable) else stringResource(R.string.profile_text_button),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }


                if (authState is AuthUiState.Error) {
                    Text("Error: ${authState.message}", color = Color.Red)
                }



            }
        }
    }
}

@Composable
fun CampoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLine: Int
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = !isEditable,
            label = { Text(label) },
            trailingIcon = {
                if (!isEditable) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.Gray
                    )
                }
            },
            colors = customTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLine
        )
    }
}

@Composable
fun customTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.Gray,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.Gray
)

data class EditableProfile(
    var name: String = "",
    var birth: String = "",
    var weight: String = "",
    var height: String = "",
    var bio: String = "",
    var ig: String = "",
    var sex: String = "Hombre"
)



fun formatDateToDisplay(isoDate: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(isoDate)
        if (date != null) formatter.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}
