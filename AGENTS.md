# ATLAS Desktop — Agent Guide

## Product identity
- Product name: ATLAS
- Studio: Ambrosia Studio
- Tagline: Your world of knowledge.
- Keep the same ATLAS brand, logo direction, and visual language across Android and Desktop.
- Desktop UX must be designed for desktop; do not stretch mobile screens into desktop layouts.

## Design language
- Swiss editorial, minimal, monochrome, calm.
- Light background: #F5F5F2; surface: #FFFFFF; primary: #111111; secondary: #777777; divider: #E6E6E2.
- Dark background: #111111; surface: #1B1B1B; primary: #F5F5F2; secondary: #999999; divider: #303030.
- English typeface: Inter. Persian typeface: Vazirmatn.
- Support RTL correctly for Persian. Do not hard-code layout assumptions that break RTL.
- Favor clear hierarchy, generous whitespace, restrained borders, and useful keyboard shortcuts.

## Architecture rules
- This is a standalone Kotlin/JVM + Compose Desktop repository.
- Do not add Android Gradle plugins, Android modules, Android Context/Uri APIs, Room, Glance, or Android-only dependencies here.
- Keep domain logic independent from UI and operating-system APIs.
- Use repository interfaces and platform adapters for persistence, files, readers, and audio.
- Use stable IDs and portable relative storage keys. Do not use absolute machine paths as persistent identifiers.
- Never modify imported source files in place. Preserve originals and make destructive actions explicit.
- Local-first by default. No account, telemetry, sync, or network requirement without a documented product decision.

## Engineering workflow
- Prefer small, buildable increments.
- Add tests for domain behavior and data migration/import edge cases.
- Do not claim a build passed unless it was actually run in CI or locally.
- Keep dependencies current and verify setup against official documentation.
- Avoid unnecessary abstractions and premature multi-module Gradle complexity.
