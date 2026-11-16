/**
 * A single time of day used by the clinic appointment desk.
 *
 * <p>The time is stored as the number of minutes after midnight. Keeping one
 * integer makes comparison, equality and sorting straightforward.</p>
 *
 * @author Xiaoyu Zhao
 */
public class AppointmentSlot implements Comparable<AppointmentSlot> {

    /** Number of minutes after midnight at which this slot starts. */
    private final int minutesAfterMidnight;

    /**
     * Creates a slot from an hour and a minute.
     *
     * @param hour   hour of the day, from 0 to 23
     * @param minute minute of the hour, from 0 to 59
     * @throws IllegalArgumentException if the hour or minute is out of range
     */
    public AppointmentSlot(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59.");
        }
        this.minutesAfterMidnight = hour * 60 + minute;
    }

    /**
     * Creates a slot from a number of minutes after midnight.
     *
     * @param minutes minutes after midnight, from 0 to 1439
     * @return the matching slot
     * @throws IllegalArgumentException if the number of minutes is out of range
     */
    public static AppointmentSlot fromMinutes(int minutes) {
        if (minutes < 0 || minutes >= 24 * 60) {
            throw new IllegalArgumentException("Minutes after midnight must be between 0 and 1439.");
        }
        return new AppointmentSlot(minutes / 60, minutes % 60);
    }

    /**
     * Reads a time written in 24-hour form, for example "09:30" or "9:30".
     *
     * @param text the text to read
     * @return the matching slot
     * @throws IllegalArgumentException if the text is missing, malformed or not
     *                                  a real time of day
     */
    public static AppointmentSlot parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a time such as 09:30.");
        }

        String value = text.trim();
        int colon = value.indexOf(':');
        if (colon < 1 || colon == value.length() - 1) {
            throw new IllegalArgumentException("Use 24-hour time such as 09:30.");
        }

        int hour;
        int minute;
        try {
            hour = Integer.parseInt(value.substring(0, colon).trim());
            minute = Integer.parseInt(value.substring(colon + 1).trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Use 24-hour time such as 09:30.");
        }

        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("The time must be between 00:00 and 23:59.");
        }
        return new AppointmentSlot(hour, minute);
    }

    /**
     * Returns the number of minutes after midnight.
     *
     * @return minutes after midnight
     */
    public int getMinutesAfterMidnight() {
        return minutesAfterMidnight;
    }

    /**
     * Returns the time in 24-hour form.
     *
     * @return the time, for example "09:30"
     */
    public String getLabel() {
        return String.format("%02d:%02d", minutesAfterMidnight / 60, minutesAfterMidnight % 60);
    }

    /**
     * Returns the time in 24-hour form.
     *
     * @return the time, for example "09:30"
     */
    @Override
    public String toString() {
        return getLabel();
    }

    /**
     * Compares this slot with another slot.
     *
     * @param other the slot to compare with
     * @return a negative number, zero or a positive number if this slot is
     *         earlier than, equal to or later than the other slot
     */
    @Override
    public int compareTo(AppointmentSlot other) {
        return Integer.compare(minutesAfterMidnight, other.minutesAfterMidnight);
    }

    /**
     * Compares this slot with another object.
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a slot at the same time
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AppointmentSlot)) {
            return false;
        }
        AppointmentSlot that = (AppointmentSlot) other;
        return minutesAfterMidnight == that.minutesAfterMidnight;
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this slot
     */
    @Override
    public int hashCode() {
        return minutesAfterMidnight;
    }
}
