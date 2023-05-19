/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 19.05.23, 10:28
 *
 */

package com.mvproject.videoapp.ui.components.toolbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit = {},
    onCloseClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.hint_msg_search),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                    )
                },
                trailingIcon = {
                    FilledIconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChange(EMPTY_STRING)
                            } else {
                                onCloseClicked()
                            }
                        },
                        modifier = Modifier.padding(MaterialTheme.dimens.size8),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchAppBar() {
    VideoAppTheme() {
        SearchAppBar(
            text = stringResource(id = R.string.app_name)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkSearchAppBar() {
    VideoAppTheme(darkTheme = true) {
        SearchAppBar(
            text = stringResource(id = R.string.app_name)
        )
    }
}