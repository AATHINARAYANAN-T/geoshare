package page.ooooo.geoshare.lib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

class IntentTools {

    private val extraProcessed = "page.ooooo.geoshare.EXTRA_PROCESSED"

    private val intentUrlRegex = Regex("https?://\\S+")

    fun isProcessed(intent: Intent): Boolean =
        intent.getStringExtra(extraProcessed) != null

    fun share(context: Context, action: String, uriString: String) {
        context.startActivity(Intent(action).apply {
            data = Uri.parse(uriString)
            putExtra(extraProcessed, "true")
        })
    }

    fun getIntentGeoUri(intent: Intent): String? {
        return if (intent.action == Intent.ACTION_VIEW && intent.data != null && intent.scheme == "geo") {
            intent.data?.toString()
        } else {
            null
        }
    }

    fun getIntentUrlString(intent: Intent): String? {
        when (val intentAction = intent.action) {
            Intent.ACTION_VIEW -> {
                val intentData: String? = intent.data?.toString()
                if (intentData == null) {
                    Log.w(null, "Missing intent data")
                    return null
                }
                return intentData.toString()
            }

            Intent.ACTION_SEND -> {
                val intentText =
                    intent.getStringExtra("android.intent.extra.TEXT")
                if (intentText == null) {
                    Log.w(null, "Missing intent extra text")
                    return null
                }
                val intentUrlMatch = intentUrlRegex.find(intentText)
                if (intentUrlMatch == null) {
                    Log.w(null, "Intent extra text does not contain a URL")
                    return null
                }
                return intentUrlMatch.value
            }

            else -> {
                Log.w(null, "Unsupported intent action $intentAction")
                return null
            }
        }
    }
}
