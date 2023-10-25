package my.significs.gep.faceid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import my.significs.gep.faceid.model.CompanyModel
import my.significs.gep.faceid.model.UserModel

class ImageListAdapter(
    private val context: Context,
    private val dataset: List<CompanyModel>,
    private val onCompanyClickListener: OnCompanyClickListener,
    private val editStatus: Boolean,
): RecyclerView.Adapter<ImageListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val nameTV: TextView = view.findViewById(R.id.fileNameTV)
        val imgItemIV: ImageView = view.findViewById(R.id.imgItemIV)
        val activePillTV: TextView = view.findViewById(R.id.activePillTV)
        val listItemMCV: MaterialCardView = view.findViewById(R.id.imageListMCV)

        val activeBTN: Button = view.findViewById(R.id.activeBTN)
        val deleteBTN: Button = view.findViewById(R.id.deleteBTN)

        fun onEditStatusFalse() {
            val params = listItemMCV.layoutParams
            activeBTN.visibility == View.INVISIBLE
            params.width = ViewGroup.LayoutParams.MATCH_PARENT

            listItemMCV.layoutParams = params
        }

        fun onEditStatusTrue() {
            val params = listItemMCV.layoutParams
            activeBTN.visibility == View.VISIBLE
            params.width = 300
            params.height = 80
            listItemMCV.layoutParams = params
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.nameTV.text = item.name
        holder.activePillTV
        if(editStatus) {
            holder.onEditStatusTrue()
        } else {
            holder.onEditStatusFalse()
        }
    }

    override fun getItemCount() = dataset.size
}