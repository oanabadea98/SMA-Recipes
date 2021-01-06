package com.example.myapplication.ui.dashboard

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.PhotoAdapter
import com.example.myapplication.Helpers.FirebaseHelper
import com.example.myapplication.Models.ListPhotoModel
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class DashboardFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: PhotoAdapter? = null
    var list: MutableList<ListPhotoModel> = ArrayList()
    var progressDialog: ProgressDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = root.findViewById<View>(R.id.rv_image_list) as RecyclerView
        FirebaseHelper.recipeDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.removeAll(list)
                for (postSnapshot in snapshot.children) {

                    //extragere sub forma ListPhotoModel
                    val nameRetrieved = postSnapshot.child("imageName").value.toString()
                    val timeRetrieved = postSnapshot.child("imageTime").value.toString()
                    val typeRetreived = postSnapshot.child("imageType").value.toString()
                    val imageRetreived = postSnapshot.child("imageURL").value.toString()
                    val ingredientsRetrieved = postSnapshot.child("imageIngredients").value.toString()
                    val prepRetrieved = postSnapshot.child("imagePrep").value.toString()
                    val listphoto = ListPhotoModel(imageRetreived, nameRetrieved, timeRetrieved, typeRetreived, ingredientsRetrieved, prepRetrieved)
                    list.add(listphoto)
                }
                setRecyclerView()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return root
    }

    fun setRecyclerView() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        adapter = PhotoAdapter(list)
        recyclerView!!.adapter = adapter
    }
}