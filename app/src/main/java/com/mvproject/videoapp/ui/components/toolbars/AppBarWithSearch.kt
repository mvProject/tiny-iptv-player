/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.toolbars

import androidx.compose.runtime.Composable
import com.mvproject.videoapp.data.enums.ChannelsViewType

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


