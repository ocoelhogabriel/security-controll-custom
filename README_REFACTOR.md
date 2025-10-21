# Status da Refatoração: Projeto security-controll-custom

Este documento serve como um guia e ponto de retomada para a refatoração do projeto para uma Arquitetura Limpa/Hexagonal.

## 🎯 Objetivo Principal

Nosso objetivo é transformar a arquitetura monolítica atual, que mistura responsabilidades, em uma arquitetura de camadas bem definida e desacoplada. Estamos aplicando os princípios de **Clean Architecture**, **SOLID** e **Domain-Driven Design (DDD)** para:

1.  **Isolar a Lógica de Negócio**: Proteger o coração do nosso sistema (o `domain`) de detalhes técnicos como frameworks, banco de dados e APIs.
2.  **Aumentar a Testabilidade**: Facilitar a criação de testes unitários para a lógica de negócio, sem a necessidade de carregar o contexto do Spring ou um banco de dados.
3.  **Melhorar a Manutenibilidade**: Tornar o código mais fácil de entender, modificar e estender no futuro.

## 💡 Arquitetura Alvo

Estamos implementando a **Regra de Dependência**, onde as camadas internas não sabem nada sobre as camadas externas. O fluxo de dependência é sempre para dentro:

```
[ Camada de Infrastructure ] -> [ Camada de Application ] -> [ Camada de Domain ]
```

-   **📁 `domain`**: O núcleo. Contém os modelos de negócio puros (`*Domain.java`) e as interfaces (Portas) dos repositórios. Não depende de ninguém.
-   **📁 `application`**: A orquestração. Contém os casos de uso (`*ServiceImpl`), DTOs e as interfaces da API (`*Controller`). Depende apenas do `domain`.
-   **📁 `infrastructure`**: Os detalhes técnicos. Contém as configurações do Spring, as entidades JPA, as implementações dos repositórios (Adaptadores) e a comunicação com o mundo externo. Depende do `application` e do `domain`.

## ✅ O Que Já Fizemos (Nosso Progresso Atualizado)

1.  **Reorganização dos Pacotes**: Criamos a estrutura de pastas `domain`, `application` e `infrastructure` e movemos a maioria das classes existentes para seus devidos lugares.
2.  **Criação dos Modelos de Domínio**: Criamos as classes de negócio puras (ex: `UserDomain.java`) dentro de `domain/entity/`.
3.  **Refatoração da Camada de Persistência (`infrastructure/persistence`)**: Concluída a transição para o padrão de repositório e adaptadores, com mapeamento entre entidades de domínio e JPA.
4.  **Refatoração da Camada de Aplicação (`application/service`)**: Serviços atualizados para operar com entidades de domínio e injetar as interfaces de repositório de domínio.
5.  **Revisão da Camada de Apresentação (`application/rest` - Controllers)**: Controllers revisados e alinhados com os princípios da Arquitetura Limpa.

## 🚀 Refatoração do Sistema de Segurança (Permissão e Abrangência)

Realizamos uma refatoração completa no sistema de segurança para torná-lo mais robusto, dinâmico e alinhado com a Arquitetura Limpa.

### Visão Geral da Nova Arquitetura de Segurança

A nova abordagem separa claramente as responsabilidades de autenticação, autorização e filtragem de dados:

1.  **Autenticação**: A responsabilidade do `JWTAuthFilter.java` foi simplificada. Ele agora é responsável **exclusivamente** por validar o token JWT e configurar o `SecurityContextHolder` com o `UserDetails` do usuário autenticado. Toda a lógica de verificação de permissão baseada em URL foi removida.

2.  **Autorização (Permissões)**: A autorização agora é declarativa e baseada em perfis. Utilizamos as anotações `@PreAuthorize` do Spring Security nos métodos dos controllers. A lógica de verificação é centralizada em um `CustomPermissionEvaluator.java`, que consulta o `PerfilPermissaoServiceImpl` para verificar, em tempo de execução e via banco de dados, se o perfil do usuário tem a permissão necessária (ex: 'create', 'list', 'find') para um determinado recurso (ex: 'USUARIO', 'EMPRESA').

