# Mapa do Projeto: security-controll-custom

Este documento descreve a estrutura e os componentes do projeto `security-controll-custom`.

## Visão Geral

*   **Linguagem**: Java
*   **Framework**: Spring Boot
*   **Build**: Maven
*   **Tipo**: Aplicação Web com API REST
*   **Arquitetura**: Em refatoração para Arquitetura Limpa / Hexagonal.

## Estrutura de Diretórios (Arquitetura Limpa)

```
security-controll-custom/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ocoelhogabriel/security_control_custom/
│   │   │       ├── SecurityControlCustomApplication.java  (Ponto de entrada)
│   │   │       ├── application/                           (Camada de Aplicação)
│   │   │       │   ├── controller/                        (Endpoints REST)
│   │   │       │   ├── dto/                               (Data Transfer Objects)
│   │   │       │   ├── handler/                           (Manipuladores de inicialização e lógica auxiliar)
│   │   │       │   ├── service/                           (Lógica de Negócio / Casos de Uso)
│   │   │       │   └── usecase/                           (Interfaces de Casos de Uso)
│   │   │       ├── domain/                                (Camada de Domínio - O Coração da Aplicação)
│   │   │       │   ├── entity/                            (Entidades de Domínio Puras)
│   │   │       │   ├── model/                             (Modelos de Domínio, como enums)
│   │   │       │   └── repository/                        (Interfaces de Repositório de Domínio - Portas)
│   │   │       ├── infrastructure/                        (Camada de Infraestrutura - Detalhes Técnicos)
│   │   │       │   ├── config/                            (Configurações do Spring, Segurança, Inicializadores)
│   │   │       │   ├── exception/                         (Exceções customizadas)
│   │   │       │   ├── persistence/                       (Implementação de Persistência)
│   │   │       │   │   ├── adapter/                       (Implementações de Repositório - Adaptadores)
│   │   │       │   │   ├── entity/                        (Entidades JPA)
│   │   │       │   │   ├── mapper/                        (Mapeadores entre Entidade JPA e Domínio)
│   │   │       │   │   ├── repository/                    (Interfaces JpaRepository e JpaSpecificationExecutor)
│   │   │       │   │   └── specification/                 (Construtores de Specifications para queries dinâmicas)
│   │   │       │   ├── security/                          (Componentes de segurança customizados, JWT)
│   │   │       │   └── utils/                             (Classes utilitárias)
│   │   ├── resources/
│   │   └── webapp/
│   └── test/
├── target/
├── .gitignore
├── docker-compose.yml
├── dockerfile
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Componentes Principais (Atualizado)

### Camada de Domínio (`src/main/java/.../domain`)
Contém a lógica de negócio central e as regras mais importantes da aplicação. É independente de frameworks e tecnologias externas.

*   **`entity/`**: Entidades de Domínio Puras (ex: `UserDomain.java`, `CompanyDomain.java`).
*   **`model/`**: Outros modelos de domínio, como enumerações (`RecursoMapEnum.java`).
*   **`repository/`**: Interfaces de Repositório de Domínio (Portas). Definem os contratos para acesso a dados (ex: `UserRepository.java`).

### Camada de Aplicação (`src/main/java/.../application`)
Orquestra a lógica de negócio. Contém os casos de uso, DTOs e a camada de apresentação (controllers).

*   **`controller/`**: Endpoints da API REST. Agora utilizam anotações `@PreAuthorize` para controle de acesso declarativo.
*   **`dto/`**: Data Transfer Objects para entrada e saída de dados.
*   **`handler/`**: Manipuladores de eventos e lógica de inicialização.
    *   `CreateAdminHandler.java`: Garante que usuários, perfis e recursos essenciais existam na inicialização.
*   **`service/`**: Implementações dos Casos de Uso. Contêm a lógica de negócio, incluindo a aplicação de filtros de **abrangência** nas consultas.
    *   `UsuarioServiceImpl.java`, `EmpresaServiceImpl.java`, etc.

### Camada de Infraestrutura (`src/main/java/.../infrastructure`)
Contém os detalhes técnicos e implementações externas.

*   **`config`**: Configurações do Spring.
    *   `SecurityConfig.java`: Configuração principal do Spring Security, agora com `@EnableMethodSecurity`.
    *   `ResourceInitializer.java`: `CommandLineRunner` para garantir a criação de recursos padrão na inicialização.
*   **`persistence/`**: Componentes de persistência de dados.
    *   **`adapter/`**: Implementações dos Repositórios de Domínio (Adaptadores), que agora usam `JpaSpecificationExecutor`.
    *   **`entity/`**: Entidades JPA. A entidade `User` foi renomeada para `app_user` e as colunas com palavras reservadas em `Permission` foram corrigidas.
    *   **`mapper/`**: Mapeadores entre Entidades JPA e de Domínio.
    *   **`repository/`**: Interfaces do Spring Data JPA (`JpaRepository`), agora estendendo `JpaSpecificationExecutor` para permitir queries dinâmicas.
    *   **`specification/`**: Classes que constroem `Specification` do Spring Data JPA para filtragem dinâmica, especialmente para a lógica de **abrangência**.
        *   `UserSpecifications.java`, `CompanySpecifications.java`.
*   **`security/`**: Componentes de segurança customizados.
    *   `JWTAuthFilter.java`: Simplificado para focar apenas na **autenticação** (validação do token).
    *   `CustomPermissionEvaluator.java`: Implementação central da lógica de **autorização**, consultando o `PerfilPermissaoServiceImpl` para validar permissões para as anotações `@PreAuthorize`.
*   **`utils/`**: Classes utilitárias.
