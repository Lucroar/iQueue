package com.Lucroar.iQueue.Entity;

public enum Status {
    CREATED,
    WAITING,
    SEATED,
    DONE,
    CANCELLED,
    MISSED,

    //Table
    AVAILABLE,
    OCCUPIED,
    DIRTY,
    CLEANING
}
