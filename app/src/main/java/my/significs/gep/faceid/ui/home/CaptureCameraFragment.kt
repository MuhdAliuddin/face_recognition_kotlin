package my.significs.gep.faceid.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.util.concurrent.ListenableFuture
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.databinding.FragmentCaptureImageBinding
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import java.io.File
import java.nio.file.Files.createFile
import java.util.Locale
import java.util.concurrent.Executors

class CaptureCameraFragment : Fragment() {

    private var _binding: FragmentCaptureImageBinding? = null
    private lateinit var previewView : PreviewView

    //CAMERA VAR
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    // Camera Facing
    private val cameraFacing = CameraSelector.LENS_FACING_BACK
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentCaptureImageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        previewView = binding.previewView

        // CAMERA START //
        if ( ActivityCompat.checkSelfPermission( requireContext() , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            requestCameraPermission()
        }
        else {
            startCameraPreview()
        }



//        val captureImageButton = binding.captureImageButton
//        captureImageButton.setOnClickListener {
//            val imageCapture = ImageCapture.Builder().build()
//            val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
//            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val savedUri = outputFileResults.savedUri
//                    val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    val msg = "Photo capture failed: ${exception.message}"
//                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, msg)
//                }
//            })
//        }

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance( requireContext() )
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider) },
            ContextCompat.getMainExecutor(requireContext()) )
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder().build()
        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing( cameraFacing )
            .build()
        preview.setSurfaceProvider( previewView.surfaceProvider )
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview  )
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch( Manifest.permission.CAMERA )
    }

    private val cameraPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() ) {
            isGranted ->
        if ( isGranted ) {
            startCameraPreview()
        }
        else {
            val alertDialog = AlertDialog.Builder( requireContext() ).apply {
                setTitle( "Camera Permission")
                setMessage( "The app couldn't function without the camera permission." )
                setCancelable( false )
                setPositiveButton( "ALLOW" ) { dialog, which ->
                    dialog.dismiss()
                    requestCameraPermission()
                }
                setNegativeButton( "CLOSE" ) { dialog, which ->
                    dialog.dismiss()
                }
                create()
            }
            alertDialog.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val navView: BottomNavigationView = (activity as MainActivity).binding.navView
        navView.visibility = View.VISIBLE
    }
}