package org.camunda.bpm.link3

import org.camunda.bpm.application.ProcessApplication
import org.camunda.bpm.application.impl.ServletProcessApplication
import org.camunda.latera.bss.executionListeners.EventLogging
import org.camunda.latera.bss.executionListeners.AutoSaveOrderData
import org.camunda.latera.bss.logging.SimpleLogger
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.engine.delegate.DelegateExecution

@ProcessApplication("pizza_order")
class PizzaOrderApplication extends ServletProcessApplication {
  ExecutionListener getExecutionListener() {
    return new ExecutionListener() {
      void notify(DelegateExecution execution) {
        new EventLogging().notify(execution)

        String eventName    = execution.getEventName()
        String activityName = execution.getCurrentActivityName()

        if (eventName == ExecutionListener.EVENTNAME_START && activityName != null) {
          new SimpleLogger(execution).info(getLogLine(execution))
        }

        if (eventName == ExecutionListener.EVENTNAME_END) {
          new AutoSaveOrderData().notify(execution)
        }
      }
    }
  }

  String getLogLine(DelegateExecution execution) {
    return String.format(
      "\n%s\n%s - %s",
      '-' * 72,
      execution.getVariable('homsOrderCode') ?: execution.getProcessInstanceId(),
      execution.getCurrentActivityName()
    )
  }
}