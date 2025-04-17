package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityPlayBinding

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

        ft.replace(R.id.play_container, PlayGroupDFragment.newInstance(key), PlayGroupDFragment::class.java.simpleName)
//        when(key){
//            4->{
//                ft.replace(R.id.play_container, PlayGroupDFragment.newInstance(key), PlayGroupDFragment::class.java.simpleName)
//            }
//            else->{
//                ft.replace(R.id.play_container, PlayGroupAFragment.newInstance(key), PlayGroupAFragment::class.java.simpleName)
//            }
//        }

        ft.commit()

    }

}
