package de.msal.muzei.nationalgeographic

import android.os.Build
import android.text.Html
import com.google.gson.*
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.reflect.Type

class ItemDeserializer : JsonDeserializer<Item> {

   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
      json as JsonObject

      val item = Gson().fromJson(json, Item::class.java)

      // clean up photographer info
      item.image?.credit = item.image?.credit
            ?.stripHtml()
            ?.replace("Photograph by ", "")
            ?.replace(", National Geographic Your Shot", "")
            ?.replace(", National Geographic", "")
            ?.replace(", Your Shot", "")
            ?.replace(", My Shot", "")
            ?.trim()
      return item
   }

   private fun String?.stripHtml(): String? {
      return when {
         this == null -> {
            this
         }
         Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
         }
         else -> {
            @Suppress("DEPRECATION")
            Html.fromHtml(this).toString()
         }
      }
   }

}
