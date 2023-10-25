package my.significs.gep.faceid.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.CompanyListAdapter
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.databinding.FragmentCompanyListBinding
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel


class CompanyListFragment : Fragment(), OnCompanyClickListener {
    private var _binding: FragmentCompanyListBinding? = null
    private val binding get() = _binding!!
    private lateinit var  recycler_view : RecyclerView
    private val companyViewModel by activityViewModels<CompanyViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyListBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "HOME"

        val root: View = binding.root

        recycler_view = binding.recyclerView

        companyViewModel.companyList.observe(viewLifecycleOwner) { it
            recycler_view.adapter = CompanyListAdapter(
                requireContext(),
                it,
                this
            )
        }

        return root
    }
    override fun onUserClick(user: UserModel) { }
    override fun onCompanyClick(company: CompanyModel) {
        companyViewModel.onLoadUsers(company)
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