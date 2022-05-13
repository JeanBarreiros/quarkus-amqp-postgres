package org.gs;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class PlayerConsumer {

  private final Logger logger = Logger.getLogger(PlayerConsumer.class);

  @Incoming("players-in")
  @Transactional
  @Produces(MediaType.APPLICATION_JSON)
  public CompletionStage<?> receive(Player player) {
    logger.infof("Player: Name: %s, Email: %s, Wallet: %s", player.name, player.email, player.wallet);

    // move to CDI producer
    ManagedExecutor executor = ManagedExecutor.builder()
        .maxAsync(5)
        .propagated(ThreadContext.CDI, ThreadContext.TRANSACTION)
        .build();

    // move to CDI producer
    ThreadContext threadContext = ThreadContext.builder()
        .propagated(ThreadContext.CDI, ThreadContext.TRANSACTION)
        .build();

    return executor.runAsync(threadContext.contextualRunnable(() -> {
      try {
        Player.persist(player);
        if (player.isPersistent()) {
          logger.infof("Added new player: Name: %s, Email: %s, Wallet: %s", player.name, player.email, player.wallet);
        } else {
          logger.infof("Not added Player: Name: %s, Email: %s, Wallet: %s", player.name, player.email, player.wallet);
        }
      } catch (Exception e) {
        logger.error(e);
      }
    }));
  }
}