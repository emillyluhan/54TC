package com.example.tutorconnect

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorconnect.adapters.AnnouncementAdapter
import com.example.tutorconnect.adapters.CommentAdapter
import com.example.tutorconnect.models.Announcement
import com.example.tutorconnect.models.AnnouncementType
import com.example.tutorconnect.models.Comment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AnnouncementsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAnnouncements)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = AnnouncementAdapter(
            announcements,
            onLikeClick = { announcement, likeButton -> handleLike(announcement, likeButton) },
            onCommentClick = { announcement -> showCommentsDialog(announcement) }
        )
        recyclerView.adapter = adapter

        // Configurar FAB para crear nuevo anuncio (solo visible para tutores)
        val fab: FloatingActionButton = findViewById(R.id.fabCreateAnnouncement)
        // TODO: Verificar si el usuario es tutor
        fab.setOnClickListener {
            showCreateAnnouncementDialog()
        }

        // Configurar botón de retroceso
        findViewById<View>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        // Cargar anuncios
        loadAnnouncements()
    }

    private fun handleLike(announcement: Announcement, likeButton: ImageButton) {
        announcement.isLikedByUser = !announcement.isLikedByUser
        if (announcement.isLikedByUser) {
            announcement.likesCount++
            likeButton.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        } else {
            announcement.likesCount--
            likeButton.setColorFilter(ContextCompat.getColor(this, R.color.gray_dark))
        }
        adapter.notifyDataSetChanged()
        // TODO: Actualizar like en la base de datos
    }

    private fun showCommentsDialog(announcement: Announcement) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_comments)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val commentInput = dialog.findViewById<TextInputEditText>(R.id.commentInput)
        val sendButton = dialog.findViewById<ImageButton>(R.id.sendCommentButton)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val commentsList = mutableListOf<Comment>()
        lateinit var commentAdapter: CommentAdapter

        // Configurar el RecyclerView y el adaptador
        recyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(
            comments = commentsList,
            currentUserId = "current_user_id", // TODO: Obtener ID real del usuario
            onEditComment = { comment ->
                showEditCommentDialog(comment) { editedContent ->
                    val index = commentsList.indexOfFirst { it.id == comment.id }
                    if (index != -1) {
                        val editedComment = comment.copy(content = editedContent)
                        commentsList[index] = editedComment
                        commentAdapter.notifyItemChanged(index)
                        // TODO: Actualizar en la base de datos
                    }
                }
            },
            onDeleteComment = { comment ->
                showDeleteConfirmationDialog(comment) {
                    val index = commentsList.indexOfFirst { it.id == comment.id }
                    if (index != -1) {
                        commentsList.removeAt(index)
                        commentAdapter.notifyItemRemoved(index)
                        announcement.commentsCount--
                        adapter.notifyDataSetChanged()
                        // TODO: Eliminar de la base de datos
                    }
                }
            }
        )
        recyclerView.adapter = commentAdapter

        // Cargar comentarios existentes
        commentsList.addAll(loadCommentsForAnnouncement(announcement.id))
        commentAdapter.notifyDataSetChanged()

        sendButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotEmpty()) {
                val comment = Comment(
                    id = UUID.randomUUID().toString(),
                    announcementId = announcement.id,
                    userId = "current_user_id", // TODO: Obtener ID real del usuario
                    userName = "Usuario Actual", // TODO: Obtener nombre real del usuario
                    content = commentText,
                    createdAt = Date()
                )
                
                commentsList.add(0, comment)
                commentAdapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
                
                announcement.commentsCount++
                adapter.notifyDataSetChanged()
                
                commentInput.text?.clear()
                // TODO: Guardar el comentario en la base de datos
            }
        }

        dialog.show()
    }

    private fun showEditCommentDialog(comment: Comment, onConfirm: (String) -> Unit) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_comment)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val editText = dialog.findViewById<TextInputEditText>(R.id.editCommentInput)
        val confirmButton = dialog.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        editText.setText(comment.content)

        confirmButton.setOnClickListener {
            val editedContent = editText.text.toString()
            if (editedContent.isNotEmpty()) {
                onConfirm(editedContent)
                dialog.dismiss()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(comment: Comment, onConfirm: () -> Unit) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar comentario")
            .setMessage("¿Estás seguro de que deseas eliminar este comentario?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun loadCommentsForAnnouncement(announcementId: String): List<Comment> {
        // TODO: Cargar comentarios desde la base de datos
        // Por ahora retornamos datos de ejemplo
        return listOf(
            Comment(
                id = UUID.randomUUID().toString(),
                announcementId = announcementId,
                userId = "user1",
                userName = "Ana López",
                content = "¡Me interesa mucho este curso!",
                createdAt = Date(System.currentTimeMillis() - 3600000) // 1 hora atrás
            ),
            Comment(
                id = UUID.randomUUID().toString(),
                announcementId = announcementId,
                userId = "user2",
                userName = "Carlos Ruiz",
                content = "¿Cuál es el horario del curso?",
                createdAt = Date(System.currentTimeMillis() - 7200000) // 2 horas atrás
            )
        )
    }

    private fun showCreateAnnouncementDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_create_announcement)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val titleInput = dialog.findViewById<TextInputEditText>(R.id.titleInput)
        val descriptionInput = dialog.findViewById<TextInputEditText>(R.id.descriptionInput)
        val typeSpinner = dialog.findViewById<Spinner>(R.id.typeSpinner)
        val createButton = dialog.findViewById<Button>(R.id.createButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        // Configurar spinner
        val types = AnnouncementType.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter

        createButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val type = types[typeSpinner.selectedItemPosition]

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val announcement = Announcement(
                    id = UUID.randomUUID().toString(),
                    tutorId = "current_user_id", // TODO: Obtener ID real del usuario
                    tutorName = "Nombre del Tutor", // TODO: Obtener nombre real del usuario
                    title = title,
                    description = description,
                    type = type,
                    imageUrl = null,
                    createdAt = Date()
                )

                announcements.add(0, announcement)
                this.adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
                dialog.dismiss()
                // TODO: Guardar en la base de datos
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadAnnouncements() {
        // TODO: Cargar anuncios desde la base de datos
        // Por ahora, agregamos datos de ejemplo
        announcements.add(
            Announcement(
                id = UUID.randomUUID().toString(),
                tutorId = "1",
                tutorName = "Juan Pérez",
                title = "Nuevo curso de Matemáticas Avanzadas",
                description = "Iniciamos nuevo curso de cálculo diferencial e integral. ¡Cupos limitados!",
                type = AnnouncementType.COURSE,
                imageUrl = null,
                createdAt = Date(),
                likesCount = 5,
                commentsCount = 2
            )
        )
        announcements.add(
            Announcement(
                id = UUID.randomUUID().toString(),
                tutorId = "2",
                tutorName = "María García",
                title = "Taller de Física Experimental",
                description = "Aprende física con experimentos prácticos. Ideal para estudiantes de ingeniería.",
                type = AnnouncementType.EVENT,
                imageUrl = null,
                createdAt = Date(),
                likesCount = 3,
                commentsCount = 1
            )
        )
        adapter.notifyDataSetChanged()
    }
}
