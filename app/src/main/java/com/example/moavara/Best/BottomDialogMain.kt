package com.example.moavara.Best

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.Main.ActivityGenre
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.BottomDialogMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogMain :
    BottomSheetDialogFragment() {

    private var _binding: BottomDialogMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogMainBinding.inflate(inflater, container, false)
        val view = binding.root

        val cate = Genre.getGenre(requireContext()).toString()

        when (cate) {
            "ALL" -> {
                binding.tviewGenre.text = "장르 무관"
            }
            "FANTASY" -> {
                binding.tviewGenre.text = "판타지"
            }
            "ROMANCE" -> {
                binding.tviewGenre.text = "로맨스"
            }
            "BL" -> {
                binding.tviewGenre.text = "BL"
            }
        }

        binding.llayoutSubmit.setOnClickListener {
//            Snackbar.make(toolbar, "Menu pressed", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(context, ActivityGenre::class.java)
            intent.putExtra("MODE", "USER")
            intent.putExtra("UID",requireContext().getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE).getString("UID", ""))
            context?.startActivity(intent)
        }

        binding.tviewDismiss.setOnClickListener {
            dismiss()
        }

        return view
    }
}