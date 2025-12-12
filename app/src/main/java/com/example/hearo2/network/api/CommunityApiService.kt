package com.example.hearo2.network.api

import com.example.hearo2.community.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommunityApiService {

    // 🔹 게시물 목록 조회
    @GET("/api/v1/community/posts")
    suspend fun getPostList(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PostListResponse

    // 🔹 게시글 단건 조회
    @GET("/api/v1/community/posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: Long
    ): PostDetailResponse

    // 🔹 게시글 검색
    @GET("/api/v1/community/posts/search")
    suspend fun searchPosts(
        @Query("q") q: String? = null,
        @Query("category") category: String? = null,
        @Query("tag") tag: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PostListResponse

    // 🔹 이미지 업로드
    @Multipart
    @POST("/api/v1/community/images")
    suspend fun uploadImages(
        @Part files: Array<MultipartBody.Part>
    ): ApiResponse<ImageUploadResponse>



    // 🔹 게시글 작성
    @POST("/api/v1/community/posts")
    suspend fun createPost(
        @Body body: PostRequest
    ): ApiResponse<Long>

    // 🔹 게시글 수정
    @PUT("/api/v1/community/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: Long,
        @Body body: PostRequest
    ): ApiResponse<Unit>

    // 🔹 게시글 삭제
    @DELETE("/api/v1/community/posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long
    ): ApiResponse<Unit>

    // 🔹 게시글 스크랩
    @POST("/api/v1/community/posts/{postId}/scrap")
    suspend fun scrapPost(
        @Path("postId") postId: Long
    ): ApiResponse<ScrapResponse>

    // 🔹 게시글 스크랩 취소
    @DELETE("/api/v1/community/posts/{postId}/scrap")
    suspend fun cancelScrapPost(
        @Path("postId") postId: Long
    ): ApiResponse<ScrapResponse>

    // 🔹 댓글 목록 조회
    @GET("/api/v1/community/posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): CommentListResponse

    // 🔹 댓글 작성
    @POST("/api/v1/community/posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Long,
        @Body body: CommentRequest
    ): ApiResponse<Long>

    // 🔹 댓글 수정
    @PUT("/api/v1/community/posts/comments/{commentId}")
    suspend fun updateComment(
        @Path("commentId") commentId: Long,
        @Body body: CommentRequest
    ): ApiResponse<Unit>

    // 🔹 댓글 삭제
    @DELETE("/api/v1/community/posts/comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Long
    ): ApiResponse<Unit>

    // 🔹 댓글 좋아요
    @POST("/api/v1/community/posts/comments/{commentId}/like")
    suspend fun likeComment(
        @Path("commentId") commentId: Long
    ): ApiResponse<LikeResponse>

    // 🔹 댓글 좋아요 취소
    @DELETE("/api/v1/community/posts/comments/{commentId}/like")
    suspend fun cancelLikeComment(
        @Path("commentId") commentId: Long
    ): ApiResponse<LikeResponse>

    // ---------------------------------------------------------
    // ⭐ 대댓글 API
    // ---------------------------------------------------------

    // 🔹 대댓글 조회
    @GET("/api/v1/community/posts/{postId}/comments/{commentId}/replies")
    suspend fun getReplies(
        @Path("postId") postId: Long,
        @Path("commentId") parentCommentId: Long
    ): ApiResponse<CommentListResponse>

    // ⭐ 대댓글 작성
    @POST("/api/v1/community/posts/{postId}/comments/{commentId}/replies")
    suspend fun createReply(
        @Path("postId") postId: Long,
        @Path("commentId") parentCommentId: Long,
        @Body body: CommentRequest
    ): ApiResponse<Long>

    // ⭐ 대댓글 수정  (댓글 수정과 동일 엔드포인트)
    @PUT("/api/v1/community/posts/comments/{replyId}")
    suspend fun updateReply(
        @Path("replyId") replyId: Long,
        @Body body: CommentRequest
    ): ApiResponse<Unit>

    // ⭐ 대댓글 삭제 (댓글 삭제와 동일 엔드포인트)
    @DELETE("/api/v1/community/posts/comments/{replyId}")
    suspend fun deleteReply(
        @Path("replyId") replyId: Long
    ): ApiResponse<Unit>

    // ⭐ 대댓글 좋아요
    @POST("/api/v1/community/posts/comments/{replyId}/like")
    suspend fun likeReply(
        @Path("replyId") replyId: Long
    ): ApiResponse<LikeResponse>

    // ⭐ 대댓글 좋아요 취소
    @DELETE("/api/v1/community/posts/comments/{replyId}/like")
    suspend fun cancelLikeReply(
        @Path("replyId") replyId: Long
    ): ApiResponse<LikeResponse>

    @GET("/api/v1/community/posts/me")
    suspend fun getMyPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PostListResponse

    // 내가 스크랩한 게시물
    @GET("/api/v1/community/posts/scrap/me")
    suspend fun getScrappedPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PostListResponse

    @GET("/api/v1/community/posts/comments/me")
    suspend fun getMyComments(): CommentListResponse

}
