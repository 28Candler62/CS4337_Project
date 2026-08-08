# CS4337_Project

## Project Assignment Summary

The semester project was a group-based Android application development project completed using Android Studio. Teams of one to three students progressed through four primary stages: Requirement Analysis, Design, Implementation, and Evaluation.

Students selected an application topic, identified the need for the proposed product, and designed its users, features, interface, and overall structure. Prototypes were used to plan the application's layout and functionality before implementation.

During implementation, the application was developed in Android Studio with emphasis on functional user-interface components, appropriate navigation, and compatibility across Android devices and screen sizes. Students were also expected to evaluate their application using an appropriate usability or evaluation method and consider any limitations or restrictions.

The final project deliverables included a project report, source-code package, and presentation video. The report documented the application's motivation, design, implementation, evaluation, limitations, and potential future improvements. Students were also expected to reflect on the development process, including project planning, workload distribution, collaboration, and possible improvements to the development approach.
## Implementation/Deliverable:

# Where To From Here

An Android application for exploring Houston METRO transit stops and planning destinations along a selected transit route.

## Overview

**Where To From Here** combines transit exploration and route planning into a single map-based Android application.

Traditional mapping applications often separate destination searching from transit navigation. This project provides two complementary map views:

1. **Stops Map** — displays nearby Houston METRO stops so users can explore the surrounding transit network.
2. **Route Map** — displays only the stops associated with a selected METRO route, making it easier to identify destinations, restaurants, landmarks, or meeting locations along that route.
3. **Route Selection** — allows users to select a METRO route and update the Route Map with the stops belonging to that route.

The application is intended to help tourists, local residents, commuters, and anyone planning activities along a Houston METRO route. The project report describes the application as a planning and exploration tool that allows users to view the same transit information in different contexts.

## Features

- Explore Houston METRO stops on an interactive Google Map.
- View transit stops in the surrounding area.
- Select a specific METRO route.
- Display only the stops associated with the selected route.
- View route information, including:
    - Route name
    - Route identifier
    - Route type
- Switch between the application's primary views using bottom navigation.
- Pan, zoom, and rotate the Google Map.
- Filter displayed map markers based on the currently visible map area.
- Store transit data locally using SQLite.
- Retrieve route and stop information from the Houston METRO API.

## Screens

### Stops Map

The Stops Map provides an overview of available METRO stops. Users can explore the map and interact with standard Google Maps controls.

### Route Map

The Route Map focuses on the stops belonging to the route selected by the user. Restricting the displayed markers reduces visual clutter and makes it easier to identify locations accessible along a particular route.

### Route Selection

The Route Selection screen provides a searchable/drop-down interface for selecting an available METRO route.

After selecting a route, the application updates the route information and uses the selected route to determine which stops are displayed on the Route Map.

## Technology Stack

| Technology          | Purpose                                |
|---------------------|----------------------------------------|
| Kotlin              | Application programming language       |
| Android             | Application platform                   |
| Jetpack Compose     | Declarative UI framework               |
| Material 3          | UI components and styling              |
| Navigation Compose  | Navigation between application screens |
| Google Maps Compose | Interactive map interface              |
| Google Maps SDK     | Map functionality                      |
| Room                | Local database framework               |
| SQLite              | Local data storage                     |
| Houston METRO API   | Transit route and stop data            |
| Android Studio      | Development environment                |

The application uses Jetpack Compose for its user interface and Navigation Compose for navigation. The main activity uses `NavigationSuiteScaffold`, a `NavHost`, and Compose UI components to construct the application interface.

## Project Dependencies

The project uses the following major dependency versions:

- **Android Gradle Plugin:** 9.3.1
- **Kotlin:** 2.4.10
- **Jetpack Compose BOM:** 2026.06.01
- **Navigation Compose:** 2.9.8
- **Google Maps Compose:** 8.4.0
- **Room:** 3.0.1
- **SQLite:** 2.7.0
- **KSP:** 2.3.10

These versions are defined in `gradle/libs.versions.toml`.

## Architecture

The application uses a Compose-based Android architecture with a `RouteSelectViewModel` responsible for route and stop data.

The main activity:

- Creates the `RouteSelectViewModel`.
- Maintains the navigation controller.
- Retrieves all available map markers.
- Retrieves stops associated with the selected route.
- Determines which markers should be displayed based on the current screen.
- Provides the route-selection interface.
- Renders the Map.

The application uses separate data types for transit information, including `MapMarker` and `MetroRoute`.

## Data Flow

The general data flow is:

```text
Houston METRO API
        |
        v
Local SQLite Database
        |
        v
RouteSelectViewModel
        |
        +--------------------+
        |                    |
        v                    v
  All METRO Stops      Selected Route
        |                    |
        v                    v
   Stops Map             Route Map
```

Transit route and stop information retrieved from the Houston METRO API is stored locally. The application then queries this data when determining which markers and route information should be displayed.

## Google Maps Configuration

The application uses Google Maps through the Maps Compose library.

