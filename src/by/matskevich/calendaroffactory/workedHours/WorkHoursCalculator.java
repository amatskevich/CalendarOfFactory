package by.matskevich.calendaroffactory.workedHours;

import by.matskevich.calendaroffactory.Statable;

import java.util.*;

/**
 * Created by kosolapy on 12/9/16.
 */
public class WorkHoursCalculator {

    private final static Map<Integer, List<Integer>> YEARS_HOLIDAYS = new HashMap<Integer, List<Integer>>(10);

    private final static Map<String, Integer> RELIGIOUS_HOLIDAYS = new HashMap<String, Integer>(11);

    static {
        YEARS_HOLIDAYS.put(Calendar.JANUARY, Arrays.asList(1, 7));
        YEARS_HOLIDAYS.put(Calendar.MARCH, Collections.singletonList(8));
        YEARS_HOLIDAYS.put(Calendar.APRIL, Collections.singletonList(31));
        YEARS_HOLIDAYS.put(Calendar.MAY, Arrays.asList(1, 9));
        YEARS_HOLIDAYS.put(Calendar.JULY, Collections.singletonList(3));
        YEARS_HOLIDAYS.put(Calendar.NOVEMBER, Collections.singletonList(7));
        YEARS_HOLIDAYS.put(Calendar.DECEMBER, Arrays.asList(25, 32));
        RELIGIOUS_HOLIDAYS.put("4_2016", 10);
        RELIGIOUS_HOLIDAYS.put("3_2017", 25);
        RELIGIOUS_HOLIDAYS.put("3_2018", 17);
        RELIGIOUS_HOLIDAYS.put("4_2019", 7);
        RELIGIOUS_HOLIDAYS.put("3_2020", 28);
        RELIGIOUS_HOLIDAYS.put("4_2021", 11);
        RELIGIOUS_HOLIDAYS.put("4_2022", 3);
        RELIGIOUS_HOLIDAYS.put("3_2023", 25);
        RELIGIOUS_HOLIDAYS.put("4_2024", 14);
        RELIGIOUS_HOLIDAYS.put("3_2025", 29);
        RELIGIOUS_HOLIDAYS.put("3_2026", 21);
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
                normalHours += holidays.contains(date.get(Calendar.DATE) + 1) ? 7 : 8;
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
        if (RELIGIOUS_HOLIDAYS.containsKey(month + "_" + year))
            result.add(RELIGIOUS_HOLIDAYS.get(month + "_" + year));
        return result;
    }
}
