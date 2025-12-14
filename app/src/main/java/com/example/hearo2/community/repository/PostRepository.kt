package com.example.hearo2.community.repository

import com.example.hearo2.community.*
import com.example.hearo2.network.api.CommunityRetrofitClient
import com.example.hearo2.network.api.RetrofitClient

class PostRepository {

    private val api = CommunityRetrofitClient.communityApi


    // ---------------------- 게시글 ----------------------
    suspend fun loadPostList(page: Int, size: Int) =
        api.getPostList(page, size)

    suspend fun loadPostDetail(postId: Long) =
        api.getPostDetail(postId)

    suspend fun searchPosts(
        query: String?,
        category: String?,
        tag: String?,
        page: Int,
        size: Int
    ) = api.searchPosts(query, category, tag, page, size)


    suspend fun deletePost(postId: Long) = api.deletePost(postId)

    suspend fun scrap(postId: Long) = api.scrapPost(postId)

    suspend fun cancelScrap(postId: Long) = api.cancelScrapPost(postId)

    // ---------------------- 댓글 ----------------------
    suspend fun loadComments(postId: Long, page: Int, size: Int) =
        api.getComments(postId, page, size)

    suspend fun createComment(postId: Long, content: String) =
        api.createComment(postId, CommentRequest(content))

    suspend fun updateComment(commentId: Long, content: String) =
        api.updateComment(commentId, CommentRequest(content))

    suspend fun deleteComment(commentId: Long) =
        api.deleteComment(commentId)

    suspend fun likeComment(commentId: Long) =
        api.likeComment(commentId)

    suspend fun cancelLikeComment(commentId: Long) =
        api.cancelLikeComment(commentId)

    // ---------------------- ⭐ 대댓글 ----------------------

    suspend fun loadReplies(postId: Long, parentCommentId: Long) =
        api.getReplies(postId, parentCommentId)

    suspend fun createReply(postId: Long, parentCommentId: Long, content: String) =
        api.createReply(postId, parentCommentId, CommentRequest(content))

    suspend fun updateReply(replyId: Long, content: String) =
        api.updateReply(replyId, CommentRequest(content))

    suspend fun deleteReply(replyId: Long) =
        api.deleteReply(replyId)

    suspend fun likeReply(replyId: Long) =
        api.likeReply(replyId)

    suspend fun cancelLikeReply(replyId: Long) =
        api.cancelLikeReply(replyId)
}
