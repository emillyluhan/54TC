package com.example.tutorconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorconnect.R
import com.example.tutorconnect.models.Comment
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(
    private val comments: List<Comment>,
    private val currentUserId: String,
    private val onEditComment: (Comment) -> Unit,
    private val onDeleteComment: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.commentUserName)
        val content: TextView = view.findViewById(R.id.commentContent)
        val timestamp: TextView = view.findViewById(R.id.commentTimestamp)
        val optionsButton: ImageButton = view.findViewById(R.id.commentOptionsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.userName.text = comment.userName
        holder.content.text = comment.content
        
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.timestamp.text = dateFormat.format(comment.createdAt)

        // Solo mostrar opciones si el comentario es del usuario actual
        if (comment.userId == currentUserId) {
            holder.optionsButton.visibility = View.VISIBLE
            holder.optionsButton.setOnClickListener { view ->
                showPopupMenu(view, comment)
            }
        } else {
            holder.optionsButton.visibility = View.GONE
        }
    }

    private fun showPopupMenu(view: View, comment: Comment) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.comment_options_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onEditComment(comment)
                        true
                    }
                    R.id.action_delete -> {
                        onDeleteComment(comment)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    override fun getItemCount() = comments.size
}
