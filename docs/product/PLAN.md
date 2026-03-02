# Plano de Implementacao — VS-00 + VS-01

## Contexto

Este plano cobre as duas primeiras Vertical Slices do MVP:

- **VS-00 (W1): Infrastructure & CI/CD** — Preparar a infraestrutura base: migration V1, configuracao de seguranca inicial, landing page, e CI pipeline.
- **VS-01 (W2): User Authentication** — Registo de utilizadores (ID01-S1) e login com gestao de sessao (ID01-S2). Primeiro deploy funcional.

O codebase actual e um scaffold vazio: apenas `Application.java`, templates placeholder (`index.html`, `layout/main.html`), configuracao de propriedades, Docker Compose, e Testcontainers. Nao existem migrations, entidades, controllers, nem configuracao de seguranca.

**Gate Criteria VS-00:** CI green, staging + production deploy working, landing page acessivel.
**Gate Criteria VS-01:** Register + login funcional em producao.

---

## Decisoes Tecnicas (Best Practices Spring Boot 3.x / Hibernate 6)

### UUID v7 (RFC 9562) — Gerado na Application Layer

- UUID gerado no domain factory method via utility class `shared/domain/UuidV7.java`
- UUID v7 e sequencial (timestamp nos MSB) → insercoes ordenadas no B-tree, sem page splits
- Sem `@GeneratedValue` nas JPA entities — o ID vem do domain object via mapper
- Zero dependencias externas — implementacao propria de UUID v7 (~15 linhas)

### Timestamps — `Instant` (UTC) com `@EnableJpaAuditing`

- Todos os timestamps como `java.time.Instant`, mapeados para `TIMESTAMPTZ` no PostgreSQL
- JVM forcado a UTC: `TimeZone.setDefault(TimeZone.getTimeZone("UTC"))` em `Application.java`
- Spring Data JPA auditing: `@CreatedDate` / `@LastModifiedDate` com `Instant` (suportado nativamente)
- `@EnableJpaAuditing` numa `@Configuration` separada (nao no `@SpringBootApplication`) para compatibilidade com `@WebMvcTest`
- Conversao para timezone do utilizador feita na camada `web/` (view models), nao no domain
- Para VS-01 nao ha necessidade de conversao de timezone (so `createdAt`/`updatedAt` de audit)

### Regra a Adicionar ao CLAUDE.md (pos-aprovacao)

> Seguir sempre as best practices modernas e convencoes do Spring Framework 6 / Spring Boot 3.x. Ao apresentar opcoes tecnicas, incluir sempre a opcao recomendada pelas convencoes actuais do Spring Boot 3.x e Hibernate 6.

---

## VS-00: Infrastructure & CI/CD

### 1. Flyway Migration V1

- [ ] Criar `src/main/resources/db/migration/V1__create_schema.sql`
  - `CREATE SCHEMA IF NOT EXISTS core;`
- [ ] Verificar que o Flyway executa a migration sem erros com `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
- [ ] Verificar que os testes de integracao passam com `./mvnw verify` (Testcontainers usa `init-schemas.sql` que ja cria o schema, mas o Flyway precisa da V1 no classpath)

### 2. Configuracao de Seguranca Inicial + JPA Auditing + UUID v7

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/shared/config/SecurityConfig.java`
  - `@Configuration` + `@EnableWebSecurity`
  - Para VS-00, permitir todos os requests (landing page publica): `authorizeHttpRequests(auth -> auth.anyRequest().permitAll())`
  - Registar bean `BCryptPasswordEncoder` (ja preparado para VS-01)
  - CSRF activado (default do Spring Security)
- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/shared/config/JpaAuditingConfig.java`
  - `@Configuration` + `@EnableJpaAuditing`
  - Classe separada do `@SpringBootApplication` para compatibilidade com `@WebMvcTest` slices
- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/shared/domain/UuidV7.java`
  - Utility class com metodo estatico `randomUUID()` que gera UUID v7 (RFC 9562)
  - Timestamp (millis) nos 48 MSB, version 7 marker, variant 2, random nos restantes bits
  - Zero dependencias — usa `java.security.SecureRandom` + `java.time.Instant`
  - Disponivel para todos os bounded contexts
