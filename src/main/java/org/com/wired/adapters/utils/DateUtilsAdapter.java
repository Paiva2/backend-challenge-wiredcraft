package org.com.wired.adapters.utils;

import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtilsAdapter implements DateUtilsPort {
    @Override
    public String dateToOutputString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
