/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:34
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*

class ContextUtil(private val context: Context) {
    @SuppressLint("Range")
    fun getFileNameFromUri(uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }
}