- [ ] Actualizar `src/main/java/dev/ivoencarnacao/jobtracker/Application.java`
  - Adicionar `TimeZone.setDefault(TimeZone.getTimeZone("UTC"))` antes de `SpringApplication.run()`
  - Garante que JVM opera sempre em UTC para consistencia com `TIMESTAMPTZ`
- [ ] Remover `spring.security.user.name/password=admin` em `application-local.properties` (sera substituido pelo `JpaUserDetailsService` em VS-01)

### 3. Landing Page

- [ ] Criar controller `src/main/java/dev/ivoencarnacao/jobtracker/shared/config/WebConfig.java` (ou `HomeController`)
  - `GET /` → renderiza `index.html` (landing page)
- [ ] Redesenhar `src/main/resources/templates/index.html` segundo a `LANDING-PAGE-SPEC.md`:
  - Decorar com `layout/main`
  - **Seccao 1 — Hero:** Headline (problema), subheadline (solucao), CTA primario ("Get Started" → `/register`), CTA secundario ("Learn More" → scroll), visual/mockup
  - **Seccao 2 — Problem Statement:** 3 pain points em cards Neo-Brutalism (grid 3 colunas desktop, 1 coluna mobile)
  - **Seccao 3 — Features:** 4 features em rows alternadas imagem-texto
  - **Seccao 4 — How It Works:** 3 steps numerados com cards
  - **Seccao 5 — Social Proof:** Placeholder (pode ser omitido no lancamento)
  - **Seccao 6 — Final CTA:** Banner full-width preto, texto branco, botao invertido
  - **Seccao 7 — Footer:** Links de navegacao, contacto, social
  - Styling Neo-Brutalism: borders 2px solid black, shadows `shadow-[4px_4px_0_#000]`, sem rounded corners, font Geist
  - Responsivo: desktop (>=1024px), tablet (768-1023px), mobile (<768px)
- [ ] Formatar HTML com Prettier: `npx prettier --write "src/main/resources/**/*.{html,css,js,json}"`

### 4. CI/CD Pipeline (GitHub Actions)

- [ ] Criar `.github/workflows/ci.yml`:
  - Trigger: push para `main` e `develop`, pull requests para `main`
  - Job `build`:
    - Setup Java 21 (Temurin)
    - Cache Maven dependencies
    - `./mvnw verify` (compila, formata, testa com Testcontainers, JaCoCo coverage)
  - Job `docker-build` (apos build):
    - Build da imagem Docker para validar o Dockerfile
  - Nao inclui deploy automatico nesta fase (deploy manual ou pipeline separada)
- [ ] Verificar que o CI passa localmente com `./mvnw verify`
- [ ] Verificar que o Dockerfile builda com sucesso: `docker build -t jobtracker .`

### 5. Deploy Staging + Production

- [ ] Definir estrategia de deploy (a clarificar com o utilizador — pode ser Docker Compose num VPS, Railway, Fly.io, etc.)
- [ ] Garantir que `application.properties` (perfil default/production) tem configuracao adequada para datasource via environment variables
- [ ] Testar deploy com `docker compose up` localmente

### 6. Verificacao VS-00

- [ ] Landing page acessivel em `http://localhost:8080/`
- [ ] CI green (todos os testes passam, JaCoCo >= 70%, formatting valido)
- [ ] Docker build funcional
- [ ] Flyway migration V1 executa sem erros

---

## VS-01: User Authentication (ID01-S1 + ID01-S2)

### 7. Flyway Migration V2 — Tabela Users

