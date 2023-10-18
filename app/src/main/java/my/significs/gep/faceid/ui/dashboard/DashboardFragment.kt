package my.significs.gep.faceid.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.DocumentsContract
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import my.significs.gep.faceid.BitmapUtils
import my.significs.gep.faceid.FileReader
import my.significs.gep.faceid.FrameAnalyser
import my.significs.gep.faceid.Logger
import my.significs.gep.faceid.MainActivity.Companion.logTextView
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentDashboardBinding
import my.significs.gep.faceid.model.FaceNetModel
import my.significs.gep.faceid.model.Models
import my.significs.gep.faceid.User
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.concurrent.Executors
import java.util.logging.Handler


class DashboardFragment : Fragment() {
    private var isSerializedDataStored = false
    private var _binding: FragmentDashboardBinding? = null
//    val Fragment.packageName get() = activity?.packageName
//    val pm = packageName

    // Serialized data will be stored ( in app's private storage ) with this filename.
    private val SERIALIZED_DATA_FILENAME = "image_data"

    // Shared Pref key to check if the data was stored.
    private val SHARED_PREF_IS_DATA_STORED_KEY = "is_data_stored"

    //CAMERA VAR
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView : PreviewView
    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fileReader : FileReader
    private lateinit var faceNetModel : FaceNetModel

    private lateinit var  scanFrameRed : ImageView
    private lateinit var  scanFrameGreen : ImageView
    private lateinit var dashCL : ConstraintLayout
    // Camera Facing
    private val cameraFacing = CameraSelector.LENS_FACING_FRONT

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // <----------------------- User controls --------------------------->

    // Use the device's GPU to perform faster computations.
    // Refer https://www.tensorflow.org/lite/performance/gpu
    private val useGpu = true

    // Use XNNPack to accelerate inference.
    // Refer https://blog.tensorflow.org/2020/07/accelerating-tensorflow-lite-xnnpack-integration.html
    private val useXNNPack = true

    // You may the change the models here.
    // Use the model configs in Models.kt
    // Default is Models.FACENET ; Quantized models are faster
    private val modelInfo = Models.FACENET
    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        previewView = binding.previewView
        logTextView = binding.logTextview

        scanFrameRed = binding.scanFrameRed
        scanFrameGreen = binding.scanFrameGreen
        dashCL = binding.dashCL

        // Necessary to keep the Overlay above the PreviewView so that the boxes are visible.
        val boundingBoxOverlay = binding.bboxOverlay
        boundingBoxOverlay.cameraFacing = cameraFacing
        boundingBoxOverlay.setWillNotDraw( false )
        boundingBoxOverlay.setZOrderOnTop( true )

        faceNetModel = FaceNetModel( requireContext() , modelInfo , useGpu , useXNNPack )
        frameAnalyser = FrameAnalyser(  requireContext() , dashboardViewModel, boundingBoxOverlay , faceNetModel,  )
        fileReader = FileReader( faceNetModel )

