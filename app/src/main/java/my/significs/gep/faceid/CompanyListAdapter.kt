package my.significs.gep.faceid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel
import my.significs.gep.faceid.ui.company.CompanyViewModel
import my.significs.gep.faceid.ui.dashboard.DashboardViewModel


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [Affirmation] data object.
 */


class CompanyListAdapter(
    private val context: Context,
    private val dataset: List<CompanyModel>,
    private val onCompanyClickListener: OnCompanyClickListener
): RecyclerView.Adapter<CompanyListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val nameTV: TextView = view.findViewById(R.id.companyNameTV)
        val listItemMCV: MaterialCardView = view.findViewById(R.id.companyListMCV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.company_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.nameTV.text = item.name
        holder.listItemMCV.setOnClickListener { view ->
            onCompanyClickListener.onCompanyClick(item)
            view.findNavController().navigate(R.id.action_navigation_company_to_employee)
        }
    }

    override fun getItemCount() = dataset.size
}