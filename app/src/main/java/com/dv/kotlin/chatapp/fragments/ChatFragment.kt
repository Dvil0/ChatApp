package com.dv.kotlin.chatapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.activities.toast
import com.dv.kotlin.chatapp.adapters.ChatAdapter
import com.dv.kotlin.chatapp.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDbRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDataBase()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatButton()

        subscribeToChatMessages()

        return _view
    }

    private fun setUpChatDataBase(){
        chatDbRef = store.collection( "Chat" )
    }

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager( context )
        adapter = ChatAdapter( messageList, currentUser.uid )
        _view.rcvChat.setHasFixedSize( true )
        _view.rcvChat.layoutManager = layoutManager
        _view.rcvChat.itemAnimator = DefaultItemAnimator()
        _view.rcvChat.adapter = adapter
    }

    private fun setUpChatButton(){
        _view.imgButtonSend.setOnClickListener {
            val messageText = edtMessage.text.toString()

            if( messageText.isNotEmpty() ){

                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run{ "" }

                val message = Message(
                    currentUser.uid,
                    messageText,
                    photo,
                    Date()
                )
                saveMessage( message )
                _view.edtMessage.setText("")
            }
        }
    }

    private fun saveMessage( message: Message ){
        val newMessage = HashMap<String, Any>()
        newMessage[ "authorId" ] = message.authorId
        newMessage[ "message" ] = message.message
        newMessage[ "profileImageURL" ] = message.profileImageURL
        newMessage[ "sentAt" ] = message.sentAt

        chatDbRef.add( newMessage )
            .addOnCompleteListener {
                activity!!.toast( "Message added!" )
            }
            .addOnFailureListener {
                activity!!.toast( "Message error, try again." )
            }
    }

    private fun subscribeToChatMessages(){
        chatSubscription = chatDbRef
                .orderBy( "sentAt", Query.Direction.DESCENDING )
                .limit(100 )
                .addSnapshotListener( object: java.util.EventListener, EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let{
                        activity!!.toast( "Exception." )
                        return
                    }
                    snapshot?.let {
                        messageList.clear()
                        val messages = it.toObjects( Message::class.java )
                        messageList.addAll( messages.asReversed() )
                        adapter.notifyDataSetChanged()
                        _view.rcvChat.smoothScrollToPosition( messageList.size )
                    }
                }
            })
    }

    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()
    }

}
