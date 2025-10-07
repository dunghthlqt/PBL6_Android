package com.demo.pbl6_android.ui.address.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.databinding.ItemAddressBinding

class AddressAdapter(
    private val addresses: List<Address>,
    private val onSetDefaultClick: (Address) -> Unit,
    private val onEditClick: (Address) -> Unit,
    private val onDeleteClick: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount(): Int = addresses.size

    inner class AddressViewHolder(
        private val binding: ItemAddressBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.apply {
                // Show/hide default badge
                tvDefaultBadge.visibility = if (address.isDefault) View.VISIBLE else View.GONE
                
                // Set recipient info
                tvRecipientInfo.text = "${address.recipientName} | ${address.phoneNumber}"
                
                // Set full address
                tvFullAddress.text = address.fullAddress
                
                // Show/hide set default button
                btnSetDefault.visibility = if (!address.isDefault) View.VISIBLE else View.GONE
                
                // Set click listeners
                btnSetDefault.setOnClickListener {
                    onSetDefaultClick(address)
                }
                
                btnEdit.setOnClickListener {
                    onEditClick(address)
                }
                
                btnDelete.setOnClickListener {
                    onDeleteClick(address)
                }
            }
        }
    }
}

