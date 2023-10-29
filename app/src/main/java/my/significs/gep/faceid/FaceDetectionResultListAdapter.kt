package my.significs.gep.faceid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import my.significs.gep.faceid.data.ResultModel
import my.significs.gep.faceid.model.CompanyModel


class FaceDetectionResultListAdapter(
    private val context: Context,
    private val dataset: List<ResultModel>,
): RecyclerView.Adapter<FaceDetectionResultListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val resultNameTV: TextView = view.findViewById(R.id.resultNameTV)
        val resultEmailTV: TextView = view.findViewById(R.id.resultEmailTV)
        val resultIdTV: TextView = view.findViewById(R.id.resultIdTV)
        val resultPerTV: TextView = view.findViewById(R.id.resultPerTV)

        val listItemMCV: MaterialCardView = view.findViewById(R.id.resultListMCV)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.resultNameTV.text = item.name
        holder.resultEmailTV.text = item.email
        holder.resultIdTV.text = item.employeeID
        holder.resultPerTV.text = item.percentage

        holder.listItemMCV.setOnClickListener { view ->
//            onCompanyClickListener.onCompanyClick(item)
            view.findNavController().navigate(R.id.action_navigation_result_to_details)
        }
    }

    override fun getItemCount() = dataset.size
}