- [ ] Criar `src/main/resources/db/migration/V2__create_users_table.sql`:
  ```sql
  CREATE TABLE core.users (
      id            UUID         NOT NULL,
      email         VARCHAR(255) NOT NULL,
      password_hash VARCHAR(255) NOT NULL,
      display_name  VARCHAR(100) NOT NULL,
      created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
      updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
      CONSTRAINT pk_users PRIMARY KEY (id),
      CONSTRAINT uq_users_email UNIQUE (email)
  );
  ```
- [ ] Verificar migration com `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`

### 8. Domain Layer — User Aggregate

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/domain/User.java`:
  - Aggregate root — classe Java pura, zero dependencias de frameworks
  - Campos: `id` (UUID), `email` (String), `passwordHash` (String), `displayName` (String), `createdAt` (Instant), `updatedAt` (Instant)
  - Factory method estatico `create(String email, String passwordHash, String displayName)`:
    - Gera UUID v7 internamente via `UuidV7.randomUUID()` (unica dependencia: `shared/domain/UuidV7`)
    - Valida invariantes:
      - Email nao pode ser null/vazio e deve ter formato valido (regex simples)
      - `passwordHash` nao pode ser null/vazio
      - `displayName` nao pode ser null/vazio e max 100 caracteres
    - Define `createdAt` e `updatedAt` como `Instant.now()`
  - Construtor privado (so acessivel via factory method e reconstrucao)
  - Metodo estatico de reconstrucao `reconstitute(UUID id, String email, String passwordHash, String displayName, Instant createdAt, Instant updatedAt)` — para o mapper de infraestrutura reconstruir a partir da JPA entity (sem validacao, assume dados validos da BD)
  - Getters para todos os campos (sem setters publicos — imutabilidade)

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/domain/UserRepository.java`:
  - Interface no domain layer, sem imports de frameworks
  - Metodos: `User save(User user)`, `Optional<User> findByEmail(String email)`, `boolean existsByEmail(String email)`

### 9. Application Layer — RegisterUserUseCase

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/application/dto/RegisterUserInput.java`:
  - Record com campos: `email`, `password` (texto plano), `displayName`
  - Anotacoes Jakarta Validation: `@NotBlank`, `@Email`, `@Size`

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/application/dto/RegisterUserOutput.java`:
  - Record com campos: `userId` (UUID), `email`, `displayName`

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/application/RegisterUserUseCase.java`:
  - `@Service` (ou `@Component`)
  - Dependencias injetadas: `UserRepository`, `PasswordEncoder` (interface do Spring Security — unica dependencia de framework permitida na application layer)
  - Logica:
    1. Verificar `userRepository.existsByEmail(input.email())` → se true, lancar excepcao (ex: `EmailAlreadyExistsException`)
    2. Hash da password: `passwordEncoder.encode(input.password())`
    3. Criar aggregate: `User.create(input.email(), hashedPassword, input.displayName())` (UUID v7 gerado internamente)
    4. Persistir: `userRepository.save(user)`
    5. Retornar `RegisterUserOutput`
  - **Sem domain events** (P1 — deferido)

### 10. Infrastructure Layer — JPA + MapStruct

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/persistence/JpaUserEntity.java`:
  - `@Entity` + `@Table(name = "users", schema = "core")`
  - `@Id` sem `@GeneratedValue` — UUID vem do domain object via mapper
  - `@EntityListeners(AuditingEntityListener.class)` para JPA auditing
  - `createdAt`: `@CreatedDate` + `@Column(name = "created_at", nullable = false, updatable = false)`
  - `updatedAt`: `@LastModifiedDate` + `@Column(name = "updated_at", nullable = false)`
  - Ambos como `Instant`, mapeados para `TIMESTAMPTZ`

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/persistence/SpringDataUserRepository.java`:
  - Interface que extends `JpaRepository<JpaUserEntity, UUID>`
  - Metodos: `Optional<JpaUserEntity> findByEmail(String email)`, `boolean existsByEmail(String email)`

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/persistence/UserMapper.java`:
  - Interface MapStruct: `@Mapper(componentModel = "spring")`
  - Metodos: `JpaUserEntity toEntity(User user)`, `User toDomain(JpaUserEntity entity)`
  - Mapear campos entre o domain `User` e o `JpaUserEntity`

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/persistence/JpaUserRepositoryAdapter.java`:
  - `@Repository`
  - Implementa `UserRepository` (interface do domain)
  - Delega para `SpringDataUserRepository` + `UserMapper`
  - Metodos: `save()` → mapper para entity, save via Spring Data, mapper de volta para domain; `findByEmail()`, `existsByEmail()`

### 11. Infrastructure Layer — Spring Security Integration

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/security/JpaUserDetailsService.java`:
  - `@Service`
  - Implementa `UserDetailsService` do Spring Security
  - Dependencia: `UserRepository` (domain interface)
  - `loadUserByUsername(String email)`:
    1. `userRepository.findByEmail(email)` → se nao encontrado, lancar `UsernameNotFoundException`
    2. Construir e retornar `org.springframework.security.core.userdetails.User` com email, passwordHash, e authorities (pode ser `ROLE_USER` por defeito)

