package com.example.takealook.Search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.takealook.R

class FragmentSearch : Fragment() {

    var etext_search : EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_search, container, false)

        etext_search = root.findViewById(R.id.etext_search)

        etext_search!!.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val intent = Intent(requireContext().applicationContext, ActivitySearch::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        return root
    }

}