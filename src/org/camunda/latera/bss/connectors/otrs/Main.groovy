package org.camunda.latera.bss.connectors.otrs

import static org.camunda.latera.bss.utils.MapUtil.camelizeKeys
import static org.camunda.latera.bss.utils.MapUtil.nvl
import static org.camunda.latera.bss.utils.MapUtil.keysList

trait Main {
  Map prepareParams(Map input) {
    return nvl(input)
  }

  Map convertKeys(Map input) {
    return camelizeKeys(input, true)
  }

  def convertValue(def value) {
    if (value instanceof Boolean) {
      return value ? 1 : 0
    }
    return value
  }

  Map convertParams(Map input) {
    LinkedHashMap result = [:]
    input.each { key, value ->
      result[key] = convertValue(value)
    }
  }

  Map suppressBase64(Map input) {
    Map params = input + [:]
    List inputKeys = keysList(input)

    if ('Attachment' in inputKeys) {
      params.Attachment = params.Attachment.collect { Map attach ->
        return attach + [Content: '*binary data*']
      }
    }
    return params
  }
}