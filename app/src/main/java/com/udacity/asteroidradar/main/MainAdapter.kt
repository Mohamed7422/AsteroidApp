package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.databinding.ItemViewMainBinding

class MainAdapter (val onClickListener: OnClickListener): ListAdapter<Asteroid,MainAdapter.AsteroidViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener:(asteroid : Asteroid) -> Unit){
        fun onClick(asteroid: Asteroid) =clickListener(asteroid)
    }


    class AsteroidViewHolder(private var binding : ItemViewMainBinding)
                                :RecyclerView.ViewHolder(binding.root){
        /***
         *binding the data from data list to the correct
         * view and make the execute method to execute the data binding immediately
         */

        fun bind(asteroid: Asteroid){
                binding.asteroid = asteroid
                binding.executePendingBindings()
            }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid> (){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
           return oldItem.id==newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ItemViewMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)

        holder.itemView.setOnClickListener{
         onClickListener.onClick(asteroid)
        }
    }
}


