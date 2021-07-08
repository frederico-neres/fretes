package br.com.zup

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FretesGrpcService: FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun calculaFrete(request: FretesRequest?, responseObserver: StreamObserver<FretesResponse>?) {
        logger.info("Calculando frete para request: $request")

        val cep = request?.cep

        if(cep == null || cep.isBlank()) {
            responseObserver?.onError(Status.INVALID_ARGUMENT
                .withDescription("Cep deve ser informado!")
                .asRuntimeException())
        }

        if(!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())) {
            responseObserver?.onError(Status.INVALID_ARGUMENT
                .withDescription("Cep inválido!")
                .asRuntimeException())
        }

        if(cep.endsWith("333")) {
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("Usuário não pode acessar esse recurso!")
                .addDetails(Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("token expirado").build())).build()

            val status = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(status)
        }

        var valor = 0.0


        try {
            valor = Random.nextDouble(50.00, 250.00)
            if(valor > 100) throw IllegalArgumentException("Erro inesperado de regra de negócio!")

        }catch (ex: Exception) {
            responseObserver?.onError(Status.INVALID_ARGUMENT
                .withDescription(ex.message)
                .asRuntimeException())
        }

        val response = FretesResponse.newBuilder()
            .setCep(cep)
            .setValor(valor)
            .build()

        logger.info("Frete calculado: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}