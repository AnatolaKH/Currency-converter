package akh.data.db

import akh.core.app.CurrentTheme
import akh.core.model.RateModel
import akh.core.repository.SecretDBRepository
import akh.data.db.SharedPreferenceConst.RATES
import akh.data.db.SharedPreferenceConst.THEME
import akh.data.db.mapper.parse
import akh.data.db.mapper.toDB
import akh.data.db.model.RateDB
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecretDBRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SecretDBRepository {

    override fun putRates(rates: List<RateModel>) = sharedPreferences.edit {
        putString(RATES, gson.toJson(rates.map { it.toDB() }))
    }

    private val rateType = object : TypeToken<List<RateDB>>() {}.type

    override fun getRates(): List<RateModel> = try {
        gson.fromJson<List<RateDB>>(
            sharedPreferences.getString(RATES, "") ?: "",
            rateType
        ).map { roleDB ->
            roleDB.parse()
        }
    } catch (t: Throwable) {
        sharedPreferences.edit {
            putString(RATES, "")
        }
        emptyList()
    }

    override fun putTheme(theme: Int) =
        sharedPreferences.edit { putInt(THEME, theme) }

    override fun getTheme(): Int = sharedPreferences.getInt(THEME, CurrentTheme.DEFAULT.ordinal)

}
