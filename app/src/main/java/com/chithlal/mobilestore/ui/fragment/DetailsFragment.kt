package com.chithlal.mobilestore.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chithlal.mobilestore.databinding.FragmentDetailsBinding
import com.chithlal.mobilestore.model.Exclusion
import com.chithlal.mobilestore.model.Features
import com.chithlal.mobilestore.model.Option
import com.chithlal.mobilestore.ui.activity.FinalActivity
import com.chithlal.mobilestore.ui.activity.PARAM_SELECTED_FEATURE
import com.chithlal.mobilestore.ui.activity.PARAM_SELECTED_PHONE
import com.chithlal.mobilestore.ui.activity.PARAM_SELECTED_STORAGE
import com.chithlal.mobilestore.ui.adapter.FeatureAdapter
import com.chithlal.mobilestore.ui.adapter.StorageOptionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_SELECTED_OPTION = "Mobile"
private const val ARG_FEATURE = "feature"


class DetailsFragment : BottomSheetDialogFragment() {

    private var selectedOption: Option? = null
    private var features: Features? = null

    // NxN matrix to represent graph
    lateinit var graph: Array<IntArray>
    lateinit var phones: List<Option>//list of phones
    lateinit var storages: List<Option> // list of storages
    lateinit var others: List<Option> // list of other features
    lateinit var exclusions: List<List<Exclusion>> // list of exclusion pairs

    val idMap = HashMap<String, Int>() // map to determine the nodes corresponding to option ids

    //adapter for storage list
    private lateinit var storageAdapter: StorageOptionAdapter

    //adapter for features list
    private lateinit var featureAdapter: FeatureAdapter

    //set to false is selected storage options are not valid, default is invalid
    private var isValidStorage = false

    //set to false is selected featurs options are not valid, default is invalid
    private var isValidFeatures = false

    private var phoneId: String? = null
    private var selectedStorageId: String? = null

    //to keep valid pairs of options
    private var selectedStorageList: ArrayList<Option>? = null
    private var selectedFeatureList: ArrayList<Option>? = null


    private lateinit var mBinding: FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedOption = it.getSerializable(ARG_SELECTED_OPTION) as Option
            features = it.getSerializable(ARG_FEATURE) as Features
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDetailsBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (selectedOption != null)
            setupView(selectedOption!!)

        if (features != null)
            prepareGraph(features!!)
    }

    private fun setupView(selectedOption: Option) {

        phoneId = selectedOption.id
        //load image
        selectedOption.icon?.let {
            Glide.with(requireActivity())
                .load(it)
                .into(mBinding.imgMobilePhone)
        }
        mBinding.tvPhoneName.text = selectedOption.name

        mBinding.btContinue.setOnClickListener {

            if (isValidStorage && isValidFeatures) {
                // user selected a valid options -> send to @FinalActivity
                val intent = Intent(requireContext(), FinalActivity::class.java)
                intent.putExtra(PARAM_SELECTED_PHONE, selectedOption)
                intent.putExtra(PARAM_SELECTED_STORAGE, selectedStorageList)
                intent.putExtra(PARAM_SELECTED_FEATURE, selectedFeatureList)
                requireContext().startActivity(intent)
            } else {
                //user selected invalid options
                Toast.makeText(
                    requireContext(),
                    "Selected Options are invalid!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /***
     * Prepare a graph representing features options as nodes and valid pairs as edges
     * if the options are connected then those pairs are valid combinations
     * */
    private fun prepareGraph(features: Features) {

        // create separate list of features
        features.features.forEach {
            when (it.name) {
                "Mobile Phone" -> {
                    phones = it.options
                }
                "Storage Options" -> {
                    storages = it.options
                }
                "Other features" -> {
                    others = it.options
                }
            }
        }
        setStorageAdapter(storages)
        //  setFeatureAdapter(others)
        exclusions = features.exclusions

        //create map of option id vs index to create graph vertex
        //this hash map will be used to determine the option_id and corresponding node in the graph
        var index = 0 // graph vertex
        phones.forEach {
            idMap[it.id] = index // map phone id and nodes
            index++
        }
        storages.forEach {
            idMap[it.id] = index // map storage id and nodes
            index++
        }
        others.forEach {
            idMap[it.id] = index //map other feature id and map
            index++
        }

        val vertexCount = idMap.size // hashmap size will be the number of nodes in the graph

        graph =
            Array(vertexCount) { IntArray(vertexCount) { 1 } } // create NxN matrix to represent graph and initialize to 1

        // find exclusion pairs and disconnect exclusions from the graph
        exclusions.forEach {

            val vertexOne = it[0].options_id
            val vertexTwo = it[1].options_id
            graph[idMap[vertexOne]!!][idMap[vertexTwo]!!] =
                0 // disconnect edges for exclusion pairs
            graph[idMap[vertexTwo]!!][idMap[vertexOne]!!] = 0

        }

        for (row in graph) {
            Log.d("graph", row.contentToString())

        }

    }

    private fun setFeatureAdapter(others: List<Option>) {

        featureAdapter =
            FeatureAdapter(requireContext(), others, object : FeatureAdapter.FeatureClickListener {
                override fun onClick(optionList: ArrayList<Option>) {
                    selectedFeatureList = optionList
                    isValidFeatures =
                        isValidOptionsSelected(optionList) // check for valid feature combinations
                }


            })

        mBinding.rvOther.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = featureAdapter
        }

    }

    private fun setStorageAdapter(storages: List<Option>) {

        storageAdapter = StorageOptionAdapter(
            requireContext(),
            storages,
            object : StorageOptionAdapter.StorageClickListener {
                override fun onClick(option: Option) {
                    selectedStorageList = arrayListOf(option)

                    if (isAvailablePair(
                            phoneId!!,
                            option.id
                        )
                    ) { // Once storage is selected check if the combination is valid
                        isValidStorage = true
                        selectedStorageId = option.id
                        setFeatureAdapter(others) // if valid storage show the feature adapter
                        mBinding.rvOther.visibility = View.VISIBLE
                    } else { // if invalid pair hide fetures
                        isValidStorage = true
                        mBinding.rvOther.visibility = View.GONE
                    }
                }

            })

        mBinding.rvStorage.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = storageAdapter
        }

    }

    /***
     * chek for valid pair in the graph ie: vertex are connected
     * return true if there is an edge between vertex
     * */
    private fun isAvailablePair(idOne: String, idTwo: String): Boolean {
        return graph[idMap[idOne]!!][idMap[idTwo]!!] != 0
    }

    /***
     * chek for valid feature selections return true if selections are valid
     * */
    private fun isValidOptionsSelected(optionList: ArrayList<Option>): Boolean {

        var isOptionsValid = true
        var idOne = phoneId // keep vertex of graph as phoneId
        optionList.forEach { // check if phone and other features are valid combination ( if vertex are connected in the graph
            if (!isAvailablePair(idOne!!, it.id)) // check if vertex are connected
                isOptionsValid = false
        }
        idOne = selectedStorageId // keep first source vertex of graph as storage id
        optionList.forEach {
            if (!isAvailablePair(
                    idOne!!,
                    it.id
                )
            ) // checking if storage and other features pair are valid
                isOptionsValid = false
        }
        return isOptionsValid
    }

    companion object {

        @JvmStatic
        fun newInstance(selectedOption: Option, features: Features) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SELECTED_OPTION, selectedOption)
                    putSerializable(ARG_FEATURE, features)
                }
            }
    }
}