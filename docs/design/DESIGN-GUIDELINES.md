# Design Guidelines

## Design Philosophy

The visual identity follows **Neo-Brutalism** — a design movement characterized by bold borders, high contrast, raw typography, solid shadows, and functional clarity. The aesthetic is intentionally stark and utilitarian: no gradients, no soft blurs, no decorative flourishes. Every visual element serves a purpose.

The palette is **black and white** with minimal accent colors reserved for functional meaning (status states, alerts, actions).

---

## Color Palette

### Core

| Token | Hex | Usage |
|-------|-----|-------|
| `black` | `#000000` | Text, borders, shadows, primary buttons |
| `white` | `#FFFFFF` | Backgrounds, card surfaces, inverted text |
| `gray-100` | `#F5F5F5` | Page background, subtle separators |
| `gray-200` | `#E5E5E5` | Disabled states, dividers |
| `gray-400` | `#A3A3A3` | Placeholder text, secondary text |
| `gray-600` | `#525252` | Body text (when lighter contrast is needed) |
| `gray-900` | `#171717` | Near-black for headings |

### Functional Accents

Used sparingly for application pipeline status and system feedback only.

| Token | Hex | Usage |
|-------|-----|-------|
| `accent-blue` | `#2563EB` | Links, informational states (Under Review) |
| `accent-green` | `#16A34A` | Success states (Offer, Passed) |
| `accent-yellow` | `#CA8A04` | Warning states (Pending, Follow-up due) |
| `accent-red` | `#DC2626` | Destructive actions, error states (Rejected) |
| `accent-purple` | `#7C3AED` | Neutral highlight (Challenge, Interview stages) |
| `accent-muted` | `#6B7280` | Ghosted, Withdrawn (terminal inactive states) |

> **Rule:** Accent colors appear only in badges, status indicators, and system alerts. They never dominate the layout. The page should read as black and white at a glance.

---

## Typography

### Font Family

**Geist** by Vercel — a modern, geometric sans-serif optimized for interfaces.

| Variant | Usage |
|---------|-------|
| Geist Sans | UI text: headings, body, labels, buttons |
| Geist Mono | Data: numbers, dates, code, status values |

### Type Scale

| Token | Size | Line Height | Weight | Usage |
|-------|------|-------------|--------|-------|
| `display` | 36px / 2.25rem | 1.2 | 700 (Bold) | Page titles, hero headings |
| `h1` | 30px / 1.875rem | 1.25 | 700 (Bold) | Section headings |
| `h2` | 24px / 1.5rem | 1.3 | 600 (Semi-Bold) | Card titles, subsections |
| `h3` | 20px / 1.25rem | 1.35 | 600 (Semi-Bold) | Widget titles |
| `body` | 16px / 1rem | 1.5 | 400 (Regular) | Default text |
| `body-sm` | 14px / 0.875rem | 1.5 | 400 (Regular) | Secondary text, descriptions |
| `caption` | 12px / 0.75rem | 1.4 | 400 (Regular) | Labels, metadata, timestamps |
| `mono` | 14px / 0.875rem | 1.5 | 400 (Regular) | Data values (Geist Mono) |

### Weight Usage

| Weight | Value | Usage |
|--------|-------|-------|
| Regular | 400 | Body text, descriptions |
| Medium | 500 | Labels, navigation items |
| Semi-Bold | 600 | Subheadings, emphasized text |
| Bold | 700 | Headings, buttons, key metrics |

---

## Spacing System

Base unit: **4px**. All spacing follows the 4px grid.

| Token | Value | Usage |
|-------|-------|-------|
| `space-1` | 4px | Tight gaps (icon-to-text, inline elements) |
| `space-2` | 8px | Default inner padding, compact spacing |
| `space-3` | 12px | Form field padding, badge padding |
| `space-4` | 16px | Card padding, section gaps |
| `space-6` | 24px | Between components, card margins |
| `space-8` | 32px | Section padding |
| `space-12` | 48px | Major section separation |
| `space-16` | 64px | Page-level vertical rhythm |

---

## Borders

Thick, solid, black. This is the defining visual trait of Neo-Brutalism.

| Token | Value | Usage |
|-------|-------|-------|
| `border-default` | 2px solid #000000 | Cards, inputs, containers |
| `border-thick` | 3px solid #000000 | Buttons, primary cards, focused elements |
| `border-thin` | 1px solid #E5E5E5 | Table rows, subtle dividers |

---

## Border Radius

Minimal to none. Sharp edges are characteristic of Neo-Brutalism.

