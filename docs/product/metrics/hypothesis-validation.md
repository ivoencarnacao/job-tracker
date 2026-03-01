# Hypothesis Validation

How to validate each hypothesis from MVP-SCOPE using tracked data.

| # | Hypothesis | Data Required | Validation Method |
|---|-----------|---------------|-------------------|
| H1 | Users will manually input data if form takes < 1 minute | Timestamps of /applications/new page load and application.created | Measure time delta; target: median < 60 seconds for first 100 applications |
| H2 | Pasting descriptions yields 3+ recognized skills | skill.extracted events with skillCount | Percentage of extractions where skillCount ≥ 3; target: > 50% |
| H3 | Tracking + insights together create higher retention | WAU segmented by page.insights_viewed (yes/no) | Compare average WAU between the two groups; target: insights users have 1.5x higher WAU |
| H4 | Follow-up reminders reduce applications left in limbo | Ghosted rate segmented by follow_up.created (yes/no) | Compare GHOSTED percentage between users who use follow-ups vs those who don't |
