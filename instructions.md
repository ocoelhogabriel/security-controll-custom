# Instruções para Manutenção do Mapa do Projeto

**Meu Objetivo**: Manter um mapa preciso e atualizado do projeto `security-controll-custom` para auxiliar no desenvolvimento e na compreensão da arquitetura.

**Minha Diretriz Principal**: Sempre que houver uma alteração na estrutura de arquivos, adição ou remoção de classes importantes (controllers, services, models, repositories), ou qualquer outra mudança arquitetural significativa, eu devo **imediatamente** atualizar o arquivo `project_map.md` para refletir essa mudança.

## Procedimento de Atualização:

1.  **Analisar a Solicitação**: Antes de executar qualquer tarefa de codificação, devo primeiro considerar o impacto da solicitação na estrutura do projeto.
2.  **Identificar Mudanças Estruturais**: A tarefa envolve a criação de novos arquivos, a renomeação de pacotes, a adição de novas dependências ou a alteração de responsabilidades entre as camadas?
3.  **Executar a Tarefa**: Realizar as alterações de código solicitadas pelo usuário.
4.  **Atualizar o `project_map.md`**: Após a conclusão da tarefa, devo ler o `project_map.md`, comparar seu conteúdo com o estado atual do projeto e aplicar as atualizações necessárias.
5.  **Confirmar com o Usuário**: Informar ao usuário que a tarefa foi concluída e que o mapa do projeto foi atualizado.

**Foco da Atualização**: Devo prestar atenção especial aos seguintes componentes:

*   Novos arquivos/classes em pacotes-chave (`controller`, `service`, `model`, `repository`).
*   Alterações em `pom.xml` que introduzam novas tecnologias ou dependências importantes.
*   Criação ou modificação de arquivos de configuração em `src/main/resources`.
*   Qualquer refatoração que altere a localização ou o nome dos principais componentes.

Ao seguir estas instruções, garanto que minha compreensão do projeto permaneça consistente e que eu possa fornecer assistência precisa e eficiente.
