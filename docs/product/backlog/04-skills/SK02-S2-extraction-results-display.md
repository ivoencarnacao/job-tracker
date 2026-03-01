# Slice SK02-S2: Extraction Results Display

**Epic:** [EPIC-SK02](EPIC-SK02-skill-extraction.md)
**Priority:** P1
**Sprint:** 5

---

## 🎯 Value Delivered

An authenticated User sees auto-extracted skills directly on the application detail page and can manually adjust them (add missing, remove incorrect). This combines the efficiency of automation with the precision of human oversight, ensuring perfect accuracy for the user's skill profile.

## 🔗 Dependencies

- **SK02-S1** — The extraction engine must exist and be fully functional to provide the automated data.
- **SK01-S2** — The manual tagging UI and underlying endpoints must be established so they can be reused here.

---

## 📖 User Story

As an **authenticated User**, I want to **view the skills automatically extracted from the job description and have the ability to modify them**, so that **my skill tracking is effortless but remains entirely under my control**.

---

## ✅ Acceptance Criteria

**Scenario 1: Displaying Auto-Extracted Skills**

- The system displays the automatically detected skills on the application detail page.
- _Optional but recommended:_ These auto-extracted skills receive a distinct visual treatment (e.g., a slightly different chip color or an icon) to differentiate them from manually tagged skills.

**Scenario 2: Fallback for No Detections**

- If the extraction engine processes the description but yields no results, the system displays a clear "No skills detected" message.
- This empty state provides a direct link or inline interface to the manual tagging functionality.

**Scenario 3: User Override (Invariant Enforcement)**

- The user retains full control to override the auto-extraction.
- The user can easily remove skills that were incorrectly extracted by clicking a remove button.
- The user can manually add any missed skills alongside the auto-extracted ones, seamlessly reusing the manual tagging interface from SK01-S2.

---

## 🛠️ Technical Tasks (Implementation)

### Web / UI

- [ ] Update `templates/tracking/application-detail.html` (or the equivalent view).
  - Render the auto-extracted skills in the skill section.
  - Implement a visual distinction (CSS class) for auto-extracted vs. manual skills (if differentiating data is available).
  - Implement the "No skills detected" conditional block, ensuring it links cleanly to the manual tagging interface.
- [ ] Integrate existing manual controls.
  - Wire the remove action on extracted skill chips to the existing `DELETE` endpoint.
  - Display the searchable skill dropdown to allow adding manual skills.
