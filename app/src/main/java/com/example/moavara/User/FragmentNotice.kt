package com.example.moavara.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Search.FCMAlert
import com.example.moavara.databinding.FragmentNoticesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class FragmentNotice : Fragment() {

    private var _binding: FragmentNoticesBinding? = null
    private val binding get() = _binding!!
    var bookCode = ""
    private var adapter: AdapterNotice? = null
    private val items = ArrayList<FCMAlert>()
    val ref = FirebaseDatabase.getInstance().reference.child("Message")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoticesBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = AdapterNotice(items)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapter

        ref.child("Notice").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    for (postSnapshot in dataSnapshot.children) {

                        val group: FCMAlert? =
                            postSnapshot.getValue(FCMAlert::class.java)

                        if (group != null) {
                            items.add(FCMAlert(
                                group.date,
                                group.title,
                                group.body,
                            ))
                        }
                    }

                    val cmpAsc: java.util.Comparator<FCMAlert> =
                        Comparator { o1, o2 -> o2.date.compareTo(o1.date) }
                    Collections.sort(items, cmpAsc)

                    adapter?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapter?.setOnItemClickListener(object : AdapterNotice.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: FCMAlert? = adapter?.getItem(position)

            }
        })

        return view
    }
}