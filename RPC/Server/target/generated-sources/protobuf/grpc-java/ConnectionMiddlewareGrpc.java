import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * Interface de serviço
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: noteManagement.proto")
public final class ConnectionMiddlewareGrpc {

  private ConnectionMiddlewareGrpc() {}

  public static final String SERVICE_NAME = "ConnectionMiddleware";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Request,
      Reply> METHOD_COMUNICATION =
      io.grpc.MethodDescriptor.<Request, Reply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ConnectionMiddleware", "Comunication"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Request.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              Reply.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ConnectionMiddlewareStub newStub(io.grpc.Channel channel) {
    return new ConnectionMiddlewareStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ConnectionMiddlewareBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ConnectionMiddlewareBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ConnectionMiddlewareFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ConnectionMiddlewareFutureStub(channel);
  }

  /**
   * <pre>
   * Interface de serviço
   * </pre>
   */
  public static abstract class ConnectionMiddlewareImplBase implements io.grpc.BindableService {

    /**
     */
    public void comunication(Request request,
        io.grpc.stub.StreamObserver<Reply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMUNICATION, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_COMUNICATION,
            asyncUnaryCall(
              new MethodHandlers<
                Request,
                Reply>(
                  this, METHODID_COMUNICATION)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface de serviço
   * </pre>
   */
  public static final class ConnectionMiddlewareStub extends io.grpc.stub.AbstractStub<ConnectionMiddlewareStub> {
    private ConnectionMiddlewareStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConnectionMiddlewareStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConnectionMiddlewareStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConnectionMiddlewareStub(channel, callOptions);
    }

    /**
     */
    public void comunication(Request request,
        io.grpc.stub.StreamObserver<Reply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMUNICATION, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Interface de serviço
   * </pre>
   */
  public static final class ConnectionMiddlewareBlockingStub extends io.grpc.stub.AbstractStub<ConnectionMiddlewareBlockingStub> {
    private ConnectionMiddlewareBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConnectionMiddlewareBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConnectionMiddlewareBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConnectionMiddlewareBlockingStub(channel, callOptions);
    }

    /**
     */
    public Reply comunication(Request request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMUNICATION, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Interface de serviço
   * </pre>
   */
  public static final class ConnectionMiddlewareFutureStub extends io.grpc.stub.AbstractStub<ConnectionMiddlewareFutureStub> {
    private ConnectionMiddlewareFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConnectionMiddlewareFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConnectionMiddlewareFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConnectionMiddlewareFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Reply> comunication(
        Request request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMUNICATION, getCallOptions()), request);
    }
  }

  private static final int METHODID_COMUNICATION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ConnectionMiddlewareImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ConnectionMiddlewareImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COMUNICATION:
          serviceImpl.comunication((Request) request,
              (io.grpc.stub.StreamObserver<Reply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class ConnectionMiddlewareDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return NoteManagement.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ConnectionMiddlewareGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ConnectionMiddlewareDescriptorSupplier())
              .addMethod(METHOD_COMUNICATION)
              .build();
        }
      }
    }
    return result;
  }
}
