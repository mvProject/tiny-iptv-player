/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 19.05.23, 10:23
 *
 */

package com.mvproject.tinyiptv.ui.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithActions(
    appBarTitle: String,
    onBackClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onViewTypeChange: (ChannelsViewType) -> Unit = {}
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        navigationIcon = {
            FilledIconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = "Back",
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = { isMenuOpen = !isMenuOpen }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ViewList,
                    contentDescription = "Change Grid",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DropdownMenu(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.menu_view_type_list),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.LIST)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.size8),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.menu_view_type_grid),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.GRID)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.size8),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.menu_view_type_card),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.CARD)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAppBarWithActions() {
    VideoAppTheme() {
        AppBarWithActions(
            appBarTitle = stringResource(id = R.string.app_name)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkAppBarWithActions() {
    VideoAppTheme(darkTheme = true) {
        AppBarWithActions(
            appBarTitle = stringResource(id = R.string.app_name)
        )
    }
}