
package com.example.plantasking.data.repository
import android.graphics.Bitmap
import android.util.Log
import com.example.plantasking.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content


class PlantRepository {

    val default_prompt = """
                Verifique inicialmente se é uma planta, caso não for, retorne o texto "Não é uma planta".
                Caso seja uma planta,
                Analise esta imagem de uma planta.
                Responda SOMENTE com um uma mensagem com a seguinte estrutura:
                  Sentimento: mood
                  Recomendação: recommendation
                Subtitua as palavras, onde:
                O 'mood' pode ser 'Feliz', 'Triste', ou 'Doente'.
                A 'recommendation' deve ser uma sugestão de cuidado curta.
            """.trimIndent()

    //TODO: essa aqui é o custom prompt para usar no method de conversação. Deixar com placeholders tipo '%s' para que quando o user digitar algo, seja substituido
    var custom_prompt = """
                 Prompt de exemplo, meu nome é %s, minha idade é %d, e eu peso %.2f
                """.trimIndent()


    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    /**
     * Analisa a imagem e retorna o texto gerado pela IA.
     * @param bitmap O bitmap da imagem da planta.
     * @return Uma String com o JSON da resposta da IA
     */
    suspend fun analyzeImage(bitmap: Bitmap): String? {
         try {
            val imageGenerated = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(default_prompt)
                }
            )
            Log.d("analyzeImage", "Resposta JSON da API: $imageGenerated.text")
            return imageGenerated.text
        } catch (exc: Exception) {
            Log.e("PlantRepositoryException", "Erro ao chamar a API Gemini", exc)
            return exc.stackTraceToString()
        }
    }

    //TODO: nos params como tu pode ver, no method ele recebe uma string. Exemplo: nome: String, idade: Int, peso: Double
    //TODO: vai utilizar o String.format(prompt, message) para formatar a msg do user dentro do prompt e retornar a str.
    //TODO: talvez precise utilizar Index para identificar onde colocar a msg do user, vai de como tu for utilizar
    suspend fun generateChatByImage(bitmap: Bitmap, nome: String, idade: Int, peso: Double): String? {
         try {
            val imageGenerated = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(default_prompt)
                }
            )
             //TODO: alterar aqui para formatar o prompt
            String.format(custom_prompt, nome, idade, peso)
            Log.d("generateChatByImage", "Resposta JSON da API: $imageGenerated.text")
            return imageGenerated.text
        } catch (exc: Exception) {
            Log.e("PlantRepositoryException", exc.message, exc)
            return exc.stackTraceToString()
        }
    }

}
