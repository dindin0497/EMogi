package com.my.emogi

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import kotlinx.android.synthetic.main.list_item.view.*
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class RecyclerAdapter (val items : ArrayList<Result>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG=RecyclerAdapter::class.java.simpleName

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val word = view.tv_word
        val imgview=view.img
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myholder=holder as ViewHolder
        myholder.word.text=items.get(position).word

        val url=items.get(position).url
        GlideApp.with(context)
                .load(url)
                .into(myholder.imgview)

    }

    override fun getItemCount(): Int {
        return items.size
    }


}