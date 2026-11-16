import java.util.ArrayList;

/**
 * A psychologist at the clinic.
 *
 * <p>A psychologist works in a private office and provides counselling sessions
 * for patients.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Psychologist extends Clinician {

    /** Office used by the psychologist. */
    private final String office;

    /**
     * Creates a psychologist with explicit working days.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @param office   counselling office; must not be blank
     * @param workDays short day names such as "Mon"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Psychologist(String staffId, String fullName, String office, ArrayList<String> workDays) {
        super(staffId, fullName, workDays);
        this.office = cleanText(office, "Office");
    }

    /**
     * Creates a psychologist who works from Monday to Friday.
     *
     * @param staffId  numeric staff identifier
     * @param fullName full name; must not be blank
     * @param office   counselling office; must not be blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Psychologist(String staffId, String fullName, String office) {
        super(staffId, fullName);
        this.office = cleanText(office, "Office");
    }

    /**
     * Returns the clinician's role.
     *
     * @return "Psychologist"
     */
    @Override
    public String getRole() {
        return "Psychologist";
    }

    /**
     * Provides a counselling session.
     *
     * @param patientName name of the patient
     * @return a description of the session
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String provideService(String patientName) {
        String patient = cleanText(patientName, "Patient name");
        return getFullName() + " runs a counselling session with " + patient + ".";
    }

    /**
     * Returns the counselling office.
     *
     * @return the office
     */
    public String getOffice() {
        return office;
    }

    /**
     * Returns the psychologist's extra detail for the staff list.
     *
     * @return the office
     */
    @Override
    protected String getExtraInfo() {
        return office;
    }
}
