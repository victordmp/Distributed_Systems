# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

import noteManagement_pb2 as noteManagement__pb2


class ConnectionMiddlewareStub(object):
    """Interface de serviço
    """

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.Comunication = channel.unary_unary(
                '/ConnectionMiddleware/Comunication',
                request_serializer=noteManagement__pb2.Request.SerializeToString,
                response_deserializer=noteManagement__pb2.Reply.FromString,
                )


class ConnectionMiddlewareServicer(object):
    """Interface de serviço
    """

    def Comunication(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_ConnectionMiddlewareServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'Comunication': grpc.unary_unary_rpc_method_handler(
                    servicer.Comunication,
                    request_deserializer=noteManagement__pb2.Request.FromString,
                    response_serializer=noteManagement__pb2.Reply.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'ConnectionMiddleware', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class ConnectionMiddleware(object):
    """Interface de serviço
    """

    @staticmethod
    def Comunication(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/ConnectionMiddleware/Comunication',
            noteManagement__pb2.Request.SerializeToString,
            noteManagement__pb2.Reply.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)