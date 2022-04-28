package com.uit.realestate.dialogflow;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DialogRequest implements Serializable {
    private String responseId;
    private String session;
    private QueryResult queryResult;
    private Map<String, LinkedHashMap> originalDetectIntentRequest;

    @Data
    public class QueryResult{
        private String queryText;
        private Map<String, Object> parameters;
        private boolean allRequiredParamsPresent;
        private String fulfillmentText;
        private Object fulfillmentMessages; // fix
        private Object outputContexts; // fix
        private Object intent; // fix
        private long intentDetectionConfidenceALong;
        private Object diagnosticInfo;
        private String languageCode;
        private String action;
    }
}
