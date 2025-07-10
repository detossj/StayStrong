package com.deto.staystrong.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomOutlinedTextFieldLoginAndRegister(value: String, onValueChange: ( String ) -> Unit, icon: ImageVector, placeholder: Int, supportingText: Int, isError: Boolean, isPassword: Boolean ) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icono",
                tint = Color.Black
            )
        },
        shape = RoundedCornerShape(50),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White ,
            disabledContainerColor = Color.White,
            errorContainerColor = Color.White,

        ),
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(placeholder),
                color = Color.Black
            )
        },
        isError = isError,
        supportingText = { if (isError) Text(stringResource(supportingText)) },
        minLines = 1,
        maxLines = 1,
        textStyle = TextStyle(Color.Black),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )

}