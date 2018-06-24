/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class with utilities to work with Jsons
 *
 * @author joaovperin
 */
public class JsonX {

    /** Gson Instance */
    private static final Gson gson;

    static {
        gson = GsonFactory.gson();
    }

    /**
     * Converts a JSON String into a class instance
     *
     * @param json JSON String
     * @param <B> Class type
     * @param clazz Class object to cast
     * @return B Class instance
     */
    public <B> B castTo(String json, Class<B> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * Represents an Object instance as a JSON String
     *
     * @param obj
     * @return String
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Represents an Object instance as a formatted JSON String
     *
     * @param obj
     * @return String
     */
    public String toFormattedJson(Object obj) {
        return gson.newBuilder().
                setPrettyPrinting().create().
                toJson(obj);
    }

    /**
     * Gson Factory
     */
    private static final class GsonFactory {

        /**
         * Returns a GSON Instance
         *
         * @return Gson
         */
        public static Gson gson() {
            return new GsonBuilder().
                    setExclusionStrategies(new GsonExclusionStrategy()).
                    create();
        }

    }

    /**
     * Exclusion strategy
     */
    private static final class GsonExclusionStrategy implements ExclusionStrategy {

        /**
         * Excludes fields annotated with JsonIgnore annotation
         *
         * @param fieldAttributes
         * @return boolean
         */
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            JsonIgnore ann = fieldAttributes.getAnnotation(JsonIgnore.class);
            return ann != null && ann.value();
        }

        /**
         * Excludes classes annotated with JsonIgnore annotation
         *
         * @param type
         * @return boolean
         */
        @Override
        public boolean shouldSkipClass(Class<?> type) {
            return type.isAnnotationPresent(JsonIgnore.class);
        }

    }

    /**
     * Annotation for entities that will not be serialized
     */
    @Target(value = {ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JsonIgnore {

        public boolean value() default true;
    }

}
