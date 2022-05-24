package com.example.matchball.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matchball.JoinActivity
import com.example.matchball.R
import com.example.matchball.adapter.RecyclerAdapter
import com.example.matchball.databinding.FragmentListBinding
import com.example.matchball.model.MatchRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ListFragment : Fragment() {

    private lateinit var listFragmentBinding : FragmentListBinding
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var matchRequestArrayList : ArrayList<MatchRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun getMatchRequestData() {

        val uid = firebaseAuth.currentUser!!.uid

        firebaseReference = FirebaseDatabase.getInstance().getReference("MatchRequest").child(uid)
        firebaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(MatchRequest::class.java)
                        matchRequestArrayList.add(request!!)
                    }

                    //recyclerView.adapter = RecyclerAdapter(matchRequestArrayList)
                    listFragmentBinding.rcvMatchRequest.adapter = recyclerAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFragmentBinding.rcvMatchRequest.apply {
            layoutManager = LinearLayoutManager(context)
//            adapter = recyclerAdapter
            matchRequestArrayList = arrayListOf<MatchRequest>()
            recyclerAdapter = RecyclerAdapter(matchRequestArrayList)
            getMatchRequestData()
        }

//        recyclerView = listFragmentBinding.rcvMatchRequest
//        recyclerView.layoutManager = LinearLayoutManager(context)
////        recyclerView.setHasFixedSize(true)
//        matchRequestArrayList = arrayListOf<MatchRequest>()
//        getMatchRequestData()

        recyclerAdapter.setOnItemClickListerner(object : RecyclerAdapter.OnItemClickListerner{
            override fun onItemClick(requestData: MatchRequest) {
                JoinActivity.startDetails(requireContext(), requestData)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        listFragmentBinding = FragmentListBinding.inflate(inflater, container, false)
        return listFragmentBinding.root
    }

    companion object {

        fun newInstance() = ListFragment()
    }
}