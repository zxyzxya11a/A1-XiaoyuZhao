import java.util.ArrayList;

/**
 * Entry point for the Riverside Clinic appointment desk.
 *
 * <p>Run the class without arguments to open the interactive booking desk. Run
 * it with {@code --demo} to print the scripted demonstration only. The same
 * demonstration is also available from option 1 of the interactive menu.</p>
 *
 * @author Xiaoyu Zhao
 */
public class AssignmentOne {

    /** Line used above and below the main banner. */
    private static final String RULE = "############################################################";

    /**
     * Prevents this entry-point class from being instantiated.
     */
    private AssignmentOne() {
    }

    /**
     * Starts the booking desk or the demonstration.
     *
     * @param args use "--demo" for the scripted demonstration, otherwise the
     *             interactive desk starts
     */
    public static void main(String[] args) {
        printBanner();

        if (args.length > 0 && args[0].equals("--demo")) {
            runGuidedDemo();
            return;
        }

        ClinicDay day = new ClinicDay();
        BookingDesk desk = new BookingDesk(day);
        ArrayList<Clinician> team = createTeam();
        seedDesk(desk, team);

        System.out.println("Clinic day : " + day);
        System.out.println("Sample bookings loaded: " + desk.count());
        System.out.println();

        ClinicConsole console = new ClinicConsole(desk, day, team);
        console.start();
    }

    /**
     * Runs the scripted demonstration required by the assignment.
     *
     * <p>The demonstration uses its own desk so that it always produces the same
     * output and never changes the bookings in the interactive desk.</p>
     */
    public static void runGuidedDemo() {
        ClinicDay day = new ClinicDay();
        BookingDesk desk = new BookingDesk(day);
        ArrayList<Clinician> team = createTeam();

        Patient olivia = new Patient("Olivia Reed", "0411222333");
        Patient jack = new Patient("Jack Turner", "0422333444");
        Patient mia = new Patient("Mia Collins", "0433444555");
        Patient henry = new Patient("Henry Adams", "0444555666");

        System.out.println();
        System.out.println(RULE);
        System.out.println("GUIDED DEMONSTRATION - RIVERSIDE CLINIC");
        System.out.println(RULE);
        System.out.println("Clinic day : " + day);
        System.out.println("Slots      : " + day.getSlotCount());
        System.out.println();
        System.out.println("CLINICIANS");
        for (Clinician clinician : team) {
            System.out.println("  " + clinician);
            System.out.println("    Service: " + clinician.provideService("Olivia Reed"));
        }

        // The first clinician in the team is always the family doctor.
        FamilyDoctor familyDoctor = (FamilyDoctor) team.get(0);
        System.out.println();
        System.out.println("Prescription example: "
                + familyDoctor.prescribe("Olivia Reed", "Amoxicillin"));

        System.out.println();
        System.out.println("BOOKINGS");
        System.out.println(desk.book(olivia, team.get(0), new AppointmentSlot(8, 30)));
        System.out.println(desk.book(jack, team.get(1), new AppointmentSlot(9, 30)));
        System.out.println(desk.book(mia, team.get(2), new AppointmentSlot(10, 30)));
        System.out.println(desk.book(henry, team.get(3), new AppointmentSlot(11, 0)));
        System.out.println(desk.book(olivia, team.get(4), new AppointmentSlot(13, 30)));
        System.out.println(desk.book(jack, team.get(0), new AppointmentSlot(14, 0)));

        System.out.println();
        System.out.println("BOOKING ERRORS");
        System.out.println(desk.book(mia, team.get(0), new AppointmentSlot(8, 30)));
        System.out.println(desk.book(henry, team.get(3), new AppointmentSlot(7, 30)));
        System.out.println(desk.book(null, team.get(1), new AppointmentSlot(15, 0)));

        printBookings("ALL BOOKINGS (SORTED BY TIME)", desk.allBookings());
        printBookings("BOOKINGS FOR CLINICIAN 1101", desk.bookingsForClinician("1101"));
        printBookings("BOOKINGS FOR PHONE 0411222333", desk.bookingsForPatient("0411222333"));

        System.out.println();
        System.out.println("CANCELLATIONS");
        System.out.println(desk.cancelByClinician("1101", new AppointmentSlot(14, 0)));
        System.out.println(desk.cancelByPhone("0411222333"));
        System.out.println(desk.cancelByClinician("1101", new AppointmentSlot(14, 0)));
        System.out.println(desk.cancelByPhone("0400000000"));

        printBookings("FINAL BOOKINGS (SORTED BY TIME)", desk.allBookings());
        System.out.println();
        System.out.println("Demonstration complete.");
    }

    /**
     * Creates the clinic team.
     *
     * @return five clinicians of different roles
     */
    private static ArrayList<Clinician> createTeam() {
        ArrayList<Clinician> team = new ArrayList<>();
        team.add(new FamilyDoctor("1101", "Dr Noah Bennett", "Room 3"));
        team.add(new Physiotherapist("2202", "Ava Brooks", "Physio 2",
                days("Mon", "Wed", "Fri")));
        team.add(new Pharmacist("3303", "Liam Chen", "Pharmacy counter",
                days("Tue", "Thu")));
        team.add(new Radiographer("4404", "Sophia Patel", "X-ray 1",
                days("Mon", "Tue", "Thu")));
        team.add(new Psychologist("5505", "Dr Ethan Moore", "Counselling 1",
                days("Wed", "Fri")));
        return team;
    }

    /**
     * Adds a few sample bookings to the interactive desk.
     *
     * @param desk the booking desk
     * @param team the clinic team
     */
    private static void seedDesk(BookingDesk desk, ArrayList<Clinician> team) {
        desk.book(new Patient("Olivia Reed", "0411222333"), team.get(0),
                new AppointmentSlot(8, 30));
        desk.book(new Patient("Jack Turner", "0422333444"), team.get(1),
                new AppointmentSlot(9, 30));
        desk.book(new Patient("Mia Collins", "0433444555"), team.get(2),
                new AppointmentSlot(10, 30));
    }

    /**
     * Prints the application banner.
     */
    private static void printBanner() {
        System.out.println();
        System.out.println(RULE);
        System.out.println("  RIVERSIDE CLINIC - APPOINTMENT DESK");
        System.out.println("  PROG2004 Assignment 1 - Xiaoyu Zhao");
        System.out.println(RULE);
    }

    /**
     * Prints a list of bookings in a table.
     *
     * @param heading the heading to print
     * @param bookings the bookings to print
     */
    private static void printBookings(String heading, ArrayList<Booking> bookings) {
        System.out.println();
        System.out.println(heading);
        if (bookings.isEmpty()) {
            System.out.println("(no bookings)");
            System.out.println("Total: 0");
            return;
        }

        System.out.println(Booking.TABLE_HEADER);
        System.out.println(Booking.TABLE_RULE);
        for (Booking booking : bookings) {
            System.out.println(booking.toTableRow());
        }
        System.out.println("Total: " + bookings.size());
    }

    /**
     * Creates a list of working days.
     *
     * @param names the short day names
     * @return a list containing the supplied days
     */
    private static ArrayList<String> days(String... names) {
        ArrayList<String> days = new ArrayList<>();
        for (String name : names) {
            days.add(name);
        }
        return days;
    }
}