### 12. Application Layer — AuthenticatedUser

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/application/AuthenticatedUser.java`:
  - `@Component`
  - Metodo `UUID getUserId()`: extrai o userId do `SecurityContextHolder`
  - Implementacao: obter `Authentication` → obter principal → extrair UUID
  - Nota: requer que o `UserDetails` retornado pelo `JpaUserDetailsService` inclua o UUID do utilizador (pode ser necessario um custom `UserDetails` que armazene o UUID)
  - Este componente sera usado por todos os outros bounded contexts para obter o userId corrente

- [ ] Se necessario, criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/infrastructure/security/AppUserDetails.java`:
  - Extends ou implementa `UserDetails`
  - Campo adicional: `userId` (UUID)
  - Usado pelo `JpaUserDetailsService` para incluir o UUID no principal

### 13. Actualizar SecurityConfig para VS-01

- [ ] Actualizar `src/main/java/dev/ivoencarnacao/jobtracker/shared/config/SecurityConfig.java`:
  - Injetar `JpaUserDetailsService` como `UserDetailsService`
  - Configurar form login:
    - `loginPage("/login")`
    - `defaultSuccessUrl("/dashboard", true)`
    - `usernameParameter("email")` (formulario usa campo email em vez de username)
  - Configurar logout:
    - `logoutSuccessUrl("/login?logout")`
    - Invalidar sessao
  - Definir rotas publicas: `/`, `/register`, `/login`, `/css/**`, `/js/**`, `/images/**`, `/webjars/**`
  - Todas as restantes rotas requerem autenticacao
  - Manter `BCryptPasswordEncoder` bean

### 14. Web Layer — Controllers

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/web/RegistrationController.java`:
  - `GET /register`:
    - Se o utilizador ja esta autenticado, redirecionar para `/dashboard`
    - Caso contrario, renderizar `identity/register` com form model vazio
  - `POST /register`:
    - Bind `RegisterUserInput` com `@Valid`
    - Se `BindingResult` tem erros, re-renderizar form com erros
    - Chamar `RegisterUserUseCase.execute(input)`
    - Se `EmailAlreadyExistsException`, adicionar erro ao campo email e re-renderizar
    - Se sucesso, redirect para `/login?registered` (com flash message de sucesso)

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/web/LoginController.java`:
  - `GET /login`:
    - Se o utilizador ja esta autenticado, redirecionar para `/dashboard`
    - Caso contrario, renderizar `identity/login`
    - O Spring Security trata do `POST /login` automaticamente

- [ ] Criar controller ou rota para `/dashboard`:
  - `GET /dashboard` → renderizar `dashboard/index.html` (stub placeholder para VS-01)
  - Pode ser um `DashboardController` simples no pacote `dashboard/web/` ou no `shared/config/WebConfig`

