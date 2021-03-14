package org.w3c.dom

external class OffscreenCanvas(width: Int, height: Int) {
    var width: Int
    var height: Int

    fun getContext(contextType: String): RenderingContext
    fun transferToImageBitmap(): ImageBitmap
}

public external class ImageBitmapRenderingContext(): RenderingContext {
    fun transferFromImageBitmap(bitmap: ImageBitmap)
}

external abstract class OffscreenCanvasRenderingContext2D : CanvasRenderingContext2D