package io.jobscheduler.scheduler.comparators;

import io.jobscheduler.models.Task;
import java.util.Comparator;

public class JobComparator implements Comparator<Task> {

  @Override
  public int compare(Task o1, Task o2) {
    return o1.getPriority() - o2.getPriority();
  }
}
