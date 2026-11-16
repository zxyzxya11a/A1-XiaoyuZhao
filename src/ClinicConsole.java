import java.util.ArrayList;
import java.util.Scanner;

/**
 * Text menu that lets a user work the clinic booking desk.
 *
 * <p>The menu is only responsible for input and output. All booking rules live
 * in {@link BookingDesk}. Option 1 runs the scripted demonstration required by
 * the assignment; the other options operate on the live desk.</p>
 *
 * @author Xiaoyu Zhao
 */
public class ClinicConsole {

    /** Input reader for the menu. */
    private final Scanner input;

    /** Live booking desk. */
    private final BookingDesk desk;

    /** Clinic day used by the desk. */
    private final ClinicDay day;

    /** Clinicians who can be booked. */
    private final ArrayList<Clinician> team;

    /**
     * Creates a console for a desk, a clinic day and a team.
     *
     * @param desk the booking desk; must not be null
     * @param day  the clinic day; must not be null
     * @param team the clinicians; must not be null
     * @throws IllegalArgumentException if any argument is null
     */
    public ClinicConsole(BookingDesk desk, ClinicDay day, ArrayList<Clinician> team) {
        if (desk == null || day == null || team == null) {
            throw new IllegalArgumentException("The console needs a desk, a clinic day and a team.");
        }
        this.desk = desk;
        this.day = day;
        this.team = team;
        this.input = new Scanner(System.in);
    }

    /**
     * Starts the menu and keeps it running until the user chooses to exit.
     */
    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            String choiceText = readLine("Select: ");
            if (choiceText == null) {
                System.out.println("Input closed. Desk closed.");
                return;
            }

