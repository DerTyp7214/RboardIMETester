package de.dertyp7214.rboardimetester.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboardcomponents.components.CheckCard
import de.dertyp7214.rboardimetester.data.CheckCardData

class CheckCardAdapter(
    private val context: Context,
    private val list: List<CheckCardData>
) : RecyclerView.Adapter<CheckCardAdapter.ViewHolder>() {
    private var recyclerView: RecyclerView? = null

    fun uncheckAll(except: CheckCard?) {
        recyclerView?.allViews?.filter { it is CheckCard && it != except }?.forEach {
            if (it is CheckCard) it.isChecked = false
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val checkCard: CheckCard = v.findViewById(CheckCard.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CheckCard.inflate(context, parent))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.checkCard.icon = item.icon
        holder.checkCard.text = item.text
        holder.checkCard.isChecked = item.isChecked
        holder.checkCard.setOnClickListener {
            uncheckAll(holder.checkCard)
            if (!it) holder.checkCard.isChecked = true
            else item.clickListener(true)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }
}