package com.demo.pbl6_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.ChatManager
import com.demo.pbl6_android.data.model.Conversation
import com.demo.pbl6_android.databinding.FragmentChatBinding
import com.demo.pbl6_android.ui.chat.adapter.MessageAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding!!
    
    private lateinit var messageAdapter: MessageAdapter
    private var conversationId: String? = null
    private var conversation: Conversation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        conversationId = arguments?.getString("conversationId")
        
        setupViews()
        setupRecyclerView()
        loadConversationInfo()
        observeMessages()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnCall.setOnClickListener {
                showToast("Chức năng gọi điện đang phát triển")
            }
            
            btnMenu.setOnClickListener {
                showOptionsMenu()
            }
            
            btnAttach.setOnClickListener {
                showToast("Chức năng đính kèm đang phát triển")
            }
            
            btnCamera.setOnClickListener {
                showToast("Chức năng chụp ảnh đang phát triển")
            }
            
            btnSend.setOnClickListener {
                sendMessage()
            }
            
            // Handle enter key on keyboard
            etMessage.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun loadConversationInfo() {
        conversationId?.let { id ->
            conversation = ChatManager.getConversation(id)
            
            conversation?.let { conv ->
                binding.apply {
                    tvShopName.text = conv.shopName
                    tvOnlineStatus.text = if (conv.isOnline) "Đang hoạt động" else "Không hoạt động"
                    
                    // TODO: Load shop avatar
                    // Glide.with(this@ChatFragment).load(conv.shopAvatar).into(ivShopAvatar)
                }
                
                // Mark conversation as read
                ChatManager.markConversationAsRead(id)
            }
        }
    }

    private fun observeMessages() {
        conversationId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                ChatManager.messages.collect { messagesMap ->
                    val messages = messagesMap[id] ?: emptyList()
                    messageAdapter.submitList(messages)
                    
                    // Scroll to bottom when new messages arrive
                    if (messages.isNotEmpty()) {
                        binding.rvMessages.scrollToPosition(messages.size - 1)
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        
        if (messageText.isNotEmpty()) {
            conversationId?.let { id ->
                ChatManager.sendMessage(id, messageText)
                binding.etMessage.text?.clear()
                
                // Hide keyboard
                binding.etMessage.clearFocus()
            }
        }
    }

    private fun showOptionsMenu() {
        val options = arrayOf("Xem thông tin shop", "Tắt thông báo", "Xóa cuộc trò chuyện")
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Tùy chọn")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showToast("Chức năng xem thông tin shop đang phát triển")
                    1 -> showToast("Đã tắt thông báo")
                    2 -> showDeleteConfirmation()
                }
            }
            .show()
    }

    private fun showDeleteConfirmation() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Xóa cuộc trò chuyện")
            .setMessage("Bạn có chắc chắn muốn xóa cuộc trò chuyện này không?")
            .setPositiveButton("Xóa") { _, _ ->
                showToast("Đã xóa cuộc trò chuyện")
                findNavController().navigateUp()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
