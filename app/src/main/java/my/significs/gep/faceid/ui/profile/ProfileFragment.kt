package my.significs.gep.faceid.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.significs.gep.faceid.R
import my.significs.gep.faceid.databinding.FragmentProfileBinding

class ProfileFragment : Fragment()  {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "PROFILE"
        val root: View = binding.root

        val passwordArrowIV = binding.root.findViewById(R.id.passwordArrowIV) as ImageView
        passwordArrowIV.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            findNavController().navigate(R.id.action_navigation_profile_to_password)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}