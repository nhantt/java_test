package com.nals.api.dto.bs;

public enum Status {
    PLANNING(1), DOING(2), COMPLETE (3);

    private int value;

    Status(int value) { this.value = value; }    

    public int getValue() { return value; }

    public static Status parse(int id) {
    	Status right = null; // Default
        for (Status item : Status.values()) {
            if (item.getValue() == id) {
                right = item;
                break;
            }
        }
        return right;
    }
};