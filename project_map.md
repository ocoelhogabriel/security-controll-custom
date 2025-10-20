# Mapa do Projeto: security-controll-custom

Este documento descreve a estrutura e os componentes do projeto `security-controll-custom`.

## Visão Geral

*   **Linguagem**: Java
*   **Framework**: Spring Boot
*   **Build**: Maven
*   **Tipo**: Aplicação Web com API REST

## Estrutura de Diretórios

```
security-controll-custom/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ocoelhogabriel/security_control_custom/
│   │   │       ├── SecurityControlCustomApplication.java  (Ponto de entrada)
│   │   │       ├── OpenApiConfig.java                     (Configuração OpenAPI/Swagger)
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── exception/
│   │   │       ├── handler/
│   │   │       ├── model/
│   │   │       ├── records/
│   │   │       ├── repository/
│   │   │       ├── services/
│   │   │       └── utils/
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

## Componentes Principais

### Controllers (`src/main/java/.../controller`)

Responsáveis por expor os endpoints da API REST.

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

### Services (`src/main/java/.../services`)

Contêm a lógica de negócio da aplicação.

*   **Interfaces**:
    *   `AuthServInterface.java`
    *   `AbrangenciaServInterface.java`
    *   `EmpresaServInterface.java`
    *   `LoggerServInterface.java`
    *   `PerfilPermServInterface.java`
    *   `PlantaServInterface.java`
    *   `RecursoServInterface.java`
    *   `UsuarioServInterface.java`
*   **Implementações** (`impl/`):
    *   `AuthServiceImpl.java`
    *   `AbrangenciaServiceImpl.java`
    *   `EmpresaServiceImpl.java`
    *   `LoggerServiceImpl.java`
    *   `PerfilPermissaoServiceImpl.java`
    *   `PlantaServiceImpl.java`
    *   `RecursoServiceImpl.java`
    *   `UsuarioServiceImpl.java`

### Models (`src/main/java/.../model`)

Representam as entidades de domínio e objetos de dados.

*   `AuthModel.java`
*   `AuthDeviceModel.java`
*   `AbrangenciaModel.java`
*   `AbrangenciaDetalhesModel.java`
*   `EmpresaModel.java`
*   `LoggerModel.java`
*   `PerfilModel.java`
*   `PermissaoModel.java`
*   `PlantaModel.java`
*   `RecursoModel.java`
*   `UsuarioModel.java`
*   `GenericResponseModel.java`
*   **Sub-pacotes**:
    *   `dto/`: Data Transfer Objects.
    *   `entity/`: Entidades JPA.
    *   `enums/`: Enumerações.

### Repositories (`src/main/java/.../repository`)

Interfaces do Spring Data JPA para acesso ao banco de dados. (Conteúdo a ser explorado).

### Outros Pacotes

*   **`config`**: Configurações do Spring (ex: segurança).
*   **`exception`**: Exceções customizadas.
*   **`handler`**: Manipuladores globais de exceção.
*   **`records`**: Java Records para DTOs.
*   **`utils`**: Classes utilitárias.
