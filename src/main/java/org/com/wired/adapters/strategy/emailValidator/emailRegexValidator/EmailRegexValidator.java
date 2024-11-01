package org.com.wired.adapters.strategy.emailValidator.emailRegexValidator;

import org.com.wired.adapters.strategy.emailValidator.EmailValidatorStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRegexValidator implements EmailValidatorStrategy {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }
}
