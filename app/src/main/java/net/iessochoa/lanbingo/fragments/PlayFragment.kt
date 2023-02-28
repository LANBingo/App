package net.iessochoa.lanbingo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.databinding.FragmentPlayBinding
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.nCartones

class PlayFragment : Fragment() {

    private lateinit var binding: FragmentPlayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sbNcartones.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progreso: Int, p2: Boolean) {
                binding.tvNcartones.text = getString(R.string.nCartones, progreso+1)
                nCartones = progreso+1
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.sbNcartones.progress=0
        binding.tvNcartones.text = getString(R.string.nCartones,1)
    }
}