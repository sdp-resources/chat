import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ChatMessage {
  private final ChatChannel channel;
  private final String userName;
  private final String text;
  private final LocalDateTime timestamp;

  ChatMessage(ChatChannel channel, String userName, String text, LocalDateTime timestamp) {
    this.channel = channel;
    this.userName = userName;
    this.text = text;
    this.timestamp = timestamp;
  }

  ChatChannel getChannel() {
    return channel;
  }

  String getUserName() {
    return userName;
  }

  String getText() {
    return text;
  }

  LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getTime() {
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(timestamp, now);
    if (duration.isNegative() || duration.isZero()) {
      return "just now";
    }
    if (duration.toSeconds() < 60) {
      return duration.toSeconds() + " secs ago";
    }
    if (duration.toHours() < 6) {
      long hours = ChronoUnit.HOURS.between(timestamp, now);
      long minutes = ChronoUnit.MINUTES.between(timestamp.plusHours(hours), now);
      String hoursPart = hours == 0 ? "" : (hours + " hrs and ");
      return hoursPart + minutes + " mins ago";
    }
    return "On " + timestamp.format(DateTimeFormatter.ofPattern("LLL dd, HH:mmaa"));
  }
}
