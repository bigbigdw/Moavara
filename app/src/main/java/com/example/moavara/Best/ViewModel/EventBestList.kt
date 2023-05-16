package com.example.moavara.Best.ViewModel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface EventBestList{
    object Today: EventBestList
    object Week: EventBestList
    object Month: EventBestList
}