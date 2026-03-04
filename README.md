# 🛡️ UDWALL (Ultimate Defense Wall)

```text
██╗   ██╗██████╗ ██╗    ██╗ █████╗ ██╗     ██╗     
██║   ██║██╔══██╗██║    ██║██╔══██╗██║     ██║     
██║   ██║██║  ██║██║ █╗ ██║███████║██║     ██║     
██║   ██║██║  ██║██║███╗██║██╔══██║██║     ██║     
╚██████╔╝██████╔╝╚███╔███╔╝██║  ██║███████╗███████╗
 ╚═════╝ ╚═════╝  ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝
```

**UDWALL** is a hardened, production-ready Android firewall designed for users who prioritize privacy and zero-leak security. Inspired by the architecture of RethinkDNS, UDWALL operates as a local VPN service to provide a "Default-Deny" network policy without requiring root or Shizuku.

---

## 🚀 Features

- **Default-Deny Architecture**: Every new application is blocked by default. You choose what earns your trust.
- **Pure VpnService**: Operates entirely within the Android `VpnService` framework. No root, no Shizuku, no leaks.
- **Per-App Control**: Granular toggles for every application with real-time icon support.
- **Live Connection Logs**: Monitor blocked connection attempts in real-time with destination IP and port details.
- **Prominent Disclosure**: Fully compliant with Google Play Store policies regarding VpnService usage and user transparency.
- **One-Tap Control**: Stop the firewall and exit the app directly from the notification shade.
- **Modern Splash Screen**: Support for the Android 12+ Splash Screen API for a smooth, high-quality startup.
- **Luxury Monogram Branding**: Custom vector-based luxury branding designed for maximum clarity.

## 🛡️ Security Hardening

- **Encrypted Storage**: All firewall rules and logs are stored in a Room database encrypted with **SQLCipher**.
- **Biometric Protection**: Biometric (Fingerprint/Face) or PIN lock required to disable firewall protection.
- **Digitally Signed**: Production builds are digitally signed as **Udrit** to ensure integrity and authenticity.
- **Zero Analytics**: No trackers, no telemetry, no internet permission for the app itself.
- **Leak Prevention**: Uses a VPN sink architecture to ensure no packet leaves the device without explicit permission.

## 🔴 Firewall Modes

| Mode | Behavior | Use Case |
| :--- | :--- | :--- |
| **Normal (Default-Deny)** | New apps are blocked; whitelisted apps are allowed. | Daily privacy & selective control. |
| **Strict (Block-All)** | **Every single packet is dropped**, ignoring the whitelist. | Emergency lockdown / High-risk environments. |

---

## 🛠️ Tech Stack

- **Language**: 100% Kotlin
- **UI**: Jetpack Compose + Material 3 (Dynamic Colors + Dark Mode)
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Local Storage**: Room + SQLCipher + DataStore (Encrypted)
- **UI Helpers**: Accompanist DrawablePainter for high-quality icon rendering.
- **Networking**: Android VpnService (IP/TCP/UDP Packet Sinking)

## 📂 Project Structure

- `service/`: VpnService implementation and low-level packet handling logic.
- `data/`: Encrypted repositories, DAOs, and Room entities.
- `ui/`: Compose-based screens (Dashboard, Apps List, Logs, Settings).
- `di/`: Hilt modules for dependency injection.
- `receiver/`: System broadcast receivers for Boot and App Exit handling.

---

## ⚙️ Requirements

- Android 8.0 (API 26) or higher.
- No root required.

## 🏗️ Building

1. Clone the repository.
2. Open in Android Studio (Ladybug or newer).
3. Ensure KSP and Hilt plugins are active.
4. **Signing**: Configure your `udrit.jks` in `gradle.properties` for release builds.
5. Build and run the `:app` module.

---

*UDWALL - made by udrit*
