package com.web151.criminalintent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.web151.criminalintent.databinding.ListItemCrimeBinding
import com.web151.criminalintent.databinding.ListItemNeedsPoliceBinding

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${crime.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

// Implemented a new class to bind the 2nd view to
class ExtremeCrimeHolder(
    private val binding: ListItemNeedsPoliceBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()
        binding.callPoliceButton.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "Calling police for crime ${crime.title}!",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${crime.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // This is where the challenges starts page 338
    // Had to change to RecyclerView.ViewHolder so it would get the appropriate view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        // looks for the view type from the getItemViewType() then binds the xml layout
        return when (viewType) {
            0 -> {
                val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
                CrimeHolder(binding)
            }
            1 -> {
                val binding = ListItemNeedsPoliceBinding.inflate(inflater, parent, false)
                ExtremeCrimeHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid viewType. CrimesListAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]

        // Determines which ViewHolder was passed then sets the data from the crime object
        when (holder) {
            is CrimeHolder -> holder.bind(crime)
            is ExtremeCrimeHolder -> holder.bind(crime)
        }
    }

    override fun getItemCount() = crimes.size

    override fun getItemViewType(position: Int): Int {
        val crime = crimes[position]

        return if (crime.requiresPolice) {
            1
        } else {
            0
        }
    }
}