### 15. Exception Handling

- [ ] Criar `src/main/java/dev/ivoencarnacao/jobtracker/identity/application/EmailAlreadyExistsException.java`:
  - `RuntimeException` com mensagem descritiva

- [ ] Considerar se e necessario um `@ControllerAdvice` global ou se o tratamento de erros no `RegistrationController` e suficiente para VS-01

### 16. Templates Thymeleaf

- [ ] Criar `src/main/resources/templates/identity/register.html`:
  - Decorar com `layout/main` via layout dialect
  - Formulario com `th:action="@{/register}"` e `th:object="${registerUserInput}"`
  - Campos: email (`type="email"`), password (`type="password"`), displayName (`type="text"`)
  - Cada campo com label, input (styling Neo-Brutalism: `border-2 border-black p-3`), e mensagem de erro (`th:errors`)
  - Botao submit estilo Neo-Brutalism: fundo preto, texto branco, border 3px, shadow
  - Link para login: "Already have an account? Log in"
  - Mostrar mensagem de erro geral se email duplicado

- [ ] Criar `src/main/resources/templates/identity/login.html`:
  - Decorar com `layout/main`
  - Formulario com `action="/login"` (Spring Security endpoint)
  - Campos: email (`name="username"` — Spring Security default, ou configurar `usernameParameter`), password (`name="password"`)
  - Styling Neo-Brutalism consistente com register
  - Mensagem de erro se `?error` presente: "Invalid email or password" (generica para prevenir enumeracao)
  - Mensagem de sucesso se `?registered` presente: "Registration successful. Please log in."
  - Mensagem se `?logout` presente: "You have been logged out."
  - Link para registo: "Don't have an account? Sign up"

- [ ] Criar `src/main/resources/templates/dashboard/index.html`:
  - Decorar com `layout/main`
  - Stub minimo: heading "Dashboard" e mensagem de boas-vindas
  - Mostrar display name do utilizador autenticado (via `sec:authentication="name"` ou model attribute)
  - Botao/link de logout

- [ ] Actualizar `src/main/resources/templates/layout/main.html` se necessario:
  - Adicionar navegacao basica (condicional: se autenticado, mostrar nav com Dashboard e Logout; se nao autenticado, mostrar Login/Register)
  - Usar `sec:authorize` do Thymeleaf Security dialect

- [ ] Formatar todos os templates com Prettier

### 17. Testes Unitarios

- [ ] Criar `src/test/java/dev/ivoencarnacao/jobtracker/identity/domain/UserTests.java`:
  - Testar factory method `User.create()`:
    - Sucesso com dados validos
    - Falha com email null/vazio
    - Falha com email formato invalido
    - Falha com displayName null/vazio
    - Falha com displayName > 100 caracteres
    - Falha com passwordHash null/vazio

- [ ] Criar `src/test/java/dev/ivoencarnacao/jobtracker/identity/application/RegisterUserUseCaseTests.java`:
  - Mock de `UserRepository` e `PasswordEncoder`
  - Testar registo com sucesso:
    - Verifica que `existsByEmail` retorna false
    - Verifica que `passwordEncoder.encode` e chamado
    - Verifica que `User.create` e chamado com password hash (nao plain text)
    - Verifica que `userRepository.save` e chamado
    - Verifica que o output tem os campos corretos
  - Testar registo com email duplicado:
    - `existsByEmail` retorna true → lanca `EmailAlreadyExistsException`
    - `save` nunca e chamado

### 18. Testes de Integracao

- [ ] Criar `src/test/java/dev/ivoencarnacao/jobtracker/identity/web/RegistrationControllerIT.java`:
  - `@SpringBootTest` + `@AutoConfigureMockMvc`
  - Importar `TestcontainersConfiguration`
  - Testar `GET /register` retorna 200 e contem formulario
  - Testar `POST /register` com dados validos:
    - Redirect para `/login?registered`
    - Utilizador existe na base de dados
    - Password esta hashed (nao plain text)
  - Testar `POST /register` com email duplicado:
    - Re-renderiza form com erro no campo email
  - Testar `POST /register` com dados invalidos (email mal formatado, displayName vazio):
    - Re-renderiza form com erros de validacao

