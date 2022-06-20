package com.jonathas.petclinic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

@BindingAdapter("goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["fromUrl", "scope", "cache"], requireAll = false)
fun ImageView.downloadImage(url: String, scope: CoroutineScope?, cacheImage: Boolean = true) {
    setImageBitmap(null)
    cachedImages.getOrDefault(url, null)?.let {
        setImageBitmap(cachedImages.getOrDefault(url, null))
    } ?: run {
        scope?.launch {
            withContext(Dispatchers.IO) {
                val inputStream: InputStream = URL(url).openStream()
                val downloadedImage = BitmapFactory.decodeStream(inputStream)
                if (cacheImage) {
                    cachedImages[url] = downloadedImage
                }
                withContext(Dispatchers.Main) {
                    setImageBitmap(downloadedImage)
                }
            }
        }
    }
}

val cachedImages: HashMap<String, Bitmap> = HashMap()
