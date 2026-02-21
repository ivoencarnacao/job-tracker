# Job Tracking App para Procura de Emprego (MVP)

## 1) Resumo do Produto

Uma aplicação para ajudar candidatos a emprego a **organizar candidaturas**, **acompanhar respostas**, **gerir entrevistas/propostas/ghosting** e **extrair tendências de skills** a partir das ofertas publicadas (ex.: linguagens, frameworks, cloud, testing, etc.).

O objetivo é reduzir o caos da procura de emprego e transformar o processo em algo mais claro, mensurável e estratégico.

---

## 2) Problema

Pessoas em procura ativa de emprego (recém-licenciados, profissionais juniores, ou pessoas em transição de carreira) enviam muitos CVs por dia e acabam por:

- perder o controlo de onde se candidataram;
- não saber quem respondeu e quem não respondeu;
- esquecer follow-ups;
- não ter visibilidade do pipeline (entrevistas, propostas, recusas, ghosting);
- não perceber padrões do mercado (skills mais pedidas, stacks em crescimento, etc.).

Resultado: ansiedade, desorganização e menor eficácia na procura.

---

## 3) Público-Alvo (Personas)

### Persona A — Recém-licenciado

- 0–1 anos de experiência
- Envia muitas candidaturas por dia
- Pouca experiência a gerir processos em paralelo
- Precisa de estrutura e clareza

### Persona B — Júnior (1–3 anos)

- Já teve experiência, mas foi despedido ou está em transição
- Está a candidatar-se a várias vagas em simultâneo
- Precisa de acompanhar entrevistas e feedback

### Persona C — Mudança de carreira (40+)

- Muita experiência noutra área (ex.: retalho)
- Licenciatura recente / requalificação
- Precisa de perceber requisitos técnicos mais frequentes
- Precisa de organizar melhor propostas, respostas e prioridades

---

## 4) Proposta de Valor (MVP)

### Valor principal

“Uma forma simples e prática de controlar todas as candidaturas num só sítio.”

### Valor adicional (diferenciador)

“Além de tracking, a app mostra tendências reais das skills pedidas nas vagas.”

---

## 5) Objetivos do MVP

### Objetivos do utilizador

- Registar facilmente candidaturas
- Ver estado atual de cada candidatura
- Não esquecer follow-ups
- Acompanhar entrevistas e propostas
- Perceber skills mais pedidas nas vagas guardadas

### Objetivos do produto

- Criar hábito de uso diário
- Ser útil com pouco esforço de input
- Entregar valor imediato (organização + insights)

---

## 6) Não-Objetivos (fora do MVP)

Para manter o MVP focado, **não incluir inicialmente**:

- geração automática de CV/cover letter com IA
- candidatura automática a vagas
- integração com LinkedIn/Indeed/etc. (API completa)
- CRM avançado de networking
- scoring inteligente complexo de fit
- app mobile nativa (começar web responsiva)
- colaboração multiutilizador

---

## 7) Funcionalidades Core do MVP

## 7.1 Gestão de Candidaturas (essencial)

Permitir criar e acompanhar candidaturas.

### Dados mínimos por candidatura

- Título da vaga
- Empresa
- Localização (remoto/híbrido/presencial + cidade opcional)
- Link da vaga
- Data de candidatura
- Estado (ver pipeline abaixo)
- Fonte (LinkedIn, site da empresa, referral, etc.)
- Notas (livre)
- Salary range (opcional)
- Contacto/recrutador (opcional)

### Estados (pipeline MVP)

- Guardada (wishlist)
- Candidatada
- Em análise
- Entrevista RH
- Entrevista Técnica
- Challenge/Case study
- Entrevista Final
- Proposta
- Rejeitada
- Sem resposta / Ghosted
- Fechada pelo utilizador (desistência)

> Nota: “Ghosted” pode ser manual ou sugerido por regra de tempo sem resposta (ex.: 21 dias).

---

## 7.2 Dashboard / Visão Geral (essencial)

Painel simples com visão do pipeline.

### Componentes do dashboard

- Nº total de candidaturas
- Nº por estado
- Candidaturas desta semana
- Taxa de resposta (responderam / candidaturas)
- Entrevistas marcadas
- Follow-ups pendentes
- “Sem resposta há +X dias”

