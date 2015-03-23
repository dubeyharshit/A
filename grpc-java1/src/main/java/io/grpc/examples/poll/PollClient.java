package io.grpc.examples.poll;
import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PollClient {
	  private static final Logger logger = Logger.getLogger(PollClient.class.getName());

	  private final ChannelImpl channel;
	  private final PollServiceGrpc.PollServiceBlockingStub blockingStub;

	  public PollClient(String host, int port) {
	    channel =
	        NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
	            .build();
	    blockingStub = PollServiceGrpc.newBlockingStub(channel);
	  }

	  public void shutdown() throws InterruptedException {
	    channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
	  }

	  public void creatingPoll(String moderatorId, String question, String startedAt, String expiredAt, String[] choice) {
	    try {
	      logger.info("Starting Transmission");
	      
	      PollRequest request1 = PollRequest.newBuilder().setModeratorId(moderatorId).build();
	      PollRequest request2 = PollRequest.newBuilder().setQuestion(question).build();
	      PollRequest request3 = PollRequest.newBuilder().setStart_at(startedAt).build();
	      PollRequest request4 = PollRequest.newBuilder().setExpired_at(expiredAt).build();
	      PollRequest request5 = PollRequest.newBuilder().setChoice(choice).build();

	      PollResponse response = blockingStub.createPoll(request1,request2,request3,request4,request5);
	      
	      logger.info("ID " + response.getId());

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
	      	
	    	String moderatorId = "1";
            String question = "What type of smartphone do you have?";
            String startedAt = "2015-03-18T13:00:00.000Z";
            String expiredAt = "2015-03-19T13:00:00.000Z";
            String[] choice = new String[] { "Android", "iPhone" };
	      
	      if (args.length > 0) {
	    	
	    	moderatorId = args[0];
	        question = args[1]; 
	        startedAt = args[2]; 
	        expiredAt = args[3]; 
	        choice[0] = args[4]; 
	        choice[1] = args[5];
	      }
	      client.creatingPoll(moderatorId,question,startedAt,expiredAt,choice);

	    } finally {
	      client.shutdown();
	    }
	  }

}