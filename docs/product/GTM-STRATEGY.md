# Go-to-Market Strategy

## Launch Strategy

### Phase 1 — Private Beta

- Deploy the MVP to a small group of 10-20 trusted testers (university peers, developer community contacts, career changers in the target demographic).
- Goal: validate core flows (registration → application tracking → dashboard), identify friction points, and collect qualitative feedback.
- Duration: 2-3 weeks.
- Feedback channels: direct messaging, short feedback form embedded in the app.

### Phase 2 — Open Beta

- Open registration to anyone with a landing page and simple sign-up.
- Goal: validate product-market fit with organic traffic. Measure activation rate (sign up → create 5+ applications).
- Duration: 4-6 weeks.
- Begin collecting usage metrics (applications created, follow-up usage, insights page visits).

### Phase 3 — Public Launch

- Announce on Product Hunt, Reddit, LinkedIn, and developer communities.
- Goal: initial traction — 100+ registered users in the first month.
- Iterate based on beta feedback before this phase.

---

## Promotion Channels

### Primary (Free / Organic)

| Channel | Approach | Target Audience |
|---------|----------|-----------------|
| Reddit | Post in r/portugal, r/devpt, r/cscareerquestions, r/webdev | Portuguese tech community, junior developers |
| LinkedIn | Personal posts, Portuguese tech groups, career transition groups | Junior professionals, career changers |
| Dev.to / Hashnode | "Building in public" technical blog posts | Developer community, potential contributors |
| Product Hunt | Launch with landing page, screenshots, clear value proposition | Early adopters, product enthusiasts |
| University career services | Partnerships with Portuguese universities (CS departments) | Recent graduates (Persona A) |
| Tech meetups | Lisbon/Porto tech meetups, career-focused events | Local developer community |

### Secondary (Paid — Post-MVP)

| Channel | Approach | When |
|---------|----------|------|
| Google Ads | Target "job application tracker" keywords | After validating PMF |
| LinkedIn Ads | Target junior developers and career changers in Portugal | After validating PMF |

> Paid channels are not part of the MVP launch. Only activate after product-market fit is validated.

---

## Content Strategy

### Building in Public

Document the development process through regular blog posts:

- Technical decisions (architecture, DDD approach, technology choices)
- Product decisions (what made it into MVP and what didn't)
- Lessons learned (what worked, what didn't)
- Market insights from the skill trend data (anonymized/aggregated)

**Benefits:** builds audience before launch, establishes credibility, attracts developer community feedback.

### SEO Content (Post-Launch)

- Job search tips and organization strategies
- "Most requested skills in [market/role]" reports from aggregated data
- Interview preparation guides

---

## Acquisition Measurement

> For precise metric definitions and instrumentation details, see [Metrics Plan](METRICS-PLAN.md).

### Registration Funnel

```
Landing page visit
  → Sign-up page view
    → Registration completed
      → First application created
        → 5+ applications created (activated user)
          → 2+ sessions in week 2 (retained user)
```

### Key Metrics

| Metric | Definition | Target |
|--------|-----------|--------|
| Conversion rate | Registrations / landing page visits | > 5% |
| Activation rate | Users with 5+ applications / total registrations | > 30% |
| Week-1 retention | Users with 2+ sessions in their second week / activated users | > 40% |
| Referral rate | Users who shared the app / total active users | Track (no target yet) |

### Tooling

- **Analytics:** Plausible or Umami (privacy-respecting, no cookies, GDPR-compliant)
- **Feedback:** In-app feedback widget (simple text form)
- **Monitoring:** Application logs + basic error tracking

> No third-party tracking scripts (Google Analytics, Meta Pixel) in the MVP. Privacy-first approach aligns with user trust for a tool that stores sensitive career data.

---

## Pricing (Suggestion)

### MVP

Entirely **free**. No paywalls, no feature gates, no trial periods. The goal is to validate product-market fit and build a user base.

### Post-MVP (When to Introduce Pricing)

Consider introducing a freemium model after:
- 500+ registered users
- Clear signal of product-market fit (activation rate > 30%, retention > 40%)
- Feature requests that justify a premium tier

### Suggested Tiers

| Tier | Price | Features |
|------|-------|----------|
| Free | €0 | Full tracking (up to 50 active applications), basic dashboard, manual skill tagging |
| Premium | €5-8/month | Unlimited applications, skill extraction from descriptions, advanced analytics (temporal trends, category breakdown), CSV export, priority support |

> These are suggestions. Validate pricing through user interviews and willingness-to-pay surveys before implementation.
