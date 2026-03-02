# Landing Page Specification

## Objective

Convert visitors into registered users. Communicate the value proposition in under 10 seconds. The page should answer: "What is this?" and "Why should I use it?"

---

## Visual Treatment

- **Style:** Neo-Brutalism — thick black borders, solid offset shadows, no gradients, no rounded corners.
- **Palette:** Black and white with minimal accent color for CTAs.
- **Font:** Geist (Vercel) — Sans for headings and body, Mono for data/stats.
- **Layout:** Single-column, full-width sections. Maximum content width: 1200px centered.
- **Refer to:** [DESIGN-GUIDELINES.md](./DESIGN-GUIDELINES.md) for full token reference.

---

## Sections

### 0. Header

**Objective:** Brand presence and persistent CTA access.

| Element | Notes |
|---------|-------|
| Logo | Text-based: "Job Tracker" in bold Geist Sans, black |
| Primary CTA | "Get Started" button (compact primary style) |

**Layout:** Sticky top bar. Logo left-aligned, CTA right-aligned. White background, `border-default` bottom border. Max content width: 1200px centered. Height: 64px.

**Mobile:** Same layout, condensed padding.

---

### 1. Hero

**Objective:** Immediate value communication.

| Element | Notes |
|---------|-------|
| Headline | Problem statement — what pain this solves (1 line) |
| Subheadline | Solution statement — what the product does (1-2 lines) |
| Primary CTA | Sign-up action (thick border button, shadow-lg) |
| Secondary CTA | "Learn more" — scrolls to Features section (ghost button) |

**Layout:** Centered content, generous vertical spacing. Text and CTAs centered on both desktop and mobile.

---

### 2. Problem Statement

**Objective:** Build empathy — show the user you understand their pain.

| Element | Notes |
|---------|-------|
| 3 pain points | Each with an icon and short description |

**Pain points:**
1. Lost track of applications
2. Missed follow-ups and timings
3. No visibility into market skill demands

**Layout:** 3-column grid on desktop, single column on mobile. Each column is a Neo-Brutalism card (thick border, solid shadow).

---

### 3. Features

**Objective:** Show what the product does — concrete capabilities.

| Feature | Description |
|---------|-------------|
| Pipeline tracking | Track applications through 11 hiring stages |
| Follow-up reminders | Never miss a follow-up with internal reminders |
| Interview and offer management | Log interviews, record offers, track outcomes |
| Skill trend insights | See which skills appear most in your saved postings |

**Layout:** Vertical feature cards in a 2x2 grid on desktop (single column on mobile). Each card is a Neo-Brutalism card with icon, title, and description. Images/screenshots to be added post-launch.

---

### 4. How It Works

**Objective:** Reduce perceived friction — show it's simple to get started.

| Step | Description |
|------|-------------|
| 1 | Create an account |
| 2 | Add your first application |
| 3 | Track, follow up, and learn from your data |

**Layout:** Horizontal 3-step flow with numbered circles and connecting lines. On mobile, vertical stack. Each step is a card with Neo-Brutalism styling.

---

### 5. Social Proof (Placeholder)

**Objective:** Build trust and credibility.

| Element | Notes |
|---------|-------|
| Testimonial cards | Placeholder — empty for launch, to be filled with real user quotes post-launch |
| Usage stats | Placeholder — "X applications tracked" counter (once there's real data) |

**Layout:** Centered section with empty state text at launch. Replace with real testimonials and usage data when available.

---

### 6. Final CTA

**Objective:** Last conversion opportunity before the user leaves.

| Element | Notes |
|---------|-------|
| Headline | Reinforcement of value — why start now |
| Primary CTA | Same sign-up action as Hero |

**Layout:** Full-width banner with dark background (black), white text, centered. CTA button in white with black text (inverted primary style).

---

### 7. Footer

| Element | Notes |
|---------|-------|
| Navigation links | About, Privacy Policy, Terms |
| Contact | Email or contact form link |
| Social links | GitHub (if open source), LinkedIn |

**Layout:** Simple single-row footer. Black background, white text. Minimal.

---

## CTA Hierarchy

| Level | Label | Placement | Style |
|-------|-------|-----------|-------|
| Primary | Sign Up / Get Started | Hero (section 1) + Final CTA (section 6) | Solid button, thick border, shadow-lg |
| Secondary | Learn More | Hero only | Ghost button, scrolls to Features |

> No more than 2 CTAs visible at any time. The page funnels toward one action: sign up.

---

## Responsive Behavior

| Breakpoint | Behavior |
|------------|----------|
| Desktop (≥1024px) | Full layout: multi-column grids, side-by-side image-text |
| Tablet (768–1023px) | Reduced columns (3 → 2), smaller images |
| Mobile (<768px) | Single column, stacked elements, full-width cards |

---

## Performance Targets

| Metric | Target |
|--------|--------|
| First Contentful Paint | < 1.5s |
| Largest Contentful Paint | < 2.5s |
| Total page weight | < 500KB (including images) |
| JavaScript | Minimal — no additional JS framework beyond Vite's build tooling. Only vanilla JS for scroll-to-section behavior on "Learn More" CTA |

---

## SEO & Meta Tags

| Tag | Value |
|-----|-------|
| `<title>` | Job Tracker — Track Applications, Land Jobs |
| `meta description` | Track every job application, never miss a follow-up, and discover which skills the market wants. Free forever. |
| `og:title` | Job Tracker — Track Applications, Land Jobs |
| `og:description` | Same as meta description |
| `og:type` | website |
| `og:image` | To be added (1200x630 sharing image) |
| `twitter:card` | summary_large_image |
