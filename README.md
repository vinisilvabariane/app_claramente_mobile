# Claramente Android

App Android nativo (Kotlin + Jetpack Compose) do Claramente. Faz login no backend .NET, restaura a
sessão guardada no aparelho, mostra o Início (feed da turma), o Perfil e a Realidade Aumentada, que
abre a experiência web (MindAR) no navegador do celular. Funcionalidades do app React Native/Expo
(`claramente-android`) com interface própria voltada ao ensino médio, mesma arquitetura do Iris Mobile.

## Requisitos

- Android Studio (AGP 9.4, Kotlin 2.2, JDK 17 embutido)
- Celular ou emulador Android 7.0+ (`minSdk 24`)
- Backend do Claramente acessível pela rede (no emulador, `10.0.2.2` aponta para o localhost do PC)

## Configuração

Equivalente ao `.env` do React. Copie `claramente.properties.example` para `claramente.properties`
na raiz do projeto (não versionado) e ajuste:

```
CLARAMENTE_API_URL=http://10.0.2.2:5090        # URL base do backend .NET, sem /api
CLARAMENTE_MOCK_AUTH=true                       # botão "Entrar em modo teste" (debug e release)
CLARAMENTE_AR_WEB_URL=https://seu-site.pages.dev # página de AR publicada em HTTPS
```

`app/build.gradle.kts` lê o arquivo e expõe `BuildConfig.API_BASE_URL`, `BuildConfig.MOCK_AUTH` e
`BuildConfig.AR_WEB_URL`. Cada chave também pode vir por `-PCLARAMENTE_...` ou variável de ambiente.
`CLARAMENTE_MOCK_AUTH` aceita `true`/`false` (ou `1`, como no `.env` do React); qualquer outro valor
vale `false`. Sem `CLARAMENTE_AR_WEB_URL` a aba AR mostra o aviso de configuração em vez do botão de
abrir. Só o build debug permite `http` (cleartext) para falar com a API local
(`res/xml/network_security_config.xml` de `src/debug`); o release bloqueia cleartext em qualquer API.

## Build

Android Studio: Sync → Run no dispositivo.

No terminal, aponte o `JAVA_HOME` para o JBR do Android Studio e use o wrapper:

```
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug                      # app/build/outputs/apk/debug/app-debug.apk
.\gradlew.bat testDebugUnitTest                  # ArchitectureRulesTest (app) + SessionUseCasesTest (core:domain)
.\gradlew.bat :app:validateDebugScreenshotTest   # compara as telas com as referências em app/src/screenshotTestDebug/reference
.\gradlew.bat :app:updateDebugScreenshotTest     # regenera as referências depois de mudar a UI de propósito
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### APK instalável (release)

O release é assinado com a chave do app, que **não fica no repositório**. O Gradle lê
`~/.claramente-signing/keystore.properties` (ou o caminho em `CLARAMENTE_SIGNING_PROPERTIES`), com
`storeFile`, `storePassword`, `keyAlias` e `keyPassword`. Sem esse arquivo o release sai sem
assinatura e o Android recusa instalar; o debug não muda.

```
.\gradlew.bat assembleRelease     # app/build/outputs/apk/release/app-release.apk
adb install -r app/build/outputs/apk/release/app-release.apk
```

A pasta `release/` guarda a última cópia gerada (`claramente-<versão>-release.apk`, assinada, e
`claramente-<versão>-debug.apk`). Diferenças: o release só fala com a API em HTTPS; o debug aceita `http`.
O botão "Entrar em modo teste" aparece em qualquer build com `CLARAMENTE_MOCK_AUTH=true`: **ponha `false`
antes de gerar um APK para distribuir**, senão qualquer pessoa entra sem senha.

- **Toda máquina que gera atualização precisa da mesma chave.** O aparelho só aceita atualizar por
  cima um APK assinado pela mesma chave; perder a chave obriga a desinstalar o app antes de instalar
  um novo. Um aparelho com o APK **debug** não aceita o release por cima (chaves diferentes).
- Suba `versionCode` (e `versionName`) em `app/build.gradle.kts` a cada versão distribuída.

Os screenshot tests (plugin `com.android.compose.screenshot`) vivem em
`app/src/screenshotTest/kotlin/.../screenshot/`: um `@PreviewTest` por arquivo (`LoginPreview`,
`HubPreview`, `ProfilePreview`, `ArPreview`, ...) e o tamanho de tela em `PreviewDevices`. As
referências PNG ficam em `app/src/screenshotTestDebug/reference/`, uma pasta por preview. A flag
`android.experimental.enableScreenshotTest=true` precisa estar nos dois lugares: em `gradle.properties`
(exigida pelo plugin) e em `experimentalProperties` de `app/build.gradle.kts` (exigida pelo AGP para
criar o source set); remover qualquer uma delas quebra a configuração do build.

## Estrutura

Multi-módulo, um tipo por arquivo e pastas por papel. As regras completas (normativas) estão em
[AGENTS.md](AGENTS.md) e são verificadas pelo `ArchitectureRulesTest` a cada `testDebugUnitTest`
(em `src/main` de todos os módulos e em `app/src/screenshotTest`).

Fluxo: `View (Compose) → ViewModel → I*UseCase (core:domain) → I*Client / I*Store → Http* / Prefs*`.

```
app/                 ClaramenteApplication, MainActivity, di/AppContainer,
                     navigation/ (AppNavigation, HomeShell, HomeTabBar, HomeTab, Routes)
