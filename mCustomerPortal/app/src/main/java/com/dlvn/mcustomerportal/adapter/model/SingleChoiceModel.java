package com.dlvn.mcustomerportal.adapter.model;

public class SingleChoiceModel {

    int id;
    String code;
    String name;
    String subname;
    boolean isSelected = false;

    public SingleChoiceModel() {
    }

    public SingleChoiceModel(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public SingleChoiceModel(int id, String code, String name, String subname, boolean isSelect) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.subname = subname;
        this.isSelected = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
