package my.significs.gep.faceid.ui.facedetection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.ImageListAdapter
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.databinding.FragmentFaceDetectionDetailsBinding
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel
import my.significs.gep.faceid.ui.company.CompanyViewModel

class FaceResultDetailsFragment : Fragment(), OnCompanyClickListener {
    private var _binding: FragmentFaceDetectionDetailsBinding? = null

    private val binding get() = _binding!!
    private lateinit var  editTextText : EditText
    private lateinit var  employeeET : EditText
    private lateinit var  accessCardET : EditText
    private lateinit var  editTV : TextView

    private lateinit var  recycler_view : RecyclerView
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
        _binding = FragmentFaceDetectionDetailsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "RESULT"
        val root: View = binding.root

        editTextText = binding.editTextText
        employeeET = binding.employeeET
        accessCardET = binding.accessCardET
        editTV = binding.editTV

        recycler_view = binding.imageListRV

        editTV.setOnClickListener { view: View? ->
            if (editStatusVal) {
                Log.println(Log.ASSERT, "editTV.setOnClickListener", "true")
                companyViewModel.onEditStatusFalse()
            } else {
                Log.println(Log.ASSERT, "editTV.setOnClickListener", "false")
                companyViewModel.onEditStatusTrue()
            }
        }

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

        companyViewModel.selectedUser.observe(viewLifecycleOwner) { it
            editTextText.setText(it.name)
            employeeET.setText(it.employeeID)
            accessCardET.setText(it.cardID)
        }
        return root
    }

    override fun onUserClick(user: UserModel) {}
    override fun onCompanyClick(company: CompanyModel) {}

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