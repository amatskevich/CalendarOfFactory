package by.matskevich.calendaroffactory.workedHours;

/**
 * Created by kosolapy on 12/9/16.
 */
public class WorkHoursDto {

    private final Double fullHours;
    private final Double overHours;
    private final Double holidayHours;
    private final Double normalHours;
    private boolean supported;

    public WorkHoursDto(double fullHours, double holidayHours, double normalHours) {
        this.fullHours = fullHours;
        this.holidayHours = holidayHours;
        this.normalHours = normalHours;
        this.overHours = fullHours - normalHours;
        this.supported = true;
    }

    public WorkHoursDto(boolean supported) {
        this.fullHours = 0.0;
        this.holidayHours = 0.0;
        this.normalHours = 0.0;
        this.overHours = 0.0;
        this.supported = supported;
    }

    public Double getFullHours() {
        return fullHours;
    }

    public Double getOverHours() {
        return overHours;
    }

    public Double getHolidayHours() {
        return holidayHours;
    }

    public Double getNormalHours() {
        return normalHours;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public String getFullHoursText() {
        return fullHours.toString();
    }

    public String getNormalHoursText() {
        return normalHours.toString();
    }

    public String getOverHoursText() {
        return overHours.toString();
    }

    public String getHolidayHoursText() {
        return holidayHours.toString();
    }
}
