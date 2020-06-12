package akh.presentation.ui.features

import akh.presentation.R
import akh.presentation.ui.base.BaseActivity
import akh.presentation.ui.features.converter.ConverterFragment
import android.content.res.Configuration
import android.os.Bundle

class MainActivity : BaseActivity() {

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun setUI(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ConverterFragment.newInstance(), "converterFragment")
                .commit()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}
