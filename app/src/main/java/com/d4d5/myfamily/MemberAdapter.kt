package com.d4d5.myfamily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val listMembers: List<MemberModel>) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_member, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {

        val item = listMembers[position]
        holder.name.text = item.name
        holder.address.text = item.address
        holder.battery.text = item.battery
        holder.distance.text = item.distance

    }

    override fun getItemCount(): Int {
        return listMembers.size
    }

    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        val imageUser = item.findViewById<ImageView>(R.id.img_user)
        val name = item.findViewById<TextView>(R.id.name)
        val address = item.findViewById<TextView>(R.id.address)
        val battery = item.findViewById<TextView>(R.id.battery_percent)
        val distance = item.findViewById<TextView>(R.id.distance_value)

    }
}