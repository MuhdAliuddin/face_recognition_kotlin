package my.significs.gep.faceid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import my.significs.gep.faceid.model.UserModel


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [Affirmation] data object.
 */
class UserListAdapter(
    private val context: Context,
    private val dataset: List<UserModel>,
    private val onCompanyClickListener: OnCompanyClickListener
): RecyclerView.Adapter<UserListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val nameTV: TextView = view.findViewById(R.id.nameTV)
        val emailTV: TextView = view.findViewById(R.id.emailTV)
        val employeeIDTV: TextView = view.findViewById(R.id.employeeIDTV)

        val listItemMCV: MaterialCardView = view.findViewById(R.id.listItemMCV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.nameTV.text = item.name
        holder.emailTV.text = item.email
        holder.employeeIDTV.text = item.employeeID

        holder.listItemMCV.setOnClickListener { view ->
            onCompanyClickListener.onUserClick(item)
            view.findNavController().navigate(R.id.action_navigation_employee_to_details)
        }
    }

    override fun getItemCount() = dataset.size
}