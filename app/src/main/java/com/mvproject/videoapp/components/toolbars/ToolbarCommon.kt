/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:46
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.components.toolbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mvproject.videoapp.data.enums.ChannelsViewType
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun AppBarWithSearch(
    searchWidgetState: Boolean,
    appBarTitle: String,
    searchTextState: String,
    onBackClick: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    onSearchTriggered: () -> Unit = {},
    onViewTypeChange: (ChannelsViewType) -> Unit = {}
) {
    if (searchWidgetState) {
        SearchAppBar(
            text = searchTextState,
            onTextChange = onTextChange,
            onCloseClicked = onSearchTriggered
        )
    } else {
        AppBarWithActions(
            appBarTitle = appBarTitle,
            onBackClick = onBackClick,
            onSearchClicked = onSearchTriggered,
            onViewTypeChange = onViewTypeChange
        )
    }
}

@Composable
fun AppBarWithBackNav(
    appBarTitle: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = appBarTitle
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.NavigateBefore,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}

@Composable
fun AppBarWithActions(
    appBarTitle: String,
    onBackClick: () -> Unit,
    onSearchClicked: () -> Unit,
    onViewTypeChange: (ChannelsViewType) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = appBarTitle
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.NavigateBefore,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(
                onClick = { isMenuOpen = !isMenuOpen }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ViewList,
                    contentDescription = "Change Grid",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false }
            ) {
                DropdownMenuItem(onClick = {
                    onViewTypeChange(ChannelsViewType.LIST)
                    isMenuOpen = !isMenuOpen
                }) {
                    Text(text = "List")
                }
                DropdownMenuItem(onClick = {
                    onViewTypeChange(ChannelsViewType.GRID)
                    isMenuOpen = !isMenuOpen
                }) {
                    Text(text = "Grid")
                }
                DropdownMenuItem(onClick = {
                    onViewTypeChange(ChannelsViewType.CARD)
                    isMenuOpen = !isMenuOpen
                }) {
                    Text(text = "CARD")
                }
            }
        }
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {

    }

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                text = "Search here...",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .alpha(MaterialTheme.dimens.alpha70),
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.onPrimary.copy(
                alpha = MaterialTheme.dimens.alpha70
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarWithSearchOpenedPreview() {
    AppBarWithSearch(
        appBarTitle = "appBarTitle",
        searchTextState = "Current Channel",
        searchWidgetState = true
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarWithSearchClosedPreview() {
    AppBarWithSearch(
        appBarTitle = "appBarTitle",
        searchTextState = "Current Channel",
        searchWidgetState = false
    )
}