core/
├─ model/            domínio puro: auth/AuthTokens
├─ network/          contract/IAuthClient, client/HttpAuthClient, http/ (ClaramenteHttp, AuthSession,
│                    ApiException, TokenExpiryReader), mapper/AuthJsonMapper
├─ auth/             controller/SessionController, model/UserSession,
│                    mapper/ (JwtClaimsMapper, Base64UrlMapper), policy/ (TokenValidityPolicy, MockTokenPolicy)
├─ data/             contract/ITokenStore, store/PrefsTokenStore
├─ domain/           camada de aplicação: contract/ (I*UseCase), usecase/ (*UseCase), error/ (*Errors)
└─ designsystem/     theme/ (ClaramenteTheme, ClaramenteColors, ClaramenteTypography, ...),
                     component/ (PrimaryButton, SecondaryButton, BrandCard, BrandTextField, BrandMark, glyphs)
feature/
├─ session/          state/SessionUiState, viewmodel/SessionViewModel (restaura e encerra a sessão)
├─ login/            view/LoginScreen, component/, state/, viewmodel/LoginViewModel
├─ hub/              view/HubScreen, component/, state/ (HubFeed, FeedItem, FeedTone)
├─ profile/          view/ProfileScreen, component/
└─ ar/               view/ArScreen, component/, state/ (ArModels, ArUiState), viewmodel/ArViewModel
```

Regras principais:

- **Um tipo por arquivo**, com o nome do arquivo igual ao da declaração.
- **Feature não depende de feature.** O que duas precisam desce para um `core`; a navegação entre
  telas fica no `app`.
- **Um caso de uso é uma operação**, com interface `I*UseCase` e retorno `Result<T>`. ViewModel só
  fala com casos de uso.
- **Toda interface vive em `contract/` com prefixo `I`.** Só o `AppContainer` instancia implementações.

### Sessão

Porta do `AuthProvider` do React.

1. **Boot:** `SessionViewModel` chama `RestoreSessionUseCase`, que lê os tokens do `PrefsTokenStore`.
   Token válido → abre a sessão; expirado → tenta `POST /auth/refresh`; refresh recusado (401/403) →
   limpa o store. Ao final marca `checked`, e `AppNavigation` troca o `LoadingScreen` por Início ou Login.
2. **Login:** `LoginViewModel` → `LoginUseCase` → `POST /auth/login`, guarda os tokens e publica a
   sessão no `SessionController`; a navegação reage e vai para o Início.
3. **Modo teste:** com `CLARAMENTE_MOCK_AUTH=true`, "Entrar em modo teste" gera um JWT
   local (`MockTokenPolicy`) sem falar com o servidor.
4. **Sair da conta:** `LogoutUseCase` avisa o servidor, limpa o store e a sessão; volta para o Login.
5. **401 em chamada autenticada:** `ClaramenteHttp.authenticated` faz o refresh uma vez e repete; se o
   refresh for recusado, o `AppContainer` limpa store e sessão e o app volta para o Login.

### Realidade aumentada nativa

A aba AR roda a AR dentro do app, com ARCore e SceneView (Filament), sem abrir o navegador. A tela
`ArScreen` escolhe o conteúdo (Sistema Solar, Molécula de água, Sólidos geométricos) e o modo; o botão
abre a `ArExperienceScreen` em tela cheia (`Routes.AR_EXPERIENCE`).

| Modo | Como funciona |
|---|---|
| Marcador | Augmented Images com `app/.../assets/ar/marker.jpg` (12 cm). O modelo aparece sobre a folha impressa. |
| Cubo | 6 faces (`cube-face-1..6.jpg`, 5 cm). Uma face ativa por vez (`CubeFacePolicy`); o modelo fica no centro do cubo. |
| QR code | O ML Kit lê o QR dentro da própria sessão ARCore (`QrFrameScanner`). `claramente:<id>` ou o id cru escolhe o modelo, fixado 0,5 m à frente. |

Os modelos são primitivas 3D (esferas, cubos, cone, texto com billboard) com as medidas do React, sem
arquivos glTF. Antes de abrir a câmera a tela verifica o ARCore (`ArCoreSupportProbe`): pede a instalação do
Google Play Services for AR quando falta e, em aparelho sem suporte, oferece a versão web (botão "Abrir
versão web", que usa `CLARAMENTE_AR_WEB_URL`). O manifest declara a AR como opcional, então o app instala em
qualquer aparelho. Os alvos do marcador e do cubo são montados em tempo de execução a partir dos JPEG de
`assets/ar/` (convertidos dos PNG do React). O release leva só `arm64-v8a` para reduzir o tamanho.

### Realidade aumentada (versão web)

A aba AR lista os conteúdos (`ArModels`) e abre `<CLARAMENTE_AR_WEB_URL>/ar/index.html?modelo=<id>`
em uma Custom Tab (toolbar na cor Ink), com fallback para o navegador padrão. A câmera roda no
navegador e exige HTTPS, por isso a página precisa estar publicada.
