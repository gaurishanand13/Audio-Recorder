package com.example.audiorecorder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_final.view.*
import java.io.File


class CourseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

interface myInterface{
    abstract fun itemClicked(position: Int)
}

class myAdapter(var audioNames : ArrayList<String>,val context: Context) : RecyclerView.Adapter<CourseViewHolder>(){


    var inter : myInterface? = null

    override fun getItemCount() = audioNames.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_final,parent,false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.itemView.textView.text = audioNames.get(position)
        Log.i("recyc",audioNames.get(position))
        holder.itemView.setOnClickListener {
            inter?.itemClicked(position)
        }
    }
}