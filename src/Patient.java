/**
 * A patient who can book appointments at the clinic.
 *
 * <p>The class keeps the two pieces of patient information required by the
 * booking desk: the patient's name and a ten-digit mobile number. Both fields
 * are final and are checked in the constructor.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Patient {

    /** Patient's full name. */
    private final String fullName;

    /** Patient's mobile number, stored as ten digits. */
    private final String phone;

    /**
     * Creates a patient.
     *
     * @param fullName full name; must not be blank
     * @param phone    mobile number; must contain exactly ten digits
     * @throws IllegalArgumentException if the name or phone is invalid
     */
    public Patient(String fullName, String phone) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name must not be empty.");
        }
        if (phone == null || !phone.trim().matches("\\d{10}")) {
            throw new IllegalArgumentException("Patient phone must contain exactly ten digits.");
        }
        this.fullName = fullName.trim();
        this.phone = phone.trim();
    }

    /**
     * Returns the patient's full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the patient's mobile number.
     *
     * @return the phone number as ten digits
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the patient in a short readable form.
     *
     * @return the name and phone number
     */
    @Override
    public String toString() {
        return fullName + " / " + phone;
    }
}
