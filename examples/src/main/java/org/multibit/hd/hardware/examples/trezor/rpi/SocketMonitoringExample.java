package org.multibit.hd.hardware.examples.trezor.rpi;

import com.google.bitcoin.core.AddressFormatException;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.Uninterruptibles;
import org.multibit.hd.hardware.core.HardwareWalletService;
import org.multibit.hd.hardware.core.events.HardwareWalletProtocolEvent;
import org.multibit.hd.hardware.core.events.HardwareWalletSystemEvent;
import org.multibit.hd.hardware.core.messages.SystemMessageType;
import org.multibit.hd.hardware.trezor.BlockingTrezorClient;
import org.multibit.hd.hardware.trezor.SocketTrezorHardwareWallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>Example of communicating with a Raspberry Pi Shield Trezor over a socket:</p>
 * <p>See the README for instructions on how to configure your RPi</p>
 *
 * @since 0.0.1
 *  
 */
public class SocketMonitoringExample {

  private static final Logger log = LoggerFactory.getLogger(SocketMonitoringExample.class);

  /**
   * <p>Main entry point to the example</p>
   *
   * @param args [0]: IP address of RPi unit (e.g. "192.168.0.1"), [1]: Port (e.g. "3000")
   *
   * @throws Exception If something goes wrong
   */
  public static void main(String[] args) throws Exception {

    Preconditions.checkNotNull(args, "Missing arguments for RPi unit.");
    Preconditions.checkState(args.length == 2, "Required arguments [0]: host name or IP, [1]: port.");

    // All the work is done in the class
    SocketMonitoringExample example = new SocketMonitoringExample();

    // Subscribe to hardware wallet events
    HardwareWalletService.hardwareEventBus.register(example);

    example.executeExample(args[0], Integer.parseInt(args[1]));

  }

  /**
   * @param host The host name or IP (e.g. "192.168.0.1")
   * @param port The port (e.g. 3000)
   *
   * @throws java.io.IOException If something goes wrong
   */
  public void executeExample(String host, int port) throws IOException, InterruptedException, AddressFormatException {

    SocketTrezorHardwareWallet wallet = (SocketTrezorHardwareWallet) HardwareWalletService.newSocketInstance(
      SocketTrezorHardwareWallet.class.getName(),
      host,
      port
    );

    // Create a socket-based default Trezor client with blocking methods (quite limited)
    BlockingTrezorClient client = new BlockingTrezorClient(wallet);

    // Connect the client
    while (!client.connect()) {

      Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);

    }

    // Initialize
    client.initialize();

    // Send a ping
    client.ping();

    log.info("Closing connections");

    // Close the connection
    client.disconnect();

    log.info("Exiting");

    // Shutdown
    System.exit(0);

  }

  @Subscribe
  public void onHardwareWalletProtocolEvent(HardwareWalletProtocolEvent event) {

    log.info("Received hardware event: {} {}", event.getMessageType().name(), event.getMessage());


  }

  @Subscribe
  public void onHardwareWalletSystemEvent(HardwareWalletSystemEvent event) {

    if (SystemMessageType.DEVICE_DISCONNECTED.equals(event.getMessageType())) {
      log.error("Device is not connected");
      System.exit(-1);
    }


  }

}
