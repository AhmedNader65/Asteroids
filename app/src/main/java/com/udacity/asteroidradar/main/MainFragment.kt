package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val asteroidsAdapter = AsteroidsAdapter(AsteroidClick { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
        viewModel.asteroids.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = false
            asteroidsAdapter.asteroids = it
        }
        viewModel.pod.observe(viewLifecycleOwner) {
            viewModel.cachePOD(it)
        }
        binding.asteroidRecycler.apply {
            adapter = asteroidsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

/**
 * Click listener for asteroids. By giving the block a name it helps a reader understand what it does.
 *
 */
class AsteroidClick(val block: (Asteroid) -> Unit) {
    /**
     * Called when an asteroid is clicked
     *
     * @param asteroid the asteroid that was clicked
     */
    fun onClick(asteroid: Asteroid) = block(asteroid)
}

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class AsteroidsAdapter(val callback: AsteroidClick) : RecyclerView.Adapter<AsteroidsHolder>() {

    /**
     * The asteroids that our Adapter will show
     */
    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsHolder {
        val binding: ItemAsteroidBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidsHolder.LAYOUT,
            parent,
            false
        )
        return AsteroidsHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidsHolder, position: Int) {
        holder.itemAsteroidBinding.also {
            it.asteroid = asteroids[position]
            it.asteroidCallback = callback
        }
    }

    override fun getItemCount(): Int = asteroids.size

}

class AsteroidsHolder(val itemAsteroidBinding: ItemAsteroidBinding) :
    RecyclerView.ViewHolder(itemAsteroidBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_asteroid
    }
}
