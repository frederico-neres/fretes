package br.com.zup

import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FretesGrpcService: FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun calculaFrete(request: FretesRequest?, responseObserver: StreamObserver<FretesResponse>?) {
        logger.info("Calculando frete para request: $request")


        val response = FretesResponse.newBuilder()
            .setCep(request?.cep)
            .setValor( Random.nextDouble(50.00, 250.00))
            .build()

        logger.info("Frete calculado: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}