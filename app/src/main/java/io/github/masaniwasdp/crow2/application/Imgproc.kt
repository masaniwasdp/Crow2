package io.github.masaniwasdp.crow2.application

import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Aplikas negativan / pozitivan inversion.
 *
 * @param src Fonta bildo.
 * @param dst Destino de inversigita bildo.
 */
fun negate(src: Mat, dst: Mat) {
    require(src.type() == CvType.CV_8UC4) { "The type of src must be CV_8UC4." }

    Core.bitwise_not(src, dst)
}

/**
 * Konvertas la bildon al grizskala.
 *
 * @param src Fonta bildo.
 * @param dst Destino de konvertita bildo.
 */
fun grayscale(src: Mat, dst: Mat) {
    require(src.type() == CvType.CV_8UC4) { "The type of src must be CV_8UC4." }

    Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGBA2GRAY)
}

/**
 * Aplikas ruĝan filtrilon al la bildo.
 *
 * @param src Fonta bildo.
 * @param dst Destino de konvertita bildo.
 */
fun redFilter(src: Mat, dst: Mat) {
    require(0.until(src.channels()).contains(0)) { "The index out of bounds." }

    pickChannel(src, dst, 0)
}

/**
 * Aplikas verdan filtrilon al la bildo.
 *
 * @param src Fonta bildo.
 * @param dst Destino de konvertita bildo.
 */
fun greenFilter(src: Mat, dst: Mat) {
    require(0.until(src.channels()).contains(1)) { "The index out of bounds." }

    pickChannel(src, dst, 1)
}

/**
 * Aplikas bluan filtrilon al la bildo.
 *
 * @param src Fonta bildo.
 * @param dst Destino de konvertita bildo.
 */
fun blueFilter(src: Mat, dst: Mat) {
    require(0.until(src.channels()).contains(2)) { "The index out of bounds." }

    pickChannel(src, dst, 2)
}

/**
 * Elektas specifitan kanalon de la bildo.
 *
 * @param src Bildo por elekti kanalon.
 * @param dst Destino de elektata kanalo.
 * @param index Nombro de kanalo.
 */
private fun pickChannel(src: Mat, dst: Mat, index: Int) {
    require(0.until(src.channels()).contains(index)) { "The index out of bounds." }

    (List(src.channels()) { null } as List<Mat?>).let {
        Core.split(src, it)

        it[index]!!.copyTo(dst)

        it.forEach { x -> x!!.release() }
    }
}
