# Clock-in Clock-Out System

A Java-based employee time tracking system for managing clock-in and clock-out records.

## Overview

The Clock-in Clock-Out System is designed to streamline employee time tracking and attendance management. This project was developed as a team project for CS310 (Fall 2023).

## Features

- **Employee Time Tracking**: Clock in and clock out functionality for employees
- **Attendance Records**: Maintain detailed records of employee work hours
- **Time Management**: Track and manage employee schedules

## Project Structure

```
cs310-teamproject-fa23/
├── src/                    # Source code directory
├── test/                   # Test cases directory
├── .gitignore             # Git ignore file
└── README.md              # This file
```

## Technologies Used

- **Language**: Java 100%
- **IDE**: NetBeans (build.xml and manifest.mf configuration included)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- NetBeans IDE (optional, but recommended)
- Git

### Installation

1. Clone the repository:
```bash
git clone https://github.com/BeterMain/cs310-teamproject-fa23.git
cd cs310-teamproject-fa23
```

2. Open the project in NetBeans or compile using:
```bash
javac -d build/classes src/**/*.java
```

### Building the Project

Using NetBeans:
- Open the project and click **Build Project** (or press F11)

Using command line:
```bash
ant build
```

## Running the Application

From NetBeans:
- Right-click the project and select **Run**

Or execute the compiled JAR:
```bash
java -jar dist/ClockInClockOut.jar
```

## Testing

Unit tests are located in the `test/` directory. Run tests using:

```bash
ant test
```

## Project Team

Developed as a team project for CS310 (Fall 2023)

## Contributing

This is a course project. For contributions, please contact the team members.

## License

This project is part of an academic course assignment.

## Notes

- Build artifacts (compiled classes and JARs) are ignored via `.gitignore`
- The project is configured for NetBeans development environment
- macOS-specific files (.DS_Store) are ignored for cross-platform compatibility
