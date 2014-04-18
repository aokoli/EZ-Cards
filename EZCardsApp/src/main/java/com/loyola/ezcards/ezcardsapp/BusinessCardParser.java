package com.loyola.ezcards.ezcardsapp;

/**
 * Created by iraziud on 4/11/14.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusinessCardParser {
    private static final String NAME_REGEX = "([a-zA-Z]+([-][a-zA-Z])*[.]?[ ]?)+".replace("(", "(?:");
    private static final String TITLE_REGEX = "([a-zA-Z]+([-][a-zA-Z])*[.]?[ ]?)+".replace("(", "(?:");
    private static final String COMPANY_REGEX = "([0-9a-zA-Z]+([-'/&][0-9a-zA-Z]+)*([. ]|[.,][ ]|[ ]&[ ])?)+".replace("(", "(?:");
    private static final String ADDRESS_REGEX = "(#?[0-9a-zA-Z]+([-'/][0-9a-zA-Z]+)*([. ]|[.,][ ])?)+".replace("(", "(?:");
    private static final String EMAIL_REGEX = "[-+.\\w]+@[-+.\\w]+[.][a-zA-Z]{2,4}";
    private static final String PHONE_REGEX = "[+(0-9][-+.()0-9 ]+[0-9]";

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\n+");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    // regular expression patterns to parse fields; field value will be in group 1
    private static final Pattern NAME_PATTERN = Pattern.compile("(" + NAME_REGEX + ")");
    private static final Pattern TITLE_PATTERN = Pattern.compile("(" + TITLE_REGEX + ")");
    private static final Pattern COMPANY_PATTERN = Pattern.compile("(" + COMPANY_REGEX + ")");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("(" + ADDRESS_REGEX + ")");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[-a-zA-Z]+[:]?[ ])?(" + EMAIL_REGEX + ")");
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(?:[office]|phone|tel|cell|mob|mobile)[:]?[ ](" + PHONE_REGEX + ")",  Pattern.CASE_INSENSITIVE);
    private static final Pattern FAX_PATTERN = Pattern.compile("f(?:ax)?[:]?[ ](" + PHONE_REGEX + ")", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_GENERAL_PATTERN = Pattern.compile("(?:[a-zA-Z]+[:]?[ ])?(" + PHONE_REGEX + ")");

    private static final BusinessCardField[] DEFAULT_FIELDS_ORDER = {
            BusinessCardField.COMPANY, BusinessCardField.NAME, BusinessCardField.TITLE,  BusinessCardField.ADDRESS,
            BusinessCardField.PHONE, BusinessCardField.FAX, BusinessCardField.EMAIL};

    private static final Map<BusinessCardField, Pattern> FIELD_PATTERNS;
    static {
        FIELD_PATTERNS = new HashMap<BusinessCardField, Pattern>();
        FIELD_PATTERNS.put(BusinessCardField.NAME, NAME_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.TITLE, TITLE_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.COMPANY, COMPANY_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.ADDRESS, ADDRESS_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.EMAIL, EMAIL_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.PHONE, PHONE_PATTERN);
        FIELD_PATTERNS.put(BusinessCardField.FAX, FAX_PATTERN);
    }

    private static String normalizeSpace(String str) {
        return WHITESPACE_PATTERN.matcher(str.trim()).replaceAll(" ");
    }

    private static Map<BusinessCardField, String> parseLine(String line) {
        Map<BusinessCardField, String> result = new HashMap<BusinessCardField, String>();
        // find matches for all patterns
        for (BusinessCardField field : FIELD_PATTERNS.keySet()) {
            Matcher matcher = FIELD_PATTERNS.get(field).matcher(line);
            if (matcher.matches()) {
                result.put(field, matcher.group(1));
            }
        }
        if (result.get(BusinessCardField.PHONE) == null && result.get(BusinessCardField.FAX) == null) {
            // try to match for PHONE_GENERAL_PATTERN
            Matcher matcher = PHONE_GENERAL_PATTERN.matcher(line);
            if (matcher.matches()) {
                result.put(BusinessCardField.PHONE, matcher.group(1));
                result.put(BusinessCardField.FAX, matcher.group(1));
            }
        }
        if (result.isEmpty()) {
            // if no match found
            result.put(BusinessCardField.UNDEFINED, line);
        }
        return result;
    }

    private List<Map<BusinessCardField, String>> generalParse;

    public BusinessCard parse(String str, BusinessCardField[] fieldsOrder) {
        // check arguments
        if (str == null) {
            throw new IllegalArgumentException("The input string should not be null.");
        }
        if (fieldsOrder != null) {
            for (BusinessCardField field : fieldsOrder) {
                if (field == null || field == BusinessCardField.UNDEFINED) {
                    throw new IllegalArgumentException("The fieldsOrder argument should not contain null or undefined elements.");
                }
            }
        }
        // split string into lines
        String[] lines = NEWLINE_PATTERN.split(str);
        // initialize generalParse
        generalParse = new ArrayList<Map<BusinessCardField, String>>();
        // parse each line
        for (String line : lines) {
            line = normalizeSpace(line);
            if (!line.isEmpty())
                generalParse.add(parseLine(line));
        }
        // compute fields priorities
        int priority = 1;
        Map<BusinessCardField, Integer> fieldsPriority = new HashMap<BusinessCardField, Integer>();
        if (fieldsOrder != null) {
            for (BusinessCardField field : fieldsOrder) {
                if (fieldsPriority.get(field) != null) continue;
                fieldsPriority.put(field, priority++);
            }
        }
        for (BusinessCardField field : DEFAULT_FIELDS_ORDER) {
            if (fieldsPriority.get(field) != null) continue;
            fieldsPriority.put(field, priority++);
        }
        // combine info into fields & phones objects considering fields order
        Map<BusinessCardField, String> fields = new HashMap<BusinessCardField, String>();
        List<String> phones = new ArrayList<String>();
        for (Map<BusinessCardField, String> parsedLine : generalParse) {
            if (parsedLine.get(BusinessCardField.UNDEFINED) != null) continue;
            // find field with best priority (lowest value)
            int priorityBest = Integer.MAX_VALUE;
            BusinessCardField fieldBest = null;
            for (BusinessCardField field : parsedLine.keySet()) {
                if (fields.get(field) != null) continue; // already assigned
                if (priorityBest > fieldsPriority.get(field)) {
                    priorityBest = fieldsPriority.get(field);
                    fieldBest = field;
                }
            }
            if (fieldBest == null) continue;
            // assign value for found field
            if (fieldBest == BusinessCardField.PHONE) {
                phones.add(parsedLine.get(fieldBest));
            } else {
                fields.put(fieldBest, parsedLine.get(fieldBest));
            }
        }
        // create & populate BusinessCard object
        BusinessCard businessCard = new BusinessCard();
        businessCard.setAddress(fields.get(BusinessCardField.ADDRESS));
        businessCard.setCompany(fields.get(BusinessCardField.COMPANY));
        businessCard.setEmail(fields.get(BusinessCardField.EMAIL));
        businessCard.setFax(fields.get(BusinessCardField.FAX));
        businessCard.setTitle(fields.get(BusinessCardField.TITLE));
        businessCard.setPhones(phones.toArray(new String[0]));
        String[] nameSplited = fields.get(BusinessCardField.NAME).split(" ");
        businessCard.setFirstName(nameSplited[0]);
        if (nameSplited.length > 1) {
            businessCard.setLastName(nameSplited[nameSplited.length - 1]);
        }
        return businessCard;
    }

    public BusinessCard parse(String str) {
        return parse(str, null);
    }

    public List<Map<BusinessCardField, String>> getGeneralParse() {
        return generalParse;
    }
}
