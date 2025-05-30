package com.Lucroar.iQueue.Entity;

public enum Status {
    CREATED,
    WAITING,
    //CONFIRMING
    SEATED,
    DONE,
    CANCELLED,
    MISSED,

    //Table
    AVAILABLE,
    CONFIRMING,
    OCCUPIED,
    DIRTY,
    CLEANING
}
