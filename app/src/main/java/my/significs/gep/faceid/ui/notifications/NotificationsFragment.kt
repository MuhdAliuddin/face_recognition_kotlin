package my.significs.gep.faceid.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentNotificationsBinding
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val logoutBtn: Button = binding.logoutButton
        logoutBtn?.setOnClickListener(View.OnClickListener {
            showConfirmDialog()
        })
        return root
    }

        private fun showConfirmDialog() {
        val alertDialog = AlertDialog.Builder( requireContext() ).apply {
            setTitle( "Confirm Dialog ")
            setMessage( "Confirm logout?" )
            setCancelable( false )
            setPositiveButton( "CONFIRM") { dialog, which ->
                dashboardViewModel.onClearScan()
                dialog.dismiss()
                findNavController().navigate(R.id.action_navigation_noti_to_logout)
            }
            setNegativeButton("CANCEL") {dialog, which ->
                dialog.dismiss()
            }
            create()
        }
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}