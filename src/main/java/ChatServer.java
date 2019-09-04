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
      Map<String, Object> model = new HashMap<>();
      String channelName = req.params("channelName");
      model.put("channelName", channelName);
      model.put("userName", req.params("userName"));
      if (!channels.containsKey(channelName)) {
        channels.put(channelName, new ChatChannel(channelName));
      }
      model.put("channel", channels.get(channelName));
      return render(model, "channel.handlebars");
    });
    post("/:channelName/:userName", (req, res) -> {
      String channelName = req.params("channelName");
      String userName = req.params("userName");
      ChatChannel channel = channels.get(channelName);
      String text = req.queryParams("text");
      LocalDateTime timestamp = LocalDateTime.now();
      if (!channels.containsKey(channelName)) {
        channels.put(channelName, new ChatChannel(channelName));
      }
      channels.get(channelName)
          .getMessages()
          .add(new ChatMessage(channel, userName, text, timestamp));
      res.redirect(req.pathInfo());
      return res;
    });
  }

  private static String render(Map<String, Object> model, String templateName) {
    Context context = Context
        .newBuilder(model)
        .resolver(
            MapValueResolver.INSTANCE,
            JavaBeanValueResolver.INSTANCE,
            FieldValueResolver.INSTANCE,
            MethodValueResolver.INSTANCE)
        .build();
    return templateEngine.render(new ModelAndView(context, templateName));
  }
}