        if ( ActivityCompat.checkSelfPermission( requireContext() , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            requestCameraPermission()
        }
        else {
            startCameraPreview()
        }

        sharedPreferences = requireActivity().getSharedPreferences( getString( R.string.app_name ) , Context.MODE_PRIVATE )
        isSerializedDataStored = sharedPreferences.getBoolean( SHARED_PREF_IS_DATA_STORED_KEY , false )
        if ( !isSerializedDataStored ) {
            Logger.log( "No serialized data was found. Select the images directory.")
            showSelectDirectoryDialog()
        }
        else {
            val alertDialog = AlertDialog.Builder( requireContext() ).apply {
                setTitle( "Serialized Data")
                setMessage( "Existing image data was found on this device. Would you like to load it?" )
                setCancelable( false )
                setNegativeButton( "LOAD") { dialog, which ->
                    dialog.dismiss()
                    frameAnalyser.faceList = loadSerializedImageData()
                    Logger.log( "Serialized data loaded.")
                }
                setPositiveButton( "RESCAN") { dialog, which ->
                    dialog.dismiss()
                    launchChooseDirectoryIntent()
                }
                create()
            }

            dashboardViewModel.showDialog.observe(viewLifecycleOwner) { it
                if (it) {
                    alertDialog.show()
                } else {
                    frameAnalyser.faceList = loadSerializedImageData()
                }
            }

        }
        dashboardViewModel.predictionResult.observe(viewLifecycleOwner) { it

            if (it.name != "") {
                val snack = Snackbar.make(dashCL,  "FACE RECOGNITION SUCCESS, REDIRECTING ${it.name}",2000)
                snack.show()

                scanFrameRed.visibility = View.INVISIBLE
                scanFrameGreen.visibility = View.VISIBLE

                countdownFunc(it)
            }
        }

        dashboardViewModel.removeMask.observe(viewLifecycleOwner) { it
            if (it == true) {
                val snack = Snackbar.make(dashCL, "Please remove your mask",1000)
                Log.println(Log.ASSERT, "removeMask", "${it}")
                dashboardViewModel.onRemoveMaskFalse()
                snack.show()
            }
        }

        return root
    }
    private fun countdownFunc(prediction: User) {
        object : CountDownTimer(1000, 1000) {
            // Callback function, fired
            // when the time is up
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (prediction.isAdmin === false) {
//                    findNavController().navigate(R.id.action_navigation_dash_to_home)
                } else {
                    dashboardViewModel.onClearScan()
                    scanFrameRed.visibility = View.VISIBLE
                    scanFrameGreen.visibility = View.INVISIBLE
                    launchActivityIntent()
                }
            }
        }.start()
    }


    private fun launchActivityIntent(){
        val pm = activity?.packageManager
        val i = pm?.getLaunchIntentForPackage("com.guardexpertpro")

        if (i != null) {
            startActivity(i)
        }
    }

    private fun showSelectDirectoryDialog() {
        val alertDialog = AlertDialog.Builder( requireContext() ).apply {
            setTitle( "Select Images Directory")
            setMessage( "As mentioned in the project\'s README file, please select a directory which contains the images." )
            setCancelable( false )
            setPositiveButton( "SELECT") { dialog, which ->
                dialog.dismiss()
                launchChooseDirectoryIntent()
            }
            create()
        }
        alertDialog.show()
    }

    private fun launchChooseDirectoryIntent() {
        val intent = Intent( Intent.ACTION_OPEN_DOCUMENT_TREE )
        // startForActivityResult is deprecated.
        // See this SO thread -> https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        directoryAccessLauncher.launch( intent )
    }

    private val directoryAccessLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
        val dirUri = it.data?.data ?: return@registerForActivityResult
        val childrenUri =
            DocumentsContract.buildChildDocumentsUriUsingTree(
                dirUri,
                DocumentsContract.getTreeDocumentId( dirUri )
            )
        val tree = DocumentFile.fromTreeUri(requireContext(), childrenUri)
        val images = ArrayList<Pair<String, Bitmap>>()
        var errorFound = false
        if ( tree!!.listFiles().isNotEmpty()) {
            for ( doc in tree.listFiles() ) {
                if ( doc.isDirectory && !errorFound ) {
                    val name = doc.name!!
                    for ( imageDocFile in doc.listFiles() ) {
                        try {
                            images.add( Pair( name , getFixedBitmap( imageDocFile.uri ) ) )
                        }
                        catch ( e : Exception ) {
                            errorFound = true
                            Logger.log( "Could not parse an image in $name directory. Make sure that the file structure is " +
                                    "as described in the README of the project and then restart the app." )
                            break
                        }
                    }
                    Logger.log( "Found ${doc.listFiles().size} images in $name directory" )
                }
                else {
                    errorFound = true
                    Logger.log( "The selected folder should contain only directories. Make sure that the file structure is " +
                            "as described in the README of the project and then restart the app." )
                }
            }
        }
        else {
            errorFound = true
            Logger.log( "The selected folder doesn't contain any directories. Make sure that the file structure is " +
                    "as described in the README of the project and then restart the app." )
        }
        if ( !errorFound ) {
            fileReader.run( images , fileReaderCallback )
            Logger.log( "Detecting faces in ${images.size} images ..." )
        }
        else {
            val alertDialog = AlertDialog.Builder( requireContext() ).apply {
                setTitle( "Error while parsing directory")
                setMessage( "There were some errors while parsing the directory. Please see the log below. Make sure that the file structure is " +
                        "as described in the README of the project and then tap RESELECT" )
                setCancelable( false )
                setPositiveButton( "RESELECT") { dialog, which ->
                    dialog.dismiss()
                    launchChooseDirectoryIntent()
                }
                setNegativeButton( "CANCEL" ){ dialog , which ->
                    dialog.dismiss()
                }
                create()
            }
            alertDialog.show()
        }
    }

    // Get the image as a Bitmap from given Uri and fix the rotation using the Exif interface
    // Source -> https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a
    private fun getFixedBitmap( imageFileUri : Uri) : Bitmap {
        var imageBitmap = BitmapUtils.getBitmapFromUri( requireActivity().contentResolver , imageFileUri )
        val exifInterface = ExifInterface( requireActivity().contentResolver.openInputStream( imageFileUri )!! )
        imageBitmap =
            when (exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION ,
                ExifInterface.ORIENTATION_UNDEFINED )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> BitmapUtils.rotateBitmap( imageBitmap , 90f )
                ExifInterface.ORIENTATION_ROTATE_180 -> BitmapUtils.rotateBitmap( imageBitmap , 180f )
                ExifInterface.ORIENTATION_ROTATE_270 -> BitmapUtils.rotateBitmap( imageBitmap , 270f )
                else -> imageBitmap
            }
        return imageBitmap
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
        val imageFrameAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size( 480, 640 ) )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
        imageFrameAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), frameAnalyser )
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageFrameAnalysis  )
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

    // ---------------------------------------------- //

    private val fileReaderCallback = object :  FileReader.ProcessCallback {
        override fun onProcessCompleted(data: ArrayList<Pair<String, FloatArray>>, numImagesWithNoFaces: Int) {
            frameAnalyser.faceList = data
            saveSerializedImageData( data )
            Logger.log( "Images parsed. Found $numImagesWithNoFaces images with no faces." )
        }
    }

    private fun saveSerializedImageData(data : ArrayList<Pair<String,FloatArray>> ) {
        val serializedDataFile = File( requireActivity().filesDir , SERIALIZED_DATA_FILENAME )
        ObjectOutputStream( FileOutputStream( serializedDataFile )  ).apply {
            writeObject( data )
            flush()
            close()
        }
        sharedPreferences.edit().putBoolean( SHARED_PREF_IS_DATA_STORED_KEY , true ).apply()
    }

    private fun loadSerializedImageData() : ArrayList<Pair<String,FloatArray>> {
        val serializedDataFile = File( requireActivity().filesDir , SERIALIZED_DATA_FILENAME )
        val objectInputStream = ObjectInputStream( FileInputStream( serializedDataFile ) )
        val data = objectInputStream.readObject() as ArrayList<Pair<String,FloatArray>>
        objectInputStream.close()
        return data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}