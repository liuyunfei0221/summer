package com.blue.gateway.common;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * loadbalancer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class BlueRandomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public BlueRandomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    private static final Function<List<ServiceInstance>, Response<ServiceInstance>> INSTANCE_RESPONSE_GETTER = instances ->
            instances.isEmpty() ? new EmptyResponse() : new DefaultResponse(instances.get(current().nextInt(instances.size())));

    private static final BiFunction<ServiceInstanceListSupplier, List<ServiceInstance>, Response<ServiceInstance>> INSTANCE_RESPONSE_PROCESSOR = (supplier, serviceInstances) -> {
        Response<ServiceInstance> serviceInstanceResponse =
                INSTANCE_RESPONSE_GETTER.apply(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer())
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        return serviceInstanceResponse;
    };


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get()
                .next()
                .map(serviceInstances ->
                        INSTANCE_RESPONSE_PROCESSOR.apply(supplier, serviceInstances));
    }

}
