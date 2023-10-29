package my.significs.gep.faceid.ui.logout

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentLogoutBinding
import my.significs.gep.faceid.databinding.FragmentNotificationsBinding
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel

class LogoutFragment : Fragment() {
    private var _binding: FragmentLogoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dashboardViewModel by activityViewModels<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "LOGOUT"
        val root: View = binding.root

        val logoutBtn: Button = binding.logoutButton
        logoutBtn?.setOnClickListener(View.OnClickListener {
            showDialog()
        })
        return root
    }


    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_dialog)

        val yesBtn = dialog.findViewById(R.id.confirmBtn) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_navigation_logout_to_login)
        }

        val noBtn = dialog.findViewById(R.id.cancelBtn) as Button
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}