- [ ] Criar `src/test/java/dev/ivoencarnacao/jobtracker/identity/web/LoginControllerIT.java`:
  - `@SpringBootTest` + `@AutoConfigureMockMvc`
  - Importar `TestcontainersConfiguration`
  - Testar `GET /login` retorna 200
  - Testar `POST /login` com credenciais validas (registar utilizador primeiro):
    - Redirect para `/dashboard`
    - Sessao autenticada
  - Testar `POST /login` com credenciais invalidas:
    - Redirect para `/login?error`
  - Testar logout:
    - `POST /logout` → redirect para `/login?logout`
    - Sessao invalidada

- [ ] Testar proteccao de rotas:
  - `GET /dashboard` sem autenticacao → redirect para `/login`
  - `GET /` (landing page) sem autenticacao → 200 (publica)
  - `GET /register` sem autenticacao → 200 (publica)

### 19. Formatar e Validar

- [ ] Formatar Java: `./mvnw spring-javaformat:apply`
- [ ] Formatar templates: `npx prettier --write "src/main/resources/**/*.{html,css,js,json}"`
- [ ] Executar build completo: `./mvnw verify`
- [ ] Verificar que JaCoCo >= 70%

---

## Estrategia de Commits (Conventional Commits)

Ordem sugerida, um commit por concern:

1. `feat(shared): add Flyway V1 migration for core schema`
2. `feat(shared): add SecurityConfig, JpaAuditingConfig, and UuidV7 utility`
3. `feat(shared): force JVM timezone to UTC in Application entry point`
4. `feat: implement landing page with Neo-Brutalism design`
5. `ci: add GitHub Actions CI workflow`
6. `feat(identity): add Flyway V2 migration for users table`
7. `feat(identity): implement User aggregate root with UUID v7 factory method`
8. `feat(identity): add RegisterUserUseCase with BCrypt hashing`
9. `feat(identity): add JPA infrastructure (entity, repository, mapper)`
10. `feat(identity): add JpaUserDetailsService and AppUserDetails`
11. `feat(identity): add AuthenticatedUser cross-context identity helper`
12. `feat(identity): configure Spring Security with form login and registration`
13. `feat(identity): add registration and login controllers`
14. `feat(identity): add register, login, and dashboard templates`
15. `test(identity): add unit tests for User aggregate and RegisterUserUseCase`
16. `test(identity): add integration tests for registration and login flows`

---

## Verificacao End-to-End

### VS-00
1. `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` → app inicia sem erros
2. Abrir `http://localhost:8080/` → landing page visivel com styling Neo-Brutalism
3. `./mvnw verify` → CI green, testes passam, JaCoCo >= 70%
4. `docker build -t jobtracker .` → imagem Docker builda com sucesso

### VS-01
1. Abrir `http://localhost:8080/register` → formulario de registo
2. Preencher e submeter com dados validos → redirect para `/login?registered` com mensagem de sucesso
3. Login com credenciais registadas → redirect para `/dashboard`
4. Dashboard mostra mensagem de boas-vindas com nome do utilizador
5. Tentar registar com email duplicado → erro no campo email
6. Tentar login com credenciais erradas → "Invalid email or password"
7. Logout → redirect para `/login?logout`
8. Tentar aceder `/dashboard` sem autenticacao → redirect para `/login`
9. `./mvnw verify` → todos os testes passam, JaCoCo >= 70%

---

## Ficheiros a Criar/Modificar

### Novos (VS-00)
- `src/main/resources/db/migration/V1__create_schema.sql`
- `src/main/java/.../shared/config/SecurityConfig.java`
- `src/main/java/.../shared/config/JpaAuditingConfig.java`
- `src/main/java/.../shared/domain/UuidV7.java`
- `.github/workflows/ci.yml`

