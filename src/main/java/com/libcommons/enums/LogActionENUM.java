package com.libcommons.enums;

import lombok.Getter;

@Getter
public enum LogActionENUM {
    CREATED(0, "Created"),
    UPDATED(1, "Updated"),
    DEACTIVATED(2, "Deactivated"),
    DELETED(3, "Deleted");

    private final int code;
    private final String description;

    LogActionENUM(int code, String description){
        this.code = code;
        this.description = description;
    }

    public LogActionENUM fromCode(int cd){
        for(LogActionENUM logActionENUM : LogActionENUM.values()){
            if(logActionENUM.code == cd){
                return logActionENUM;
            }
        }
        throw new IllegalArgumentException("No enum found for code: " + cd);
    }

    public LogActionENUM fromDescription(String description) {
        for(LogActionENUM logActionENUM : LogActionENUM.values()){
            if(logActionENUM.description.equalsIgnoreCase(description)) {
                return logActionENUM;
            }
        }
        throw new IllegalArgumentException("No enum found for description: " + description);
    }
}
