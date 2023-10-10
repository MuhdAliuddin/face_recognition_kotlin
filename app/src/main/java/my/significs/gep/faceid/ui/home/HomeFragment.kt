package my.significs.gep.faceid.ui.home

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var  editTextText : EditText
    private lateinit var pickImages: ActivityResultLauncher<String>
    private lateinit var selectFolder: ActivityResultLauncher<Intent>
    private val uris = mutableListOf<Uri>()
    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        editTextText = binding.editTextText

        dashboardViewModel.predictionResult.observe(viewLifecycleOwner) { it
            Log.println(Log.ASSERT, "HomeFragment", it.name)
                editTextText.setText(it.name)
        }
//        var inputStream: InputStream?


//        val myCard1 = root.findViewById(R.id.cameraCV) as CardView
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

//         pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { selectedUris  ->
//            // Handle the returned URIs
//             uris.addAll(selectedUris)
//            if (selectedUris.isNotEmpty()) {
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//                Log.d("PhotoPicker", "Number of items selected: ${selectedUris}")
//                selectFolder.launch(intent)
//            }
//        }

//         selectFolder = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null) {
//                    Log.d("selectFolderz", "selectFolderz selected: ${uri}")
//                    Log.println(Log.ASSERT, "PhotoPicker", uri.toString())
//                    saveImages(uris, uri)
//                }
//            }
//        }

//        startActivityForResult(Intent.createChooser(intent, "Select Directory"), 100)
//        val selectFolder = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri = result.data?.data
//                if (uri != null) {
//                    Log.d("selectFolder", "No media selected")
//                }
//            }
//        }
//
//        val pickMultipleMedia =
//            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
//                // Callback is invoked after the user selects media items or closes the
//                // photo picker.
//                if (uris.isNotEmpty()) {
//                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
//                } else {
//                    Log.d("PhotoPicker", "No media selected")
//                }
//            }

//        myCard1.setOnClickListener {
//            launchActivityIntent()
////            Navigation.findNavController(myCard1).navigate(R.id.action_navigation_home_to_camera)
////            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
////            startActivityForResult(Intent.createChooser(intent, "Select Directory"), 100)
////            pickAndSaveImages()
//        }

        return root
    }
    fun pickAndSaveImages() {
        pickImages.launch("image/*")
    }

    private fun launchActivityIntent(){
        val pm = activity?.packageManager
        val i = pm?.getLaunchIntentForPackage("com.guardexpertpro")

        if (i != null) {
            startActivity(i)
        }
    }

    private fun saveImages(uris: List<Uri>, folderUri: Uri) {
        val resolver = requireContext().contentResolver
        Log.println(Log.ASSERT, "saveImages", uris.toString())

        uris.forEach { uri ->
            val directory = DocumentFile.fromTreeUri(requireContext(), uri)
            Log.d("saveImages", "selectFolderz selected: ${directory}")
            val inputStream = resolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val file = File(folderUri.path, uri.toString())
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//            data?.data?.also { uri ->
//                // The document tree is the first URI in the data
//                val treeUri = DocumentsContract.buildTreeDocumentUri(uri.authority, DocumentsContract.getTreeDocumentId(uri))
//
//                val directory = File(requireContext().getExternalFilesDir(null), "myImages")
//                    if (!directory.exists()) {
//                        directory.mkdirs()
//                    }
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                val file = File(directory, "${System.currentTimeMillis()}.jpg")
//                val outputStream = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                outputStream.close()
//                // Use the treeUri as needed
//            }
//        }
//    }

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