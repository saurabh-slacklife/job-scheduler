package io.jobscheduler.scheduler.action;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ActionFactory {

  // TODO Use strategy pattern here and convert Individual <T> to specific model
  final static Map<String, Supplier<IAction>> actionMap = new HashMap<>();

  static {
//    TODO update these actions
    actionMap.put(Action.index.name(), PushNotificationActionImpl::new);
    actionMap.put(Action.save.name(), PushNotificationActionImpl::new);
    actionMap.put(Action.push_notification.name(), PushNotificationActionImpl::new);
    actionMap.put(Action.email.name(), SendEmail::new);
  }

  public static IAction getActor(String type) {
    Supplier<? extends IAction> supplier = actionMap.get(type);
    if (supplier != null) {
      return supplier.get();
    }
    return null;
  }
}
