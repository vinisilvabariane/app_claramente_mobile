# Claramente Android — Padrão de arquitetura

Referência normativa do projeto. Segue o mesmo padrão do Iris Mobile (Clean Architecture, contrato
separado de implementação, um tipo por arquivo, pastas por papel, sufixo por papel). O teste
`ArchitectureRulesTest` (módulo `app`) verifica as regras abaixo; não afrouxe o teste para acomodar
código fora do padrão.

## 1. Um tipo por arquivo

- Cada arquivo `.kt` tem **exatamente uma declaração de topo** (class, data class, interface,
  sealed interface, enum class, object ou função `@Composable`) e o **nome do arquivo é o nome dela**.
- Proibido no topo do arquivo: outra classe, `val`/`const val`, função utilitária, `typealias`.
  Constantes vão para o `companion object` do dono ou para um `object XxxDefaults`/`XxxStyle`.
  Funções auxiliares viram membros de um `object` com nome de papel (`XxxReader`, `XxxPolicy`).
- Única exceção: os subtipos de uma `sealed interface`/`sealed class` ficam aninhados nela
  (a hierarquia é um tipo só). Qualquer outro tipo aninhado vai para arquivo próprio.
- Composable: uma função por arquivo. Telas são públicas; peças de tela ficam em `component/`
  como `internal`. As peças do `core:designsystem` são públicas porque as features as usam.
- A regra vale para todos os source sets, inclusive testes: em `app/src/screenshotTest` cada
  `@PreviewTest` fica no próprio arquivo (`LoginPreview.kt`) e as constantes de preview em
  `object PreviewDevices`. O `ArchitectureRulesTest` verifica `src/main` de todos os módulos e
  `app/src/screenshotTest`.

## 2. Módulos e direção das dependências

```
app ──► feature:* ──► core:designsystem, core:auth, core:domain, core:model
core:domain ──► core:model, core:network, core:auth, core:data
core:network ──► core:model                 feature nunca depende de outra feature
core:auth ──► core:network, core:model      core nunca depende de feature
core:data ──► core:model
core:designsystem                           sem dependência interna
```

`app` é a raiz de composição: só ele conhece todos os módulos, monta o `AppContainer` e a navegação.

## 3. Pastas por papel

Pastas permitidas dentro do pacote de cada módulo (crie só as que tiverem arquivo):

| Pasta | O que guarda | Sufixo/forma |
|---|---|---|
| `contract/` | **somente interfaces**: clientes, stores e casos de uso | prefixo `I`: `IAuthClient`, `ITokenStore`, `ILoginUseCase` |
| `usecase/` | implementação de um caso de uso (uma operação) | `*UseCase` implementando `I*UseCase` |
| `error/` | mensagens de negócio/erro do módulo como constantes | `*Errors` (`object`) |
| `client/` | implementação HTTP de um contrato | `Http` + nome: `HttpAuthClient : IAuthClient` |
| `store/` | implementação de persistência local | `Prefs` + nome: `PrefsTokenStore : ITokenStore` |
| `http/` | infraestrutura HTTP compartilhada | `ClaramenteHttp`, `AuthSession`, `ApiException`, `TokenExpiryReader` |
| `dto/` | formato de request/response do servidor | `*Request`, `*Response` |
| `mapper/` | conversão JSON/JWT/base64 ⇄ modelo | `*JsonMapper`, `JwtClaimsMapper`, `Base64UrlMapper` |
| `model/` | modelo de domínio (só dados, sem org.json/Android) | substantivo: `UserSession` |
| `policy/` | regra pura, sem estado e sem I/O | `*Policy` |
| `helper/` | lógica com estado de uma feature | `*Tracker`, `*Recorder` |
| `view/` | tela/diálogo de topo da feature | `*Screen`, `*Dialog` |
| `component/` | peça de tela usada pela própria feature (ou pelo design system) | substantivo + papel: `FeedCard`, `PrimaryButton` |
| `state/` | estado de UI, ações da tela e dados estáticos da tela | `*UiState`, `*Actions`, `HubFeed`, `ArModels` |
| `viewmodel/` | `ViewModel` da tela | `*ViewModel` |
| `controller/` | orquestrador de escopo de aplicação (vive fora de uma tela) | `*Controller` |
| `service/` | **somente** subclasses de `android.app.Service` | `*Service` |
| `di/`, `navigation/` | só no `app` | `AppContainer`, `AppNavigation`, `HomeShell`, `Routes` |
| `screenshot/` | só em `app/src/screenshotTest`: um `@PreviewTest` por arquivo | `*Preview`, `PreviewDevices` |
| `auth/`, `ar/` | áreas do `core:model` (modelo de domínio) | substantivo: `AuthTokens` |
| `theme/` | tema do `core:designsystem` (cores, tipografia, raios, movimento) | `ClaramenteTheme`, `ClaramenteColors` |

Em `core:*` e `feature:*` todo arquivo fica dentro de uma pasta de papel; nada solto na raiz do pacote.

Proibido: `utils/`, `common/`, `misc/`, `helpers/` genérico, `Manager` como saco de responsabilidades.

## 4. Fluxo único e casos de uso

