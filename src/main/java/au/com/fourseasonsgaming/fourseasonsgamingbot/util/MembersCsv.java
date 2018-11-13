package au.com.fourseasonsgaming.fourseasonsgamingbot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MembersCsv {
    private static final String SEPARATOR = ",";
    private static final String REPLACEMENT_SEPARATOR = ";";

    private static final String CSV_JOIN_DATE_TIME = "Join Date/Time";
    private static final String CSV_YEARS_AGO = "Years Ago";
    private static final String CSV_MONTHS_AGO = "Months Ago";
    private static final String CSV_DAYS_AGO = "Days Ago";
    private static final String CSV_HOURS_AGO = "Hours Ago";
    private static final String CSV_EFFECTIVE_NAME = "Effective Name";
    private static final String CSV_NAME = "Name";
    private static final String CSV_DISCRIMINATOR = "Discriminator";

    private final List<String> csvHeaders;
    private final List<Member> members;

    {
        csvHeaders = new ArrayList<>();
        csvHeaders.addAll(Arrays.asList(CSV_JOIN_DATE_TIME, CSV_YEARS_AGO, CSV_MONTHS_AGO, CSV_DAYS_AGO, CSV_HOURS_AGO, CSV_EFFECTIVE_NAME, CSV_NAME, CSV_DISCRIMINATOR));
    }

    public MembersCsv(final List<Member> members) {
        this.members = members;
        Guild guild = members.get(0).getGuild();
        guild.getRoles().forEach(r -> csvHeaders.add(cleanseSeparator(r.getName())));
    }

    private static String cleanseSeparator(String data) {
        return data.replaceAll(SEPARATOR, REPLACEMENT_SEPARATOR);

    }

    public String getCsvHeader() {
        return String.join(SEPARATOR, csvHeaders);
    }

    public String getCsvData() {
        List<String> datas = new ArrayList<>();
        for (Member member : members) {
            MemberCsv csv = new MemberCsv(member);
            datas.add(csv.getCsvLine());
        }
        return String.join("\n", datas);
    }

    public String getCsv() {
        return getCsvHeader() + "\n" + getCsvData();
    }

    class MemberCsv {
        private final int DAYS_IN_YEAR = 365;
        private final int DAYS_IN_MONTH = 30;
        private final int MONTHS_IN_YEAR = 12;

        private final DateTimeFormatter dateTimePattern;
        private final Member member;
        private final List<String> csvData;

        private int totalDays;
        private int totalHours;

        {
            dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            csvData = new ArrayList<>();
            for (int i = 0; i < csvHeaders.size(); i++) {
                csvData.add("");
            }
        }

        public MemberCsv(final Member member) {
            this.member = member;
            this.totalDays = getTotalDays();
            this.totalHours = getTotalHours();
            populateCsvData();
        }

        private void populateCsvData() {
            setJoinDateTime();
            setYearsAgo();
            setMonthsAgo();
            setDaysAgo();
            setHoursAgo();
            setEffectiveName();
            setName();
            setDiscriminator();
            setRoles();
        }

        private void setJoinDateTime() {
            int index = csvHeaders.indexOf(CSV_JOIN_DATE_TIME);
            String joinDateTime = member.getJoinDate().format(dateTimePattern);
            csvData.add(index, joinDateTime);
        }

        private void setYearsAgo() {
            int index = csvHeaders.indexOf(CSV_YEARS_AGO);
            csvData.add(index, getYearsAgoString());
        }

        private void setMonthsAgo() {
            int index = csvHeaders.indexOf(CSV_MONTHS_AGO);
            csvData.add(index, getMonthsAgoString());
        }

        private void setDaysAgo() {
            int index = csvHeaders.indexOf(CSV_DAYS_AGO);
            csvData.add(index, getDaysAgoString());
        }

        private void setHoursAgo() {
            int index = csvHeaders.indexOf(CSV_HOURS_AGO);
            csvData.add(index, getHoursAgoString());
        }

        private void setEffectiveName() {
            int index = csvHeaders.indexOf(CSV_EFFECTIVE_NAME);
            csvData.add(index, cleanseSeparator(member.getEffectiveName()));
        }

        private void setName() {
            int index = csvHeaders.indexOf(CSV_NAME);
            csvData.add(index, cleanseSeparator(member.getUser().getName()));
        }

        private void setDiscriminator() {
            int index = csvHeaders.indexOf(CSV_DISCRIMINATOR);
            csvData.add(index, cleanseSeparator(member.getUser().getDiscriminator()));
        }

        private void setRoles() {
            member.getRoles().forEach(r -> {
                int index = csvHeaders.indexOf(r.getName());
                csvData.add(index, "x");
            });
        }

        private String getYearsAgoString() {
            int years = this.totalDays / DAYS_IN_YEAR;

            return String.valueOf(years);
        }

        private String getMonthsAgoString() {
            int months = (this.totalDays / DAYS_IN_MONTH) % MONTHS_IN_YEAR;

            return String.valueOf(months);
        }

        private String getDaysAgoString() {
            int days = (this.totalDays % DAYS_IN_YEAR) % DAYS_IN_MONTH;

            return String.valueOf(days);
        }

        private String getHoursAgoString() {
            return String.valueOf(this.totalHours);
        }

        // TODO Duplicate code with getTotalHours
        // This calculation could probably be changed to be on method call instead of instantiation
        private int getTotalDays() {
            long joined = member.getJoinDate().toInstant().toEpochMilli();
            long difference = new Date().toInstant().toEpochMilli() - joined;

            int days = (int) TimeUnit.MILLISECONDS.toDays(difference);

            return days;
        }

        // TODO Duplicate code with getTotalDays
        // This calculation could probably be changed to be on method call instead of instantiation
        private int getTotalHours() {
            long joined = member.getJoinDate().toInstant().toEpochMilli();
            long difference = new Date().toInstant().toEpochMilli() - joined;

            int days = (int) TimeUnit.MILLISECONDS.toDays(difference);
            int hours = (int) (TimeUnit.MILLISECONDS.toHours(difference) - TimeUnit.DAYS.toHours(days));

            return hours;
        }

        public String getCsvLine() {
            String data = "";

            for (int i = 0; i < csvData.size(); i++) {
                data += csvData.get(i);
                if (i != csvData.size() - 1) {
                    data += SEPARATOR;
                }
            }

            return data;
        }
    }
}