---

## 7.3 Calendário e Follow-ups (essencial)

Evitar perder timings.

### Funcionalidades

- Definir lembrete de follow-up por candidatura
- Ver lista de follow-ups pendentes
- Marcar follow-up como feito
- Registar resultado do follow-up (respondeu / sem resposta)

> No MVP, pode ser só dentro da app (sem integração com Gmail/Calendar).

---

## 7.4 Gestão de Entrevistas (essencial)

Registar e acompanhar entrevistas sem complicação.

### Dados mínimos

- Data/hora
- Tipo (RH, técnica, final)
- Formato (online/presencial)
- Pessoa(s) entrevistadora(s) (opcional)
- Notas de preparação
- Resultado (pendente / passou / não passou)

---

## 7.5 Propostas e Decisão (essencial-lite)

Porque é comum perder contexto entre várias fases.

### Dados mínimos

- Empresa
- Data da proposta
- Valor (opcional)
- Tipo de contrato (opcional)
- Status: recebida / aceite / recusada / expirada
- Notas (benefícios, condições, dúvidas)

---

## 7.6 Análise de Skills e Tendências (diferenciador MVP)

Extrair hard skills das vagas guardadas pelo utilizador.

### Como funciona (MVP)

Ao guardar uma vaga, o utilizador pode:

- colar descrição da vaga **ou**
- adicionar skills manualmente (fallback)

A app identifica/guarda skills e mostra:

### Insights mínimos

- Top skills mais pedidas (ex.: JavaScript, React, SQL)
- Top categorias:

  - Linguagens
  - Frameworks
  - Databases
  - Cloud
  - DevOps
  - Testing
  - Arquitetura
  - Ferramentas

- Frequência por skill (% das vagas)
- Tendência temporal simples (últimos 30/60 dias, se houver dados)

### Exemplo de valor

“Nas tuas últimas 50 vagas, React aparece em 62%, TypeScript em 48%, Docker em 41%, AWS em 36%.”

---

## 8) User Stories (MVP)

### Tracking básico

- Como candidato, quero registar rapidamente uma candidatura para não me esquecer dela.
- Como candidato, quero atualizar o estado de uma candidatura para saber em que fase estou.
- Como candidato, quero filtrar por estado para ver só entrevistas/propostas/rejeições.

### Organização e follow-up

- Como candidato, quero definir um lembrete para follow-up para não deixar oportunidades morrer.
- Como candidato, quero ver candidaturas sem resposta há muito tempo para decidir se faço follow-up ou marco como ghosted.

### Entrevistas e propostas

- Como candidato, quero guardar entrevistas com data e notas para me preparar melhor.
- Como candidato, quero registar propostas e compará-las nas notas.

### Tendências de mercado

- Como candidato, quero ver quais skills aparecem mais nas vagas para ajustar o meu estudo e CV.
- Como candidato em transição, quero perceber que stack é mais pedida para focar o esforço certo.

---

## 9) Fluxos Principais (MVP)

## Fluxo 1 — Adicionar candidatura

1. Clicar “Nova candidatura”
2. Preencher campos mínimos (vaga, empresa, data, estado)
3. (Opcional) Colar link e descrição
4. Guardar
5. App atualiza dashboard + skills (se houver descrição)

## Fluxo 2 — Atualizar estado

1. Abrir candidatura
2. Alterar estado (ex.: Candidatada → Entrevista RH)
3. (Opcional) Adicionar entrevista e lembrete
4. Guardar

## Fluxo 3 — Follow-up

1. Dashboard mostra “Follow-ups pendentes”
2. Utilizador entra na candidatura
3. Marca follow-up feito e atualiza notas/estado
4. Se sem resposta prolongada, pode marcar como Ghosted

## Fluxo 4 — Ver tendências

1. Abrir secção “Insights”
2. Ver top skills e categorias
3. Filtrar por período
4. Usar informação para ajustar candidaturas/CV/aprendizagem

---

## 10) Requisitos Funcionais (MVP)

### RF1 — CRUD de candidaturas

- Criar, editar, arquivar, eliminar candidatura
- Alterar estado
- Guardar notas e metadados mínimos

### RF2 — Dashboard

