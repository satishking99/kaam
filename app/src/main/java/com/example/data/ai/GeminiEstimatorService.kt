package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class JobEstimateResult(
    val suggestedCategory: String,
    val estimatedLabourCost: String,
    val estimatedMaterialCost: String,
    val estimatedTimeRequired: String,
    val expertAdvice: String,
    val breakdownSteps: List<String>,
    val safetyTips: List<String>
)

object GeminiEstimatorService {
    private const val TAG = "GeminiEstimator"
    private const val MODEL = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getJobEstimate(userQuery: String, locationArea: String): Result<JobEstimateResult> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide an intelligent local fallback estimate
            return@withContext Result.success(createLocalFallbackEstimate(userQuery, locationArea))
        }

        try {
            val systemPrompt = """
                You are 'KaamWala Sahayak', an expert construction, home maintenance, and skilled labor cost estimator for Indian households.
                Analyze the user's repair/maintenance work description and area location: '$locationArea'.
                Provide a realistic Indian local market price estimate (in INR ₹).
                Format your output STRICTLY as valid JSON with no markdown backticks, with the following keys:
                {
                   "suggestedCategory": "Mason / Mistri" or "Electrician" or "Plumber" or "Carpenter" or "Painter" or "Tile Worker" or "Mechanic" or "Deep Cleaning" or "Appliance Repair",
                   "estimatedLabourCost": "₹400 - ₹700",
                   "estimatedMaterialCost": "₹200 - ₹500 (Taps, Teflon, Washer)",
                   "estimatedTimeRequired": "1 - 2 hours",
                   "expertAdvice": "Brief friendly advice in conversational Hinglish/Hindi explaining what the worker will check.",
                   "breakdownSteps": ["Step 1...", "Step 2...", "Step 3..."],
                   "safetyTips": ["Turn off main valve/switch...", "Inspect for dampness..."]
                }
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "Task Description: $userQuery\nLocation: $locationArea\nPlease give estimation as JSON.")
                            })
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: ""
                Log.w(TAG, "API failed code: ${response.code}, msg: $errorBody")
                return@withContext Result.success(createLocalFallbackEstimate(userQuery, locationArea))
            }

            val responseString = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseString)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: ""

            val parsedJson = JSONObject(text.trim())
            val breakdownArray = parsedJson.optJSONArray("breakdownSteps")
            val breakdownList = mutableListOf<String>()
            if (breakdownArray != null) {
                for (i in 0 until breakdownArray.length()) {
                    breakdownList.add(breakdownArray.getString(i))
                }
            }
            val safetyArray = parsedJson.optJSONArray("safetyTips")
            val safetyList = mutableListOf<String>()
            if (safetyArray != null) {
                for (i in 0 until safetyArray.length()) {
                    safetyList.add(safetyArray.getString(i))
                }
            }

            val result = JobEstimateResult(
                suggestedCategory = parsedJson.optString("suggestedCategory", "General Home Service"),
                estimatedLabourCost = parsedJson.optString("estimatedLabourCost", "₹350 - ₹600"),
                estimatedMaterialCost = parsedJson.optString("estimatedMaterialCost", "₹150 - ₹400"),
                estimatedTimeRequired = parsedJson.optString("estimatedTimeRequired", "1 - 2 Ghante"),
                expertAdvice = parsedJson.optString("expertAdvice", "Kaam shuru karne se pehle worker se rate final karein."),
                breakdownSteps = if (breakdownList.isNotEmpty()) breakdownList else listOf("Inspection", "Execution", "Testing"),
                safetyTips = if (safetyList.isNotEmpty()) safetyList else listOf("Check for safety before work")
            )
            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Gemini estimation error", e)
            Result.success(createLocalFallbackEstimate(userQuery, locationArea))
        }
    }

    private fun createLocalFallbackEstimate(query: String, area: String): JobEstimateResult {
        val lower = query.lowercase()
        return when {
            lower.contains("pipe") || lower.contains("leak") || lower.contains("tap") || lower.contains("plumb") || lower.contains("nal") -> {
                JobEstimateResult(
                    suggestedCategory = "Plumber",
                    estimatedLabourCost = "₹350 - ₹650",
                    estimatedMaterialCost = "₹150 - ₹450 (Washer, Teflon tape, CPVC fittings)",
                    estimatedTimeRequired = "1 - 2 Ghante",
                    expertAdvice = "Plumber ke aane se pehle main water supply band kar dein taaki leakage se aur damage na ho.",
                    breakdownSteps = listOf(
                        "Main line pressure check and leak detection",
                        "Faulty pipe / washer replacement",
                        "Joint bonding with solvent cement and sealing test",
                        "Water flow confirmation at 100% capacity"
                    ),
                    safetyTips = listOf(
                        "Always insist on ISI marked CPVC / UPVC pipes",
                        "Ensure worker checks bathroom drainage slope"
                    )
                )
            }
            lower.contains("wire") || lower.contains("switch") || lower.contains("mcb") || lower.contains("light") || lower.contains("fan") || lower.contains("electric") -> {
                JobEstimateResult(
                    suggestedCategory = "Electrician",
                    estimatedLabourCost = "₹300 - ₹550",
                    estimatedMaterialCost = "₹100 - ₹350 (Copper wire, Modular switch)",
                    estimatedTimeRequired = "45 mins - 1.5 Ghante",
                    expertAdvice = "Tripping ya short-circuit hone par MCB ko forcefully on na karein, pehle neutral wire leak test karwayen.",
                    breakdownSteps = listOf(
                        "Multimeter voltage & grounding leakage test",
                        "Isolation of the burnt switch or terminal",
                        "Tight crimping and replacement with fire-retardant parts",
                        "Load testing with appliances"
                    ),
                    safetyTips = listOf(
                        "Do not touch wet switchboards",
                        "Always maintain a separate earthing wire for heavy appliances"
                    )
                )
            }
            lower.contains("deewar") || lower.contains("wall") || lower.contains("plaster") || lower.contains("brick") || lower.contains("mistri") || lower.contains("mason") -> {
                JobEstimateResult(
                    suggestedCategory = "Mason / Mistri",
                    estimatedLabourCost = "₹600 - ₹950 per day",
                    estimatedMaterialCost = "₹500 - ₹1200 (Cement bag, Sand, Brick)",
                    estimatedTimeRequired = "1 Din (8 Ghante)",
                    expertAdvice = "Plaster karne ke baad 3 din tak tarai (curing) zaroor karwayen jisse deewar me cracks na aayen.",
                    breakdownSteps = listOf(
                        "Purane loose plaster ki hacking aur safai",
                        "Cement slurry bonding coat application",
                        "1:4 ratio concrete/sand plastering with spirit level",
                        "Smooth sponge finish and edge alignment"
                    ),
                    safetyTips = listOf(
                        "Use fresh 43/53 grade PPC cement",
                        "Check wall moisture before applying finish"
                    )
                )
            }
            lower.contains("paint") || lower.contains("putty") || lower.contains("color") || lower.contains("seelan") -> {
                JobEstimateResult(
                    suggestedCategory = "Painter",
                    estimatedLabourCost = "₹12 - ₹18 per sq ft",
                    estimatedMaterialCost = "₹25 - ₹45 per sq ft (Putty + Primer + Emulsion)",
                    estimatedTimeRequired = "2 - 3 Din",
                    expertAdvice = "Seelan (dampness) wali jagah par direct paint na lagwayen, pehle waterproof damp-block coating zaroori hai.",
                    breakdownSteps = listOf(
                        "Scraping of damaged paint and wall sanding",
                        "Waterproof primer and 2 coats acrylic putty",
                        "Zero-grade sanding for marble-smooth base",
                        "2 coats of premium emulsion roller finish"
                    ),
                    safetyTips = listOf(
                        "Ensure room ventilation while painting",
                        "Cover furniture and flooring with plastic drop cloths"
                    )
                )
            }
            else -> {
                JobEstimateResult(
                    suggestedCategory = "General Skilled Worker",
                    estimatedLabourCost = "₹400 - ₹800",
                    estimatedMaterialCost = "₹200 - ₹500 (as required)",
                    estimatedTimeRequired = "2 Ghante",
                    expertAdvice = "KaamWala verified worker aane ke baad digital quotation check karein aur TrustX escrow me advance lock karein.",
                    breakdownSteps = listOf(
                        "Site inspection and requirement assessment",
                        "Material arrangement with customer consent",
                        "Professional execution and quality verification",
                        "Customer sign-off and TrustX payment release"
                    ),
                    safetyTips = listOf(
                        "Verify worker's KaamWala digital ID badge",
                        "Do not pay direct cash before job completion"
                    )
                )
            }
        }
    }
}
