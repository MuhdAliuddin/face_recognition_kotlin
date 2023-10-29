package my.significs.gep.faceid.ui.password

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentPasswordBinding
import my.significs.gep.faceid.databinding.FragmentProfileBinding

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var  changeBtn : Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "CHANGE PASSWORD"

        changeBtn = binding.changeBtn

        changeBtn.setOnClickListener {
            showSuccess()
        }

        val root: View = binding.root


        return root
    }

    private fun showSuccess() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.success_update_password_dialog)

        dialog.show()

        object : CountDownTimer(1000, 1000) {
            // Callback function, fired
            // when the time is up
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                dialog.dismiss()
                findNavController().navigate(R.id.action_navigation_password_to_profile)

            }
        }.start()
    }
}