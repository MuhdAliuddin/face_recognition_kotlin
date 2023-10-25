package my.significs.gep.faceid.ui.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import my.significs.gep.faceid.CompanyListAdapter
import my.significs.gep.faceid.databinding.FragmentCompanyListBinding
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import my.significs.gep.faceid.ui.company.CompanyViewModel

class UserDetailsFragment : Fragment() {
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//    private val companyViewModel by activityViewModels<CompanyViewModel>()
//
//    private lateinit var  editTextText : EditText
//    private lateinit var  employeeET : EditText
//    private lateinit var  accessCardET : EditText
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        editTextText = binding.editTextText
//        employeeET = binding.employeeET
//        accessCardET = binding.accessCardET
//
//
////        companyViewModel.selectedUser.observe(viewLifecycleOwner) { it
////            Log.println(Log.ASSERT, "editTextText", "${it.toString()}")
////            editTextText.setText(it.name)
////            employeeET.setText(it.employeeID)
////            accessCardET.setText(it.email)
////        }
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}