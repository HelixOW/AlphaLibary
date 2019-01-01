package io.github.alphahelixdev.alpary.utils;

public class Utils {
	
	private static final ArrayUtil ARRAYS = new ArrayUtil();
	private static final LocationUtil LOCATIONS = new LocationUtil();
    private static final MessageUtil MESSAGES = new MessageUtil();
    private static final ScheduleUtil SCHEDULES = new ScheduleUtil();
    private static final SerializationUtil SERIALIZATIONS = new SerializationUtil();
    private static final SkinUtil SKINS = new SkinUtil();
    private static final StringUtil STRINGS = new StringUtil();

    public static ArrayUtil arrays() {
        return ARRAYS;
    }

    public static LocationUtil locations() {
        return LOCATIONS;
    }

    public static MessageUtil messages() {
        return MESSAGES;
    }

    public static ScheduleUtil schedules() {
        return SCHEDULES;
    }

    public static SerializationUtil serializations() {
        return SERIALIZATIONS;
    }

    public static SkinUtil skins() {
        return SKINS;
    }

    public static StringUtil strings() {
        return STRINGS;
    }
}