3.  **Abrangência (Filtragem de Dados)**: A lógica de filtragem de dados, que define *quais* registros um usuário pode ver, foi movida para a camada de serviço (`*ServiceImpl`). Utilizamos o padrão **Specification** do Spring Data JPA para construir consultas dinâmicas. Os serviços agora obtêm o `Scope` do usuário autenticado, consultam os `ScopeDetails` para obter as regras de filtragem (armazenadas como JSON no banco de dados) e aplicam essas regras dinamicamente às consultas, garantindo que apenas os dados dentro da abrangência do usuário sejam retornados.

### Componentes Implementados

-   **`CustomPermissionEvaluator.java`**: Nova classe na camada de infraestrutura que implementa a lógica de autorização customizada para o `@PreAuthorize`.
-   **`*Specifications.java`** (ex: `UserSpecifications.java`, `CompanySpecifications.java`): Novas classes na camada de infraestrutura que constroem `Specification` para filtragem dinâmica, incluindo a aplicação dos filtros de abrangência.
-   **`*JpaRepository.java`**: As interfaces de repositório JPA (ex: `UserJpaRepository`, `CompanyJpaRepository`) foram atualizadas para estender `JpaSpecificationExecutor`, permitindo a execução de queries baseadas em `Specification`.
-   **`*ServiceImpl.java`**: Os serviços (ex: `UsuarioServiceImpl`, `EmpresaServiceImpl`) foram refatorados para usar as `Specifications` e aplicar a lógica de filtragem de abrangência.
-   **`CreateAdminHandler.java` e `ResourceInitializer.java`**: A lógica de inicialização foi ajustada para criar recursos padrão e para não depender de um contexto de segurança autenticado durante a inicialização da aplicação.

### Correções de Entidades

-   **Palavras Reservadas do SQL**: Corrigimos as entidades JPA para evitar conflitos com palavras reservadas do SQL. A tabela `user` foi renomeada para `app_user` (via `@Table(name = "app_user")` na entidade `User.java`) e as colunas de ação na entidade `Permission.java` foram renomeadas (ex: de `create` para `can_create`).
-   **Nomes de Propriedades**: Corrigimos os nomes de métodos em várias interfaces `JpaRepository` (ex: `UserJpaRepository`, `ProfileJpaRepository`) para corresponderem aos nomes corretos das propriedades nas entidades JPA (ex: `findByPernom` para `findByName`), resolvendo erros de criação de query do Spring Data.

## ✅ Status Atual

As principais camadas da arquitetura limpa foram implementadas e a refatoração do sistema de segurança e abrangência foi concluída. O projeto agora possui um mecanismo de controle de acesso dinâmico, configurável via banco de dados e alinhado com os princípios de Clean Code e Arquitetura Hexagonal.

## 🚀 Próximo Passo Imediato

Com a nova arquitetura de segurança estabelecida, os próximos passos são:

1.  **Aplicar o Padrão de Abrangência**: Estender a implementação da filtragem de dados baseada em `Specification` para os serviços restantes (ex: `PlantaServiceImpl`, `AbrangenciaServiceImpl`, etc.).
2.  **Popular o Banco de Dados**: Garantir que as tabelas `resources` e `scope_details` estejam corretamente populadas com os nomes dos recursos e as regras de filtragem JSON para que a segurança funcione como esperado.
3.  **Testes Abrangentes**: Criar testes de integração e unitários para validar exaustivamente as novas regras de permissão (`@PreAuthorize`) e a lógica de filtragem de abrangência nos serviços.
4.  **Limpeza Final**: Remover completamente as classes `PermissaoHandler` e `URLValidator`, que se tornaram redundantes.
