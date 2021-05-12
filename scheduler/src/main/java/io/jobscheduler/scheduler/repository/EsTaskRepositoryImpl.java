package io.jobscheduler.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class EsTaskRepositoryImpl<T> implements ITaskRepository<T> {

  //TODO Change T to ES specific Index
  public EsTaskRepositoryImpl() {
  }


  @Override
  public T save(T data) {
    log.info("Persisting data in ES={}", data);
    return data;
  }

  @Override
  public boolean act(T task) {
    return this.save(task) != null;
  }
}
