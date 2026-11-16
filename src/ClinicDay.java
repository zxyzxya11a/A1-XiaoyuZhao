import java.util.ArrayList;

/**
 * The fixed opening hours and appointment slots of the clinic.
 *
 * <p>The default day starts at 08:00, ends at 16:00 and uses 30-minute slots.
 * The constructor can be given another opening time, closing time and slot
 * length, and the rest of the program will use the new day unchanged.</p>
 *
 * @author Xiaoyu Zhao
 */
public class ClinicDay {

    /** Time at which the clinic opens. */
    private final AppointmentSlot opensAt;

    /** Time at which the clinic closes. */
    private final AppointmentSlot closesAt;

    /** Length of one appointment slot in minutes. */
    private final int slotMinutes;

    /** All usable slots of the day, in time order. */
    private final ArrayList<AppointmentSlot> slots;

    /**
     * Creates the default clinic day: 08:00 to 16:00 with 30-minute slots.
     */
    public ClinicDay() {
        this(new AppointmentSlot(8, 0), new AppointmentSlot(16, 0), 30);
    }

    /**
     * Creates a clinic day with the supplied opening time, closing time and
     * slot length.
     *
     * @param opensAt     opening time; must not be null
     * @param closesAt    closing time; must not be null
     * @param slotMinutes length of each slot in minutes; must be positive
     * @throws IllegalArgumentException if a time is missing, the opening time
     *                                  is not before the closing time, or the
     *                                  slot length does not fit the day evenly
     */
    public ClinicDay(AppointmentSlot opensAt, AppointmentSlot closesAt, int slotMinutes) {
        if (opensAt == null || closesAt == null) {
            throw new IllegalArgumentException("Opening and closing times are required.");
        }
        if (slotMinutes <= 0) {
            throw new IllegalArgumentException("Slot length must be greater than zero.");
        }

        int start = opensAt.getMinutesAfterMidnight();
        int end = closesAt.getMinutesAfterMidnight();
        if (start >= end) {
            throw new IllegalArgumentException("Opening time must be before closing time.");
        }
        if ((end - start) % slotMinutes != 0) {
            throw new IllegalArgumentException("Slot length must divide the clinic day evenly.");
        }

        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.slotMinutes = slotMinutes;
        this.slots = buildSlots(start, end);
    }

    /**
     * Builds the slots between the opening time and the closing time.
     *
     * @param start first minute of the day
     * @param end   minute at which the clinic closes
     * @return the slots in time order
     */
    private ArrayList<AppointmentSlot> buildSlots(int start, int end) {
        ArrayList<AppointmentSlot> built = new ArrayList<>();
        // The loop follows the constructor values, so another clinic day needs
        // no change in this class.
        for (int minute = start; minute < end; minute += slotMinutes) {
            built.add(AppointmentSlot.fromMinutes(minute));
        }
        return built;
    }

    /**
     * Checks whether a time is one of this day's slots.
     *
     * @param slot the time to check; may be null
     * @return {@code true} if the time is a valid slot
     */
    public boolean contains(AppointmentSlot slot) {
        return slot != null && slots.contains(slot);
    }

    /**
     * Returns a copy of the day's slots.
     *
     * @return the slots in time order
     */
    public ArrayList<AppointmentSlot> getSlots() {
        return new ArrayList<>(slots);
    }

    /**
     * Returns the opening time.
     *
     * @return the opening time
     */
    public AppointmentSlot getOpensAt() {
        return opensAt;
    }

    /**
     * Returns the closing time.
     *
     * @return the closing time
     */
    public AppointmentSlot getClosesAt() {
        return closesAt;
    }

    /**
     * Returns the length of one slot.
     *
     * @return the slot length in minutes
     */
    public int getSlotMinutes() {
        return slotMinutes;
    }

    /**
     * Returns the number of slots in the day.
     *
     * @return the number of slots
     */
    public int getSlotCount() {
        return slots.size();
    }

    /**
     * Returns a short description of the clinic day.
     *
     * @return the opening time, closing time and slot length
     */
    @Override
    public String toString() {
        return opensAt + " to " + closesAt + " (" + slotMinutes + " min slots)";
    }
}