### Novos (VS-01)
- `src/main/resources/db/migration/V2__create_users_table.sql`
- `src/main/java/.../identity/domain/User.java`
- `src/main/java/.../identity/domain/UserRepository.java`
- `src/main/java/.../identity/application/dto/RegisterUserInput.java`
- `src/main/java/.../identity/application/dto/RegisterUserOutput.java`
- `src/main/java/.../identity/application/RegisterUserUseCase.java`
- `src/main/java/.../identity/application/EmailAlreadyExistsException.java`
- `src/main/java/.../identity/application/AuthenticatedUser.java`
- `src/main/java/.../identity/infrastructure/persistence/JpaUserEntity.java`
- `src/main/java/.../identity/infrastructure/persistence/SpringDataUserRepository.java`
- `src/main/java/.../identity/infrastructure/persistence/UserMapper.java`
- `src/main/java/.../identity/infrastructure/persistence/JpaUserRepositoryAdapter.java`
- `src/main/java/.../identity/infrastructure/security/JpaUserDetailsService.java`
- `src/main/java/.../identity/infrastructure/security/AppUserDetails.java`
- `src/main/java/.../identity/web/RegistrationController.java`
- `src/main/java/.../identity/web/LoginController.java`
- `src/main/resources/templates/identity/register.html`
- `src/main/resources/templates/identity/login.html`
- `src/main/resources/templates/dashboard/index.html`
- `src/test/java/.../identity/domain/UserTests.java`
- `src/test/java/.../identity/application/RegisterUserUseCaseTests.java`
- `src/test/java/.../identity/web/RegistrationControllerIT.java`
- `src/test/java/.../identity/web/LoginControllerIT.java`

### Modificados
- `src/main/java/.../Application.java` (adicionar `TimeZone.setDefault(UTC)`)
- `src/main/resources/templates/index.html` (landing page redesign)
- `src/main/resources/templates/layout/main.html` (adicionar navegacao condicional)
- `src/main/java/.../shared/config/SecurityConfig.java` (actualizado de VS-00 para VS-01)
- `src/main/resources/application-local.properties` (remover spring.security.user default)

---

## Notas e Decisoes

- **Sem domain events em VS-01** — `UserRegistered` e P1 (deferido para VS-09/VS-10)
- **UUID v7 (RFC 9562)** gerado na application layer via `UuidV7.randomUUID()` — sequencial, index-friendly, sem dependencias externas
- **`Persistable<UUID>`** — deferido para FUTURE.md como sugestao de melhoria de performance (evita `SELECT` antes de `INSERT` quando ID e pre-atribuido)
- **Timestamps como `Instant`** no Java, mapeados para `TIMESTAMPTZ` no PostgreSQL. JVM em UTC.
- **Spring Data JPA Auditing** (`@CreatedDate`, `@LastModifiedDate`) para audit timestamps automaticos
- **`@EnableJpaAuditing`** numa `@Configuration` separada para compatibilidade com `@WebMvcTest`
- **`usernameParameter("email")`** no Spring Security para o campo de login ser email
- **Enums:** A tabela `users` nao tem enums. Discrepancia encontrada entre `DATA-SCHEMA-DETAIL.md` (VARCHAR) e `CLAUDE.md`/`MIGRATIONS.md` (PostgreSQL native enum types) — nao afecta VS-01 mas deve ser resolvida antes de VS-02
- **Deploy:** Estrategia de deploy para staging/production ainda nao definida — sera abordada no contexto de VS-00
- **Regra CLAUDE.md:** Apos aprovacao, adicionar regra sobre seguir best practices Spring Boot 3.x / Hibernate 6
- **FUTURE.md:** Adicionar `Persistable<UUID>` como sugestao de melhoria de performance na seccao Infrastructure
