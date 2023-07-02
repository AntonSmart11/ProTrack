package com.antonsmart.protrack

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.antonsmart.protrack.databinding.FragmentPageProjectBinding

class PageProjectFragment : Fragment(R.layout.fragment_page_project) {

    private lateinit var binding: FragmentPageProjectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageProjectBinding.bind(view)

        val progressBar= binding.progressBar

        progressBar.post {

            val progressBarWidth = progressBar.width

            val progress = (progressBarWidth * 0.6).toFloat()
            val background = (progressBarWidth * 0.4).toFloat()

            val progressLayout = binding.progressBarColor1
            val backgroundLayout = binding.progressBarColor2

            val layoutParams1 = progressLayout.layoutParams as LinearLayout.LayoutParams
            val layoutParams2 = backgroundLayout.layoutParams as LinearLayout.LayoutParams

            layoutParams1.weight = progress
            layoutParams2.weight = background

            progressLayout.layoutParams = layoutParams1
            backgroundLayout.layoutParams = layoutParams2

        }

    }

}