            switch (parseChoice(choiceText)) {
                case 1:
                    AssignmentOne.runGuidedDemo();
                    break;
                case 2:
                    printBookings("All bookings", desk.allBookings());
                    break;
                case 3:
                    bookAppointment();
                    break;
                case 4:
                    showForClinician();
                    break;
                case 5:
                    showForPatient();
                    break;
                case 6:
                    cancelForClinician();
                    break;
                case 7:
                    cancelForPatient();
                    break;
                case 8:
                    showTeam();
                    break;
                case 0:
                    running = false;
                    System.out.println("Desk closed.");
                    break;
                default:
                    System.out.println("[!] Choose a number from 0 to 8.");
                    break;
            }
        }
    }

    /**
     * Prints the main menu.
     */
    private void showMenu() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("RIVERSIDE CLINIC BOOKING DESK");
        System.out.println("------------------------------------------------------------");
        System.out.println("1. Run guided demonstration");
        System.out.println("2. Show all bookings");
        System.out.println("3. Book an appointment");
        System.out.println("4. Find bookings by clinician ID");
        System.out.println("5. Find bookings by patient phone");
        System.out.println("6. Cancel by clinician ID and time");
        System.out.println("7. Cancel by patient phone");
        System.out.println("8. Show clinicians and clinic day");
        System.out.println("0. Exit");
    }

    /**
     * Runs the booking dialog.
     */
    private void bookAppointment() {
        System.out.println();
        System.out.println("BOOK AN APPOINTMENT");
        showClinicianLines();

        String staffId = readLine("Clinician ID: ");
        if (staffId == null) {
            return;
        }
        Clinician clinician = findClinician(staffId);
        if (clinician == null) {
            System.out.println("[!] No clinician with ID " + staffId + ".");
            return;
        }

        if (!printFreeSlots(clinician)) {
            return;
        }

        String name = readLine("Patient name: ");
        if (name == null) {
            return;
        }
        if (name.isEmpty()) {
            System.out.println("[!] Patient name must not be empty.");
            return;
        }
        String phone = readLine("Patient phone (10 digits): ");
        if (phone == null) {
            return;
        }
        if (!phone.matches("\\d{10}")) {
            System.out.println("[!] Phone must contain exactly ten digits.");
            return;
        }
        String time = readLine("Time (HH:mm): ");
        if (time == null) {
            return;
        }

        try {
            Patient patient = new Patient(name, phone);
            AppointmentSlot slot = AppointmentSlot.parse(time);
            String result = desk.book(patient, clinician, slot);
            System.out.println(result);
            if (result.startsWith("[OK]")) {
                printBookings("Bookings for " + phone, desk.bookingsForPatient(phone));
            }
        } catch (IllegalArgumentException error) {
            System.out.println("[!] " + error.getMessage());
        }
    }

    /**
     * Shows bookings for a clinician ID entered by the user.
     */
    private void showForClinician() {
        String staffId = readLine("Clinician ID: ");
        if (staffId == null) {
            return;
        }
        if (findClinician(staffId) == null) {
            System.out.println("[!] No clinician with ID " + staffId + ".");
            return;
        }
        printBookings("Bookings for clinician " + staffId, desk.bookingsForClinician(staffId));
    }

    /**
     * Shows bookings for a patient phone entered by the user.
     */
    private void showForPatient() {
        String phone = readLine("Patient phone (10 digits): ");
        if (phone == null) {
            return;
        }
        if (!phone.matches("\\d{10}")) {
            System.out.println("[!] Phone must contain exactly ten digits.");
            return;
        }
        printBookings("Bookings for phone " + phone, desk.bookingsForPatient(phone));
    }

    /**
     * Cancels a booking by clinician ID and time.
     */
    private void cancelForClinician() {
        String staffId = readLine("Clinician ID: ");
        if (staffId == null) {
            return;
        }
        String time = readLine("Time (HH:mm): ");
        if (time == null) {
            return;
        }

        try {
            AppointmentSlot slot = AppointmentSlot.parse(time);
            String result = desk.cancelByClinician(staffId, slot);
            System.out.println(result);
            if (result.startsWith("[OK]")) {
                printBookings("Updated bookings", desk.allBookings());
            }
        } catch (IllegalArgumentException error) {
            System.out.println("[!] " + error.getMessage());
        }
    }

    /**
     * Cancels the earliest booking for a patient phone.
     */
    private void cancelForPatient() {
        String phone = readLine("Patient phone (10 digits): ");
        if (phone == null) {
            return;
        }
        if (!phone.matches("\\d{10}")) {
            System.out.println("[!] Phone must contain exactly ten digits.");
            return;
        }
        String result = desk.cancelByPhone(phone);
        System.out.println(result);
        if (result.startsWith("[OK]")) {
            printBookings("Updated bookings", desk.allBookings());
        }
    }

    /**
     * Shows the clinic day and every clinician.
     */
    private void showTeam() {
        System.out.println();
        System.out.println("CLINIC DAY");
        System.out.println("  " + day + ", " + day.getSlotCount() + " slots");
        System.out.println();
        System.out.println("CLINICIANS");
        showClinicianLines();
    }

    /**
     * Prints one line for every clinician.
     */
    private void showClinicianLines() {
        for (Clinician clinician : team) {
            System.out.println("  " + clinician);
        }
    }

    /**
     * Prints the free slots for a clinician.
     *
     * @param clinician the clinician
     * @return {@code true} if at least one slot is free
     */
    private boolean printFreeSlots(Clinician clinician) {
        ArrayList<AppointmentSlot> free = desk.freeSlots(clinician);
        System.out.println("Free slots for " + clinician.getFullName() + ":");
        if (free.isEmpty()) {
            System.out.println("  (none)");
            return false;
        }

        for (int index = 0; index < free.size(); index++) {
            System.out.print(free.get(index));
            if ((index + 1) % 8 == 0 || index == free.size() - 1) {
                System.out.println();
            } else {
                System.out.print("  ");
            }
        }
        return true;
    }

    /**
     * Finds a clinician in the team by staff ID.
     *
     * @param staffId the staff ID to find
     * @return the clinician, or null if no clinician has that ID
     */
    private Clinician findClinician(String staffId) {
        if (staffId == null) {
            return null;
        }
        for (Clinician clinician : team) {
            if (clinician.getStaffId().equals(staffId)) {
                return clinician;
            }
        }
        return null;
    }

    /**
     * Prints a list of bookings under a heading.
     *
     * @param heading the heading to print
     * @param list    the bookings to print
     */
    private void printBookings(String heading, ArrayList<Booking> list) {
        System.out.println();
        System.out.println(heading);
        if (list.isEmpty()) {
            System.out.println("(no bookings)");
            System.out.println("Total: 0");
            return;
        }

        System.out.println(Booking.TABLE_HEADER);
        System.out.println(Booking.TABLE_RULE);
        for (Booking booking : list) {
            System.out.println(booking.toTableRow());
        }
        System.out.println("Total: " + list.size());
    }

    /**
     * Reads one line from the user.
     *
     * @param prompt the text shown before the input
     * @return the trimmed line, or null if input has ended
     */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            System.out.println();
            return null;
        }
        return input.nextLine().trim();
    }

    /**
     * Converts the menu choice to a number.
     *
     * @param text the text entered by the user
     * @return the number, or -1 if the text is not a number
     */
    private int parseChoice(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException error) {
            return -1;
        }
    }
}
