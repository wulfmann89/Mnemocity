# Mnemocity

Mnemocity is a modular, emotionally validating memory and executive function app designed for neurodivergent users. Built with Kotlin and Jetpack Compose, it supports secure login, anonymous sessions, calendar-based task tracking, and speech-to-text task capture.

## Features

- 🧠 Trait-based identity intake and guest mode fallback
- 🔐 Secure token and identity storage with encrypted preferences
- 🗓️ Calendar integration with task breakdowns and reminders
- 🎙️ Speech-to-text task capture
- 🧩 Modular architecture with emotionally clear onboarding

## Architecture

- `identity/`: User identity, intake screens, guest mode
- `session/`: Session management, routing, token logic
- `security/`: Encryption, secure storage, integrity checks
- `calendar/`: Calendar UI and scheduling logic
- `task/`: Task classification, scheduling, reminders
- `core/`: App entry, navigation, debug overlay

## Getting Started

1. Clone the repo
2. Open in Android Studio
3. Run on emulator or device

## Security Testing

- Validate encrypted storage integrity
- Simulate session hijack scenarios
- Confirm audit trail consistency
- Test fallback logic for corrupted identity

## License

MIT License