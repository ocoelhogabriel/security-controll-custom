# Análise Arquitetural do Projeto security-control-custom

Este documento serve como um guia contínuo para a evolução arquitetural do projeto, seguindo princípios como Clean Architecture, SOLID, Arquitetura Hexagonal e Design Patterns. Ele será atualizado regularmente para refletir o estado atual, as necessidades de refatoração e os próximos passos.

**Última Atualização:** O projeto passou por um ciclo de refatoração significativo para alinhar os fluxos de negócio principais (Usuário, Autenticação, Perfil, Abrangência) com a Arquitetura Hexagonal.

---

## 1. O que Falta Implementar (Próximos Passos)

*   [ ] **Finalizar Refatoração dos Serviços Simples:**
    *   **Ação:** Aplicar o mesmo processo de refatoração (mover interface para `application.port.in`, limpar dependências, refatorar serviço e controller) para os serviços restantes: `IEmpresaService`, `IPlantaService`, `IRecursoService`.
    *   **Objetivo:** Completar a modernização da camada de aplicação, garantindo consistência arquitetural em todo o projeto.

*   [ ] **Refinar o Mecanismo de Autorização:**
    *   **Ação:** Extrair a lógica do método `checkPermission` (atualmente em `PerfilPermissaoServiceImpl`) para uma classe `PermissionEvaluator` customizada do Spring Security.
    *   **Objetivo:** Centralizar a lógica de autorização e permitir o uso de expressões mais poderosas e declarativas nas anotações `@PreAuthorize`, como `@PreAuthorize("hasPermission(#id, 'usuario', 'edit')")`.

*   [ ] **Purificar a Paginação na Camada de Aplicação:**
    *   **Ação:** Criar classes agnósticas ao framework (ex: `PageRequest`, `PagedResult`) dentro do pacote `application` ou `domain`.
    *   **Ação:** Refatorar os `Ports` em `application.port.in` para usar essas novas classes em vez de `Page` e `Pageable` do Spring Data.
    *   **Objetivo:** Atingir o desacoplamento total da camada de aplicação, removendo as últimas dependências de frameworks.

*   [ ] **Implementar Testes Abrangentes:**
    *   **Ação:** Desenvolver testes unitários para o `domain` (lógica de negócio pura), testes de integração para a camada de `application` (casos de uso) e testes de aceitação para a camada de `infrastructure` (controllers).
    *   **Objetivo:** Garantir a qualidade, robustez e manutenibilidade da nova arquitetura.

---

## 2. O que Precisa Refatorar (Oportunidades de Melhoria)

*   [ ] **Revisão de Injeção de Dependências:**
    *   **Ação:** Analisar se serviços de aplicação estão injetando outros serviços de aplicação diretamente (ex: `AuthServiceImpl` injetando `UsuarioServiceImpl`).
    *   **Objetivo:** Avaliar se a lógica pode ser reorganizada para que os serviços de aplicação dependam apenas de `Ports` da camada de infraestrutura (ex: repositórios), ou se a dependência entre serviços é justificada para o caso de uso.

---

## 3. O que Já Está Certo (Concluído)

*   [x] **Estrutura de Pacotes Alinhada com Arquitetura Hexagonal:** A separação em `domain`, `application` e `infrastructure` está clara. Os `Ports` (interfaces) e `Adapters` (implementações) estão nos pacotes corretos.

*   [x] **Fluxo de Usuário Refatorado:**
    *   `IUsuarioService` movido para `application.port.in` e desacoplado de `ResponseEntity`.
    *   `UsuarioServiceImpl` refatorado para implementar o novo `Port` e usar exceções de aplicação (`UserNotFoundException`, `AdminUserModificationException`).
    *   `UsuarioController` refatorado para ser um `Adapter` puro, responsável por HTTP e tratamento de exceções.

*   [x] **Fluxo de Autenticação Refatorado:**
    *   `IAuthService` e `IAutentication` substituídos por um `IAuthenticationService` limpo em `application.port.in`.
    *   `AuthServiceImpl` agora implementa `IAuthenticationService` (Port) e `UserDetailsService` (Adapter), separando as responsabilidades.
    *   `AuthenticationController` refatorado para ser um `Adapter` web puro.

*   [x] **Fluxo de Perfil e Permissão Refatorado:**
    *   `IPerfilPermService` movido para `application.port.in` e desacoplado.
    *   `PerfilPermissaoServiceImpl` refatorado com lógica transacional (`@Transactional`) e exceções de aplicação (`ProfileNotFoundException`).
    *   `PerfilController` refatorado para ser um `Adapter` web puro.

*   [x] **Fluxo de Abrangência (Scope) Refatorado:**
    *   `IAbrangenciaService` movido para `application.port.in` e desacoplado.
    *   `AbrangenciaServiceImpl` refatorado com lógica transacional e exceções de aplicação (`ScopeNotFoundException`).
    *   `AbrangenciaController` refatorado para ser um `Adapter` web puro.

*   [x] **Modelo de Domínio Puro:** As classes `...Domain` (ex: `UserDomain`) são POJOs sem anotações de framework, o que é fundamental para um domínio isolado.

*   [x] **Inversão de Dependência (DIP) Aplicada:** A camada de `infrastructure` (ex: `UserAdapterImpl`, `Controllers`) depende de abstrações (`Ports`) definidas na camada de `application`.

---

## Como Manter o Alinhamento (Para Trabalho em Múltiplos Computadores)

1.  **Sempre faça `git pull` antes de começar a trabalhar** para garantir que você tenha a versão mais recente deste e de outros arquivos.
2.  **Atualize este arquivo (`ARCHITECTURE_REVIEW.md`)** sempre que houver uma discussão arquitetural, uma decisão importante ou uma mudança significativa no projeto.
3.  **Faça `git commit` e `git push` regularmente** com mensagens de commit claras, especialmente após atualizar este documento.

Este arquivo será a sua "fonte da verdade" para o estado arquitetural do projeto.
