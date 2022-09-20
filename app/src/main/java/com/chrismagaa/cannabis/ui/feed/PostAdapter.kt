package com.chrismagaa.cannabis.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chrismagaa.cannabis.R
import com.chrismagaa.cannabis.databinding.ItemPostBinding
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.utils.fromHTMLToSpanned

class PostAdapter(
    private val onItemClickListener: (Post) -> Unit,
    private val onSaveClick: (Post, Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var items: List<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClickListener) {
            onSaveClick(it, position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(items: List<Post>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun updateItem(position: Int){
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int){
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPostBinding.bind(itemView)
        fun bind(item: Post, onItemClickListener: (Post) -> Unit, onSaveClick: (Post) -> Unit) {
            if(item.getPictureURLFromDescription().isNotBlank()){
                binding.cvPicture.visibility = View.VISIBLE
                binding.ivPicture.load(item.getPictureURLFromDescription()) {
                    crossfade(true)
                }
            }else{
                binding.cvPicture.visibility = View.GONE
            }

            binding.tvDate.text = item.getDateFormatted()
            binding.tvTitle.text = item.title.fromHTMLToSpanned()
            binding.tvTittleBlog.text = item.titleBlog.fromHTMLToSpanned()

            if(item.isFavorite){
                binding.btnSave.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_baseline_turned_in_24))
            }else{
                binding.btnSave.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_outline_bookmark_add_24))
            }
            binding.btnSave.setOnClickListener {
                onSaveClick(item)
            }

            Log.d("FeedAdapter", "${item.getPictureURLFromDescription()}")

            itemView.setOnClickListener {
               onItemClickListener(item)
            }
        }
    }




}