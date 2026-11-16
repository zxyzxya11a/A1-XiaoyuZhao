/**
 * One appointment at the clinic.
 *
 * <p>A booking joins a patient, a clinician and a time slot. The clinician is
 * stored through the common {@link Clinician} type, so a booking can refer to
 * any role. Bookings are ordered by time, then by clinician ID and patient name
 * so that a list is always displayed in a predictable order.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Booking implements Comparable<Booking> {

    /** Column headings used when bookings are printed in a table. */
    public static final String TABLE_HEADER =
            "Time  | Patient            | Phone      | Clinician                    | Role";

    /** Rule printed under the table headings. */
    public static final String TABLE_RULE =
            "------+--------------------+------------+------------------------------+-----";

    /** Patient who made the booking. */
    private final Patient patient;

    /** Clinician who will see the patient. */
    private final Clinician clinician;

    /** Time of the appointment. */
    private final AppointmentSlot slot;

    /**
     * Creates a booking.
     *
     * @param patient   the patient; must not be null
     * @param clinician the clinician; must not be null
     * @param slot      the appointment time; must not be null
     * @throws IllegalArgumentException if any argument is null
     */
    public Booking(Patient patient, Clinician clinician, AppointmentSlot slot) {
        if (patient == null) {
            throw new IllegalArgumentException("A booking needs a patient.");
        }
        if (clinician == null) {
            throw new IllegalArgumentException("A booking needs a clinician.");
        }
        if (slot == null) {
            throw new IllegalArgumentException("A booking needs a time.");
        }
        this.patient = patient;
        this.clinician = clinician;
        this.slot = slot;
    }

    /**
     * Returns the patient.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the clinician.
     *
     * @return the clinician
     */
    public Clinician getClinician() {
        return clinician;
    }

    /**
     * Returns the appointment time.
     *
     * @return the time
     */
    public AppointmentSlot getSlot() {
        return slot;
    }

    /**
     * Returns one row for the booking table.
     *
     * @return the time, patient, phone, clinician and role
     */
    public String toTableRow() {
        return String.format("%-5s | %-18s | %-10s | %-28s | %s",
                slot, patient.getFullName(), patient.getPhone(),
                clinician.getFullName(), clinician.getRole());
    }

    /**
     * Returns the booking in a short readable form.
     *
     * @return the time, patient and clinician
     */
    @Override
    public String toString() {
        return "Booking " + slot + " | " + patient + " | " + clinician.getSummary();
    }

    /**
     * Compares this booking with another booking.
     *
     * @param other the booking to compare with
     * @return a negative number, zero or a positive number if this booking is
     *         earlier than, equal to or later than the other booking
     */
    @Override
    public int compareTo(Booking other) {
        int byTime = slot.compareTo(other.slot);
        if (byTime != 0) {
            return byTime;
        }

        int byClinician = clinician.getStaffId().compareTo(other.clinician.getStaffId());
        if (byClinician != 0) {
            return byClinician;
        }

        // The final comparison only makes equal-time rows stable.
        return patient.getFullName().compareToIgnoreCase(other.patient.getFullName());
    }
}
