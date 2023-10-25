package my.significs.gep.faceid.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.ImageListAdapter
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel
import my.significs.gep.faceid.ui.company.CompanyViewModel

class HomeFragment : Fragment(), OnCompanyClickListener {

    private var _binding: FragmentHomeBinding? = null

        @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var  editTextText : EditText
    private lateinit var  employeeET : EditText
    private lateinit var  accessCardET : EditText
    private lateinit var  editTV : TextView

    private lateinit var homeCL : ConstraintLayout
    private lateinit var  recycler_view : RecyclerView
    private val companyViewModel by activityViewModels<CompanyViewModel>()

    private var userList: List<UserModel> = listOf<UserModel>(
        UserModel(
            "",
            "",
            "",
            ""
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "PROFILE"
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