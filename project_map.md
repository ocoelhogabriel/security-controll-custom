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
│   │   │       ├── OpenApiConfig.java                     (Configuração OpenAPI/Swagger)
│   │   │       ├── application/                           (Camada de Aplicação)
│   │   │       │   ├── controller/                        (Endpoints REST)
│   │   │       │   ├── dto/                               (Data Transfer Objects)
│   │   │       │   ├── handler/                           (Manipuladores de eventos/lógica auxiliar)
│   │   │       │   ├── service/                           (Lógica de Negócio / Casos de Uso)
│   │   │       │   └── usecase/                           (Interfaces de Casos de Uso)
│   │   │       ├── domain/                                (Camada de Domínio - O Coração da Aplicação)
│   │   │       │   ├── entity/                            (Entidades de Domínio Puras)
│   │   │       │   ├── model/                             (Modelos de Domínio, como enums)
│   │   │       │   └── repository/                        (Interfaces de Repositório de Domínio - Portas)
│   │   │       ├── infrastructure/                        (Camada de Infraestrutura - Detalhes Técnicos)
│   │   │       │   ├── config/                            (Configurações do Spring, Segurança)
│   │   │       │   ├── exception/                         (Exceções customizadas)
│   │   │       │   ├── persistence/                       (Implementação de Persistência)
│   │   │       │   │   ├── adapter/                       (Implementações de Repositório - Adaptadores)
│   │   │       │   │   ├── entity/                        (Entidades JPA)
│   │   │       │   │   ├── mapper/                        (Mapeadores entre Entidade JPA e Domínio)
│   │   │       │   │   └── repository/                    (Interfaces JpaRepository)
│   │   │       │   ├── security/                          (Configurações de Segurança, JWT)
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

*   **`entity/`**: Entidades de Domínio Puras (ex: `UserDomain.java`, `CompanyDomain.java`). Representam o estado e o comportamento do negócio.
*   **`model/`**: Outros modelos de domínio, como enumerações (`RecursoMapEnum.java`).
*   **`repository/`**: Interfaces de Repositório de Domínio (Portas). Definem os contratos para acesso a dados, sem especificar a implementação (ex: `UserRepository.java`, `CompanyRepository.java`).

### Camada de Aplicação (`src/main/java/.../application`)
Orquestra a lógica de negócio definida na camada de domínio. Contém os casos de uso e DTOs.

*   **`controller/`**: Responsáveis por expor os endpoints da API REST.
    *   `AuthenticationController.java`: Autenticação de usuários.
    *   `SecurityRestController.java`: Endpoints relacionados à segurança.
    *   `AbrangenciaController.java`: Gerenciamento de abrangências.
    *   `EmpresaController.java`: Gerenciamento de empresas.
    *   `LoggerController.java`: Endpoints para logs.
    *   `PerfilController.java`: Gerenciamento de perfis de usuário.
    *   `PlantaController.java`: Gerenciamento de plantas.
    *   `RecursoController.java`: Gerenciamento de recursos.
    *   `UsuarioController.java`: Gerenciamento de usuários.
    *   `DefaultController.java`: Endpoints padrão ou de fallback.
*   **`dto/`**: Data Transfer Objects. Usados para entrada e saída de dados da camada de aplicação. Foram refatorados para trabalhar com as Entidades de Domínio.
*   **`handler/`**: Manipuladores de eventos ou lógica auxiliar da aplicação (ex: `AbrangenciaHandler.java`).
*   **`service/`**: Implementações dos Casos de Uso. Contêm a lógica de negócio da aplicação, interagindo com as interfaces de repositório de domínio.
    *   `AuthServiceImpl.java`
    *   `AbrangenciaServiceImpl.java`
    *   `EmpresaServiceImpl.java`
    *   `PerfilPermissaoServiceImpl.java`
    *   `PlantaServiceImpl.java`
    *   `RecursoServiceImpl.java`
    *   `UsuarioServiceImpl.java`
*   **`usecase/`**: Interfaces dos Casos de Uso. Definem os contratos para os serviços da camada de aplicação.

### Camada de Infraestrutura (`src/main/java/.../infrastructure`)
Contém os detalhes técnicos e implementações externas. Depende das camadas de Domínio e Aplicação.

*   **`config`**: Configurações do Spring (ex: segurança).
*   **`exception`**: Exceções customizadas.
*   **`persistence/`**: Componentes relacionados à persistência de dados.
    *   **`adapter/`**: Implementações dos Repositórios de Domínio (Adaptadores). Traduzem as operações de domínio para a tecnologia de persistência (JPA) e vice-versa (ex: `UserAdapterImpl.java`, `CompanyAdapterImpl.java`).
    *   **`entity/`**: Entidades JPA. Representam a estrutura das tabelas no banco de dados (ex: `User.java`, `Company.java`).
    *   **`mapper/`**: Mapeadores entre Entidades JPA e Entidades de Domínio (ex: `UserMapper.java`, `CompanyMapper.java`).
    *   **`repository/`**: Interfaces do Spring Data JPA (`JpaRepository`).
*   **`security/`**: Implementações de segurança (ex: `JWTUtil.java`).
*   **`utils/`**: Classes utilitárias.
