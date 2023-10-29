package my.significs.gep.faceid.ui.facedetection

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.significs.gep.faceid.CompanyListAdapter
import my.significs.gep.faceid.FaceDetectionResultListAdapter
import my.significs.gep.faceid.ImageListAdapter
import my.significs.gep.faceid.MainActivity
import my.significs.gep.faceid.OnCompanyClickListener
import my.significs.gep.faceid.R
import my.significs.gep.faceid.data.Datasource
import my.significs.gep.faceid.databinding.FragmentCompanyListBinding
import my.significs.gep.faceid.databinding.FragmentFaceDetectionResultBinding
import my.significs.gep.faceid.ui.company.CompanyViewModel

class FaceDetectionFragment : Fragment() {
    private var _binding: FragmentFaceDetectionResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var  recycler_view : RecyclerView
    private val companyViewModel by activityViewModels<CompanyViewModel>()

    private var firstLoad : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceDetectionResultBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "RESULT"

        val root: View = binding.root

        recycler_view = binding.resultListRV

        val myDataset = Datasource().loadResultList()

        companyViewModel.firstLoad.observe(viewLifecycleOwner) { it
            if(it) {
                showSuccess("Update success", "Your face has been updated in the system")
                companyViewModel.onFirstLoad()
            }
        }

        recycler_view.adapter = FaceDetectionResultListAdapter(
            requireContext(),
            myDataset,
        )

        return root
    }

    private fun showSuccess(mainText: String, sucText: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.success_update_dialog)

        dialog.show()

        val successTV = dialog.findViewById(R.id.successTV) as TextView
        val mainDialogTV = dialog.findViewById(R.id.mainDialogTV) as TextView

        successTV.text = sucText
        mainDialogTV.text = mainText

        object : CountDownTimer(1000, 1000) {
            // Callback function, fired
            // when the time is up
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                dialog.dismiss()
//                findNavController().navigate(R.id.action_navigation_new_to_result)
            }
        }.start()
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