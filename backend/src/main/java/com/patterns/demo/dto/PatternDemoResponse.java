package com.patterns.demo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Ответ бэкенда на запрос демонстрации паттерна.
 * Фронт использует поля steps / instances для пиксельной анимации.
 */
public class PatternDemoResponse {

    private String pattern;
    private String variant;       // "good" | "bad"
    private String title;
    private String description;
    private String code;          // исходник примера
    private List<Step> steps = new ArrayList<>();
    private List<InstanceInfo> instances = new ArrayList<>();
    private String verdict;       // короткий вердикт: PASS / FAIL
    private String explanation;   // развернутое объяснение

    public static class Step {
        private long t;            // ms от старта демо
        private String actor;      // кто (например Thread-1)
        private String action;     // что сделал
        private String result;     // итог
        private boolean ok;        // зелёный/красный

        public Step() {}
        public Step(long t, String actor, String action, String result, boolean ok) {
            this.t = t; this.actor = actor; this.action = action;
            this.result = result; this.ok = ok;
        }
        public long getT() { return t; }
        public void setT(long t) { this.t = t; }
        public String getActor() { return actor; }
        public void setActor(String actor) { this.actor = actor; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public boolean isOk() { return ok; }
        public void setOk(boolean ok) { this.ok = ok; }
    }

    public static class InstanceInfo {
        private String hash;
        private String createdBy;
        public InstanceInfo() {}
        public InstanceInfo(String hash, String createdBy) {
            this.hash = hash; this.createdBy = createdBy;
        }
        public String getHash() { return hash; }
        public void setHash(String hash) { this.hash = hash; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    }

    // getters / setters
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }
    public List<InstanceInfo> getInstances() { return instances; }
    public void setInstances(List<InstanceInfo> instances) { this.instances = instances; }
    public String getVerdict() { return verdict; }
    public void setVerdict(String verdict) { this.verdict = verdict; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
