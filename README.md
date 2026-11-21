# ATLAS Desktop

**Your world of knowledge.** A calm, local-first desktop library and reading workspace.

ATLAS Desktop is the Windows-first desktop companion to ATLAS for Android. It has its own desktop-native experience and repository while following the same product identity and visual language.

## Current status

**Milestone 2 foundation in progress.** The application now has a Compose Desktop shell, persistent SQLite library catalogue, multi-file import, local file copies, search, format grouping, recently opened items, and a basic TXT/Markdown reading view. EPUB/PDF/FB2/CBZ reader engines, audio playback, writing studio, and sync are not implemented yet.

## Technology

- Kotlin/JVM
- Compose Multiplatform for Desktop
- JDK 17
- Gradle Kotlin DSL
- SQLite (local catalogue)
- Windows MSI distribution

## Run locally

Use JDK 17 or newer and Gradle 9.2.1:

```powershell
gradle run
```

Package a Windows installer from Windows:

```powershell
gradle packageMsi
```

## Local data

ATLAS stores its local database and imported copies under:

```
%USERPROFILE%\.atlas\
  atlas.db
  library\
```

Importing copies supported files into the ATLAS library. Original source files are never modified or deleted.

## Supported import formats

EPUB, PDF, TXT, Markdown, FB2, CBZ, MP3, M4A, M4B, FLAC, and OGG.

TXT and Markdown currently open in the built-in text view. Other formats are handed to the operating system's default application until their dedicated reader adapters are implemented.

## Project principles

- Local-first. A user's library belongs to them.
- Preserve original imported files; never silently rewrite them.
- Keep platform-specific APIs behind adapters.
- Stable IDs and portable relative storage keys; never persist machine-specific absolute paths as identity.
- Backups must be validated before restore.
- No account, telemetry, cloud sync, or network dependency in the initial release.
- Desktop-first interaction: keyboard access, resizable layouts, drag-and-drop, and native file dialogs.

See [AGENTS.md](AGENTS.md) and [docs/architecture.md](docs/architecture.md).
