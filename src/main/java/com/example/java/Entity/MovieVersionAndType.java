package com.example.java.Entity;


public class MovieVersionAndType {
    private String versionIds;   // 可以是逗号分隔的版本ID字符串
    private String typeNames;    // 可以是逗号分隔的类型名称字符串

    // Getters and setters
    public String getVersionIds() {
        return versionIds;
    }

    public void setVersionIds(String versionIds) {
        this.versionIds = versionIds;
    }

    public String getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String typeNames) {
        this.typeNames = typeNames;
    }
}
