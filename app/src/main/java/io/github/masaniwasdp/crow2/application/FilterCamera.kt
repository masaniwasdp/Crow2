package io.github.masaniwasdp.crow2.application

import android.graphics.Bitmap
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat

/**
 * Filtrila fotilo.
 *
 * @constructor Kreas la objekton de filtrila fotilo.
 * @property view La vido de fotilo.
 * @property store La stokado de amaskomunikilaroj.
 */
class FilterCamera(
    private val view: IFilterCameraView, private val store: IMediaStore
) : IFilterCamera {
    override fun initializeFrame(w: Int, h: Int) {
        frame?.release()

        frame = Mat(h, w, CvType.CV_8UC4)
    }

    override fun finaliseFrame() {
        frame?.release()

        frame = null
    }

    override fun updateFrame(frame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        this.frame?.let {
            when (filter) {
                IFilterCamera.Filter.None -> frame.rgba().copyTo(it)
                IFilterCamera.Filter.Negative -> negate(frame.rgba(), it)
                IFilterCamera.Filter.Grayscale -> grayscale(frame.rgba(), it)
                IFilterCamera.Filter.Red -> redFilter(frame.rgba(), it)
                IFilterCamera.Filter.Green -> greenFilter(frame.rgba(), it)
                IFilterCamera.Filter.Blue -> blueFilter(frame.rgba(), it)
            }

            return it
        }

        return frame.rgba()
    }

    override fun saveFrame() {
        frame?.let {
            try {
                Bitmap.createBitmap(it.cols(), it.rows(), Bitmap.Config.ARGB_8888)
                    .let { x ->
                        Utils.matToBitmap(it, x)

                        store.saveImage(x)
                    }

                view.notifySuccess()
            } catch (e: Exception) {
                view.notifyFailed()
            }
        }
    }

    override fun useFilter(filter: IFilterCamera.Filter) {
        this.filter = filter
    }

    /** Filtrilo de la fotilo. */
    private var filter = IFilterCamera.Filter.None

    /** Fotila kadro. */
    private var frame: Mat? = null
}
