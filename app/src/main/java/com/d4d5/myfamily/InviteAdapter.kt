package com.d4d5.myfamily

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.d4d5.myfamily.databinding.ItemInviteBinding

class InviteAdapter(private val listContacts: List<ContactModel>) :
    RecyclerView.Adapter<InviteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = ItemInviteBinding.inflate(inflater, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: InviteAdapter.ViewHolder, position: Int) {

        val item = listContacts[position]
        holder.name.text = item.name

    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    class ViewHolder(private val item: ItemInviteBinding) : RecyclerView.ViewHolder(item.root) {
        val name = item.name

    }
}