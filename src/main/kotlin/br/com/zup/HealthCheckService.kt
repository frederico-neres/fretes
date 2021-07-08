package br.com.zup

import grpc.health.v1.HealthGrpc
import grpc.health.v1.HealthOuterClass
import grpc.health.v1.HealthOuterClass.HealthCheckResponse
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class HealthCheckService: HealthGrpc.HealthImplBase() {

    override fun check(
        request: HealthOuterClass.HealthCheckRequest?,
        responseObserver: StreamObserver<HealthCheckResponse>?
    ) {

        responseObserver?.onNext(HealthCheckResponse.newBuilder()
            .setStatus(HealthCheckResponse.ServingStatus.SERVING)
            .build())

        responseObserver?.onCompleted()
    }

    override fun watch(
        request: HealthOuterClass.HealthCheckRequest?,
        responseObserver: StreamObserver<HealthCheckResponse>?
    ) {
        responseObserver?.onNext(HealthCheckResponse.newBuilder()
            .setStatus(HealthCheckResponse.ServingStatus.SERVING)
            .build())

        responseObserver?.onCompleted()
    }
}