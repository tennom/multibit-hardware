package org.multibit.hd.hardware.core.wallets;

import com.google.protobuf.Message;
import org.multibit.hd.hardware.core.HardwareWalletSpecification;

import java.io.DataInputStream;

/**
 * <p>Interface to provide the following to applications:</p>
 * <ul>
 * <li>Common methods available to hardware wallet devices</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public interface HardwareWallet {

  /**
   * @return The hardware wallet specification in use for this exchange
   */
  HardwareWalletSpecification getSpecification();

  /**
   * @return A default hardware wallet specification to use during the creation process if one is not supplied
   */
  HardwareWalletSpecification getDefaultSpecification();

  /**
   * <p>Apply any hardware wallet specific parameters</p>
   * <p>Implementers should override this, but call super.applySpecification(specification) as part of the application process</p>
   *
   * @param specification The {@link HardwareWalletSpecification}
   */
  void applySpecification(HardwareWalletSpecification specification);

  /**
   * <p>Perform any pre-connection initialisation</p>
   */
  void initialise();

  /**
   * <p>Attempt a connection to the device</p>
   *
   * @return True if the connection was successful
   */
  boolean connect();

  /**
   * <p>Break the connection to the device</p>
   */
  void disconnect();

  /**
   * <p>Send a message to the device using the generated protocol buffer classes</p>
   * <p>Any response will be provided through the event bus subscribers</p>
   * <p>If this call fails the device will be closed and a DISCONNECT message will be emitted</p>
   *
   * @param message A generated protocol buffer message (e.g. Message.Initialize)
   */
  void sendMessage(Message message);

  /**
   * <p>Convert the protocol message(s) in the data stream to events</p>
   *
   * @param in The data input stream from the device
   */
  boolean adaptProtocolMessageToEvents(DataInputStream in);

  // TODO consider a session ID for multiple attached devices and event separation


}
