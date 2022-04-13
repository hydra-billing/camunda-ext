package org.camunda.latera.bss.connectors.odooOpenApi

import static org.camunda.latera.bss.utils.DateTimeUtil.isDate
import static org.camunda.latera.bss.utils.DateTimeUtil.iso
import static org.camunda.latera.bss.utils.StringUtil.isString
import static org.camunda.latera.bss.utils.StringUtil.join
import static org.camunda.latera.bss.utils.StringUtil.capitalize
import static org.camunda.latera.bss.utils.MapUtil.isMap
import static org.camunda.latera.bss.utils.MapUtil.nvl
import static org.camunda.latera.bss.utils.MapUtil.snakeCaseKeys
import static org.camunda.latera.bss.utils.ListUtil.isList

trait Main {
  private static LinkedHashMap DEFAULT_WHERE  = [:]
  private static LinkedHashMap DEFAULT_ORDER  = [:]
  private static List          DEFAULT_FIELDS = []
  private static Integer       DEFAULT_LIMIT  = 0
  private static Integer       DEFAULT_OFFSET = 0

  List<List<List>> getSearchConditionsList(List params) {
    List result = []
    params.eachWithIndex {def param, int i ->
      def currentResult
      if (param instanceof Map) {
        currentResult = [
                param.name,
                param.condition,
                param.value
        ]
      } else if (param instanceof String) {
        if (param.size()==0 || !(param[0]  in ['&' ,'|', '!'])) {
          throw new Exception("\'|\' or \'&\' or \'!\' only")
        }
        currentResult = param[0]
        if (i != 0) {
          throw new Exception("\'|\' or \'&\' or \'!\' must be first element of list")
        }
        if (params.size() == 2) {
          throw new Exception("Only one parameter with \'$currentResult\'")
        }
      } else {
        throw new Exception('Only char or Map allowed')
      }
      result.add(currentResult)
    }
    return [result]
  }

  Map prepareParams(Closure paramsParser, Map input, Map additionalParams) {
    return convertParams(nvl(paramsParser(input) + negativeParser(paramsParser, input)) + convertKeys(additionalParams))
  }

  private Map negativeParser(Closure paramsParser, Map negativeInput) {
    LinkedHashMap originalInput = [:]
    LinkedHashMap input         = [:]
    LinkedHashMap negativeWhere = [:]

    // 'stageId!': 3 -> 'stageId': 3
    if (negativeInput?.size() > 0) {
      negativeInput.each{ CharSequence field, def value ->
        if (field ==~ /^(.*)!$/) {
          String originalField = field.replaceFirst(/^(.*)!$/, '$1')
          originalInput[originalField] = value
        }
      }
    }

    // 'stageId': 3 -> 'stage_id': 3
    input = paramsParser(originalInput)

    // 'stage_id': 3 -> 'stage_id!': 3
    input.each{ CharSequence field, def value ->
      negativeWhere["${field}!".toString()] = value
    }
    return negativeWhere
  }

  private Map convertKeys(Map input) {
    return snakeCaseKeys(input)
  }

  private def escapeSearchValue(def value) {
    if (value instanceof Boolean) {
      return capitalize("${value}")
    }
    if (isString(value)) {
      return "'${value}'"
    }
    if (isList(value)) {
      List newList = []
      value.each { def it ->
        newList += escapeSearchValue(it)
      }
      return "[${join(newList, ',')}]"
    }
    return value
  }

  private def convertValue(def value) {
    if (value == null && value == 'null') {
      return false //D`oh
    }
    if (isDate(value)) {
      return "'${iso(value)}'"
    }
    return value
  }

  private Map convertParams(Map input) {
    LinkedHashMap result = [:]
    input.each { CharSequence key, def value ->
      result[key] = convertValue(value)
    }
    return result
  }
}