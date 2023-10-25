package my.significs.gep.faceid.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentPasswordBinding
import my.significs.gep.faceid.databinding.FragmentProfileBinding

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "CHANGE PASSWORD"
        val root: View = binding.root


        return root
    }

}