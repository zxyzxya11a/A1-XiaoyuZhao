import java.util.ArrayList;

/**
 * A pharmacist at the clinic.
 *
 * <p>A pharmacist works at the pharmacy counter and reviews the medication that
 * a patient is using.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Pharmacist extends Clinician {

    /** Counter at which the pharmacist works. */
    private final String counter;

    /**
     * Creates a pharmacist with explicit working days.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @param counter  pharmacy counter; must not be blank
     * @param workDays short day names such as "Mon"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Pharmacist(String staffId, String fullName, String counter, ArrayList<String> workDays) {
        super(staffId, fullName, workDays);
        this.counter = cleanText(counter, "Pharmacy counter");
    }

    /**
     * Creates a pharmacist who works from Monday to Friday.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @param counter  pharmacy counter; must not be blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Pharmacist(String staffId, String fullName, String counter) {
        super(staffId, fullName);
        this.counter = cleanText(counter, "Pharmacy counter");
    }

    /**
     * Returns the clinician's role.
     *
     * @return "Pharmacist"
     */
    @Override
    public String getRole() {
        return "Pharmacist";
    }

    /**
     * Reviews a patient's medication.
     *
     * @param patientName name of the patient
     * @return a description of the medication review
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String provideService(String patientName) {
        String patient = cleanText(patientName, "Patient name");
        return getFullName() + " reviews " + patient + "'s medication at the pharmacy counter.";
    }

    /**
     * Returns the pharmacy counter.
     *
     * @return the counter
     */
    public String getCounter() {
        return counter;
    }

    /**
     * Returns the pharmacist's extra detail for the staff list.
     *
     * @return the pharmacy counter
     */
    @Override
    protected String getExtraInfo() {
        return counter;
    }
}
