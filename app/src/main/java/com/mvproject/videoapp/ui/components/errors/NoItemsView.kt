/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.errors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.videoapp.R
import com.mvproject.videoapp.ui.theme.appTypography
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING

@Composable
fun NoItemsView(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.msg_no_items_found),
    navigateTitle: String = EMPTY_STRING,
    onNavigateClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primary
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size16)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size96),
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = title
                )

                Text(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.dimens.size16,
                            bottom = MaterialTheme.dimens.size24
                        ),
                    text = title,
                    style = MaterialTheme.appTypography.textSemiBold,
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )

                if (navigateTitle.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.dimens.size16,
                                bottom = MaterialTheme.dimens.size24
                            )
                            .clickable { onNavigateClick() },
                        text = navigateTitle,
                        style = MaterialTheme.appTypography.textExtraBold,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}