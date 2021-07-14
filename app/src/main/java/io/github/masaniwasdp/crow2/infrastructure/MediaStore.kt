package io.github.masaniwasdp.crow2.infrastructure

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import io.github.masaniwasdp.crow2.application.IMediaStore
import java.io.FileOutputStream

/**
 * La stokado de amaskomunikilaroj.
 *
 * @constructor Kreas la objekton de la stokado.
 * @property resolver Content-resolver.
 */
class MediaStore(private val resolver: ContentResolver) : IMediaStore {
    override fun saveImage(bitmap: Bitmap) {
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val newValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, TYPE)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val item = resolver.insert(collection, newValues)!!

        resolver.openFileDescriptor(item, "w", null).use {
            FileOutputStream(it!!.fileDescriptor).use { x ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, x)
            }
        }

        val finalValue = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }

        resolver.update(item, finalValue, null, null)
    }
}

/** La MIME-tipo de bildoj dosieroj. */
private const val TYPE = "image/jpeg"

/** La kvalito de savi bildojn. */
private const val QUALITY = 95
