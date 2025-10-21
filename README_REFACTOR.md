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
3.  **Refatoração da Camada de Persistência (`infrastructure/persistence`)**:
    *   **Criação e Atualização de Mappers**: Criamos ou atualizamos todas as classes `*Mapper.java` em `infrastructure/persistence/mapper/` para traduzir dados entre as Entidades JPA e os Modelos de Domínio (incluindo `PlanMapper`, `PermissionMapper`, `ScopeDetailsMapper`).
    *   **Definição e Implementação do Repository Pattern (Adaptadores)**: Todas as interfaces de repositório de domínio (`*Repository.java`) foram atualizadas com métodos de consulta customizados, e suas implementações (`*AdapterImpl.java`) na camada de infraestrutura foram finalizadas para usar os `JpaRepository`s e os `*Mapper`s correspondentes.
    *   **Renomeação de Adaptadores**: O arquivo `CompanyJpaAdapterImpl.java` foi renomeado para `CompanyAdapterImpl.java` e suas funções foram ajustadas para seguir o padrão.
4.  **Refatoração da Camada de Aplicação (`application/service`)**:
    *   **Atualização de DTOs**: Todos os DTOs (`*DTO.java`) foram refatorados para trabalhar com as Entidades de Domínio (`*Domain.java`).
    *   **Refatoração de Serviços**: Todos os serviços (`*ServiceImpl.java`) foram atualizados para:
        *   Injetar as interfaces de repositório de domínio (`*Repository.java`) em vez dos `JpaRepository`s.
        *   Operar exclusivamente com as Entidades de Domínio (`*Domain.java`).
        *   Remover a lógica de mapeamento manual e de `Specification`.
        *   Ajustar métodos auxiliares e limpar imports.
    *   **Integração de Segurança**: Criamos `UserAuthDetails.java` para adaptar `UserDomain` à interface `UserDetails` do Spring Security, e `JWTUtil.java` foi atualizado para trabalhar com `UserDomain`.
5.  **Revisão da Camada de Apresentação (`application/rest` - Controllers)**:
    *   Todos os controllers foram revisados e confirmados como já alinhados com os princípios da Arquitetura Limpa.
    *   Eles injetam as interfaces de serviço da camada de aplicação (`*Service.java`).
    *   Operam exclusivamente com DTOs para entrada e saída de dados.
    *   Delegam a lógica de negócio aos serviços da camada de aplicação.
    *   Não possuem conhecimento direto das Entidades de Domínio ou da camada de Infraestrutura.

## ✅ Status Atual

As camadas de Domínio, Infraestrutura (Persistência), Aplicação (Serviços) e Apresentação (Controllers) foram completamente refatoradas ou revisadas para aderir aos princípios da Arquitetura Limpa. O projeto está agora com uma estrutura de camadas bem definida e desacoplada.

## 🚀 Próximo Passo Imediato

Com as principais camadas da arquitetura limpa implementadas e revisadas, o próximo passo é focar na **validação e testes** para garantir que todas as funcionalidades continuem operando corretamente e que os novos princípios arquiteturais estejam sendo seguidos. Além disso, podemos começar a explorar a implementação de **casos de uso** mais complexos ou a adição de novas funcionalidades, sempre mantendo a aderência à arquitetura definida.
