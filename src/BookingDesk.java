import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds the bookings for one clinic day and applies the booking rules.
 *
 * <p>Bookings are kept in an {@link ArrayList}. The list is small, bookings are
 * added and removed during the day, and the insertion order is useful when the
 * desk is displayed. Read-only views return sorted copies so that showing a
 * list never changes the stored order.</p>
 *
 * <p>Every booking and cancellation returns a short message. A message starting
 * with "[OK]" describes a completed action; "[!]" describes an action that was
 * refused.</p>
 *
 * @author Xiaoyu Zhao
 */
public class BookingDesk {

    /** All bookings for the day. */
    private final ArrayList<Booking> bookings;

    /** The clinic day used to validate appointment times. */
    private final ClinicDay clinicDay;

    /**
     * Creates an empty booking desk for the supplied clinic day.
     *
     * @param clinicDay the clinic day; must not be null
     * @throws IllegalArgumentException if the clinic day is null
     */
    public BookingDesk(ClinicDay clinicDay) {
        if (clinicDay == null) {
            throw new IllegalArgumentException("A booking desk needs a clinic day.");
        }
        this.clinicDay = clinicDay;
        this.bookings = new ArrayList<>();
    }

    /**
     * Books a patient with a clinician at a time.
     *
     * @param patient   the patient; may be null
     * @param clinician the clinician; may be null
     * @param slot      the appointment time; may be null
     * @return a confirmation or a refusal message
     */
    public String book(Patient patient, Clinician clinician, AppointmentSlot slot) {
        if (patient == null || clinician == null || slot == null) {
            return "[!] A patient, clinician and time are required.";
        }
        if (!clinicDay.contains(slot)) {
            return "[!] " + slot + " is outside the clinic day. " + clinicDay + ".";
        }
        if (isTaken(clinician, slot)) {
            return "[!] " + clinician.getFullName() + " already has a booking at " + slot + ".";
        }

        bookings.add(new Booking(patient, clinician, slot));
        return "[OK] Booked " + patient.getFullName() + " with "
                + clinician.getFullName() + " at " + slot + ".";
    }

    /**
     * Checks whether a clinician already has a booking at a time.
     *
     * @param clinician the clinician; may be null
     * @param slot      the time; may be null
     * @return {@code true} if the clinician is already booked then
     */
    public boolean isTaken(Clinician clinician, AppointmentSlot slot) {
        if (clinician == null || slot == null) {
            return false;
        }
        for (Booking booking : bookings) {
            // Clinician.equals compares staff IDs, so the same person is found
            // even if a different object is used.
            if (booking.getClinician().equals(clinician) && booking.getSlot().equals(slot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cancels a clinician's booking at a time.
     *
     * @param staffId the clinician's staff ID
     * @param slot    the appointment time
     * @return a confirmation or a refusal message
     */
    public String cancelByClinician(String staffId, AppointmentSlot slot) {
        if (staffId == null || staffId.trim().isEmpty() || slot == null) {
            return "[!] A clinician ID and a time are required.";
        }

        String id = staffId.trim();
        for (int index = 0; index < bookings.size(); index++) {
            Booking booking = bookings.get(index);
            if (booking.getClinician().getStaffId().equals(id) && booking.getSlot().equals(slot)) {
                bookings.remove(index);
                return "[OK] Cancelled " + booking.getPatient().getFullName()
                        + "'s booking with " + booking.getClinician().getFullName()
                        + " at " + slot + ".";
            }
        }
        return "[!] No booking found for clinician " + id + " at " + slot + ".";
    }

    /**
     * Cancels the earliest booking that belongs to a phone number.
     *
     * <p>If the phone number has more than one booking, only the earliest one is
     * cancelled. The message states how many other bookings remain.</p>
     *
     * @param phone the patient's ten-digit phone number
     * @return a confirmation or a refusal message
     */
    public String cancelByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "[!] A patient phone number is required.";
        }

        String number = phone.trim();
        Booking earliest = null;
        for (Booking booking : bookings) {
            if (booking.getPatient().getPhone().equals(number)) {
                if (earliest == null || booking.getSlot().compareTo(earliest.getSlot()) < 0) {
                    earliest = booking;
                }
            }
        }

        if (earliest == null) {
            return "[!] No booking found for phone " + number + ".";
        }

        bookings.remove(earliest);
        int remaining = countForPhone(number);
        String message = "[OK] Cancelled " + earliest.getPatient().getFullName()
                + "'s earliest booking at " + earliest.getSlot() + ".";
        if (remaining > 0) {
            message += " " + remaining
                    + (remaining == 1 ? " other booking remains." : " other bookings remain.");
        }
        return message;
    }

    /**
     * Returns every booking, sorted by time.
     *
     * @return a sorted copy of the bookings
     */
    public ArrayList<Booking> allBookings() {
        ArrayList<Booking> sorted = new ArrayList<>(bookings);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Returns the bookings for one clinician, sorted by time.
     *
     * @param staffId the clinician's staff ID
     * @return a sorted copy of the matching bookings
     */
    public ArrayList<Booking> bookingsForClinician(String staffId) {
        ArrayList<Booking> matches = new ArrayList<>();
        if (staffId == null) {
            return matches;
        }

        String id = staffId.trim();
        for (Booking booking : bookings) {
            if (booking.getClinician().getStaffId().equals(id)) {
                matches.add(booking);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    /**
     * Returns the bookings for one phone number, sorted by time.
     *
     * @param phone the patient's phone number
     * @return a sorted copy of the matching bookings
     */
    public ArrayList<Booking> bookingsForPatient(String phone) {
        ArrayList<Booking> matches = new ArrayList<>();
        if (phone == null) {
            return matches;
        }

        String number = phone.trim();
        for (Booking booking : bookings) {
            if (booking.getPatient().getPhone().equals(number)) {
                matches.add(booking);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    /**
     * Returns the free slots for a clinician.
     *
     * @param clinician the clinician; may be null
     * @return the free slots in time order
     */
    public ArrayList<AppointmentSlot> freeSlots(Clinician clinician) {
        ArrayList<AppointmentSlot> free = new ArrayList<>();
        if (clinician == null) {
            return free;
        }
        for (AppointmentSlot slot : clinicDay.getSlots()) {
            if (!isTaken(clinician, slot)) {
                free.add(slot);
            }
        }
        return free;
    }

    /**
     * Returns the number of bookings on the desk.
     *
     * @return the number of bookings
     */
    public int count() {
        return bookings.size();
    }

    /**
     * Counts the bookings for one phone number.
     *
     * @param phone the phone number to count
     * @return the number of matching bookings
     */
    private int countForPhone(String phone) {
        int count = 0;
        for (Booking booking : bookings) {
            if (booking.getPatient().getPhone().equals(phone)) {
                count++;
            }
        }
        return count;
    }
}
