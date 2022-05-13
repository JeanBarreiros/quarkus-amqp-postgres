package org.gs;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class PlayerProducer {
  
  @Inject
  @Channel("players-out")
  Emitter<Player> emitter;

  public void send(Player player) {
    emitter.send(player);
  }
  
}
