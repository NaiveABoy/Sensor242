package com.jingge.sensorcollect.data;

public class DirectoryEntry {

    private String name;
    private String permission;
    private int mtime;
    private String type;
    private String id;
    private String parent_dir;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setMtime(int mtime) {
        this.mtime = mtime;
    }

    public int getMtime() {
        return mtime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setParent_dir(String parent_dir) {
        this.parent_dir = parent_dir;
    }

    public String getParent_dir() {
        return parent_dir;
    }

}