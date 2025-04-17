package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBoxEntry(var seqNo: Long? = null,
                    var luckyboxProductGroupSeqNo: Long? = null,
                    var luckyboxSeqNo: Long? = null,
                    var luckyboxProductGroup: LuckyBoxProductGroup? = null) : Parcelable {

}