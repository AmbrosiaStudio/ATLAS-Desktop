# ATLAS Desktop Architecture

## Direction

ATLAS Desktop is a standalone Kotlin/JVM application built with Compose Multiplatform for Desktop. It lives in a separate repository from the Android app. The two apps share product identity and documented interchange formats, not platform-specific implementation details.

Do not create a colocated Kotlin Multiplatform module that depends on the Android repository. If genuinely reusable pure Kotlin logic emerges later, extract it into a separately versioned library after the boundaries are proven.

## Current package boundaries

Keep one Gradle module initially; use package boundaries until the codebase justifies splitting modules.

- `domain`: book model, repository contract, and library import service.
- `data`: SQLite database and book repository implementation.
- `reader`: planned reading sessions and format adapters.
- `writer`: planned Writing Studio documents and export.
- `designsystem`: theme tokens and reusable components (currently colocated in UI).
- `ui`: desktop navigation and screens.
- `platform`: Windows/desktop file selection and OS integration.

## Data and privacy

- Local-first storage; no sign-in required.
- Give every library item a stable UUID.
- Store portable relative storage keys, not absolute file paths, as durable identity.
- Keep original imported files intact; import by copying into `%USERPROFILE%/.atlas/library`.
- SQLite catalogue lives at `%USERPROFILE%/.atlas/atlas.db`.
- Generated covers, extracted text, and caches are derived data.
- Backups must be versioned, validated before restore, and safe against partial failure.
- Sync is out of scope until a conflict model, encryption model, and explicit opt-in design exist.

## Implemented library behavior

- Import multiple files through a desktop file chooser.
- Supported import extensions: EPUB, PDF, TXT, Markdown, FB2, CBZ, MP3, M4A, M4B, FLAC, OGG.
- Search catalogue by title, author, and format.
- Group catalogue entries by format.
- Track recently opened items.
- Display TXT and Markdown files in a basic built-in text view.
- Delegate other imported formats to the operating system's default application for now.

## Reader adapters

- EPUB: first full reader target.
- TXT and Markdown: lightweight text adapters (basic view implemented; pagination and typography controls remain planned).
- PDF: desktop-specific PDF engine behind an interface.
- FB2: XML-based adapter.
- CBZ: ZIP/image adapter.
- Audiobooks: playback engine behind an interface.

Reader engines must not leak into domain models or Compose UI.

## Desktop interaction

Use a resizable application shell, persistent navigation, keyboard shortcuts, drag-and-drop, native file dialogs, and layouts that adapt to window size. Preserve accessible focus order and meaningful labels.

## Delivery milestones

1. Foundation: app shell, theme, navigation, build, Windows CI, MSI.
2. Library: local storage, import, metadata, collections, cover cache.
3. EPUB reader: table of contents, typography, progress, bookmarks, highlights, notes, search.
4. Additional formats: PDF, TXT, Markdown, FB2, CBZ.
5. Audiobooks.
6. Writing Studio, statistics, goals, and exports.
7. Optional Android interoperability through a stable import/export contract.
8. Installer polish, signing, accessibility, performance, and release QA.
