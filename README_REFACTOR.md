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

## ✅ O Que Já Fizemos (Nosso Progresso)

1.  **Reorganização dos Pacotes**: Criamos a estrutura de pastas `domain`, `application` e `infrastructure` e movemos a maioria das classes existentes para seus devidos lugares.
2.  **Criação dos Modelos de Domínio**: Criamos as classes de negócio puras (ex: `UserDomain.java`) dentro de `domain/entity/`.
3.  **Criação dos Mappers**: Criamos as classes `*Mapper.java` em `infrastructure/persistence/mapper/` para traduzir dados entre as Entidades JPA e os Modelos de Domínio.
4.  **Definição do Repository Pattern**: Demos o passo mais importante para `User`:
    *   **Renomeamos** a interface JPA para `UsuarioJpaRepository.java`.
    *   **Criamos a "Porta" de Domínio**: Uma nova interface `domain/repository/UsuarioRepository.java` que só fala a língua do domínio (usa e retorna `UserDomain`).
    *   **Criamos o "Adaptador" de Infraestrutura**: A classe `infrastructure/persistence/repository/UsuarioRepositoryImpl.java` que implementa a interface do domínio, usando o `JpaRepository` e o `Mapper` para fazer a tradução.

## ⏸️ Onde Paramos (Ponto de Retomada)

Nós acabamos de definir e criar toda a estrutura do **Repository Pattern** para a entidade `User`. A ponte entre a camada de domínio e a camada de infraestrutura para usuários está construída.

No entanto, a camada de aplicação ainda não está usando essa ponte. O arquivo `application/service/IUsuarioServiceImpl.java` **ainda não foi refatorado** para usar a nova e limpa interface `UsuarioRepository`. Ele ainda contém a lógica de `Specification` e depende diretamente do `JpaRepository`.

## 🚀 Próximo Passo Imediato

A próxima ação, ao retornar, é **refatorar a classe `IUsuarioServiceImpl.java`**.

O objetivo é simplificá-la drasticamente, fazendo o seguinte:

1.  **Injetar a Interface Limpa**:
    *   Remover: `@Autowired private UsuarioJpaRepository userRepository;`
    *   Adicionar: `@Autowired private UsuarioRepository usuarioRepository;` (a nossa nova interface de domínio).
2.  **Delegar a Responsabilidade**:
    *   Simplificar todos os métodos para que eles apenas chamem os métodos correspondentes do novo `usuarioRepository` (ex: `usuarioRepository.findById(id)`, `usuarioRepository.save(userDomain)`).
    *   Remover completamente o método `filterByFields` (a lógica de `Specification`) de dentro do `IUsuarioServiceImpl`, pois essa lógica agora é uma responsabilidade interna do `UsuarioRepositoryImpl`.
3.  **Resultado Final**: O `IUsuarioServiceImpl` se tornará um orquestrador puro, sem nenhum conhecimento sobre JPA, `Specification` ou Mappers. Ele apenas coordenará a chamada ao repositório e a outros serviços.
