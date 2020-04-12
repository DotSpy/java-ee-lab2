package by.bsuir.jee;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
@Remote(MessageService.class)
public class DefaultMessageService implements MessageService {

  @Inject
  private JMSContext context;

  @Resource(lookup = "java:/queue/MESSAGEQueue")
  private Queue queue;

  @Override
  public void postText(String text) {
    final Destination destination = queue;
    context.createProducer().send(destination, text);
  }
}
