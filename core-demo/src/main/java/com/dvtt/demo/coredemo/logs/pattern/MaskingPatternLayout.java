package com.dvtt.demo.coredemo.logs.pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linhtn on 1/24/2022.
 */
@Component
public class MaskingPatternLayout extends PatternLayout {

    private String patternsProperty;
    private Optional<Pattern> pattern;

    public String getPatternsProperty() {
        return patternsProperty;
    }

    public void setPatternsProperty(String patternsProperty) {
        this.patternsProperty = patternsProperty;
        if (this.patternsProperty != null) {
            this.pattern = Optional.of(Pattern.compile(patternsProperty, Pattern.MULTILINE));
        } else {
            this.pattern = Optional.empty();
        }
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        final StringBuilder message = new StringBuilder(super.doLayout(event));

        if (pattern.isPresent()) {
            Matcher matcher = pattern.get().matcher(message);
            while (matcher.find()) {

                int group = 1;
                while (group <= matcher.groupCount()) {
                    if (matcher.group(group) != null) {
                        final int startGrpIndex = matcher.start(group);
                        final int endGrpIndex = matcher.end(group);
                        final int diff = endGrpIndex - startGrpIndex + 1;
                        int startIndex = startGrpIndex + diff;
                        final int endIndex1 = message.indexOf(",", startIndex);
                        final int endIndex2 = message.indexOf(" ", startIndex);
                        final int endIndex3 = message.indexOf(")", startIndex);
                        final int endIndex4 = message.indexOf("\n", startIndex);

                        final int endIndex = getSmallestInt(
                                Arrays.asList(endIndex1, endIndex2, endIndex3, endIndex4));
                        if (endIndex <= 0) {
                            continue;
                        }

                        for (int i = startIndex; i < endIndex; i++) {
                            message.setCharAt(i, '*');
                        }
                    }
                    group++;
                }
            }
        }
        return message.toString();
    }

    private Integer getSmallestInt(List<Integer> integerList) {

        return integerList.stream().filter(integer -> integer > 0).reduce((x, y) -> x < y ? x : y).get();
    }

}