```
View (Composable) ──► ViewModel / Controller ──► I*UseCase ──► I*Client / I*Store ──► Http* / Prefs*
```

- **Um caso de uso é uma operação.** Cada `*UseCase` tem exatamente um método público,
  `execute(...)` (`suspend` ou não), e recebe no construtor só o que essa operação usa.
  Interface `I*UseCase` em `core:domain/contract/`, implementação `*UseCase` em
  `core:domain/usecase/`, mensagens em `core:domain/error/`. O `core:domain` é a camada de
  aplicação: concentra os casos de uso de todas as features (`RestoreSession`, `Login`, `MockLogin`,
  `Logout`).
- **ViewModel/Controller são finos:** chamam o caso de uso, tratam sucesso/falha e publicam estado.
  Não importam `client/`, `store/`, `http/ClaramenteHttp` nem os contratos de cliente/store — só
  `I*UseCase`, `SessionController`, modelos e `ApiException`.
- Regra de negócio fica no caso de uso ou numa `policy/`; a View só desenha e repassa ações.

## 5. Contratos e injeção

- Toda interface vive em `contract/` com prefixo `I`; `contract/` não tem classe concreta.
- Crie contrato quando a dependência fala com o mundo externo (servidor, disco) ou é um caso de
  uso. Não crie interface para data class nem para regra pura (`policy/`).
- Dependa do contrato, nunca da implementação. Contratos pequenos e por área (segregação de
  interface): cada área da API tem seu cliente.
- Injeção manual pelo construtor. Só o `AppContainer` instancia implementações. A configuração
  (`API_BASE_URL`, `MOCK_AUTH`, `AR_WEB_URL`) entra pelo `BuildConfig`, lido de
  `claramente.properties`, e só o `AppContainer` a conhece.

## 6. Resultados e erros

- **Erro esperado é retornado, não lançado:** o caso de uso devolve `Result<T>`
  (`runCatching`/`Result.failure`), e quem chama trata `onSuccess`/`onFailure`.
- Exceção de infraestrutura (`ApiException`, I/O) é capturada no caso de uso e vira `Result.failure`.
- **Mensagem de negócio é constante** em `error/*Errors` do módulo (`AuthErrors`), nunca literal
  solta no ViewModel. Única exceção: `core:network` e `core:auth` não enxergam `core:domain`, então
  "Sessão expirada. Entre de novo." (`ClaramenteHttp`) e "Token inválido." (`SessionController`)
  ficam como constante privada do próprio dono.

## 7. Modelos e nomes

- `model/` é Kotlin puro: nada de `org.json`, `Context` ou tipos de rede. `core:model` inteiro é
  Kotlin puro (sem `import android`).
- Parsing de JSON só em `mapper/`.
- Sufixos proibidos: `Manager`, `Util`, `Utils`, `Helper` (como nome de classe), `Impl`, `Service`
  fora de `service/`.
- Código em inglês, textos de tela em português, sem comentários.

## 8. Interface

- Público-alvo: estudantes do ensino médio. Visual sóbrio de app de estudos: superfícies planas
  com borda fina (`BrandCard`), um único azul de destaque (`Primary`), fonte única Nunito Sans,
  sem mascote, sem sombras deslocadas "de bala" e sem textos infantilizados. Desde 2026-10-06 o
  visual não espelha mais o app React.
- Compose Material 3 + Foundation, sem biblioteca de ícones: glyphs e a marca são desenhados em
  `Canvas` (`HomeGlyph`, `ProfileGlyph`, `ArGlyph`, `BrandMark`).
- Cores, fontes, raios e curvas de movimento vêm só de `core:designsystem/theme/`
  (`ClaramenteColors`, `ClaramenteFonts`, `ClaramenteTypography`, `ClaramenteRadius`,
  `ClaramenteMotion`); nenhuma cor literal em feature.
- O app é só claro (não há tema escuro): a `MainActivity` roda com `enableEdgeToEdge()` forçando
  `SystemBarStyle.light` nas duas barras, independentemente do modo escuro do sistema
  (`ClaramenteColors.Overlay` é o scrim da barra de navegação em API 24–25).
- As telas aplicam `statusBarsPadding()`; o `HomeShell` repassa os insets laterais
  (`safeDrawing` horizontal) pelo `Scaffold`; a `HomeTabBar` aplica os insets da barra de navegação
  com pelo menos 8dp embaixo, como o `Math.max(insets.bottom, 8)` do React.

## 9. Alvos de AR são imutáveis

- O marcador (`marker.jpg`, 12 cm) e as 6 faces do cubo (`cube-face-1..6.jpg`, 5 cm cada) em
  `feature/ar/src/main/assets/ar/` estão impressos em papel (`release/marcador.pdf` e `release/cubo.pdf`).
  Trocar a imagem ou a medida faz o app deixar de reconhecer o que já foi impresso.
- O `ImageTargetsIntegrityTest` (módulo `feature:ar`) trava o SHA-256 de cada imagem, o nome, o caminho e
  a largura física de cada alvo. Não altere o teste nem os hashes para acomodar uma imagem nova: uma
  imagem nova é um alvo novo, com nome novo, e exige reimprimir.