| Token | Value | Usage |
|-------|-------|-------|
| `radius-none` | 0px | Default for all elements |
| `radius-sm` | 2px | Optional softening for small elements (badges, tags) |
| `radius-md` | 4px | Maximum allowed — used rarely |

> **Rule:** Never use `rounded-lg`, `rounded-xl`, or `rounded-full` on structural elements. Only `radius-sm` is acceptable for small inline elements like badges.

---

## Shadows

Solid offset shadows with **no blur**. This is the signature Neo-Brutalism shadow.

| Token | Value | Usage |
|-------|-------|-------|
| `shadow-sm` | 2px 2px 0 #000000 | Badges, small interactive elements |
| `shadow-md` | 4px 4px 0 #000000 | Cards, dropdowns, modals |
| `shadow-lg` | 6px 6px 0 #000000 | Primary CTAs, hero elements, featured cards |
| `shadow-hover` | 6px 6px 0 #000000 | Hover state for interactive cards/buttons |
| `shadow-active` | 2px 2px 0 #000000 | Active/pressed state (reduced offset) |

> **Interaction pattern:** On hover, shadow grows (sm → md or md → lg). On click/active, shadow shrinks and element translates toward the shadow direction, creating a "press" effect.

---

## Component Guidance

### Buttons

- **Primary:** Black background, white text, thick border (3px), solid shadow (`shadow-md`). Hover: shadow grows to `shadow-lg`. Active: shadow shrinks, element translates 2px down-right.
- **Secondary:** White background, black text, thick border (3px), solid shadow (`shadow-sm`). Same hover/active pattern.
- **Destructive:** Same as primary but with `accent-red` background.
- **Ghost:** No background, no border, no shadow. Black text. Hover: underline.

### Cards

- White background, `border-default` (2px), `shadow-md` (4px 4px 0 #000).
- No border-radius.
- Padding: `space-4` (16px).
- Hover (if interactive): shadow grows to `shadow-lg`, subtle translate.

### Inputs

- White background, `border-default` (2px), no shadow.
- Padding: `space-3` (12px).
- Focus: border color stays black, add `shadow-sm` to indicate focus.
- Placeholder text: `gray-400`.
- Error state: `accent-red` border.

### Status Badges

- Small inline elements for pipeline states.
- Background: corresponding accent color (low opacity, e.g., 10-15%).
- Text: corresponding accent color (full opacity).
- Border: 1px solid corresponding accent color.
- Border-radius: `radius-sm` (2px).
- Font: `caption` size, `medium` weight, uppercase.

### Tables

- `border-thin` between rows.
- Header row: black background, white text, bold.
- Alternating row colors: white / `gray-100`.

### Navigation

- Top bar: white background, `border-default` bottom border.
- Active link: bold weight, underline.
- Hover: underline.

---

## Iconography

- Style: outlined, monochrome (black on white).
- Stroke width: 2px (matching border weight).
- Size: 20px (default), 16px (compact), 24px (prominent).
- Library suggestion: Lucide Icons (consistent with Geist aesthetic).

---

## Visual References

The following design movements and examples inform the visual direction:

- **Neo-Brutalism in web design:** bold borders, solid shadows, raw typography, high contrast.
- **Gumroad** (2021+ redesign) — pioneered Neo-Brutalism in SaaS.
- **Figma community Neo-Brutalism kits** — for component patterns.
- **Vercel's design system** — for Geist Font usage and clean data presentation.

---

## Tailwind CSS 4 Token Mapping

Reference for translating design tokens into Tailwind classes:

```
/* Borders */
border-2 border-black          /* border-default */
border-3 border-black          /* border-thick (custom) */
border border-gray-200         /* border-thin */

/* Shadows (custom utilities) */
shadow-[2px_2px_0_#000]        /* shadow-sm */
shadow-[4px_4px_0_#000]        /* shadow-md */
shadow-[6px_6px_0_#000]        /* shadow-lg */

/* Radius */
rounded-none                    /* radius-none (default) */
rounded-sm                      /* radius-sm */
rounded                         /* radius-md */

/* Typography */
font-geist                      /* Geist Sans */
font-geist-mono                 /* Geist Mono */

/* Spacing follows Tailwind's default 4px scale */
p-1 (4px), p-2 (8px), p-3 (12px), p-4 (16px), p-6 (24px), p-8 (32px)
```

> These mappings assume a Tailwind CSS 4 configuration with custom theme extensions for Geist fonts, the 3px border width, and solid shadow utilities.
