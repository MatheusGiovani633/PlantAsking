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

### 1. MainActivity & Navegação

**Arquivo:** `ui/MainActivity.kt`

#### PlantAskingApp

Gerencia o fluxo de navegação entre login, home e chat.

```kotlin
@Composable
fun PlantAskingApp(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var isInitChat by remember { mutableStateOf(false) }
    if (isLoggedIn) {
        HomeScreen(
            modifier = modifier,
            onInitChat = {
               if(isLoggedIn && !isInitChat){
                   isInitChat = true
               }
                if(isInitChat){
                   ChatScreen()
               }
            },
        )
    } else {
        LoginScreen(
            modifier = modifier,
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}
```

---

### 2. Login

**Arquivo:** `ui/login/LoginScreen.kt`

#### LoginScreen

Tela de login com campos para email e senha.

```kotlin
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // ... campos de texto e botão ...
    Button(
        onClick = {
            if (login(email, password)) {
                onLoginSuccess()
            } else {
                println("Login failed")
            }
        },
        // ...
    ) { /* ... */ }
}
```

#### Login

Função simples de autenticação.

```kotlin
private fun login(email: String, password: String): Boolean {
    return email == "Admin" && password == "admin"
}
```

---

### 3. Home (Câmera & Análise)

**Arquivo:** `ui/home/HomeScreen.kt`

#### HomeScreen

Gerencia permissões, preview da câmera, captura de foto e exibição dos resultados.

```kotlin
fun HomeScreen(
    onInitChat: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    // ... gerenciamento de permissões ...
    if (hasRequiredPermissions) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(), onTakePictureClick = { imageCapture ->
                viewModel.onTakePicture(context, imageCapture)
            })
    } else {
        PermissionDeniedContent(
            onRequestPermission = { /* ... */ }
        )
    }
    if (uiState.showDialog) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onDialogDismiss() }, sheetState = sheetState
        ) {
            ActionMenuContent(
                onInitChat = { viewModel.onDialogDismiss() },
                onSaveMoodClick = { viewModel.onDialogPictured(context) },
                onDismiss = { viewModel.onDialogDismiss() }
            )
        }
    }
    if (uiState.analysisResult != null) {
        ViewMood(
            onDismiss = { viewModel.onDialogDismiss() }, analysisText = uiState.analysisResult
        )
    }
}
```

#### CameraPreview

Preview da câmera e botão para tirar foto.

```kotlin
@Composable
fun CameraPreview(modifier: Modifier = Modifier, onTakePictureClick: (ImageCapture) -> Unit) {
    // ...
    IconButton(
        onClick = {
            onTakePictureClick(imageCapture)
        },
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 64.dp)
            .size(150.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.iconphoto),
            contentDescription = "Tirar Foto"
        )
    }
}
```

#### ActionMenuContent

Menu de ações após tirar foto.

```kotlin
private fun ActionMenuContent(
    onSaveMoodClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onInitChat: () -> Unit,
) {
    Column(
        // ...
    ) {
        Row(
            // ...
        ) {
            ActionMenuItem(
                drawableResId = R.drawable.talk,
                text = "Conversar",
                onClick = onInitChat,
                modifier = Modifier.weight(1f)
            )
            ActionMenuItem(
                drawableResId = R.drawable.mood,
                text = "Ver Humor",
                onClick = onSaveMoodClick,
                modifier = Modifier.weight(1f)
            )
        }
        // ...
        ActionMenuItem(
            drawableResId = R.drawable.photo,
            text = "Tirar nova foto",
            onClick = onDismiss,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}
```

#### ViewMood

Exibe o resultado da análise da planta.

```kotlin
fun ViewMood(
    onDismiss: () -> Unit,
    analysisText: String
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text("Humor da Planta")
    },
        text = {
            Text(analysisText)
        }, confirmButton = {
            TextButton(
                onClick = onDismiss, modifier = Modifier.fillMaxWidth()
            ) {
                Text("OK")
            }
        }, icon = {
            if (analysisText.contains("feliz")) {
                Image(painterResource(id = R.drawable.moodhappy), null, Modifier.size(50.dp))
            } else if(analysisText.contains("doente")){
                Image(painterResource(id = R.drawable.moodsick), null, Modifier.size(50.dp))
            } else if(analysisText.contains("triste")){
                Image(painterResource(id = R.drawable.moodsad), null, Modifier.size(50.dp))
            } else{
                Image(painterResource(id = R.drawable.error), null, Modifier.size(50.dp))
            }
        })
}
```

---

### 4. HomeViewModel

**Arquivo:** `ui/home/HomeScreenViewModel.kt`

Gerencia o estado da tela principal e integra com a análise de imagem via IA.

```kotlin
class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun onTakePicture(context: Context, imageCapture: ImageCapture) {
        uiState = uiState.copy(isLoading = true, showDialog = false)
        val file = File.createTempFile("IMG_", ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        uiState = uiState.copy(
                            capturedImageUri = uri, isLoading = false, showDialog = true
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("HomeViewModel", "Erro ao salvar a foto.", exception)
                    uiState = uiState.copy(isLoading = false)
                }
            })
    }

    fun onDialogPictured(context: Context) {
        viewModelScope.launch {
            uiState.capturedImageUri?.let { uri ->
                val bitmap = convertUriToBitmap(context, uri)
                if (bitmap != null) {
                    val analysisJson = PlantRepository().analyzeImage(bitmap)
                    uiState = uiState.copy(
                        isLoading = false, analysisResult = analysisJson, showDialog = true
                    )
                }
            }
        }
    }

    fun onDialogDismiss() {
        uiState = uiState.copy(
            isLoading = false, analysisResult = null, showDialog = false, capturedImageUri = null
        )
    }

    // ...
}
```

---

### 5. Chat

**Arquivo:** `ui/chat/ChatScreen.kt`

#### ChatScreen

Tela do chat com histórico de mensagens e campo de envio.

```kotlin
fun ChatScreen() {
    val messages = listOf(
        Message("Olá! Como posso te ajudar com sua planta hoje?", MessageAuthor.BOT),
        Message("Oi! As folhas dela estão meio amareladas...", MessageAuthor.USER),
        // ...
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF80E2825))
            .drawBehind { /* ... */ }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatMenu(onBackClicked = {})
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp), reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    MessageBubble(message = message)
                }
            }
            TextChat(onMessageSend = {})
        }
    }
}
```

#### MessageBubble

Exibe cada mensagem no chat.

```kotlin
fun MessageBubble(message: Message) {
    val horizontalArrangement =
        if (message.author == MessageAuthor.USER) Arrangement.End else Arrangement.Start

    val bubbleColor = if (message.author == MessageAuthor.USER) Color(0x7CF6F049) else Color(0xC6FF9100)
    // ...
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {

        if (message.author == MessageAuthor.BOT) {
            Image(painterResource(id = R.drawable.iconphoto), "Plant", Modifier.size(40.dp))
            Box(/* ... */) { Text(text = message.text, color = Color.White) }
        } else {
            Box(/* ... */) { Text(text = message.text, color = Color.White) }
            Image(painterResource(id = R.drawable.user), "User", Modifier.size(35.dp).padding(start=8.dp))
        }
    }
}
```
