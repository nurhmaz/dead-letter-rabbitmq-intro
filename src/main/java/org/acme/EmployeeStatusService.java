package org.acme;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeStatusService {
    private final Emitter<Employee> employeeInfoEmitter;

    public EmployeeStatusService(@Channel("received-employee-info") Emitter<Employee> employeeInfoEmitter) {
        this.employeeInfoEmitter = employeeInfoEmitter;
    }

    public Uni<Employee> processEmployee(Employee employee) {
        return Uni.createFrom()
                .item(employee)
                .onItem()
                .transform(em -> {
                    /*if (em.getSalary() < 0) {
                        throw new RuntimeException("Invalid Salary");
                    }*/
                    if (em.getSalary() < 50000) {
                        em.setObservation("Entry level employee");
                    }
                    if (em.getSalary() >= 50000 && em.getSalary() < 100000) {
                        em.setObservation("Mid level employee");
                    }

                    if (em.getSalary() >= 100000) {
                        em.setObservation("Senior employee");
                    }

                    employeeInfoEmitter.send(em);
                    return em;
                })
                ;
    }
}
