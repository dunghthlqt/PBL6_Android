package com.demo.pbl6_android.ui.address.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Address
import com.google.android.material.button.MaterialButton

class AddressAdapter(
    private val addresses: List<Address>,
    private val onAddressSelected: (Address) -> Unit,
    private val onEditAddress: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount(): Int = addresses.size

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRecipientName: TextView = itemView.findViewById(R.id.tv_recipient_name)
        private val tvPhoneNumber: TextView = itemView.findViewById(R.id.tv_phone_number)
        private val tvFullAddress: TextView = itemView.findViewById(R.id.tv_full_address)
        private val tvDefaultBadge: TextView = itemView.findViewById(R.id.tv_default_badge)
        private val btnEdit: MaterialButton = itemView.findViewById(R.id.btn_edit)

        fun bind(address: Address) {
            tvRecipientName.text = address.recipientName
            tvPhoneNumber.text = address.phoneNumber
            tvFullAddress.text = address.fullAddress
            tvDefaultBadge.visibility = if (address.isDefault) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                onAddressSelected(address)
            }

            btnEdit.setOnClickListener {
                onEditAddress(address)
            }
        }
    }
}
