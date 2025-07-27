package com.tracker.dss.config;

import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

public class StaticParameter {
    public static Map<String, Sinks.Many<String>> sinksMap = new HashMap<>();
    public static Map<String, Long> sinksCount = new HashMap<>();
    public static Map<String, Long> sinksTargetCount = new HashMap<>();
}
