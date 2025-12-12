package com.example.hearo2.community

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentCommunityWriteBinding
import com.example.hearo2.network.api.CommunityRetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CommunityWriteFragment : Fragment() {

    private lateinit var binding: FragmentCommunityWriteBinding
    private val args: CommunityWriteFragmentArgs by navArgs()

    private var isEditMode = false
    private var editPostId: Long? = null

    /** 🔥 카테고리 */
    private var selectedCategory: String? = null

    /** 🔥 태그 */
    private val tagList = mutableListOf<String>()

    /** 🔥 이미지 */
    private val selectedImages = mutableListOf<Uri>()
    private val uploadedImageUrls = mutableListOf<String>()

    // ===============================
    // LifeCycle
    // ===============================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        isEditMode = args.isEditMode
        editPostId = args.postId.takeIf { it != 0 }?.toLong()

        setupCategory()
        setupTag()
        setupImagePicker()
        setupSubmitButton()

        if (isEditMode) loadOldPostData()
    }

    // ===============================
    // 🔥 기존 글 불러오기 (수정 모드)
    // ===============================
    private fun loadOldPostData() {
        lifecycleScope.launch {
            try {
                val response =
                    CommunityRetrofitClient.communityApi.getPostDetail(editPostId!!)
                val post = response.data

                binding.etTitle.setText(post.title)
                binding.etContent.setText(post.content)

                selectedCategory = post.category

                uploadedImageUrls.clear()
                uploadedImageUrls.addAll(post.images ?: emptyList())

                binding.btnSubmitPost.text = "수정하기"

            } catch (e: Exception) {
                Log.e("WRITE_POST", "기존 글 불러오기 실패", e)
            }
        }
    }

    // ===============================
    // 🔥 카테고리 선택
    // ===============================
    private fun setupCategory() {
        binding.categoryContainer.children.forEach { view ->
            val tv = view as? TextView ?: return@forEach

            tv.setOnClickListener {

                binding.categoryContainer.children.forEach { child ->
                    (child as? TextView)?.apply {
                        setBackgroundResource(R.drawable.category_unselected_bg)
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_700
                            )
                        )
                    }
                }

                tv.setBackgroundResource(R.drawable.category_selected_bg)
                tv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )

                selectedCategory = tv.tag as? String
            }
        }
    }

    // ===============================
    // 🔥 태그
    // ===============================
    private fun setupTag() {
        binding.btnAddTag.setOnClickListener {

            val tag = binding.etTag.text.toString().trim()
            if (tag.isEmpty()) return@setOnClickListener

            if (tagList.size >= 5) {
                Toast.makeText(requireContext(), "태그는 최대 5개까지 가능합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tagList.contains(tag)) return@setOnClickListener

            tagList.add(tag)
            addTagChip(tag)
            binding.etTag.text.clear()
            updateTagCount()
        }
    }

    private fun addTagChip(tag: String) {
        val chip = layoutInflater.inflate(
            R.layout.item_tag_chip,
            binding.popularTagContainer,
            false
        ) as TextView

        chip.text = tag
        chip.setOnClickListener {
            tagList.remove(tag)
            binding.popularTagContainer.removeView(chip)
            updateTagCount()
        }

        binding.popularTagContainer.addView(chip)
    }

    private fun updateTagCount() {
        binding.tvTagLimit.text =
            "태그는 최대 5개까지 추가할 수 있습니다 (${tagList.size}/5)"
    }

    // ===============================
    // 🔥 이미지 선택
    // ===============================
    private fun setupImagePicker() {
        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            imagePickerLauncher.launch(intent)
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                selectedImages.clear()
                val data = result.data

                if (data?.clipData != null) {
                    val count = minOf(data.clipData!!.itemCount, 5)
                    for (i in 0 until count) {
                        selectedImages.add(data.clipData!!.getItemAt(i).uri)
                    }
                } else {
                    data?.data?.let { selectedImages.add(it) }
                }

                binding.tvImageCount.text =
                    "이미지 업로드 (${selectedImages.size}/5)"
            }
        }

    // ===============================
    // 🔥 작성 / 수정
    // ===============================
    private fun setupSubmitButton() {
        binding.btnSubmitPost.setOnClickListener {

            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (selectedCategory == null) {
                Toast.makeText(requireContext(), "카테고리를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (selectedImages.isNotEmpty()) {
                        uploadedImageUrls.clear()
                        uploadedImageUrls.addAll(uploadImages())
                    }

                    val body = PostRequest(
                        title = title,
                        content = content,
                        category = selectedCategory!!,
                        visibility = "PUBLIC",
                        imageUrls = uploadedImageUrls,
                        tags = tagList
                    )

                    if (isEditMode && editPostId != null) {
                        CommunityRetrofitClient.communityApi.updatePost(editPostId!!, body)
                    } else {
                        CommunityRetrofitClient.communityApi.createPost(body)
                    }

                    Toast.makeText(requireContext(), "게시글 작성 완료!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Log.e("WRITE_POST", "작성 실패", e)
                    Toast.makeText(requireContext(), "작성 실패", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ===============================
    // 🔥 이미지 업로드 API (⭐ 수정된 핵심)
    // ===============================
    private suspend fun uploadImages(): List<String> {

        val parts = selectedImages.map { uri ->

            val inputStream =
                requireContext().contentResolver.openInputStream(uri)
                    ?: throw IllegalStateException("이미지 InputStream 열기 실패")

            val file = File.createTempFile(
                "upload_",
                ".jpg",
                requireContext().cacheDir
            )

            file.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()

            val requestBody =
                file.asRequestBody("image/*".toMediaType())

            MultipartBody.Part.createFormData(
                "files",      // 서버 명세 그대로
                file.name,
                requestBody
            )
        }.toTypedArray()   // ⭐⭐⭐ 이게 핵심

        return CommunityRetrofitClient.communityApi
            .uploadImages(parts)
            .data.imageUrls
    }



}
