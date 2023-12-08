/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:48
 *
 */

package com.mvproject.tinyiptv.ui.components.toolbars

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.mvproject.tinyiptv.data.enums.ChannelsViewType

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
    AnimatedContent(
        targetState = searchWidgetState,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it }
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it }
            )
        },
        label = "AppBarWithSearch"
    ) { state ->
        if (state) {
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
}


