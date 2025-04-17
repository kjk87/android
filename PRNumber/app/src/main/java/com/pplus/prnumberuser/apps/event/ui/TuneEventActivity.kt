package com.pplus.prnumberuser.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityTuneBinding

class TuneEventActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityTuneBinding

    override fun getLayoutView(): View {
        binding = ActivityTuneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val tab = intent.getIntExtra(Const.TAB, 1)


        val ft = supportFragmentManager.beginTransaction()
        when(tab){
            1->{
                ft.replace(R.id.tune_container, TuneEventGroupAFragment.newInstance(), TuneEventGroupAFragment::class.java.simpleName)
            }
            2->{
                ft.replace(R.id.tune_container, TuneEventGroupBFragment.newInstance(), TuneEventGroupBFragment::class.java.simpleName)
            }
            3->{
                ft.replace(R.id.tune_container, TuneEventGroupCFragment.newInstance(), TuneEventGroupCFragment::class.java.simpleName)
            }
        }

        ft.commit()

    }

}
