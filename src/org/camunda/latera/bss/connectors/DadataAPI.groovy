package org.camunda.latera.bss.connectors

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.latera.bss.http.HTTPRestProcessor
import org.camunda.latera.bss.logging.SimpleLogger

class DadataAPI {
  String url
  Integer version
  private String token
  private String secret
  HTTPRestProcessor http
  SimpleLogger logger

  DadataAPI(DelegateExecution execution) {
    this.logger  = new SimpleLogger(execution)
    def ENV      = System.getenv()

    this.url     =  execution.getVariable('dadataApiUrl')                                             ?: ENV['DADATA_API_URL']     ?: 'https://dadata.ru/api'
    this.version = (execution.getVariable('dadataApiVersion')                                         ?: ENV['DADATA_API_VERSION'] ?: 2).toInteger()
    this.token   =  execution.getVariable('dadataApiToken')   ?: execution.getVariable('dadataToken') ?: ENV['DADATA_API_TOKEN']   ?: ENV['DADATA_TOKEN']
    this.secret  =  execution.getVariable('dadataSecret')                                             ?: ENV['DADATA_API_SECRET']

    LinkedHashMap headers = [
      'Authorization' : "Token ${token}",
      'X-Secret'      : secret
    ]
    this.http = new HTTPRestProcessor(
      baseUrl   : "${url}/v${version}",
      headers   : headers,
      execution : execution
    )
  }

  Map cleanData(CharSequence type, CharSequence input){
    try {
      return http.sendRequest(
        'post',
        path: 'clean',
        body: [
          structure: [
            type
          ],
          data: [
            [input]
          ]
        ]
      ).data[0][0]
    }
    catch (Exception e) {
      logger.error(e)
      return null
    }
  }

  Map cleanName(CharSequence input) {
    return cleanData('NAME', input)
  }

  Map cleanPassport(CharSequence input) {
    return cleanData('PASSPORT', input)
  }

  Map cleanAddress(CharSequence input) {
    return cleanData('ADDRESS', input)
  }

  Map cleanPhone(CharSequence input) {
    return cleanData('PHONE', input)
  }

  Map cleanEmail(CharSequence input) {
    return cleanData('EMAIL', input)
  }

  Map cleanVehicle(CharSequence input) {
    return cleanData('VEHICLE', input)
  }
}
