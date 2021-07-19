package io.github.masaniwasdp.crow2.application

import android.graphics.Bitmap
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.Closeable

/**
 * Filtrila fotilo.
 *
 * @constructor Kreas la objekton de filtrila fotilo.
 * @property view La vido de fotilo.
 * @property store La stokado de amaskomunikilaroj.
 */
class FilterCamera(
    private val view: IFilterCameraView, private val store: IMediaStore
) : IFilterCamera, Closeable {
    override fun updateFrame(frame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        when (filter) {
            IFilterCamera.Filter.None -> frame.rgba().copyTo(this.frame)
            IFilterCamera.Filter.Negative -> negate(frame.rgba(), this.frame)
            IFilterCamera.Filter.Grayscale -> grayscale(frame.rgba(), this.frame)
            IFilterCamera.Filter.Red -> redFilter(frame.rgba(), this.frame)
            IFilterCamera.Filter.Green -> greenFilter(frame.rgba(), this.frame)
            IFilterCamera.Filter.Blue -> blueFilter(frame.rgba(), this.frame)
        }

        return this.frame
    }

    override fun saveFrame() {
        try {
            Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888)
                .let {
                    Utils.matToBitmap(frame, it)

                    store.saveImage(it)
                }

            view.notifySuccess()
        } catch (e: Exception) {
            view.notifyFailed()
        }
    }

    override fun useFilter(filter: IFilterCamera.Filter) {
        this.filter = filter
    }

    override fun close() {
        frame.release()
    }

    /** Fotila kadro. */
    private val frame = Mat()

    /** Filtrilo de la fotilo. */
    private var filter = IFilterCamera.Filter.None
}
