# Project Structure

This document describes the organisation of the project source code and explains the purpose of each major package. The package structure was designed to group components by responsibility making it easier to locate related functionality as the project grows.

---

# Root Package Structure

The application source code is organised into four primary packages:

```
mymaps/
├── activities/
├── coordinators/
├── core/
└── services/
```

| Package        | Purpose                                                         |
| -------------- | --------------------------------------------------------------- |
| `activities`   | User interface screens, fragments and controllers               |
| `coordinators` | Application coordination and business logic                     |
| `core`         | Shared models, interfaces, repositories and reusable components |
| `services`     | External integrations and infrastructure                        |

---

# Activities Package

The `activities` package contains the application's user-facing screens together with their supporting fragments and controllers.

```
activities/
    auth/
    map/
    planner/
```

## auth

Contains the authentication workflow.

Responsibilities include:

* User registration
* User login
* Initial authentication flow

Primary classes:

* `SignInActivity`
* `SignUpActivity`
* `PreMenuLogin`

---

## map

Contains the runtime planning experience.

Responsibilities include:

* Google Maps integration
* Bottom sheet interfaces
* Planner prompt display
* Timeline visualisation
* Map interaction

Important sub-packages include:

| Package       | Purpose                                   |
| ------------- | ----------------------------------------- |
| `controllers` | Toolbar, FAB and Bottom Sheet controllers |
| `fragments`   | Top and Bottom sheet UI                   |
| `behaviour`   | Map browsing modes                        |
| `models`      | View-specific state models                |

The `MapViewActivity` acts as the entry point for the application's live planning functionality.

---

## planner

Contains the planning and configuration interface.

Responsibilities include:

* Weekly planner
* Daily planner
* Task management
* Calendar views
* Planner configuration

The planner package contains multiple feature-oriented sub-packages rather than one large collection of fragments.

---

# Coordinators Package

The coordinator layer contains the application's business logic.

Unlike Activities, coordinators contain very little UI code and instead orchestrate communication between different parts of the application.

```
coordinators/
    scheduling/
    session/
    ui/
    workflows/
```

## scheduling

Implements the scheduling engine.

Key responsibilities include:

* Schedule calculation
* Planner evaluation
* Planner state generation
* Notification dispatch
* Navigation management

Core classes include:

* `SchedulingManager`
* `PlannerEngine`
* `PlannerStateBuilder`
* `ScheduleCalculator`

---

## session

Responsible for maintaining the current planning session.

Responsibilities include:

* Session lifecycle
* Loading active plans
* Exposing LiveData
* Session workflows

---

## ui

Contains coordinators responsible for synchronising application state with the user interface.

Currently this package contains:

* `MapViewCoordinator`

This coordinator receives updates from the scheduling and workflow systems before updating the appropriate UI components.

---

## workflows

Contains reusable workflows for creating entities within the application.

Each workflow encapsulates a complete user interaction, allowing complex creation processes to be reused throughout the application.

---

# Core Package

The `core` package contains functionality shared across the entire application.

Unlike the Activities or Coordinators packages, components within `core` are generally reusable and have few dependencies on higher application layers.

```
core/
    contracts/
    models/
    storage/
    ui/
    utils/
    viewmodels/
```

## contracts

Defines interfaces used to decouple application layers.

Examples include:

* Route services
* Notification services
* Planner interfaces
* UI coordination contracts

---

## models

Contains the application's domain model.

Models are grouped into logical categories including:

* Tasks
* Plans
* Navigation
* Sessions
* Prompts
* Locations

These classes represent the application's core data structures.

---

## storage

Contains repository implementations responsible for persistent data access.

Repositories provide a consistent abstraction over the underlying database.

---

## ui

Contains reusable user interface components.

Examples include:

* TimelineView
* PlannerStateView
* Configuration forms
* Picker dialogs
* Prompt fragments

These components are designed for reuse across multiple screens.

---

## utils

Contains helper classes and factories used throughout the application.

---

## viewmodels

Contains Android ViewModels responsible for exposing repository data to the presentation layer.

Each major entity has an associated ViewModel.

---

# Services Package

The Services package contains implementations of interfaces defined within the `core.contracts` package.

```
services/
    entity_creation/
    routes/
```

These services communicate with external APIs or platform functionality while remaining isolated behind interfaces.

Current services include:

* Google Maps route estimation
* Entity creation

Additional services, such as notification scheduling, may be added in future without affecting higher application layers.

---

# Package Design Principles

The following principles were used when organising the project.

## Feature grouping

Classes are grouped according to application features rather than purely by technical type.

For example, all map-related UI components reside within the `activities.map` package.

---

## Separation of concerns

Business logic is isolated from presentation code.

Activities and fragments focus on rendering the interface while coordinators perform application logic.

---

## Reusable components

Shared functionality is placed within the `core` package wherever practical.

This avoids duplication and promotes consistency across the application.

---

## Interface-driven design

External dependencies are accessed through interfaces defined within the `contracts` package.

This reduces coupling and simplifies testing and future extension.

---

# Future Improvements

Potential improvements to the project structure include:

* Splitting the application into Gradle modules.
* Introducing dependency injection with Hilt.
* Moving scheduling into a standalone library module.
* Adding automated unit tests alongside feature packages.
