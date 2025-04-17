//package com.pplus.prnumberuser.apps.chat.ui
//
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.DocumentChange
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.chat.data.ChatAdapter
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.database.DBManager
//import com.pplus.prnumberuser.core.database.entity.Chat
//import com.pplus.prnumberuser.core.database.entity.ChatDao
//import com.pplus.prnumberuser.core.database.entity.ChatRoom
//import com.pplus.prnumberuser.core.database.entity.ChatRoomDao
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.ChatData
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_chat.*
//import kotlinx.android.synthetic.main.fragment_event.*
//import retrofit2.Call
//
//
//class ChatActivity : BaseActivity(), ImplToolbar {
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_chat
//    }
//
//    override fun getPID(): String? {
//        return ""
//    }
//
//    var mAdapter: ChatAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    var mTotalCount = 0L
//    var mChatDao: ChatDao? = null
//    var mChatRoomDao: ChatRoomDao? = null
//    var mRoomName:String? = null
//    var mPageSeqNo:Long ? = null
//    lateinit var mCollection: CollectionReference
//    var mTargetMemberSeqNo = -1L
//
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val db = Firebase.firestore
//
//        mRoomName = intent.getStringExtra(Const.ROOM_NAME)
//        mPageSeqNo = intent.getLongExtra(Const.PAGE_SEQ_NO, -1)
//        if(StringUtils.isEmpty(mRoomName)){
//            mRoomName = "room_${mPageSeqNo}_${LoginInfoManager.getInstance().user.no}"
//            mTargetMemberSeqNo = intent.getLongExtra(Const.TARGET_MEMBER_SEQ_NO, -1)
//        }
//
//        text_chat_confirm.setOnClickListener {
//            val msg = edit_chat.text.toString()
//            if (StringUtils.isNotEmpty(msg)) {
//                edit_chat.setText("")
//                val chatData = ChatData()
//                chatData.msg = msg
//                chatData.name = "user"
//                chatData.roomName = mRoomName
//                chatData.memberSeqNo = LoginInfoManager.getInstance().user.no
//                chatData.targetMemberSeqNo = mTargetMemberSeqNo
//                chatData.pageSeqNo = mPageSeqNo
//                ApiBuilder.create().insertChat(chatData).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//
//                    }
//                }).build().call()
//            }
//        }
//        DBManager.getInstance(this).session.database.execSQL("update " + ChatDao.TABLENAME + " set " + ChatDao.Properties.IsRead + " = ? where " + ChatDao.Properties.RoomName + " = ? ", arrayOf<Any>(true, mRoomName!!))
//
//        mChatDao = DBManager.getInstance(this).session.chatDao
//        mChatRoomDao = DBManager.getInstance(this).session.chatRoomDao
//        mTotalCount = mChatDao!!.queryBuilder().where(ChatDao.Properties.RoomName.eq(mRoomName)).count()
//
//        mAdapter = ChatAdapter()
//        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
//        recycler_chat.layoutManager = mLayoutManager
//        recycler_chat.adapter = mAdapter
//
//        recycler_chat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                    LogUtil.e(LOG_TAG, "scrolled")
//                    listCal()
//                }
//            }
//        })
//
//        listCal()
//
//        if(mTargetMemberSeqNo == -1L && mAdapter!!.itemCount > 0){
//            val firstChatData = mChatDao!!.queryBuilder().where(ChatDao.Properties.RoomName.eq(mRoomName)).orderAsc(ChatDao.Properties.TimeMillis).limit(1).list()[0]
//            if(firstChatData.memberSeqNo != LoginInfoManager.getInstance().user.no){
//                mTargetMemberSeqNo = firstChatData.memberSeqNo
//            }
//        }
//
//        mCollection = db.collection("chat").document("chat_room").collection(mRoomName!!)
//        loadNewMsg()
//    }
//
//    private fun loadNewMsg(){
//        var millis = 0L
//        if (mAdapter!!.itemCount > 0) {
//            millis = mAdapter!!.getItem(0).timeMillis
//        }
//
//        mCollection.whereGreaterThan("timeMillis", millis).get().addOnSuccessListener {
//            for (document in it.documents) {
//
//                LogUtil.e(LOG_TAG, "${document.id} => ${document.data}")
//                if (document.data != null) {
//                    val chat = Chat()
//                    chat.msg = document.data!!["msg"].toString()
//                    chat.name = document.data!!["name"].toString()
//                    chat.roomName = document.data!!["roomName"].toString()
//                    chat.memberSeqNo = document.data!!["memberSeqNo"].toString().toLong()
//                    chat.targetMemberSeqNo = document.data!!["targetMemberSeqNo"].toString().toLong()
//                    if (document.data!!["pageSeqNo"] != null) {
//                        chat.pageSeqNo = document.data!!["pageSeqNo"].toString().toLong()
//                    }
//                    chat.timeMillis = document.data!!["timeMillis"].toString().toLong()
//                    chat.isRead = true
//                    mChatDao!!.insertOrReplace(chat)
//                    mAdapter!!.add(0, chat)
//                    mTotalCount++
//                }
//            }
//            addSnapshotListener()
//
//        }.addOnFailureListener { exception ->
//            LogUtil.e(LOG_TAG, "Error getting documents: ", exception)
//            addSnapshotListener()
//        }
//    }
//
//    private fun addSnapshotListener(){
//        mCollection.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                LogUtil.e(LOG_TAG, "Listen failed.{}", e.toString())
//                return@addSnapshotListener
//            }
//
//            if(snapshot == null){
//                LogUtil.e(LOG_TAG, "snapshot is null")
//                return@addSnapshotListener
//            }
//
//            for(dc in snapshot.documentChanges){
//                val document = dc.document
//
//                when(dc.type){
//                    DocumentChange.Type.ADDED -> {
//                        val chat = Chat()
//                        chat.msgId = document.data["msgId"].toString()
//                        chat.msg = document.data["msg"].toString()
//                        chat.name = document.data["name"].toString()
//                        chat.roomName = document.data["roomName"].toString()
//                        chat.memberSeqNo = document.data["memberSeqNo"].toString().toLong()
//                        chat.targetMemberSeqNo = document.data["targetMemberSeqNo"].toString().toLong()
//                        if (document.data["pageSeqNo"] != null) {
//                            chat.pageSeqNo = document.data["pageSeqNo"].toString().toLong()
//                        }
//                        chat.timeMillis = document.data["timeMillis"].toString().toLong()
//                        chat.isRead = true
//                        mChatDao!!.insertOrReplace(chat)
//                        mAdapter!!.add(0, chat)
//                        mTotalCount++
//
//                        val chatRoom = ChatRoom()
//                        chatRoom.roomName = chat.roomName
//                        chatRoom.lastMsgId = chat.msgId
//                        chatRoom.memberSeqNo = chat.memberSeqNo
//                        chatRoom.pageSeqNo = chat.pageSeqNo
//                        chatRoom.timeMillis = chat.timeMillis
//                        mChatRoomDao!!.insertOrReplace(chatRoom)
//
//                    }
//                    DocumentChange.Type.MODIFIED -> {
//                        val chat = Chat()
//                        chat.msgId = document.data["msgId"].toString()
//                        chat.msg = document.data["msg"].toString()
//                        chat.name = document.data["name"].toString()
//                        chat.roomName = document.data["roomName"].toString()
//                        chat.memberSeqNo = document.data["memberSeqNo"].toString().toLong()
//                        chat.targetMemberSeqNo = document.data["targetMemberSeqNo"].toString().toLong()
//                        if (document.data["pageSeqNo"] != null) {
//                            chat.pageSeqNo = document.data["pageSeqNo"].toString().toLong()
//                        }
//                        chat.timeMillis = document.data["timeMillis"].toString().toLong()
//                        chat.isRead = true
//                        mChatDao!!.insertOrReplace(chat)
//                        for (i in 0 until mAdapter!!.mDataList!!.size) {
//                            if (mAdapter!!.mDataList!![i].msgId == chat.msgId) {
//                                mAdapter!!.mDataList!![i] = chat
//                                mAdapter!!.notifyItemChanged(i)
//                                break
//                            }
//                        }
//                    }
//                    DocumentChange.Type.REMOVED -> {
//                        val chat = Chat()
//                        chat.msgId = document.data["msgId"].toString()
//                        mChatDao!!.delete(chat)
//                    }
//                }
//            }
//        }
//    }
//
//    var mLastMillis = 0L
//    private fun listCal() {
//        val dataList: List<Chat>
//        if (mLastMillis == 0L) {
//            dataList = mChatDao!!.queryBuilder().where(ChatDao.Properties.RoomName.eq(mRoomName)).orderDesc(ChatDao.Properties.TimeMillis).limit(100).list()
//        } else {
//            dataList = mChatDao!!.queryBuilder().where(ChatDao.Properties.RoomName.eq(mRoomName), ChatDao.Properties.TimeMillis.lt(mLastMillis)).orderDesc(ChatDao.Properties.TimeMillis).limit(100).list()
//        }
//
//        if (dataList.size > 0) {
//            mLastMillis = dataList[dataList.size - 1].timeMillis
//            mAdapter!!.addAll(dataList)
//        }
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Const.currentChatRoom = mRoomName
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Const.currentChatRoom = ""
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val key = intent.getStringExtra(Const.KEY)
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
//
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}