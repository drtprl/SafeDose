package dam.a47736.safedose.data.operations

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dam.a47736.safedose.data.Dose
import dam.a47736.safedose.data.Interaction
import dam.a47736.safedose.data.Medication
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class GeminiOperations{

    private val apiKey = "AIzaSyCMzQFf4KsdLYvan4410xgYV846M9jwU-g"
    private val model = "gemini-2.0-flash"
    private val endpoint = "https://generativelanguage.googleapis.com/v1/models/$model:generateContent?key=$apiKey"
    private val client = OkHttpClient()
    private val gson = Gson()

    private val system = """System prompt:
    You are a medical expert specialized in drug interactions.
    Given list of drug or medication names, your task is to identify and return only the known interactions between pairs or groups of these substances that could negatively impact health if taken within the same timeframe.

    Instructions:
    Only include combinations with clinically relevant interactions.
    Do not include reversed duplicates (e.g., if ["Viagra", "Poppers"] is listed, do not include ["Poppers", "Viagra"]).
    If no known interaction exists between two or more substances, omit that combination.

    Every interaction need to contain the following field:
    - risk: DEATH, SEVERE ou MODERATE
    - drugs: list of the involved drugs
    - explanation: explanation of the interaction

    Your response must be formatted as a JSON array of objects with the following structure:
    ```json
    [
    {"risk":"DEATH","drugs":["Aspirine", "Varfarina"],"explanation":"This combination can cause fatal hemo"},
    {"risk":"MODERATE","drugs":["Ibuprofeno", "Lisinopril"],"explanation":"Can cause renal loss"}
    ]
    User prompt:
    Return a list of interactions between this list of drugs:"""

    suspend fun makeQuery(dose:Dose, dailyMed: List<Medication>): List<Interaction>{

        return withContext(Dispatchers.IO) {
            try{
                val prompt = system + dose.drugs + dailyMed.map { it.name }
                val jsonBody = buildJsonBody(prompt)
                val request = Request.Builder()
                    .url(endpoint)
                    .addHeader("Content-Type", "application/json")
                    .post(jsonBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext emptyList()

                val text = JSONObject(body)
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val json = text
                    .trim()
                    .removePrefix("```json")
                    .removeSuffix("```")
                    .trim()

                val type = object : TypeToken<List<Interaction>>() {}.type
                gson.fromJson(json, type)
            }catch (e: Exception) {
                println("Erro no Gemini: ${e.message}")
                emptyList()
            }
        }

    }

    private fun buildJsonBody(prompt: String): String {
        val parts = JSONArray().put(JSONObject().put("text", prompt))
        val content = JSONArray().put(JSONObject().put("role", "user").put("parts", parts))
        return JSONObject().put("contents", content).toString()
    }


}
/*
val json = output
                .trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()
            val gson = Gson()
            val type = object : TypeToken<List<Interaction>>() {}.type
            gson.fromJson(json, type)
        } catch(e: Exception){
            println("Error on gemini: ${e.message}")
            return emptyList()
        }
 */