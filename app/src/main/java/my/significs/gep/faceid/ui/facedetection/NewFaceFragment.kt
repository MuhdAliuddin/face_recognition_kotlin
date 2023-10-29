package my.significs.gep.faceid.ui.facedetection

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.FaceDetectionResultListAdapter
import my.significs.gep.faceid.ImageListAdapter
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.R
import my.significs.gep.faceid.data.Datasource
import my.significs.gep.faceid.databinding.FragmentFaceDetectionResultBinding
import my.significs.gep.faceid.databinding.FragmentNewFaceBinding
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel
import my.significs.gep.faceid.ui.company.CompanyViewModel

class NewFaceFragment : Fragment(), OnCompanyClickListener {
    private var _binding: FragmentNewFaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var  cameraCV : CardView
    private lateinit var  recycler_view : RecyclerView
    private lateinit var  editTV : TextView
    private lateinit var  noteTV : TextView
    private lateinit var  listCV : CardView
    private lateinit var  listCV2 : CardView
    private lateinit var  saveButton : Button

    private val companyViewModel by activityViewModels<CompanyViewModel>()

    private var userList: List<UserModel> = listOf<UserModel>(
        UserModel(
            "MOHD AMIRUL BIN AHMAD",
            "",
            "F0923551",
            "18726892787399"
        ),
    )

    private var companyLists: List<CompanyModel>  = listOf<CompanyModel>(
        CompanyModel(
            "TLP Sdn Bhd",
            userList
        )
    )
    private var editStatusVal: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewFaceBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "PROFILE"

        cameraCV = binding.cameraCV
        editTV = binding.editTV
        noteTV = binding.noteTV
        listCV = binding.listCV
        listCV2 = binding.listCV2
        saveButton = binding.saveButton

        saveButton.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_new_to_result)
        }

        cameraCV.setOnClickListener {
            showDialog()
        }

        val root: View = binding.root

        recycler_view = binding.imageListRV

        companyViewModel.companyList.observe(viewLifecycleOwner) { it
            companyLists = it
            editStatusVal = false
            recycler_view.adapter = ImageListAdapter(
                requireContext(),
                it,
                this,
                false,
            )
        }

        companyViewModel.editStatus.observe(viewLifecycleOwner) { it
            if (it) {
                editStatusVal = true
                recycler_view.adapter = ImageListAdapter(
                    requireContext(),
                    companyLists,
                    this,
                    true
                )
            } else {
                editStatusVal = false
                recycler_view.adapter = ImageListAdapter(
                    requireContext(),
                    companyLists,
                    this,
                    false
                )
            }
        }

        return root
    }
    override fun onUserClick(user: UserModel) {}
    override fun onCompanyClick(company: CompanyModel) {}
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.camera_dialog)

        val yesBtn = dialog.findViewById(R.id.savedBtn) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            showSuccess("Upload successful","Your scan has been successfully updated to your profile")
        }

        val noBtn = dialog.findViewById(R.id.discardBtn) as Button
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSuccess(mainText: String, sucText: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.success_update_dialog)

        dialog.show()

        val successTV = dialog.findViewById(R.id.successTV) as TextView
        val mainDialogTV = dialog.findViewById(R.id.mainDialogTV) as TextView

        successTV.text = sucText
        mainDialogTV.text = mainText

        object : CountDownTimer(1000, 1000) {
            // Callback function, fired
            // when the time is up
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                dialog.dismiss()
                listCV2.visibility = View.VISIBLE
                listCV.visibility = View.INVISIBLE
                saveButton.visibility = View.VISIBLE

            }
        }.start()
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