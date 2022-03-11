package org.acme;

import lombok.Data;

@Data
public class Employee {
    private final int id;
    private final String name;
    private final int salary;
    private String observation;
}
