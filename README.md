# Aves

<img src=".github/assets/aves.jpg" alt="Aves" width="300"/>

## Overview

Aves is a utility library designed for Minecraft servers running the Minestom server software.  
It provides a collection of tools and utilities to streamline server development, making it easier to manage files,
inventories, resource packs and more.

## Features

Aves offers various utility packages to assist in common server development tasks:

- **File Management**: File operations and GSON serialization
- **Internationalization (i18n)**: Multi-language support for in-game text
- **Inventory System**:
    - Custom inventory handling
    - Pageable inventories
    - Functional inventory components
    - Slot management utilities
- **Item Utilities**: Simplifies item creation and manipulation
- **Map Utilities**: Tools for managing and rendering Minecraft maps
- **Resource Pack Management**: Utilities for handling and deploying resource packs
- **General Utilities**:
    - Collection helpers
    - Exception handling
    - Functional programming utilities
    - Vector manipulation

### Gradle (Kotlin DSL)

Add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("net.theevilreaper.aves:aves:<version>")
}
```

> [!CAUTION]
> The usage of this project is **only** for internal purposes.
> It is **not** intended for public use and should **not** be published to public repositories.