package org.acme;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.faulttolerance.api.ExponentialBackoff;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Data
public class EmployeeInfoConsumer {
    private final Logger log;

    @Incoming("employee-info")
    @Retry(delay = 5000)
    @ExponentialBackoff(maxDelay = 20000)
    @Fallback(fallbackMethod = "fallbackAfterRetry")
    @NonBlocking
    public Uni<CompletionStage<Void>> consume(Message<JsonObject> employeeMessage) {
        return processEmployeeMessage(employeeMessage)
                .onItem()
                .transform(employee -> {
                    if (employee.getSalary() < 0)
                        throw new RuntimeException("Salary cannot be less than 0");
                    return employeeMessage.ack();
                })
                .onFailure()
                .invoke(log::error);
    }

    private Uni<CompletionStage<Void>> fallbackAfterRetry(Message<JsonObject> employeeMessage) {
        return processEmployeeMessage(employeeMessage)
                .onItem()
                .transform(employee -> {
                    if (employee.getSalary() < 0)
                        return employeeMessage.nack(new RuntimeException("Salary cannot be less than 0"));
                    return employeeMessage.ack();
                });
    }

    private Uni<Employee> processEmployeeMessage(Message<JsonObject> employeeMessage) {
        return Uni.createFrom().item(employeeMessage.getPayload())
                .onItem()
                .transform(jsonObject -> jsonObject.mapTo(Employee.class))
                .onItem()
                .invoke(log::info);
    }
}
