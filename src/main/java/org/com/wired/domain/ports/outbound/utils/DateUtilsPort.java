package org.com.wired.domain.ports.outbound.utils;

import java.util.Date;

public interface DateUtilsPort {
    String dateToOutputString(Date date);

    String dateTimeToOutputString(Date date);
}
