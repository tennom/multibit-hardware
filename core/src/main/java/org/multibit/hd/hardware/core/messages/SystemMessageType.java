package org.multibit.hd.hardware.core.messages;

/**
 * <p>Enum to provide the following to HardwareWallet events:</p>
 * <ul>
 * <li>Provision of event types to allow easy triage in consuming applications</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public enum SystemMessageType {

  /**
   * Device encountered an error not associated with I/O (e.g. thread interrupt due to timeout)
   */
  DEVICE_FAILURE,

  /**
   * Received EOF from device (no data in receive buffer after timeout when some is expected)
   */
  DEVICE_EOF,

  /**
   * Received on a device connect (communications established at the wire level)
   */
  DEVICE_CONNECTED,

  /**
   * Received on a device disconnect (no longer able to communicate)
   */
  DEVICE_DISCONNECTED,

  // End of enum
  ;

}
