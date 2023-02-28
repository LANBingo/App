package net.iessochoa.lanbingo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.iessochoa.lanbingo.databinding.FragmentConnectionBinding

class ConnectionFragment : Fragment() {

    private lateinit var binding: FragmentConnectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectionBinding.inflate(layoutInflater)
        return binding.root
    }
}