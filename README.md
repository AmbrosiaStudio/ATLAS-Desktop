# ATLAS Desktop

**Your world of knowledge.** A calm, local-first desktop library and reading workspace.

ATLAS Desktop is the Windows-first desktop companion to ATLAS for Android. It has its own repository and desktop-native experience while following the same product identity and visual language.

## Current status

Early foundation — the first milestone is the application shell, design tokens, and Windows packaging pipeline. Reader and library features are planned, not yet implemented.

## Technology

- Kotlin/JVM
- Compose Multiplatform for Desktop
- JDK 17
- Gradle Kotlin DSL
- Windows MSI distribution

## Run locally

Use JDK 17 or newer and Gradle 9.2.1 or a compatible Gradle installation:

```powershell
gradle run
```

Package a Windows installer from Windows:

```powershell
gradle packageMsi
```

## Project principles

- Local-first. A user's library belongs to them.
- Preserve original imported files; never silently rewrite them.
- Keep platform-specific APIs behind adapters.
- Stable IDs and portable relative storage keys; never persist machine-specific absolute paths as identity.
- No account, telemetry, cloud sync, or network dependency in the initial release.
- Desktop-first interaction: keyboard access, resizable layouts, drag-and-drop, and native file dialogs.

See [AGENTS.md](AGENTS.md) and [docs/architecture.md](docs/architecture.md).
