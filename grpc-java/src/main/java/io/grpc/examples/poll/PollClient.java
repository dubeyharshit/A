package io.grpc.examples.poll;
import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */

public class PollClient {
	  private static final Logger logger = Logger.getLogger(PollClient.class.getName());

	  private final ChannelImpl channel;
	  private final GreeterGrpc.GreeterBlockingStub blockingStub;

	  public PollClient(String host, int port) {
	    channel =
	        NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
	            .build();
	    blockingStub = GreeterGrpc.newBlockingStub(channel);
	  }

	  public void shutdown() throws InterruptedException {
	    channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
	  }

	  public void greet(String question, String start_at, String expired_at, String choice) {
	    try {
	      logger.info("Starting Transmission");
	      
	      PollRequest request1 = PollRequest.newBuilder().setQuestion(question).build();
	      PollRequest request2 = PollRequest.newBuilder().setStart_at(start_at).build();
	      PollRequest request3 = PollRequest.newBuilder().setExpired_at(expired_at).build();
	      PollRequest request4 = PollRequest.newBuilder().setChoice(choice).build();

	      PollResponse response = blockingStub.sayPoll(request1,request2,request3,request4);
	      
	      logger.info("Details: " + response.getMessage());

	    } catch (RuntimeException e) {
	      logger.log(Level.WARNING, "RPC failed", e);
	      return;
	    }
	  }

	  public static void main(String[] args) throws Exception {
	    PollClient client = new PollClient("localhost", 50051);
	    try {
	      /* Access a service running on the local machine on port 50051 */
	      //String user = "world";
	      String question = "What type of smartphone do you have?";
	      String started_at = "2015-02-23T13:00:00.000Z";
	      String expired_at = "2015-02-24T13:00:00.000Z";
	      String choice = "Android";
	      
	      if (args.length > 0) {
	        question = args[0]; /* Use the arg as the name to greet if provided */
	        started_at = args[1]; /* Use the arg as the name to greet if provided */
	        expired_at = args[2]; /* Use the arg as the name to greet if provided */
	        choice = args[3]; /* Use the arg as the name to greet if provided */
	      }
	      client.greet(question,started_at,expired_at,choice);

	    } finally {
	      client.shutdown();
	    }
	  }

}