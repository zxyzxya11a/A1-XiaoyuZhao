import java.util.ArrayList;

/**
 * A physiotherapist at the clinic.
 *
 * <p>A physiotherapist works in a treatment room and helps patients with
 * movement, strength and rehabilitation.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Physiotherapist extends Clinician {

    /** Treatment room used by the physiotherapist. */
    private final String treatmentRoom;

    /**
     * Creates a physiotherapist with explicit working days.
     *
     * @param staffId       numeric staff identifier
     * @param fullName      full name; must not be blank
     * @param treatmentRoom treatment room; must not be blank
     * @param workDays      short day names such as "Mon"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Physiotherapist(String staffId, String fullName, String treatmentRoom,
            ArrayList<String> workDays) {
        super(staffId, fullName, workDays);
        this.treatmentRoom = cleanText(treatmentRoom, "Treatment room");
    }

    /**
     * Creates a physiotherapist who works from Monday to Friday.
     *
     * @param staffId       numeric staff identifier
     * @param fullName      full name; must not be blank
     * @param treatmentRoom treatment room; must not be blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Physiotherapist(String staffId, String fullName, String treatmentRoom) {
        super(staffId, fullName);
        this.treatmentRoom = cleanText(treatmentRoom, "Treatment room");
    }

    /**
     * Returns the clinician's role.
     *
     * @return "Physiotherapist"
     */
    @Override
    public String getRole() {
        return "Physiotherapist";
    }

    /**
     * Provides a rehabilitation session.
     *
     * @param patientName name of the patient
     * @return a description of the session
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String provideService(String patientName) {
        String patient = cleanText(patientName, "Patient name");
        return getFullName() + " guides " + patient + " through a rehabilitation session.";
    }

    /**
     * Returns the treatment room.
     *
     * @return the treatment room
     */
    public String getTreatmentRoom() {
        return treatmentRoom;
    }

    /**
     * Returns the physiotherapist's extra detail for the staff list.
     *
     * @return the treatment room
     */
    @Override
    protected String getExtraInfo() {
        return treatmentRoom;
    }
}
