package com.demo.pbl6_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ChatManager
import com.demo.pbl6_android.databinding.FragmentMessageListBinding
import com.demo.pbl6_android.ui.chat.adapter.ConversationAdapter
import kotlinx.coroutines.launch

class MessageListFragment : Fragment() {

    private var _binding: FragmentMessageListBinding? = null
    private val binding: FragmentMessageListBinding
        get() = _binding!!
    
    private lateinit var conversationAdapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupRecyclerView()
        observeConversations()
    }

    private fun setupViews() {
        binding.apply {
            btnSearch.setOnClickListener {
                // TODO: Implement search functionality
            }
            
            btnFilter.setOnClickListener {
                // TODO: Implement filter functionality
            }
        }
    }

    private fun setupRecyclerView() {
        conversationAdapter = ConversationAdapter { conversation ->
            navigateToChat(conversation.id)
        }
        
        binding.rvConversations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = conversationAdapter
        }
    }

    private fun observeConversations() {
        viewLifecycleOwner.lifecycleScope.launch {
            ChatManager.conversations.collect { conversations ->
                conversationAdapter.submitList(conversations)
                updateEmptyState(conversations.isEmpty())
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                rvConversations.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            } else {
                rvConversations.visibility = View.VISIBLE
                emptyState.visibility = View.GONE
            }
        }
    }

    private fun navigateToChat(conversationId: String) {
        val bundle = Bundle().apply {
            putString("conversationId", conversationId)
        }
        findNavController().navigate(R.id.action_messageListFragment_to_chatFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
