package akh.presentation.ui.features.splash

import akh.presentation.ui.base.BaseActivity
import akh.presentation.ui.features.MainActivity
import android.content.Intent
import android.os.Bundle

class SplashActivity : BaseActivity() {

    override fun getLayoutID(): Int? = null

    override fun setUI(savedInstanceState: Bundle?) {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}
