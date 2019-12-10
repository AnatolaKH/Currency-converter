package akh.converter.common

import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        TODO Add crash report
//        if (priority == Log.ERROR || priority == Log.WARN)
    }

}