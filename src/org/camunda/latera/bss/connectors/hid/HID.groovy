package org.camunda.latera.bss.connectors

import groovy.net.xmlrpc.*
import java.time.LocalDateTime
import static org.camunda.latera.bss.utils.StringUtil.*
import static org.camunda.latera.bss.utils.DateTimeUtil.*
import static org.camunda.latera.bss.utils.Oracle.*
import org.camunda.latera.bss.connectors.hid.Table
import org.camunda.bpm.engine.delegate.DelegateExecution

class HID implements Table {
  String url
  String user
  private String password
  XMLRPCServerProxy proxy

  HID(DelegateExecution execution) {
    def ENV       = System.getenv()
    this.url      = ENV['HID_URL']      ?: execution.getVariable('hidUrl')  ?: 'http://hid:10080/xml-rpc/db'
    this.user     = ENV['HID_USER']     ?: execution.getVariable('hidUser') ?: 'hydra'
    this.password = ENV['HID_PASSWORD'] ?: execution.getVariable('hidPassword')

    this.proxy = new XMLRPCServerProxy(this.url)
    this.proxy.setBasicAuth(this.user, this.password)
  }

  List queryDatabase(CharSequence query, Boolean asMap = false, Integer limit = 0, Integer page = 1) {
    List result = []
    if (limit != 0 && limit != null) {
      query = """SELECT * FROM (
${query}
)
WHERE ROWNUM <= ${limit}"""
    }
    LinkedHashMap answer = this.proxy.invokeMethod('SELECT', [query.toString(), page])
    List rows = answer.SelectResult
    if (rows) {
      rows.each{ row ->
        // There is row number, just remove it
        row.removeAt(0)

        // Convert codepage from
        List convertedRow = []
        row.each{ value ->
          if (isString(value)) {
            convertedRow.add(varcharToUnicode(value))
          } else {
            convertedRow.add(value)
          }
        }

        if (asMap) {
          // Use "'VC_VALUE', VC_VALUE" format because SELECT procedure don't return column names
          LinkedHashMap mappedRow = (convertedRow as Object[]).toSpreadMap()
          result.add(mappedRow)
        } else {
          result.add(convertedRow)
        }
      }
    }

    return result
  }

  List queryDatabaseList(CharSequence query, Integer limit = 0, Integer page = 1) {
    return queryDatabase(query, false, limit, page)
  }

  List queryDatabaseMap(CharSequence query, Integer limit = 0, Integer page = 1) {
    return queryDatabase(query, true, limit, page)
  }

  def queryFirst(CharSequence query, Boolean asMap = false) {
    List result = this.queryDatabase(query, asMap, 1)

    if (result) {
      return result.getAt(0)
    } else {
      return null
    }
  }

  List queryFirstList(CharSequence query) {
    return queryFirst(query, false)
  }

  Map queryFirstMap(CharSequence query) {
    return queryFirst(query, true)
  }

  def execute(CharSequence execName, Map params) {
    LinkedHashMap encodedParams = [:]
    params.each{ key, value ->
      if (isDate(value)) {
        value = encodeDate(value)
      }
      if (isString(value)) {
        value = value.toString() // Convert GStringImpl to String
      }
      encodedParams[key] = encodeNull(value)
    }
    return this.proxy.invokeMethod(execName, [encodedParams])
  }
}