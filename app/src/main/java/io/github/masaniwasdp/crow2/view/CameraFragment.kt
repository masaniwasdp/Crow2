package io.github.masaniwasdp.crow2.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.github.masaniwasdp.crow2.R
import io.github.masaniwasdp.crow2.application.IFilterCamera
import io.github.masaniwasdp.crow2.application.IFilterCameraView
import io.github.masaniwasdp.crow2.databinding.FragmentCameraBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

/**
 * La fragmento de fotilo.
 */
class CameraFragment : Fragment(), IFilterCameraView {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
            .apply {
                cameraView.setCvCameraViewListener(cameraViewListener)
                cameraView.setOnClickListener(cameraViewListener)
                selectButton.setOnClickListener(selectButtonListener)
            }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    override fun onResume() {
        super.onResume()

        PermissionWrapper(requireActivity(), R.string.camera, Manifest.permission.CAMERA)
            .request { binding?.cameraView?.enableView() }
    }

    override fun onPause() {
        super.onPause()

        binding?.cameraView?.disableView()
    }

    override fun onStop() {
        super.onStop()

        binding?.cameraView?.disableView()
    }

    override fun notifySuccess() {
        Toast.makeText(requireActivity(), getString(R.string.success), Toast.LENGTH_SHORT)
            .show()
    }

    override fun notifyFailed() {
        Toast.makeText(requireActivity(), getString(R.string.failed), Toast.LENGTH_SHORT)
            .show()
    }

    /** La filtrila fotilo. */
    var filterCamera: IFilterCamera? = null

    /** La datuma deviga objekto. */
    private var binding: FragmentCameraBinding? = null

    /** Aŭskultanto de fotila vido. */
    private val cameraViewListener = object
        : CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {
        override fun onCameraViewStarted(width: Int, height: Int) {
            filterCamera?.initializeFrame(width, height)
        }

        override fun onCameraViewStopped() {
            filterCamera?.finaliseFrame()
        }

        override fun onCameraFrame(frame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
            filterCamera?.let { return it.updateFrame(frame) }

            return frame.rgba()
        }

        override fun onClick(v: View?) {
            filterCamera?.saveFrame()
        }
    }

    /** Aŭskultanto de butono por elekti efektojn. */
    private val selectButtonListener = View.OnClickListener {
        SelectDialog(R.array.filters) {
            filterCamera?.useFilter(IFilterCamera.Filter.values()[it])
        }.show(requireActivity().supportFragmentManager, TAG_SELECT_FILTER)
    }
}

/** La etikedo de elekti efektojn. */
private const val TAG_SELECT_FILTER = "SelectFilter"