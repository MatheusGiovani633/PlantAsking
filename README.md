# **PlantAsking - Documentação Técnica**

## **Visão Geral do Projeto**

O **PlantAsking** é um aplicativo Android desenvolvido em Kotlin com Jetpack Compose, cujo objetivo é permitir que usuários interajam com plantas de forma inovadora. O app possibilita:

- Escanear uma planta usando a câmera do dispositivo.
- Analisar a saúde e o "humor" da planta por meio de IA generativa (Google Gemini).
- Exibir recomendações de cuidados.
- Conversar com a planta via chat, simulando uma interação natural.

O projeto adota princípios de usabilidade, acessibilidade e integração com recursos modernos do Android, como permissões dinâmicas, CameraX, e Material 3.

---

## **Módulos e Bibliotecas Utilizadas**

### **Principais Bibliotecas**

- **Jetpack Compose**: Framework declarativo para UI nativa Android.
- **CameraX** (`androidx.camera.core`, `androidx.camera.lifecycle`, `androidx.camera.view`): Para captura e preview de imagens da câmera.
- **Material 3** (`androidx.compose.material3`): Componentes visuais modernos.
- **Google Gemini AI** (`com.google.ai.client.generativeai`): Para análise de imagens e geração de respostas via IA.
- **AndroidX Lifecycle**: Gerenciamento de ciclo de vida em componentes Compose.
- **Kotlin Coroutines**: (implícito via `suspend` functions) para operações assíncronas.

### **Outros Recursos**

- **Permissões Dinâmicas**: `ActivityResultContracts.RequestMultiplePermissions`
- **PreviewView**: Para exibir o preview da câmera.
- **ViewModel**: (presumido, pois há uso de `viewModel()`).

---

## **Arquitetura e Estrutura dos Arquivos**

O projeto está organizado nos seguintes pacotes principais:

- `ui.home`: Tela principal, preview da câmera, permissões, análise e exibição do humor da planta.
- `ui.chat`: Tela de chat, bolhas de mensagem, menu do chat.
- `ui.login`: Tela de login.
- `data.remote`: Comunicação com a API Gemini para análise de imagens.
- `ui`: MainActivity e inicialização do app.

---

## **Documentação do Código**

### **1. MainActivity & Navegação**

**Arquivo:** `ui/MainActivity.kt`

- **PlantAskingApp**: Função composable que gerencia o fluxo de navegação entre login, home e chat. Usa estados locais para controlar se o usuário está logado e se o chat foi iniciado.
- **MainActivity**: Inicializa o tema e chama `PlantAskingApp`.

### **2. Login**

**Arquivo:** `ui/login/LoginScreen.kt`

- **LoginScreen**: Tela de login com campos para email e senha. Usa estados locais para armazenar os valores dos campos. Ao clicar em "Entrar", chama a função `login`.
- **login(email, password)**: Função simples que valida se o email é "Admin" e a senha "admin". Se sim, chama o callback de sucesso.

### **3. Home (Câmera & Análise)**

**Arquivo:** `ui/home/HomeScreen.kt`

#### Componentes principais:

- **HomeScreen**: Tela principal após login. Gerencia permissões de câmera/áudio, exibe preview da câmera, botão para tirar foto, loading, menu de ações (chat, ver humor, tirar nova foto) e exibe resultados da análise.
    - Usa `rememberLauncherForActivityResult` para solicitar permissões.
    - Exibe diferentes conteúdos conforme o estado das permissões e do ViewModel.

- **CameraPreview**: Exibe o preview da câmera usando CameraX. Possui botão para capturar imagem, que aciona callback com o objeto `ImageCapture`.

- **PermissionDeniedContent**: Exibe mensagem e botão para solicitar permissões caso não estejam concedidas.

- **ActionMenuContent**: Menu exibido em um modal bottom sheet após tirar foto. Permite iniciar chat, ver humor ou tirar nova foto.

- **ActionMenuItem**: Item visual do menu de ações.

- **ViewMood**: Exibe um AlertDialog com o resultado da análise da planta (humor), mostrando um ícone correspondente ao sentimento detectado (feliz, doente, triste ou erro).

### **4. Chat**

**Arquivo:** `ui/chat/ChatScreen.kt`

#### Componentes principais:

- **ChatScreen**: Tela do chat. Exibe histórico de mensagens (mockado), campo para digitar mensagem e enviar, além do menu superior.
    - Usa uma lista fixa de mensagens para simular conversa.
    - Layout customizado com fundo decorativo.

- **TextChat**: Campo de texto para digitar mensagem e botão de envio.

- **MessageBubble**: Exibe cada mensagem no chat, diferenciando visualmente mensagens do usuário e do bot (incluindo ícones).

- **ChatMenu**: Barra superior do chat com botão de voltar e título.

### **5. Integração com IA (Gemini)**

**Arquivo:** `data/remote/PlantRepository.kt`

- **PlantRepository**
    - Instancia um modelo generativo Gemini usando a chave da API.
    - Função `analyzeImage(bitmap)`:
        - Envia imagem + prompt para a IA.
        - Prompt instrui a IA a identificar se é uma planta e retornar apenas um texto estruturado com sentimento ("Feliz", "Triste", "Doente") e recomendação curta.
        - Retorna resposta textual da IA ou null em caso de erro.

---

## **Fluxo Principal do App**

1. Usuário faz login na tela inicial.
2. Após login bem-sucedido, é direcionado à tela Home.
3. App solicita permissões de câmera/áudio.
4. Usuário pode tirar foto da planta:
    - Foto é enviada para análise via IA Gemini.
    - Resultado é exibido em um modal (humor + recomendação).
5. Usuário pode abrir o chat para conversar com a planta (simulação).
6. Usuário pode repetir processo ou sair.

---

## **Resumo das Funções Principais**

| Função/Componente         | Descrição                                                                 |
|--------------------------|---------------------------------------------------------------------------|
| PlantAskingApp           | Gerencia navegação entre login, home e chat                               |
| LoginScreen/login        | Tela e lógica de autenticação                                             |
| HomeScreen               | Tela principal; gerencia permissões, preview da câmera, ações             |
| CameraPreview            | Preview da câmera + captura de imagem                                     |
| PermissionDeniedContent  | Mensagem para solicitar permissões                                        |
| ActionMenuContent/Item   | Menu de ações após tirar foto                                             |
| ViewMood                 | Exibe resultado da análise da planta                                      |
| ChatScreen/TextChat      | Tela e campo de chat                                                      |
| MessageBubble            | Exibe mensagens no chat                                                   |
| ChatMenu                 | Barra superior do chat                                                    |
| PlantRepository          | Integração com Gemini AI para análise de imagem                           |
