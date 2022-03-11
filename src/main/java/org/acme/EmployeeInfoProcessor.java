package org.acme;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Merge;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZonedDateTime;

@ApplicationScoped
public class EmployeeInfoProcessor {

    @Incoming("received-employee-info")
    @Outgoing("employees")
    @Merge
    public Uni<Message<Employee>> produceEmployeeInfo(Employee employee) {
        return Uni.createFrom().item(Message.of(employee
                , Metadata.of(new OutgoingRabbitMQMetadata
                        .Builder()
                        .withRoutingKey("emplooyee.info")
                        .withTimestamp(ZonedDateTime.now())
                        .build()
                )));
    }
}
