package org.acme;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;
import org.acme.grpc.HelloReply;
import org.acme.grpc.HelloRequest;
import org.acme.grpc.HelloServiceGrpc;

@GrpcService
@Singleton
public class HelloGrpcService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello, " + request.getName() + "!")
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloStream(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        for (int i = 1; i <= 5; i++) {
            HelloReply reply = HelloReply.newBuilder()
                    .setMessage("Hello #" + i + ", " + request.getName() + "!")
                    .build();
            responseObserver.onNext(reply);
        }
        responseObserver.onCompleted();
    }
}
