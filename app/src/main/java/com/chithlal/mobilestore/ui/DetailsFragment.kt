package com.chithlal.mobilestore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chithlal.mobilestore.R
import com.chithlal.mobilestore.model.Feature
import com.chithlal.mobilestore.model.Option
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_SELECTED_OPTION = "Mobile"
private const val ARG_FEATURE = "feature"


class DetailsFragment : BottomSheetDialogFragment() {

    private var selectedOption: Option? = null
    private var features: Feature? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedOption = it.getSerializable(ARG_SELECTED_OPTION) as Option
            features = it.getSerializable(ARG_FEATURE) as Feature
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(selectedOption: Option, feature: Feature) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SELECTED_OPTION, selectedOption)
                    putSerializable(ARG_FEATURE, feature)
                }
            }
    }
}