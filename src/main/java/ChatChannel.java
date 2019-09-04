import java.util.ArrayList;
import java.util.List;

public class ChatChannel {
  private final List<ChatMessage> messages;
  private final String channelName;

  ChatChannel(String channelName) {
    this.channelName = channelName;
    this.messages = new ArrayList<>();
  }

  List<ChatMessage> getMessages() {
    return messages;
  }

  String getChannelName() {
    return channelName;
  }
}
