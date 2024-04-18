package com.lucendar.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucendar.common.gson.ByteArrayBase64Materializer;
import com.lucendar.common.gson.LocalDateMaterializer;
import com.lucendar.common.gson.LocalDateTimeMaterializer;
import com.lucendar.common.gson.LocalTimeMaterializer;
import com.lucendar.common.gson.OffsetDateTimeMaterializer;
import com.lucendar.common.gson.ZoneIdMaterializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class Consts {

    public static GsonBuilder defaultGsonBuilder() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(LocalDate.class, new LocalDateMaterializer())
                .registerTypeHierarchyAdapter(LocalTime.class, new LocalTimeMaterializer())
                .registerTypeHierarchyAdapter(LocalDateTime.class, new LocalDateTimeMaterializer())
                .registerTypeHierarchyAdapter(OffsetDateTime.class, new OffsetDateTimeMaterializer())
                .registerTypeHierarchyAdapter(ZoneId.class, new ZoneIdMaterializer())
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayBase64Materializer())
                ;
    }

    public static final Gson GSON = defaultGsonBuilder().create();
    public static final Gson GSON_PRETTY = defaultGsonBuilder().setPrettyPrinting().create();

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final String LINE_BREAK = System.lineSeparator();
    public static final String LINUX_LINE_BREAK = "\n";
}
