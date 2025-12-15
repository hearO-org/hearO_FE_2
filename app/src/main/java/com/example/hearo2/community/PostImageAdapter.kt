package com.example.hearo2.community

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hearo2.R

class PostImageAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<PostImageAdapter.ViewHolder>() {

    inner class ViewHolder(
        val imageView: ImageView
    ) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_image, parent, false) as ImageView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.imageView)
            .load(images[position])
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size
}