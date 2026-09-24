# Revisão Crítica da Refatoração SOLID: Missão Marte Unifor

**Equipe:** Lucas, Miguel, Carlos  
**Instituição:** UNIFOR  
**Disciplina:** Programação Orientada a Objetos / Engenharia de Software  
**Data:** 23/09/2026  

---

## 1. Como Validei a Solução

Registre os comandos executados e os fluxos testados:

- [x] Compilação do código inicial em `src/exercicio10/`;
- [x] Compilação isolada dos pacotes da versão refatorada em `src/solidexercicio10/`;
- [x] Execução do menu e início de uma nova missão;
- [x] Movimentação com `w`, `s`, `a`, `d` e cálculo do custo de movimento (-1 ponto);
- [x] Embarque polimórfico de passageiros com tecla `c` (+15, +20, +10);
- [x] Detecção de colisão com asteroides e inimigos móveis (redução de vidas);
- [x] Conclusão da missão ao retornar à base `(0, 0)` com todos resgatados;
- [x] Persistência das pontuações em `ranking-solid-exercicio10.json`;
- [x] Consulta do ranking ordenado por pontuação decrescente;
- [x] Limpeza e reset do arquivo de ranking em tempo de execução.

---

## 2. Achados da Revisão (Análise dos Princípios SOLID)

### Observação 1: Single Responsibility Principle (SRP)
* **Local:** `solidexercicio10.presentation.MapaRenderer` e `solidexercicio10.service.JogoService`
* **Princípio relacionado:** SRP (Princípio da Responsabilidade Única)
* **Observação:** Na versão original (`exercicio10.Main`), o loop de jogo continha chamadas diretas de impressão de mapa em matriz de caracteres, formatação de strings e manipulação de arquivos. Na versão refatorada, o `MapaRenderer` cuida unicamente de transformar o estado das entidades em visualização console, enquanto o `JogoService` gerencia regras de jogo.
* **Impacto:** Alta facilidade de evolução. Se for necessário trocar o console por interface gráfica (JavaFX/Swing) ou mudar o layout dos caracteres, nenhuma regra de negócio é impactada.
* **Proposta:** Manter o renderer isolado. Futuramente, criar a interface `Renderer` para permitir múltiplos temas visuais.
* **Prioridade:** Média

### Observação 2: Open/Closed Principle (OCP)
* **Local:** `solidexercicio10.model.Passageiro` e suas subclasses (`Professor`, `Engenheiro`, `Astronauta`)
* **Princípio relacionado:** OCP (Princípio Aberto/Fechado)
* **Observação:** O sistema permite a criação de novas categorias de sobreviventes (ex: `Medico`, `Cientista`) apenas herdando de `Passageiro` e implementando `getPontuacao()` e `getSimbolo()`. A classe `Missao` e o método de embarque não precisam ser alterados ou recompilados para suportar novos passageiros.
* **Impacto:** Elimina estruturas extensas de `switch/case` e `if/else` encadeados para pontuação.
* **Proposta:** Refatoração implementada com sucesso no pacote `model`.
* **Prioridade:** Alta

### Observação 3: Liskov Substitution Principle (LSP)
* **Local:** `solidexercicio10.model.Missao.getPassageiros()` e `nave.embarcar(Passageiro p)`
* **Princípio relacionado:** LSP (Princípio da Substituição de Liskov)
* **Observação:** Todas as subclasses (`Professor`, `Engenheiro`, `Astronauta`) respeitam integralmente o contrato de `Passageiro`. Nenhuma subclasse lança exceções inesperadas, nem restringe parâmetros ou quebra expectativas. A nave e a missão tratam todos os passageiros de forma uniforme em `List<Passageiro>`.
* **Impacto:** Zero necessidade de operadores `instanceof` ou downcasting na lógica de embarque e contagem.
* **Proposta:** Manter a hierarquia estrita sem dependência de tipos concretos no serviço.
* **Prioridade:** Alta

### Observação 4: Interface Segregation Principle (ISP)
* **Local:** `solidexercicio10.model.Posicionavel` e `solidexercicio10.model.Movel`
* **Princípio relacionado:** ISP (Princípio da Segregação de Interfaces)
* **Observação:** Foram criadas interfaces enxutas e coesas. `Posicionavel` declara apenas `getX()` e `getY()`. `Movel` declara apenas `mover(int dx, int dy)`. Classes como `Asteroide` implementam apenas `EntidadeMapa` (que realiza `Posicionavel`), sem serem forçadas a implementar métodos vazios de movimento.
* **Impacto:** Código limpo e interfaces focadas estritamente nas capacidades reais das entidades.
* **Proposta:** Interface segregation mantida como padrão para novas entidades.
* **Prioridade:** Média

### Observação 5: Dependency Inversion Principle (DIP)
* **Local:** `solidexercicio10.service.JogoService` e `solidexercicio10.repository.RankingRepository`
* **Princípio relacionado:** DIP (Princípio da Inversão de Dependência)
* **Observação:** `JogoService` recebe via construtor a interface abstrata `RankingRepository`. O serviço não conhece `Path`, `Files` nem o formato físico do arquivo. A implementação concreta `RankingService` é instanciada apenas na borda do sistema (`Main.java`).
* **Impacto:** Permite injetar facilmente um repositório em memória para testes automatizados sem tocar no disco rígido.
* **Proposta:** Padrão de injeção por construtor consolidado.
* **Prioridade:** Alta

---

## 3. Decisões com as Quais Concordo

* **Extração da Interface `RankingRepository` (DIP):**  
  Concordamos fortemente com essa decisão do tutorial. No código inicial, qualquer alteração na forma de salvar o ranking exigiria mexer diretamente na classe principal do jogo. Com a abstração, isolamos o detalhe técnico da persistência (I/O, charset UTF-8, formatação) da regra de negócio da partida. Isso possibilita trocar o arquivo texto por um banco de dados relacional ou mock em memória com impacto nulo sobre as regras de negócio.

---

## 4. Decisões com as Quais Não Concordo (Visão Crítica)

* **Concentração de Regras de Inicialização em `JogoService`:**  
  O tutorial manteve a geração aleatória do mapa e a distribuição de passageiros/inimigos dentro do próprio `JogoService` (`colocar()`, `colocarPerigos()`, `livre()`, `ocupado()`).  
  **Justificativa Técnica:** Embora o tutorial justifique isso para manter o fluxo visível para fins didáticos, na prática essa decisão viola levemente o SRP. `JogoService` acumula duas razões para mudar: (1) o fluxo de entrada e controle dos turnos, e (2) os algoritmos de geração procedural do mapa e balanceamento de perigos.  
  **Proposta de Melhoria:** Em uma próxima iteração, essas regras deveriam ser extraídas para uma classe especializada `MissaoFactory` ou `GeradorMapa`, deixando `JogoService` focado unicamente na orquestração dos comandos do jogador.  
  **Prioridade:** Média.

---

## 5. Melhorias Adicionais Identificadas

1. **Criação de `MissaoFactory` (Prioridade Média):**  
   Extrair a geração procedural de coordenadas, garantindo que o serviço apenas receba uma `Missao` pronta.
2. **Interface `Renderer` para Apresentação (Prioridade Baixa):**  
   Permitir plugar renderizadores alternativos (ex: visualização com cores ANSI no terminal ou exportação de log).
3. **Enum de Comandos (Prioridade Baixa):**  
   Substituir as letras brutas `'w'`, `'s'`, `'a'`, `'d'`, `'c'`, `'q'` por um enum `ComandoJogo` para tipagem estrita de ações.
