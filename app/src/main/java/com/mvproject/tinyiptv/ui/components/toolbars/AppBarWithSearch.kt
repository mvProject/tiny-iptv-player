/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.tinyiptv.ui.components.toolbars

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import com.mvproject.tinyiptv.data.enums.ChannelsViewType

@OptIn(ExperimentalAnimationApi::class)
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
            ) with slideOutHorizontally(
                targetOffsetX = { -it }
            )
        }
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
}


