# Riverside Clinic Appointment Desk

**PROG2004 Object Oriented Programming - Assignment 1**  
**Author:** Xiaoyu Zhao

A console-based appointment system for a single clinic day, written in plain Java. The project demonstrates object-oriented class design, inheritance, polymorphism, encapsulation and abstraction. It includes an interactive booking desk and a scripted demonstration for assessment.

## Features

- Five clinician roles:
  - General Practitioner (`FamilyDoctor`)
  - Physiotherapist
  - Pharmacist
  - Radiographer
  - Psychologist
- Fixed clinic day from **08:00 to 16:00** with **30-minute slots** (16 slots).
- Book an appointment with any clinician type.
- Reject bookings that:
  - are missing a patient, clinician or time;
  - use a time outside the clinic day;
  - double-book the same clinician at the same time.
- View all bookings sorted from earliest to latest.
- Filter bookings by clinician ID or patient phone number.
- Cancel a booking by clinician ID and time.
- Cancel the earliest booking for a patient phone number, with a count of remaining bookings.
- Clear `[OK]` and `[!]` result messages for successful and rejected actions.
- Interactive console menu for real booking, filtering and cancellation.
- Guided demonstration mode that covers normal cases and error cases.

## Requirements

- JDK 17 or newer.
- No external libraries, frameworks, database or build tool.

The project was compiled with JDK 26 and also verified with `javac --release 17`.

## How to Run

From the project's `src` directory:

```bash
cd src
javac -encoding UTF-8 *.java
java AssignmentOne
```

The program starts with three sample bookings and opens the interactive booking desk.

### Interactive menu

```text
------------------------------------------------------------
RIVERSIDE CLINIC BOOKING DESK
------------------------------------------------------------
1. Run guided demonstration
2. Show all bookings
3. Book an appointment
4. Find bookings by clinician ID
5. Find bookings by patient phone
6. Cancel by clinician ID and time
7. Cancel by patient phone
8. Show clinicians and clinic day
0. Exit
```

After a successful booking, the patient's bookings are printed automatically. After a successful cancellation, the updated booking table is printed automatically, so the change is visible immediately.

### Guided demonstration

To run only the scripted demonstration, from the `src` directory:

```bash
java AssignmentOne --demo
```

The same demonstration is available from option `1` of the interactive menu. It creates the five clinician types, books several appointments, shows double-booking and invalid-slot errors, filters bookings, cancels by clinician/time and by phone, and prints the final state.

## Example Session

```text
Select: 3
BOOK AN APPOINTMENT
  [1101] Dr Noah Bennett | General Practitioner | Mon, Tue, Wed, Thu, Fri | Room 3
  ...
Clinician ID: 1101
Free slots for Dr Noah Bennett:
08:00  09:00  09:30  10:00  10:30  11:00  11:30  12:00
...
Patient name: Alice Brown
Patient phone (10 digits): 0455666777
Time (HH:mm): 12:00
[OK] Booked Alice Brown with Dr Noah Bennett at 12:00.

Bookings for 0455666777
Time  | Patient            | Phone      | Clinician                    | Role
------+--------------------+------------+------------------------------+-----
12:00 | Alice Brown        | 0455666777 | Dr Noah Bennett              | General Practitioner
Total: 1
```

## Project Structure

```text
A1-XiaoyuZhao/
├── README.md
└── src/
    ├── AssignmentOne.java
    ├── AppointmentSlot.java
    ├── Booking.java
    ├── BookingDesk.java
    ├── ClinicConsole.java
    ├── ClinicDay.java
    ├── Clinician.java
    ├── FamilyDoctor.java
    ├── Patient.java
    ├── Pharmacist.java
    ├── Physiotherapist.java
    ├── Psychologist.java
    └── Radiographer.java
```

## Class Responsibilities

