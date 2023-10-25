package my.significs.gep.faceid.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.UserListAdapter
import my.significs.gep.faceid.databinding.FragmentUserListBinding
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel
import my.significs.gep.faceid.ui.company.CompanyViewModel


class UserListFragment : Fragment(), OnCompanyClickListener {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var  recycler_view : RecyclerView
    private val companyViewModel by activityViewModels<CompanyViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "USER"
        val root: View = binding.root

        recycler_view = binding.userListRV

        companyViewModel.userList.observe(viewLifecycleOwner) { it
            recycler_view.adapter = UserListAdapter(
                requireContext(),
                it,
                this
            )
        }

        return root
    }
    override fun onUserClick(user: UserModel) {
        companyViewModel.onSelectUser(user)
    }
    override fun onCompanyClick(company: CompanyModel) {}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}