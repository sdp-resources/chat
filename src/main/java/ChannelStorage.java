import java.util.HashMap;
import java.util.Map;

public class ChannelStorage {
  private final Map<String, ChatChannel> channels = new HashMap<>();

  public ChatChannel getOrCreateChannel(String channelName) {
    createChannelIfNotExists(channelName);
    return channels.get(channelName);
  }

  private void createChannelIfNotExists(String channelName) {
    if (!channels.containsKey(channelName)) {
      channels.put(channelName, new ChatChannel(channelName));
    }
  }

}
