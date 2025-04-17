package com.pplus.luckybol.apps.mobilegift.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.mobilegift.data.SelectContactAdapter
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.Contact
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.MsgTarget
import com.pplus.luckybol.core.util.SoundSearcher
import com.pplus.luckybol.databinding.ActivitySelectOneContactBinding
import com.pplus.utils.part.utils.NumberUtils
import com.pplus.utils.part.utils.StringUtils

class SelectContactActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivitySelectOneContactBinding

    override fun getLayoutView(): View {
        binding = ActivitySelectOneContactBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdapter: SelectContactAdapter? = null
    private var mContactList: List<Contact>? = null
    private var mSearchWord: String? = null
    override fun initializeView(savedInstanceState: Bundle?) {

        binding.recyclerSelectContact.layoutManager = LinearLayoutManager(this)
        mAdapter = SelectContactAdapter()
        binding.recyclerSelectContact.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : SelectContactAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val contact = mAdapter!!.getItem(position)
                val target = MsgTarget()
                target.mobile = contact.mobileNumber
                target.name = contact.memberName
                val data = Intent()
                data.putExtra(Const.DATA, target)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        })
        val contactDao = DBManager.getInstance(this).session.contactDao
        mContactList = contactDao.queryBuilder().where(ContactDao.Properties.Delete.eq(false)).orderAsc(ContactDao.Properties.MemberName).list()
        binding.editSelectContactSearch.setSingleLine()
        binding.editSelectContactSearch.imeOptions = EditorInfo.IME_ACTION_DONE


        binding.editSelectContactSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (StringUtils.isNotEmpty(mSearchWord) && mSearchWord == s.toString()) {
                    return
                }
                mSearchWord = s.toString()
                setData()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        setData()
    }

    private fun setData() {
        mAdapter!!.clear()
        for (contact in mContactList!!) {
            if (NumberUtils.isNumber(mSearchWord)) {
                if (StringUtils.isNotEmpty(mSearchWord) && !SoundSearcher.matchString(contact.mobileNumber, mSearchWord)) {
                    continue
                }
            } else {
                if (StringUtils.isNotEmpty(mSearchWord) && !SoundSearcher.matchString(contact.memberName, mSearchWord)) {
                    continue
                }
            }
            mAdapter!!.add(contact)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_receiver), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}