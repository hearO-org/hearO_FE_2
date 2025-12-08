package com.example.hearo2.community

object CommentRepository {

    val comments = mutableListOf<CommentModel>()
    private var nextId = 1

    fun addComment(postId: Int, text: String) {
        comments.add(
            CommentModel(
                id = nextId++,
                postId = postId,
                writer = "hearO",
                comment = text,
                time = "방금 전"
            )
        )
    }

    fun getComments(postId: Int): List<CommentModel> {
        return comments.filter { it.postId == postId }
    }
}
