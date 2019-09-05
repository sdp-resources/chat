import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class ChatServer {
  private static HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();
  private static Map<String, ChatChannel> channels = new HashMap<>();

  public static void main(String[] args) {
    port(4590);
    staticFiles.location("/public");
    get("/", (req, res) ->
        render(new HashMap<>(), "index.handlebars"));
    post("/", (req, res) -> {
      res.redirect(String.format("/%s/%s",
                                 req.queryParams("channelName"),
                                 req.queryParams("userName")));
      return res;
    });
    get("/:channelName/:userName", (req, res) -> {
      Map<String, Object> model = prepareChannelModel(req.params("channelName"), req.params("userName"));
      return render(model, "channel.handlebars");
    });
    post("/:channelName/:userName", (req, res) -> {
      submitNewMessage(req.params("channelName"), req.params("userName"), req.queryParams("text"));
      res.redirect(req.pathInfo());
      return res;
    });
  }

  private static Map<String, Object> prepareChannelModel(String channelName, String userName) {
    Map<String, Object> model = new HashMap<>();
    model.put("channelName", channelName);
    model.put("userName", userName);
    model.put("channel", getOrCreateChannel(channelName));
    return model;
  }

  private static void submitNewMessage(String channelName, String userName, String text) {
    ChatChannel channel = getOrCreateChannel(channelName);
    createMessage(channel, userName, text);
  }

  private static void createMessage(ChatChannel channel, String userName, String text) {
    channel.getMessages()
        .add(new ChatMessage(channel, userName, text, LocalDateTime.now()));
  }

  private static ChatChannel getOrCreateChannel(String channelName) {
    createChannelIfNotExists(channelName);
    return channels.get(channelName);
  }

  private static void createChannelIfNotExists(String channelName) {
    if (!channels.containsKey(channelName)) {
      channels.put(channelName, new ChatChannel(channelName));
    }
  }

  private static String render(Map<String, Object> model, String templateName) {
    Context context = prepareContext(model);
    return templateEngine.render(new ModelAndView(context, templateName));
  }

  private static Context prepareContext(Map<String, Object> model) {
    return Context.newBuilder(model)
        .resolver(
            MapValueResolver.INSTANCE,
            JavaBeanValueResolver.INSTANCE,
            FieldValueResolver.INSTANCE,
            MethodValueResolver.INSTANCE)
        .build();
  }
}

