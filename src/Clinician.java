import java.util.ArrayList;

/**
 * A health professional who can be booked by the clinic.
 *
 * <p>Every clinician has a numeric staff ID, a full name and a set of working
 * days. The class is abstract because a clinician must have a concrete role.
 * Concrete subclasses provide their role name, their service and any extra
 * detail that should appear in the staff list.</p>
 *
 * @author Xiaoyu Zhao
 */
public abstract class Clinician {

    /** Accepted short names for the days of the week. */
    private static final String[] VALID_DAYS = {
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    };

    /** Numeric staff identifier. */
    private final String staffId;

    /** Clinician's full name. */
    private final String fullName;

    /** Short names of the days on which the clinician works. */
    private final ArrayList<String> workDays;

    /**
     * Creates a clinician who works from Monday to Friday.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @throws IllegalArgumentException if the ID or name is invalid
     */
    protected Clinician(String staffId, String fullName) {
        this(staffId, fullName, defaultDays());
    }

    /**
     * Creates a clinician with an explicit set of working days.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @param workDays short day names such as "Mon"; must not be empty
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected Clinician(String staffId, String fullName, ArrayList<String> workDays) {
        this.staffId = requireId(staffId);
        this.fullName = cleanText(fullName, "Clinician name");
        this.workDays = requireDays(workDays);
    }

    /**
     * Returns the clinician's role, for example "Physiotherapist".
     *
     * @return the role name
     */
    public abstract String getRole();

    /**
     * Describes the service this clinician provides to a patient.
     *
     * @param patientName name of the patient
     * @return a description of the service
     */
    public abstract String provideService(String patientName);

    /**
     * Returns the extra detail shown after the shared clinician information.
     *
     * @return the role-specific detail
     */
    protected abstract String getExtraInfo();

    /**
     * Returns the numeric staff identifier.
     *
     * @return the staff ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * Returns the clinician's full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns a copy of the clinician's working days.
     *
     * @return the working days
     */
    public ArrayList<String> getWorkDays() {
        return new ArrayList<>(workDays);
    }

    /**
     * Returns the working days as one line.
     *
     * @return the day names separated by commas
     */
    public String getDaysText() {
        return String.join(", ", workDays);
    }

    /**
     * Returns a short description used in booking messages.
     *
     * @return the name, role and staff ID
     */
    public String getSummary() {
        return fullName + " (" + getRole() + ", ID " + staffId + ")";
    }

    /**
     * Returns one line containing all clinician details.
     *
     * @return the staff ID, name, role, working days and extra detail
     */
    @Override
    public String toString() {
        return "[" + staffId + "] " + fullName + " | " + getRole()
                + " | " + getDaysText() + " | " + getExtraInfo();
    }

    /**
     * Compares this clinician with another object.
     *
     * <p>Two clinicians are the same member of staff when their staff IDs are
     * equal. The booking clash check uses this rule.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a clinician with the same ID
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Clinician)) {
            return false;
        }
        Clinician that = (Clinician) other;
        return staffId.equals(that.staffId);
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the staff ID
     */
    @Override
    public int hashCode() {
        return staffId.hashCode();
    }

    /**
     * Checks that a text value is present and trims it.
     *
     * @param value the value to check
     * @param label the field name used in the error message
     * @return the trimmed value
     * @throws IllegalArgumentException if the value is null or blank
     */
    protected static String cleanText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " must not be empty.");
        }
        return value.trim();
    }

    /**
     * Checks a numeric staff identifier.
     *
     * @param staffId the identifier to check
     * @return the trimmed identifier
     * @throws IllegalArgumentException if the identifier is not digits only
     */
    private static String requireId(String staffId) {
        if (staffId == null || !staffId.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Staff ID must contain digits only.");
        }
        return staffId.trim();
    }

    /**
     * Checks and normalises a set of working days.
     *
     * @param days the days supplied by the caller
     * @return a new list of valid day names
     * @throws IllegalArgumentException if the list is empty, contains an
     *                                  unknown day or contains a duplicate
     */
    private static ArrayList<String> requireDays(ArrayList<String> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("A clinician must have at least one working day.");
        }

        ArrayList<String> checked = new ArrayList<>();
        for (String day : days) {
            String normalised = normaliseDay(day);
            if (checked.contains(normalised)) {
                throw new IllegalArgumentException("Working days must not contain duplicates.");
            }
            checked.add(normalised);
        }
        return checked;
    }

    /**
     * Converts one day name to its short, correctly capitalised form.
     *
     * @param day the day supplied by the caller
     * @return the matching short day name
     * @throws IllegalArgumentException if the day is missing or unknown
     */
    private static String normaliseDay(String day) {
        if (day == null) {
            throw new IllegalArgumentException("Working day must not be null.");
        }
        String value = day.trim();
        for (String validDay : VALID_DAYS) {
            if (validDay.equalsIgnoreCase(value)) {
                return validDay;
            }
        }
        throw new IllegalArgumentException("Use a short day name such as Mon or Wed.");
    }

    /**
     * Returns the default working days, Monday to Friday.
     *
     * @return a new list containing Mon to Fri
     */
    private static ArrayList<String> defaultDays() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Mon");
        days.add("Tue");
        days.add("Wed");
        days.add("Thu");
        days.add("Fri");
        return days;
    }
}
