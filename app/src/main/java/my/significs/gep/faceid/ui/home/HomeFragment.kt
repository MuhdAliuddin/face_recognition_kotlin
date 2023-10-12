package my.significs.gep.faceid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.databinding.FragmentHomeBinding
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var  editTextText : EditText
    private lateinit var  employeeET : EditText
    private lateinit var  accessCardET : EditText

    private lateinit var homeCL : ConstraintLayout

    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        editTextText = binding.editTextText
        employeeET = binding.employeeET
        accessCardET = binding.accessCardET

        dashboardViewModel.predictionResult.observe(viewLifecycleOwner) { it
            editTextText.setText(it.name)
            employeeET.setText(it.employeeId)
            accessCardET.setText(it.icNum)
        }
        return root
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