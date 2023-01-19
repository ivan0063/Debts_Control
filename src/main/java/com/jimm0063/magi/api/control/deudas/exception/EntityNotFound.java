package com.jimm0063.magi.api.control.deudas.exception;

public class EntityNotFound extends Exception {
    public EntityNotFound () {
        super("The system couldn't found the entity in the DB");
    }

    public EntityNotFound (String entityName) {
        super("The system couldn't found the " + entityName + " entity in the DB");
    }
}
