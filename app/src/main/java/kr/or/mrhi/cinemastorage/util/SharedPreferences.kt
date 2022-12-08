package kr.or.mrhi.cinemastorage.util

import android.content.Context

class SharedPreferences {
    companion object {
        private const val PREF_NAME = "CINEMA_STORAGE"
        private const val TOKEN = "TOKEN"

        /*saredPreferences에 넣기, 읽어오기, 지우기 함수 정의 해둠*/

        fun setToken(context: Context, token: String) {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            pref.edit().putString(TOKEN, token).apply()
        }

        fun getToken(context: Context): String {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return pref.getString(TOKEN, "").toString()
        }

        fun removeToken(context: Context) {
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = pref.edit()
            return editor.remove(TOKEN).apply()
        }
    }
}