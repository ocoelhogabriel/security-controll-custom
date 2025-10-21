# Instruções para Manutenção do Projeto e Documentação

**Meu Objetivo**: Manter um mapa preciso e atualizado do projeto `security-controll-custom` para auxiliar no desenvolvimento e na compreensão da arquitetura, especialmente após a refatoração para a Arquitetura Limpa/Hexagonal.

**Minha Diretriz Principal**: Sempre que houver uma alteração na estrutura de arquivos, adição ou remoção de classes importantes (entidades de domínio, interfaces de repositório de domínio, adaptadores, serviços de aplicação, controladores), ou qualquer outra mudança arquitetural significativa, eu devo **imediatamente** atualizar os arquivos `project_map.md` e `README_REFACTOR.md` para refletir essa mudança.

## Procedimento de Atualização:

1.  **Analisar a Solicitação**: Antes de executar qualquer tarefa de codificação, devo primeiro considerar o impacto da solicitação na estrutura do projeto e nas camadas da Arquitetura Limpa.
2.  **Identificar Mudanças Estruturais**: A tarefa envolve a criação de novos arquivos, a renomeação de pacotes, a adição de novas dependências ou a alteração de responsabilidades entre as camadas (Domínio, Aplicação, Infraestrutura)?
3.  **Executar a Tarefa**: Realizar as alterações de código solicitadas pelo usuário, sempre buscando aderência aos princípios da Arquitetura Limpa.
4.  **Atualizar a Documentação**: Após a conclusão da tarefa, devo ler o `project_map.md` e o `README_REFACTOR.md`, comparar seus conteúdos com o estado atual do projeto e aplicar as atualizações necessárias.
    *   **`project_map.md`**: Detalhar a estrutura de diretórios e os componentes principais de cada camada (Domínio, Aplicação, Infraestrutura).
    *   **`README_REFACTOR.md`**: Registrar o progresso da refatoração, os próximos passos e quaisquer desafios ou decisões arquiteturais relevantes.
5.  **Confirmar com o Usuário**: Informar ao usuário que a tarefa foi concluída e que a documentação do projeto foi atualizada.

**Foco da Atualização**: Devo prestar atenção especial aos seguintes componentes e suas respectivas camadas:

*   **Camada de Domínio (`domain/`)**:
    *   Novas entidades de domínio (`*Domain.java`).
    *   Novas interfaces de repositório de domínio (`*Repository.java`).
*   **Camada de Aplicação (`application/`)**:
    *   Novos serviços de aplicação (`*ServiceImpl.java`).
    *   Novos DTOs (`*DTO.java`).
    *   Novas interfaces de casos de uso (`*Service.java`).
*   **Camada de Infraestrutura (`infrastructure/`)**:
    *   Novos adaptadores de repositório (`*AdapterImpl.java`).
    *   Novos mappers (`*Mapper.java`).
    *   Novas entidades JPA (`*Entity.java`).
    *   Novos JpaRepositories (`*JpaRepository.java`).
*   Alterações em `pom.xml` que introduzam novas tecnologias ou dependências importantes.
*   Criação ou modificação de arquivos de configuração.
*   Qualquer refatoração que altere a localização ou o nome dos principais componentes.

Ao seguir estas instruções, garanto que minha compreensão do projeto permaneça consistente e que eu possa fornecer assistência precisa e eficiente, sempre alinhado com a arquitetura limpa do projeto.
