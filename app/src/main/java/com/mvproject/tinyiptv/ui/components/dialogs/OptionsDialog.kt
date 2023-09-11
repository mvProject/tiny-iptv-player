/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 01.09.23, 13:22
 *
 */

package com.mvproject.tinyiptv.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants

@Composable
fun OptionsDialog(
    modifier: Modifier = Modifier,
    isDialogOpen: MutableState<Boolean>,
    title: String? = null,
    items: List<String>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int) -> Unit = {},
) {
    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }
        ) {
            Surface(
                modifier = modifier
                    .wrapContentSize()
                    .padding(MaterialTheme.dimens.size8),
                shape = MaterialTheme.shapes.medium,
                shadowElevation = MaterialTheme.dimens.size8
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > AppConstants.INT_NO_VALUE) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    title?.let { text ->
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                    .padding(MaterialTheme.dimens.size12)
                            ) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }

                    itemsIndexed(items) { index, item ->
                        val selectedItem = index == selectedIndex
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(),
                            onClick = { onItemSelected(index) }
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selectedItem)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.onSurface,

                                )
                        }

                        if (index < items.lastIndex) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.dimens.size16),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}