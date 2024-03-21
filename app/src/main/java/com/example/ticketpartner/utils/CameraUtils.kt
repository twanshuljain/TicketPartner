package com.example.ticketpartner.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.ticketpartner.common.APP_NAME
import com.example.ticketpartner.common.APP_PACKAGE_NANE
import com.example.ticketpartner.common.IMAGE_EXTENSION
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraUtils {
    companion object {
        // Function to save Bitmap as a file with max file size in megabytes
        fun saveBitmapAsFileWithMaxSizeInMB(bitmap: Bitmap, maxSizeMB: Int): File? {
            val maxSizeBytes = maxSizeMB * 1024 * 1024 // Convert megabytes to bytes

            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "IMG_$timeStamp${IMAGE_EXTENSION}"

            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                APP_NAME
            )

            // Create the storage directory if it does not exist
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            val imageFile = File(storageDir, fileName)

            val scale = calculateScaleFactor(bitmap.width, bitmap.height, maxSizeBytes.toLong())

            try {
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * scale).toInt(),
                    (bitmap.height * scale).toInt(),
                    false
                )

                val fileOutputStream = FileOutputStream(imageFile)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.close()

                return imageFile

            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception appropriately
                return null
            }
        }

        // Function to calculate the scale factor based on the target file size
        private fun calculateScaleFactor(width: Int, height: Int, maxSizeBytes: Long): Float {
            val imageSize = width * height * 4 // Assuming ARGB_8888 format
            val scale = Math.sqrt(maxSizeBytes.toDouble() / imageSize)
            return if (scale < 1) scale.toFloat() else 1.toFloat()
        }

        fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.let {
                    return BitmapFactory.decodeStream(it)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        // Convert File to Uri and return the Uri
        fun fileToUri(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(
                context,
                APP_PACKAGE_NANE,
                file
            )
        }

        fun getImageSize(context: Context, imageUri: Uri): Double {
            var sizeInMB: Double = 0.0

            try {
                val contentResolver: ContentResolver = context.contentResolver
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

                if (inputStream != null) {
                    // Get the size of the input stream in bytes
                    val sizeInBytes = inputStream.available().toDouble()

                    // Convert bytes to megabytes
                    sizeInMB = sizeInBytes / (1024.0 * 1024.0)

                    // Close the input stream
                    inputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return sizeInMB
        }
    }
}