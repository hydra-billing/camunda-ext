package org.camunda.latera.bss.connectors

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.latera.bss.http.HTTPRestProcessor
import org.camunda.latera.bss.logging.SimpleLogger

import org.camunda.latera.bss.connectors.odooOpenApi.Main
import org.camunda.latera.bss.connectors.odooOpenApi.Entity
import org.camunda.latera.bss.connectors.odooOpenApi.types.Lead
import org.camunda.latera.bss.connectors.odooOpenApi.types.Customer
import org.camunda.latera.bss.connectors.odooOpenApi.types.Country

class OdooOpenApi implements Main, Entity, Lead, Customer, Country {
  String url
  private String token
  String db
  HTTPRestProcessor http
  SimpleLogger logger

  OdooOpenApi(DelegateExecution execution) {
    this.logger   = new SimpleLogger(execution)
    Map ENV       = System.getenv()

    this.url      = execution.getVariable('odooUrl')      ?: ENV['ODOO_URL'] ?: 'http://odoo:8069/api/v1/demo'
    this.db       = execution.getVariable('odooDatabase') ?: ENV['ODOO_DB']  ?: 'odoo'
    this.token    = execution.getVariable('odooToken')    ?: ENV['ODOO_TOKEN']
    this.http     = new HTTPRestProcessor(
      baseUrl     : url,
      execution   : execution
    )
  }

  private String authToken() {
    return "odoo:$token".bytes.encodeBase64().toString()
  }

  private Map authHeader() {
    return ['authorization': "Basic ${this.authToken()}".toString()]
  }

  def sendRequest(Map input, String method = 'get') {
    if (!input.headers) {
      input.headers = [:]
    }
    input.headers += this.authHeader()
    if (method.toLowerCase() in ['get', 'delete']) {
      input.headers += ['content-type': 'application/x-www-form-urlencoded']
    } else {
      input.headers += ['content-type': 'application/json']
    }
    return http.sendRequest(input, method)
  }
}
