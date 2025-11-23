
package com.example.plantasking.data.remote
import android.graphics.Bitmap
import android.util.Log
import com.example.plantasking.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content


class PlantRepository {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    /**
     * Analisa a imagem e retorna o texto gerado pela IA.
     * @param bitmap O bitmap da imagem da planta.
     * @return Uma String com o JSON da resposta da IA ou null em caso de erro.
     */
    suspend fun analyzeImage(bitmap: Bitmap): String? {
        return try {
            val prompt = """
                Verifique inicialmente se é uma planta, caso não for, retorne o texto "Não é uma planta".
                Caso seja uma planta,
                Analise esta imagem de uma planta.
                Responda SOMENTE com um objeto JSON com a seguinte estrutura: {"mood": "...", "recommendation": "..."}.
                O 'mood' pode ser 'feliz', 'triste', ou 'doente'.
                A 'recommendation' deve ser uma sugestão de cuidado curta.
            """.trimIndent()
            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )
            val analysisText = response.text
            Log.d("PlantRepository", "Resposta JSON da API: $analysisText")
            analysisText
        } catch (e: Exception) {
            Log.e("PlantRepository", "Erro ao chamar a API Gemini", e)
            null
        }
    }
}
