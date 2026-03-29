package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(
    onResult: (String?) -> Unit
): ImagePicker