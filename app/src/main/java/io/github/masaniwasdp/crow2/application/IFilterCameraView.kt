package io.github.masaniwasdp.crow2.application

/** Interfaco de la vido de fotilo. */
interface IFilterCameraView {
    /** Sciigis tiun konservitan bildon sukcese. */
    fun notifySuccess()

    /** Sciigis tiun konservitan bildon malsukcese. */
    fun notifyFailed()
}