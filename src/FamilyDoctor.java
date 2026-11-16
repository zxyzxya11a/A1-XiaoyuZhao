import java.util.ArrayList;

/**
 * A family doctor, also called a general practitioner.
 *
 * <p>A family doctor sees patients in a consulting room and can prescribe
 * medication. This is the general-practitioner role required by the
 * assignment.</p>
 *
 * @author Xiaoyu Zhao
 */
public class FamilyDoctor extends Clinician {

    /** Consulting room used by the doctor. */
    private final String consultingRoom;

    /**
     * Creates a family doctor with explicit working days.
     *
     * @param staffId       numeric staff identifier
     * @param fullName      full name; must not be blank
     * @param consultingRoom consulting room; must not be blank
     * @param workDays      short day names such as "Mon"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public FamilyDoctor(String staffId, String fullName, String consultingRoom,
            ArrayList<String> workDays) {
        super(staffId, fullName, workDays);
        this.consultingRoom = cleanText(consultingRoom, "Consulting room");
    }

    /**
     * Creates a family doctor who works from Monday to Friday.
     *
     * @param staffId       numeric staff identifier
     * @param fullName      full name; must not be blank
     * @param consultingRoom consulting room; must not be blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public FamilyDoctor(String staffId, String fullName, String consultingRoom) {
        super(staffId, fullName);
        this.consultingRoom = cleanText(consultingRoom, "Consulting room");
    }

    /**
     * Returns the clinician's role.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getRole() {
        return "General Practitioner";
    }

    /**
     * Provides a general consultation.
     *
     * @param patientName name of the patient
     * @return a description of the consultation
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String provideService(String patientName) {
        String patient = cleanText(patientName, "Patient name");
        return getFullName() + " consults with " + patient + " and reviews their treatment.";
    }

    /**
     * Prescribes a medicine for a patient.
     *
     * @param patientName name of the patient
     * @param medicine    name of the medicine
     * @return a description of the prescription
     * @throws IllegalArgumentException if the patient name or medicine is blank
     */
    public String prescribe(String patientName, String medicine) {
        String patient = cleanText(patientName, "Patient name");
        String drug = cleanText(medicine, "Medicine");
        return getFullName() + " prescribes " + drug + " for " + patient + ".";
    }

    /**
     * Returns the consulting room.
     *
     * @return the consulting room
     */
    public String getConsultingRoom() {
        return consultingRoom;
    }

    /**
     * Returns the doctor's extra detail for the staff list.
     *
     * @return the consulting room
     */
    @Override
    protected String getExtraInfo() {
        return consultingRoom;
    }
}
