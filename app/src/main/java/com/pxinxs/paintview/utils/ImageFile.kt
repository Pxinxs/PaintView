package com.pxinxs.paintview.utils

import android.graphics.*
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 *Created by AndroidDev on 16.01.2018.
 */
class ImageFile {

    fun saveImage(bitmap: Bitmap, height: Float, width: Float): String {
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PaintView")
        val success = if (!folder.exists())
            folder.mkdirs()
        else true

        if (success) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(folder.path + File.separator + timeStamp + ".png")
            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    return e.message.toString()
                }
            }
            try {
                val fileOutputStream: FileOutputStream? = FileOutputStream(file)
                val save = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
                val paint = Paint()
                paint.color = Color.WHITE
                val now = Canvas(save)
                now.drawRect(Rect(0, 0, width.toInt(), height.toInt()), paint)
                now.drawBitmap(bitmap, Rect(0, 0, bitmap.width, bitmap.height), Rect(0, 0, width.toInt(), height.toInt()), null)
                if (save == null) {
                    // :(
                }
                save.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            } catch (e: Exception) {
                return e.message.toString()
            }
        } else {
            return "Folder can't be created"
        }

        return "Your drawable was saved"
    }

}