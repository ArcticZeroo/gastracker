package io.frozor.gastracker.ui.components.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.frozor.gastracker.constants.Styles

@Composable
fun PageContainer(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(Styles.DefaultPadding), content = content)
}