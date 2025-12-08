package com.example.hearo2.community

object PostRepository {

    // 임시 저장 (API 연동 전)
    val posts = mutableListOf<PostModel>()

    private var nextId = 1

    fun addPost(category: String, title: String, content: String): PostModel {
        val post = PostModel(
            id = nextId++,
            category = category,
            title = title,
            content = content,
            writer = "hearO"
        )
        posts.add(0, post)
        return post
    }

    fun getPostById(id: Int): PostModel? {
        return posts.find { it.id == id }
    }
}
