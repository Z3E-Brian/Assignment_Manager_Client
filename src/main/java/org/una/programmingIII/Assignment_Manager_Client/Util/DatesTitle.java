package org.una.programmingIII.Assignment_Manager_Client.Util;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DatesTitle {
public String addWeeklyTitledPanes(LocalDate startDate, LocalDate endDate) {
    return startDate.getDayOfMonth() + " of " + startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " - " +
                    endDate.getDayOfMonth() + " of " + endDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
}




}
