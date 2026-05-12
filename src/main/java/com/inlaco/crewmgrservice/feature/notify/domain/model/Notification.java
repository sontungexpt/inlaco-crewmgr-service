package com.inlaco.crewmgrservice.feature.notify.domain.model;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationLevel;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationType;
import java.time.Instant;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

/**
 * Represents a notification sent to users in the crew management system.
 *
 * <p>Notifications are used to inform users about important events, updates, and actions that
 * require their attention. Each notification has a specific type, urgency level, and can contain
 * additional structured data in the payload.
 *
 * <p>Notifications track their read status and creation time, providing a complete audit trail of
 * user communications within the system.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Getter
@Builder
public class Notification {

  /** Unique identifier for the notification */
  private String id;

  /** ID of the user who should receive this notification */
  private String recipientId;

  /** Brief title or subject of the notification */
  private String title;

  /** Detailed message content of the notification */
  private String message;

  /** Type/category of the notification (e.g., SYSTEM, CREW_ASSIGNMENT, etc.) */
  private NotificationType type;

  /** Urgency/importance level of the notification */
  private NotificationLevel level;

  /** Additional structured data associated with the notification */
  private NotificationPayload payload;

  /** Whether the notification has been read by the recipient - defaults to false */
  @Default private boolean read = false;

  /** Timestamp when the notification was created */
  private Instant createdAt;
}
