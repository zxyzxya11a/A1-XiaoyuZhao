import java.util.ArrayList;

/**
 * A radiographer at the clinic.
 *
 * <p>A radiographer works in an imaging room and takes X-rays for patients who
 * have been referred by another clinician.</p>
 *
 * @author Xiaoyu Zhao
 */
public class Radiographer extends Clinician {

    /** Imaging room used by the radiographer. */
    private final String imagingRoom;

    /**
     * Creates a radiographer with explicit working days.
     *
     * @param staffId     numeric staff identifier
     * @param fullName    full name; must not be blank
     * @param imagingRoom imaging room; must not be blank
     * @param workDays    short day names such as "Mon"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Radiographer(String staffId, String fullName, String imagingRoom,
            ArrayList<String> workDays) {
        super(staffId, fullName, workDays);
        this.imagingRoom = cleanText(imagingRoom, "Imaging room");
    }

    /**
     * Creates a radiographer who works from Monday to Friday.
     *
     * @param staffId     numeric staff identifier
     * @param fullName    full name; must not be blank
     * @param imagingRoom imaging room; must not be blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Radiographer(String staffId, String fullName, String imagingRoom) {
        super(staffId, fullName);
        this.imagingRoom = cleanText(imagingRoom, "Imaging room");
    }

    /**
     * Returns the clinician's role.
     *
     * @return "Radiographer"
     */
    @Override
    public String getRole() {
        return "Radiographer";
    }

    /**
     * Takes an X-ray for a patient.
     *
     * @param patientName name of the patient
     * @return a description of the imaging service
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String provideService(String patientName) {
        String patient = cleanText(patientName, "Patient name");
        return getFullName() + " takes an X-ray for " + patient + " in the imaging room.";
    }

    /**
     * Returns the imaging room.
     *
     * @return the imaging room
     */
    public String getImagingRoom() {
        return imagingRoom;
    }

    /**
     * Returns the radiographer's extra detail for the staff list.
     *
     * @return the imaging room
     */
    @Override
    protected String getExtraInfo() {
        return imagingRoom;
    }
}
