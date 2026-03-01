# Non-Functional Requirements

## Usability

- Simple, fast interface without excessive steps
- Mobile-friendly (responsive web design)
- Adding an application should take less than 1 minute
- Clear visual feedback on save/update actions ("Saved" confirmation or autosave indicator)
- Consistent navigation and layout across all pages

## Performance

| Metric                       | Target                                          |
| ---------------------------- | ----------------------------------------------- |
| Page load (dashboard)        | < 2 seconds for datasets up to 500 applications |
| Page load (application list) | < 2 seconds with filters applied                |
| Search results               | < 1 second for text search                      |
| Skill extraction             | < 3 seconds per job description                 |
| Form submission              | < 500ms server response time                    |

## Security and Privacy

- User data is private and isolated — users can only see their own applications
- Authentication via username/password with secure session management
- Passwords hashed with BCrypt (or equivalent adaptive hashing)
- CSRF protection on all forms
- HTTPS enforced in production
- Input sanitization to prevent XSS
- Parameterized queries to prevent SQL injection
- Clear privacy policy, especially regarding salary data and personal notes
- No third-party analytics that track personal data without consent

## Reliability

- Autosave or explicit save feedback — users should never lose notes due to a simple error
- Graceful error handling with user-friendly messages
- Database backups (daily minimum in production)
- Application should handle concurrent access from the same user (multiple tabs) without data corruption

## Accessibility

- Semantic HTML structure
- Sufficient color contrast (WCAG 2.1 AA where practical)
- Keyboard navigation support for core flows
- Form labels and error messages associated with inputs

## Browser Support

| Browser | Minimum Version       |
| ------- | --------------------- |
| Chrome  | Last 2 major versions |
| Firefox | Last 2 major versions |
| Safari  | Last 2 major versions |
| Edge    | Last 2 major versions |

> Mobile browsers (Chrome for Android, Safari for iOS) should work via responsive design. No native app in MVP.

## Localization

- MVP interface in English
- Portuguese localization as a future enhancement
- Date/time formats follow the user's locale where possible
- Currency codes use ISO 4217 standard
