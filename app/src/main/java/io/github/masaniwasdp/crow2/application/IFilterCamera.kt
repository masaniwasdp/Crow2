package io.github.masaniwasdp.crow2.application

import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

/** Interfaco de filtrila fotilo. */
interface IFilterCamera {
    /** La filtriloj. */
    enum class Filter { None, Negative, Grayscale, Red, Green, Blue; }

    /**
     * Inicializas la fotilan kadron.
     *
     * @param w Larĝeco de la kadro.
     * @param h Alteco de la kadro.
     */
    fun initializeFrame(w: Int, h: Int)

    /** Liberigas la fotilan kadron. */
    fun finaliseFrame()

    /**
     * Ĝisdatigas la fotilan kadron.
     *
     * @param frame La eniga fotila bildo.
     * @return Filtrita bildo.
     */
    fun updateFrame(frame: CameraBridgeViewBase.CvCameraViewFrame): Mat

    /** Savas la fotilan kadron. */
    fun saveFrame()

    /**
     * Uzas la filtrilon.
     *
     * @param filter La filtrilo uzota.
     */
    fun useFilter(filter: Filter)
}