The manifest expects the Google Maps API key to be supplied through the `MAPS_API_KEY` build variable:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```

### API Key Security

**Do not commit a Google Maps API key directly to the repository.**

The project is configured to reference `MAPS_API_KEY` rather than placing the credential directly in `AndroidManifest.xml`.

When configuring the project locally, provide your own Google Maps API key through the Gradle configuration used by the project.

The Google Maps API key must have the appropriate Google Maps Android APIs enabled for the application.

## Building the Project

### Prerequisites

You will need:

- Android Studio
- An Android SDK installation compatible with the project's Gradle configuration
- A Google Maps API key
- Internet access for downloading Gradle and project dependencies

### Clone the Repository

```bash
git clone https://github.com/28Candler62/CS4337_Project.git
cd CS4337_Project
```

Open the project in Android Studio and allow Gradle to synchronize the project dependencies.

### Configure Google Maps

Configure the project so that the `MAPS_API_KEY` variable resolves to your Google Maps API key.

Do not add the actual key to source-controlled files.

### Build

From Android Studio, select:

**Build → Make Project**

Alternatively, use Gradle from the project directory:

```bash
./gradlew build
```

### Run

Connect an Android device or start an Android emulator, then run the application from Android Studio.

The application launches into the **Stops Map** view.

## Using the Application

### Explore METRO Stops

1. Launch the application.
2. The Stops Map is displayed.
3. Pan and zoom around the map.
4. Explore the available METRO stop markers.

### Select a Route

1. Open the Route Selection screen using the bottom navigation.
2. Select or search for a METRO route.
3. Review the displayed route information.
4. Open the Route Map.
5. The map displays stops associated with the selected route.

### Explore a Route

The Route Map provides a focused view of stops belonging to the selected route. This allows users to consider destinations and meeting locations along the route without displaying the entire set of available transit stops.

## Map Marker Optimization

The application tracks the visible geographic bounds of the Map.

When the map stops moving, the application determines the visible region and filters the available markers so that only markers inside the current map bounds are displayed.

This approach is intended to reduce the number of markers that must be rendered at one time.

## API Integration

### Houston METRO API

The Houston METRO API provides route and transit stop information used by the application.

The API data supplies the locations and attributes required to display transit stops and route information.

API documentation:

https://api-portal.ridemetro.org/api-details

### Google Maps

Google Maps provides the interactive mapping interface used by the application.

The Maps interface supports standard map interactions such as:

- Panning
- Zooming
- Rotating
- Exploring geographic locations

Custom METRO stop markers are displayed on top of the Map.

Google Maps Android SDK documentation:

https://developers.google.com/maps/documentation/android-sdk

Google Maps Compose documentation:

https://developers.google.com/maps/documentation/android-sdk/maps-compose

## Database

The application uses a local SQLite database to store transit information retrieved from the Houston METRO API.

The project includes Room and SQLite dependencies to support local data storage and database access.

SQLite documentation:

https://sqlite.org/docs.html

## Project Structure

The project uses a package structure that separates application components into areas such as:

```text
com.example.projectcs4337
├── data
│   ├── MapMarker
│   └── MetroRoute
├── ui
│   ├── theme
│   └── viewModel
│       └── RouteSelectViewModel
└── MainActivity.kt
```

The exact repository structure may contain additional files and packages not represented in the supplied source files.

## Limitations

The current version has several known limitations:

- A large number of map markers may affect performance on older devices.
- Displaying many markers can make the map difficult to read.
- The application does not currently provide real-time METRO arrival information.
- Routes cannot currently be selected directly by tapping a stop on the overview map.
- Route favorites are not currently implemented.
- Trip recommendations based on a selected route are not currently implemented.

## Future Improvements

Potential future enhancements include:

- Add real-time METRO arrival information.
- Allow users to select routes directly from stops on the overview map.
- Add favorite routes.
- Provide trip recommendations based on the selected route.
- Improve map-marker performance and readability for areas with many transit stops.
- Conduct additional usability testing and refine the interface based on user feedback.

## Evaluation

The project is intended to be evaluated through scenario-based usability testing.

Example tasks include:

- Selecting a METRO route.
- Switching between map views.
- Identifying a potential meeting location along a selected route.

Potential evaluation measures include:

- Percentage of users completing tasks without assistance.
- Task completion time.
- Navigation errors.
- User feedback regarding useful, confusing, or unnecessary features.

The project's primary evaluation goal is to identify usability issues and opportunities for improvement rather than optimize application performance.

## Development Notes

This project was developed as part of a course project and provided experience with:

- Kotlin programming
- Android Studio
- Jetpack Compose
- Android application development
- REST API integration
- Google Maps integration
- Local database management
- User interface design
- Map state management

## References

- Google Maps Platform Documentation — Maps SDK for Android  
  https://developers.google.com/maps/documentation/android-sdk

- Jetpack Compose  
  https://developer.android.com/compose

- Houston METRO API  
  https://api-portal.ridemetro.org/api-details

- Maps Compose Library  
  https://developers.google.com/maps/documentation/android-sdk/maps-compose

- SQLite Documentation  
  https://sqlite.org/docs.html

## License

MIT license