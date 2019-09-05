import java.util.HashMap;
import java.util.Map;

public class ChannelStorage {
  private final Map<String, ChatChannel> channels = new HashMap<>();

  public Map<String, ChatChannel> getChannels() {
    return channels;
  }
}
