package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.grpc.HelloReply;
import org.acme.grpc.HelloRequest;
import org.acme.grpc.HelloServiceGrpc;

/**
 * Registers proto-generated classes from the external grpc-api module for
 * GraalVM native image reflection.
 *
 * WHY THIS IS NEEDED:
 * Quarkus build-time processing only scans the application module. Classes
 * living in a separate module (grpc-api) are not auto-discovered, so GraalVM
 * strips them during native compilation. At runtime this surfaces as:
 *   - Code: Unimplemented / HTTP 404 from grpcurl
 *   - ClassNotFoundException or NoSuchMethodException in native logs
 *
 * TO REPRODUCE THE FAILURE:
 * Delete or comment out this class AND remove reflect-config.json, then build:
 *   ./gradlew :app:build -Dquarkus.package.type=native
 * The binary will start but every gRPC call returns UNIMPLEMENTED.
 *
 * TO VERIFY THE FIX:
 * Keep this class and rebuild. grpcurl calls should resolve correctly.
 */
@RegisterForReflection(targets = {
        HelloRequest.class,
        HelloRequest.Builder.class,
        HelloReply.class,
        HelloReply.Builder.class,
        HelloServiceGrpc.class,
        HelloServiceGrpc.HelloServiceImplBase.class,
        HelloServiceGrpc.HelloServiceBlockingStub.class,
        HelloServiceGrpc.HelloServiceFutureStub.class,
        HelloServiceGrpc.HelloServiceStub.class,
})
public class NativeReflectionConfig {
}
