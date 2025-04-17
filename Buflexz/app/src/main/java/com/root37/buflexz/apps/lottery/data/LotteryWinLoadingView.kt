package com.root37.buflexz.apps.lottery.data

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.DialogFragment
import com.root37.buflexz.R

/**
 * Created by Administrator on 2016/3/30.
 */
class LotteryWinLoadingView : DialogFragment() {
    //    Animation operatingAnim;
    var mDialog: Dialog? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mDialog == null) {
            mDialog = Dialog(requireActivity(), R.style.lotto_loading_dialog)
            mDialog!!.setContentView(R.layout.dialog_lotto_win)
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