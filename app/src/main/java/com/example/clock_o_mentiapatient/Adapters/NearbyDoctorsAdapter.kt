package com.example.clock_o_mentiapatient.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clock_o_mentiapatient.databinding.NearbyDoctorsItemBinding
import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorDetails

class NearbyDoctorsAdapter(
    private var nearbyDoctorList : List<DoctorDetails?>,
    private val nearbyDoctorsInterface: NearbyDoctorsInterface
) : RecyclerView.Adapter<NearbyDoctorsAdapter.NearbyDoctorsViewHolder>() {


    interface NearbyDoctorsInterface {
        fun onDoctorSelected(pos : Int)
    }

    inner class NearbyDoctorsViewHolder(val binding : NearbyDoctorsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyDoctorsViewHolder {
        return NearbyDoctorsViewHolder(NearbyDoctorsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NearbyDoctorsViewHolder, position: Int) {
        holder.binding.doctorData = nearbyDoctorList[position]
        holder.itemView.setOnClickListener {
            nearbyDoctorsInterface.onDoctorSelected(position)
        }
    }

    fun updateList(newList : List<DoctorDetails>){
        nearbyDoctorList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return nearbyDoctorList.size
    }
}

