/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.core.ml.inference.trainedmodel;

import org.elasticsearch.Version;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.xpack.core.ml.utils.ExceptionsHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassificationConfig implements InferenceConfig {

    public static final String NAME = "classification";

    public static final String DEFAULT_TOP_CLASSES_RESULT_FIELD = "top_classes";
    public static final ParseField  NUM_TOP_CLASSES = new ParseField("num_top_classes");
    public static final ParseField  TOP_CLASSES_RESULT_FIELD = new ParseField("top_classes_result_field");
    private static final Version MIN_SUPPORTED_VERSION = Version.V_7_6_0;

    public static ClassificationConfig EMPTY_PARAMS = new ClassificationConfig(0, DEFAULT_TOP_CLASSES_RESULT_FIELD);

    private final int numTopClasses;
    private final String topClassesResultsField;

    public static ClassificationConfig fromMap(Map<String, Object> map) {
        Map<String, Object> options = new HashMap<>(map);
        Integer numTopClasses = (Integer)options.remove(NUM_TOP_CLASSES.getPreferredName());
        String topClassesResultsField = (String)options.remove(TOP_CLASSES_RESULT_FIELD.getPreferredName());
        if (options.isEmpty() == false) {
            throw ExceptionsHelper.badRequestException("Unrecognized fields {}.", options.keySet());
        }
        return new ClassificationConfig(numTopClasses, topClassesResultsField);
    }

    public ClassificationConfig(Integer numTopClasses) {
        this(numTopClasses, null);
    }

    public ClassificationConfig(Integer numTopClasses, String topClassesResultsField) {
        this.numTopClasses = numTopClasses == null ? 0 : numTopClasses;
        this.topClassesResultsField = topClassesResultsField == null ? DEFAULT_TOP_CLASSES_RESULT_FIELD : topClassesResultsField;
    }

    public ClassificationConfig(StreamInput in) throws IOException {
        this.numTopClasses = in.readInt();
        this.topClassesResultsField = in.readString();
    }

    public int getNumTopClasses() {
        return numTopClasses;
    }

    public String getTopClassesResultsField() {
        return topClassesResultsField;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeInt(numTopClasses);
        out.writeString(topClassesResultsField);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassificationConfig that = (ClassificationConfig) o;
        return Objects.equals(numTopClasses, that.numTopClasses) && Objects.equals(topClassesResultsField, that.topClassesResultsField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numTopClasses, topClassesResultsField);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (numTopClasses != 0) {
            builder.field(NUM_TOP_CLASSES.getPreferredName(), numTopClasses);
        }
        builder.field(TOP_CLASSES_RESULT_FIELD.getPreferredName(), topClassesResultsField);
        builder.endObject();
        return builder;
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isTargetTypeSupported(TargetType targetType) {
        return TargetType.CLASSIFICATION.equals(targetType);
    }

    @Override
    public Version getMinimalSupportedVersion() {
        return MIN_SUPPORTED_VERSION;
    }

}
