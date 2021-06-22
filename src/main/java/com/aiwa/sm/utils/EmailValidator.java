package com.aiwa.sm.utils;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class EmailValidator implements Predicate<String> {

    private final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    private final Predicate<String> predicate = pattern.asPredicate();

    @Override
    public boolean test(final String email) {
        return predicate.test(email);
    }
}