- Mostrar métricas principais
- Atualização em tempo real (ou refresh manual simples)

### RF3 — Follow-ups

- Criar lembrete interno por candidatura
- Lista de follow-ups pendentes
- Marcar follow-up como concluído

### RF4 — Entrevistas

- Registar entrevistas associadas a uma candidatura
- Guardar tipo, data e notas
- Atualizar resultado

### RF5 — Propostas

- Registar proposta e estado
- Guardar notas e condições

### RF6 — Skills / Tendências

- Guardar skills por vaga
- Categorizar skills (manual/heurística)
- Mostrar frequências e top skills
- Mostrar evolução temporal básica

### RF7 — Pesquisa e filtros

- Pesquisar por empresa/vaga
- Filtrar por estado, data, fonte
- Ordenar por data de candidatura / última atualização

---

## 11) Requisitos Não Funcionais (MVP)

### Usabilidade

- Interface simples, rápida e sem excesso de passos
- Mobile-friendly (web responsiva)
- Adicionar candidatura em < 1 minuto

### Performance

- Dashboard carregar em < 2 segundos (datasets pequenos/médios)
- Pesquisa e filtros rápidos

### Segurança e Privacidade

- Dados do utilizador privados
- Autenticação básica segura
- Encriptação de passwords
- Política de privacidade clara (especialmente se guardar salários/notas)

### Fiabilidade

- Autosave ou feedback claro de “guardado”
- Não perder notas por erro simples

---

## 12) Métricas de Sucesso (MVP)

### Produto

- % utilizadores que criam 10+ candidaturas na primeira semana
- Nº médio de candidaturas registadas por utilizador
- % utilizadores que atualizam estados (não só criam)

### Retenção / hábito

- WAU/DAU (ou pelo menos uso semanal)
- % utilizadores com 2+ sessões por semana

### Valor percebido

- % utilizadores que usam follow-ups
- % utilizadores que visitam secção de insights
- Tempo até primeira utilidade (criar candidatura + ver dashboard)

### Resultados de processo (não de emprego)

- Taxa de resposta acompanhada
- Tempo médio entre candidatura e resposta
- % candidaturas com estado final conhecido

---

## 13) Suposições e Riscos

### Suposições

- Utilizadores estão dispostos a registar dados manualmente (se for rápido)
- Colar descrição da vaga é suficiente para extrair valor de skills
- Tracking + insights é uma combinação útil e diferenciadora

### Riscos

- Fricção no input manual (se a app pedir demasiados campos)
- Extração de skills imperfeita no MVP
- Utilizadores abandonam se não houver valor imediato

### Mitigações

- Formulário curto com campos opcionais
- Skills também podem ser adicionadas manualmente
- Dashboard útil logo desde a primeira candidatura

---

## 14) Scope Técnico (MVP simplificado)

## Entidades principais

- Utilizador
- Candidatura
- Entrevista
- FollowUp
- Proposta
- Skill
- JobSkill (ligação candidatura ↔ skill)

## Lógica mínima de tendências

- Dicionário inicial de skills (ex.: JS, TS, React, Node, SQL, AWS, Docker, Kubernetes, CI/CD, etc.)
- Match por palavras-chave na descrição
- Normalização simples (ex.: “PostgreSQL” = “Postgres”)
- Contagem por frequência

> Isto já entrega valor sem precisar de IA complexa no início.

---

## 15) Roadmap Pós-MVP (prioridades futuras)

### Fase 2

- Import automático de vagas via URL (scraping leve)
- Integração com Gmail/Calendar
- Templates de follow-up
- Kanban board de candidaturas

### Fase 3

- Sugestões personalizadas de skills a estudar
- CV matching (comparar CV vs requisitos das vagas)
- Relatórios avançados e benchmarking por função
- App mobile nativa

---

## 16) Critérios de Aceitação (MVP)

O MVP é considerado pronto quando um utilizador consegue:

1. Criar conta e entrar
2. Registar candidaturas com estado
3. Atualizar o pipeline (entrevista, rejeição, proposta, ghosted)
4. Criar e gerir follow-ups
5. Registar entrevistas/propostas com notas
6. Ver dashboard com métricas básicas
7. Ver insights de skills a partir das vagas guardadas

---
