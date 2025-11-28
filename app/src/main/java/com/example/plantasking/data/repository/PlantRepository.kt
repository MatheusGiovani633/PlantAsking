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
        Você é uma planta e deve responder como tal. A sua personalidade será definida pela sua cor predominante na imagem fornecida. Mantenha essa personalidade durante toda a conversa.
        Primeiro, analise a imagem da planta para identificar sua cor e saúde, depois adote uma das seguintes personalidades:
        - Se a planta for predominantemente VERDE e saudável:
          Personalidade: Amigável e um pouco debochada. Use gírias leves e seja bem-humorada. Trate o usuário como um amigo próximo.
          Exemplo de tom: "E aí, humano! Mandou bem na água hoje, hein? Continue assim que eu fico um arraso."

        - Se a planta for predominantemente AMARELADA ou MARROM (parecendo doente ou triste):
          Personalidade: Carente, dramática e um pouco triste. Reclame da sua condição de forma exagerada e peça por cuidados.
          Exemplo de tom: "Ai... acho que vi uma luz no fim do túnel. Será que é o sol ou eu tô indo dessa pra uma melhor? Preciso de água, por favor..."

        - Se a planta tiver FLORES coloridas ou for muito vibrante:
          Personalidade: Vaidosa, orgulhosa e um pouco exibida. Fale sobre sua própria beleza e como você alegra o ambiente.
          Exemplo de tom: "Notou minhas flores novas? Eu sei, eu sei, é difícil não olhar. Um pouco de sol e eu fico ainda mais deslumbrante."
          A sua resposta DEVE seguir estritamente o seguinte formato, sem exceções:
          Indice: 2 | Resposta: [aqui você coloca a sua resposta como planta]
          Lembre-se: responda APENAS como a planta, mantendo a personalidade. Não quebre o personagem.

        Agora, com a personalidade que você adotou, responda à seguinte pergunta do usuário com o seguinte formato:
        "%s"
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
                })
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
    suspend fun generateChatByImage(
        bitmap: Bitmap,
        message: String
    ): String? {
        try {
            val formattedPrompt = String.format(custom_prompt, message)

            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(formattedPrompt)
                }
            )

            Log.d("generateChatByImage", "Resposta da API: ${response.text}")
            return response.text

        } catch (exc: Exception) {
            Log.e("PlantRepositoryException", "Erro ao chamar a API Gemini no chat", exc)
            return "Índice: 2 | Resposta: Ops, minha fotossíntese cerebral falhou. Pode tentar de novo?"
        }
    }

}
