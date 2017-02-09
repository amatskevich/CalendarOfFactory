package by.matskevich.calendaroffactory.workedHours;

import by.matskevich.calendaroffactory.Statable;

import java.util.*;

/**
 * Created by kosolapy on 12/9/16.
 */
public class WorkHoursCalculator {

    private final static Map<Integer, List<Integer>> YEARS_HOLIDAYS = new HashMap<Integer, List<Integer>>(10);

    private final static Map<String, List<Integer>> SPECIAL_HOLIDAYS = new HashMap<String, List<Integer>>(17);

    static {
        YEARS_HOLIDAYS.put(Calendar.JANUARY, Arrays.asList(1, 7));
        YEARS_HOLIDAYS.put(Calendar.MARCH, Collections.singletonList(8));
        YEARS_HOLIDAYS.put(Calendar.APRIL, Collections.singletonList(31));
        YEARS_HOLIDAYS.put(Calendar.MAY, Arrays.asList(1, 9));
        YEARS_HOLIDAYS.put(Calendar.JULY, Collections.singletonList(3));
        YEARS_HOLIDAYS.put(Calendar.NOVEMBER, Collections.singletonList(7));
        YEARS_HOLIDAYS.put(Calendar.DECEMBER, Arrays.asList(25, 32));
        SPECIAL_HOLIDAYS.put("4_2016", Arrays.asList(10, 29)); //Raduniza //Chemical day
        SPECIAL_HOLIDAYS.put("3_2017", Collections.singletonList(25)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2017", Collections.singletonList(28)); //Chemical day
        SPECIAL_HOLIDAYS.put("3_2018", Collections.singletonList(17)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2018", Collections.singletonList(27)); //Chemical day
        SPECIAL_HOLIDAYS.put("4_2019", Arrays.asList(7, 26)); //Raduniza //Chemical day
        SPECIAL_HOLIDAYS.put("3_2020", Collections.singletonList(28)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2020", Collections.singletonList(31)); //Chemical day
        SPECIAL_HOLIDAYS.put("4_2021", Arrays.asList(11, 30)); //Raduniza //Chemical day
        SPECIAL_HOLIDAYS.put("4_2022", Arrays.asList(3, 29)); //Raduniza //Chemical day
        SPECIAL_HOLIDAYS.put("3_2023", Collections.singletonList(25)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2023", Collections.singletonList(28)); //Chemical day
        SPECIAL_HOLIDAYS.put("4_2024", Arrays.asList(14, 26)); //Raduniza //Chemical day
        SPECIAL_HOLIDAYS.put("3_2025", Collections.singletonList(29)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2025", Collections.singletonList(25)); //Chemical day
        SPECIAL_HOLIDAYS.put("3_2026", Collections.singletonList(21)); //Raduniza
        SPECIAL_HOLIDAYS.put("4_2026", Collections.singletonList(31)); //Chemical day
    }

    /**
     * Calculating work's hours
     *
     * @param firstDate - first day of month
     * @param state     - first state of shift
     * @return - worked hours
     */
    public static WorkHoursDto calculate(final Calendar firstDate, Statable state) {
        final int year = firstDate.get(Calendar.YEAR);
        if (year < 2016 || year > 2026) {
            return new WorkHoursDto(false);
        }
        double fullHours = 0;
        double holidayHours = 0;
        double normalHours = 0;
        Calendar date = Calendar.getInstance();
        date.set(year, firstDate.get(Calendar.MONTH), firstDate.get(Calendar.DATE));
        boolean isHoliday;
        int currentMonth = date.get(Calendar.MONTH);
        Set<Integer> holidays = getHolidaysByMonthYear(year, currentMonth);
        while (date.get(Calendar.MONTH) == currentMonth) {

            int day = date.get(Calendar.DAY_OF_WEEK);
            isHoliday = holidays.contains(date.get(Calendar.DATE));
            fullHours += state.getWorkHours();
            if (isHoliday) {
                holidayHours += state.getWorkHours();
            }
            if (!isHoliday && (day >= Calendar.MONDAY) && (day <= Calendar.FRIDAY)) {
                normalHours += holidays.contains(date.get(Calendar.DATE) + 1) ? state.getNormalHours() - 1 : state.getNormalHours();
            }

            date.add(Calendar.DATE, 1);
            state = state.next();
        }
        return new WorkHoursDto(fullHours, holidayHours, normalHours);
    }

    private static Set<Integer> getHolidaysByMonthYear(final int year, final int month) {
        Set<Integer> result = new HashSet<Integer>();
        if (YEARS_HOLIDAYS.containsKey(month))
            result.addAll(YEARS_HOLIDAYS.get(month));
        if (SPECIAL_HOLIDAYS.containsKey(month + "_" + year))
            result.addAll(SPECIAL_HOLIDAYS.get(month + "_" + year));
        return result;
    }
}
