package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable

@Composable
expect fun CameraScreenContent(
    onResult: (String?) -> Unit,
)