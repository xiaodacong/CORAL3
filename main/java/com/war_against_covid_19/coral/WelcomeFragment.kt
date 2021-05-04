package com.war_against_covid_19.coral

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_localNewsFragment)
        }
        view.findViewById<Button>(R.id.statistic_button).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_staticticFragment)
        }
        view.findViewById<Button>(R.id.risk_level_and_tracking_button).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_riskLevelAndTrackingFragment)
        }
        view.findViewById<Button>(R.id.location_rl_button).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_regionFragment)
        }
        view.findViewById<Button>(R.id.upload_tracking_button).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_uploadUserLocationFragment)
        }
        view.findViewById<Button>(R.id.voice_input_button).setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_uploadVoiceFragment)
        }
    }
}