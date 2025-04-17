package com.lejel.wowbox.apps.luckybox.data

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import com.lejel.wowbox.R

/**
 * Created by Administrator on 2016/3/30.
 */
class LuckyBoxLoadingView : DialogFragment() {
    //    Animation operatingAnim;
    var mDialog: Dialog? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mDialog == null) {
            mDialog = Dialog(requireActivity(), R.style.lotto_loading_dialog)
            mDialog!!.setContentView(R.layout.dialog_lucky_box_loading)
            mDialog!!.setCanceledOnTouchOutside(true)
            mDialog!!.window!!.setGravity(Gravity.CENTER)


        }
        return mDialog!!
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mDialog = null
        System.gc()
    }
}