| Class | Responsibility |
| --- | --- |
| `AssignmentOne` | Entry point, sample team, sample bookings and guided demonstration. |
| `Clinician` | Abstract base class for staff ID, name, working days and the clinician contract. |
| `FamilyDoctor` | General-practitioner role; consultation and prescribing behaviour. |
| `Physiotherapist` | Physiotherapy role; rehabilitation service. |
| `Pharmacist` | Pharmacy role; medication review service. |
| `Radiographer` | Imaging role; X-ray service. |
| `Psychologist` | Counselling role; therapy service. |
| `Patient` | Patient name and ten-digit mobile number. |
| `AppointmentSlot` | Time of day stored as minutes after midnight, with parsing and ordering. |
| `ClinicDay` | Builds and validates the fixed set of clinic slots. |
| `Booking` | Links a patient, a clinician and a slot; supports time ordering and table output. |
| `BookingDesk` | Stores bookings, applies booking rules, filters, sorts and cancels. |
| `ClinicConsole` | Reads menu input and displays the interactive booking desk. |

## Object-Oriented Design

### Encapsulation

- All model fields are `private` and `final` where possible.
- Constructors validate IDs, names, phone numbers, working days, times and slot lengths.
- `Clinician.getWorkDays()` and `ClinicDay.getSlots()` return copies so callers cannot modify internal state.
- Booking rules are kept inside `BookingDesk` rather than in the console.

### Inheritance

`Clinician` defines the common data and behaviour. The five roles extend it:

```text
Clinician
├── FamilyDoctor
├── Physiotherapist
├── Pharmacist
├── Radiographer
└── Psychologist
```

Each subclass adds its own room or service information and overrides the role, service and extra-detail methods.

### Polymorphism

- `AssignmentOne` stores the team in an `ArrayList<Clinician>`.
- The demonstration calls `getRole()`, `provideService()` and `toString()` through the common `Clinician` type.
- Each object responds with its own role-specific behaviour.

### Abstraction

- `Clinician` is abstract and cannot be instantiated directly.
- `getRole()`, `provideService(String)` and `getExtraInfo()` are abstract, so every concrete role must provide them.
- `Booking` implements `Comparable<Booking>` so a collection of bookings can be sorted by time.

## Business Rules

- Only the current clinic day is modelled; there is no date field.
- The default `ClinicDay` contains slots from 08:00 to 16:00 at 30-minute intervals.
- A clinician cannot be booked twice in the same slot.
- Two clinicians may be booked at the same time.
- `Clinician.equals()` compares staff IDs, so the same clinician is recognised even through a different object reference.
- Cancelling by phone removes the earliest booking for that phone and reports how many other bookings remain.
- All data is held in memory. The program does not save to a file, database or external service.

## Assignment Coverage

| Brief part | Implementation |
| --- | --- |
| Part 1 - Model a health professional | `Clinician` base class with staff ID, name and working days. |
| Part 2 - Specialise professionals | Five concrete roles with unique data and behaviour. |
| Part 3 - Work with professional objects | Team stored and processed through `ArrayList<Clinician>`. |
| Part 4 - Model an appointment | `Patient`, `AppointmentSlot` and `Booking`. |
| Part 5 - Manage appointments | `BookingDesk` with booking rules, filters, sorting and cancellation. |
| Part 6 - Demonstrate the system | Interactive menu and `AssignmentOne --demo`. |

## Verification

From the `src` directory, the project was checked with:

```bash
javac -Xlint:all -encoding UTF-8 *.java
java AssignmentOne --demo
```

The following cases were exercised manually:

- successful booking;
- duplicate booking for the same clinician and time;
- booking outside the clinic day;
- filtering by clinician ID;
- filtering by patient phone;
- cancellation by clinician ID and time;
- cancellation by patient phone with a remaining-booking message;
- cancellation of a booking that does not exist;
- invalid menu input, invalid phone number, invalid time and unknown clinician ID.

## Notes

- The interactive menu is an additional console front end. The scripted demonstration required by the assignment is available with `--demo` and from menu option `1`.
- Compiled `.class` files are not part of the source project and should not be committed to the repository.
- The project uses only Java Standard Edition APIs.
