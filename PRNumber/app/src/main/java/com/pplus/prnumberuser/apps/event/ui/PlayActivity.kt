package com.pplus.prnumberuser.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityPlayBinding

class PlayActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlayBinding

    override fun getLayoutView(): View {
        binding = ActivityPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = intent.getIntExtra(Const.KEY, 1)
        val ft = supportFragmentManager.beginTransaction()
        when(key){
            1->{
                ft.replace(R.id.play_container, PlayGroupAFragment.newInstance(key), PlayGroupAFragment::class.java.simpleName)
            }
            2->{
                ft.replace(R.id.play_container, PlayGroupBFragment.newInstance(), PlayGroupBFragment::class.java.simpleName)
            }
            4->{
                ft.replace(R.id.play_container, PlayGroupDFragment.newInstance(), PlayGroupDFragment::class.java.simpleName)
            }
        }

        ft.commit()

    }

}
