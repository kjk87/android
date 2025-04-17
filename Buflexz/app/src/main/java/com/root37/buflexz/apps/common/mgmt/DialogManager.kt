package com.root37.buflexz.apps.common.mgmt

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.TextView
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.R

/**
 * Created by ksh on 2016-09-19.
 */
class DialogManager private constructor() {
    private val dialogMap: MutableMap<String, Dialog>

    init { // 다이얼로그 맵을 관리합니다.
        dialogMap = HashMap()
    }

    fun showLoadingDialog(activity: Activity?, message: String? = null, cancelable: Boolean = false) {
        if (activity == null) {
            return
        }
        val tag = activity.hashCode().toString()
        val dialog: Dialog?
        if (dialogMap.containsKey(tag)) {
            dialog = dialogMap[tag]
            if (dialog != null) {
                val textView = dialog.findViewById<View>(R.id.tv_pb) as TextView
                if (!TextUtils.isEmpty(message)) {
                    textView.visibility = View.VISIBLE
                    textView.text = message
                }
                dialog.show()
            }
        } else {
            dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_layout)
            val textView = dialog.findViewById<View>(R.id.tv_pb) as TextView
            if (!TextUtils.isEmpty(message)) {
                textView.visibility = View.VISIBLE
                textView.text = message
            }
            dialog.setCancelable(cancelable)
            if (!dialog.isShowing && !activity.isFinishing) {
                try {
                    dialog.show()
                }catch (e:Exception){

                }

                dialogMap[tag] = dialog
            }
        }
    }

    fun hideLoadingDialog(activity: Activity?) {
        if (activity == null) {
            hideAll()
            return
        }
        val tag = activity.hashCode().toString()
        if (dialogMap.containsKey(tag)) {
            val dialog = dialogMap[tag]
            if (dialog != null) {
                if (dialog.isShowing && !activity.isFinishing) {
                    dialog.dismiss()
                }
            }
            dialogMap.remove(tag)
        }
        hideAll()
    }

    fun hideAll() {
        try {
            for (tag in dialogMap.keys) {
                val dialog = dialogMap[tag]
                if (dialog != null && dialog.isShowing) {
                    try {
                        dialog.dismiss()
                    } catch (e: Exception) {
                        LogUtil.e("hideAll", e.toString())
                    }
                }
                dialogMap.remove(tag)
            }
        } catch (e: Exception) {
            LogUtil.e("DialogManager", e.toString())
        }
    }

    companion object {
        private var mDialogManager: DialogManager? = null
        val instance: DialogManager
            get() {
                if (mDialogManager == null) {
                    mDialogManager = DialogManager()
                }
                return mDialogManager!!
            }
    }
}