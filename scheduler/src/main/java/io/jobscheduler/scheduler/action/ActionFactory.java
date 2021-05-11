package io.jobscheduler.scheduler.action;

import io.jobscheduler.scheduler.notification.PushNotificationActionImpl;
import io.jobscheduler.scheduler.repository.EsTaskRepositoryImpl;
import io.jobscheduler.scheduler.repository.MongoTaskRepositoryImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ActionFactory {

  final static Map<String, Supplier<IAction>> actionMap = new HashMap<>();

  static {
    actionMap.put("elastic-search", EsTaskRepositoryImpl::new);
    actionMap.put("mongo", MongoTaskRepositoryImpl::new);
    actionMap.put("push-notification", PushNotificationActionImpl::new);
  }

  public static IAction getActor(String type) {
    Supplier<? extends IAction> supplier = actionMap.get(type);
    if (supplier != null) {
      return supplier.get();
    }

    throw new IllegalArgumentException("No such dao type " + type);
  }
}
