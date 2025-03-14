package org.camunda.latera.bss.http

import java.util.concurrent.TimeUnit
import groovyx.net.http.OkHttpBuilder
import groovyx.net.http.FromServer
import groovyx.net.http.HttpException
import static groovyx.net.http.util.SslUtils.ignoreSslIssues
import org.camunda.latera.bss.logging.SimpleLogger
import static org.camunda.latera.bss.utils.StringUtil.notEmpty
import static org.camunda.latera.bss.utils.StringUtil.isEmpty
import static org.camunda.latera.bss.utils.ListUtil.firstNotNull
import static org.camunda.latera.bss.utils.JSON.escape
import org.camunda.latera.bss.http.YmlApiProcessor

class HTTPRestProcessor {
  String baseUrl
  String basePath
  Boolean testEnv
  SimpleLogger logger
  Boolean supressRequestBodyLog
  Boolean supressResponseBodyLog
  Map headers
  String user
  String password

  private static final Map<String, OkHttpBuilder> cachedBuilders = [:]

  HTTPRestProcessor(Map params) {
    this.logger = new SimpleLogger(params.execution)
    def ENV = System.getenv()

    this.testEnv  = ENV['CAMUNDA_EXT_ENV'] == 'test'
    this.baseUrl  = params.baseUrl
    this.basePath = parsePath(this.baseUrl)
    this.supressRequestBodyLog  = Boolean.valueOf(firstNotNull([
      params.supressRequestBodyLog,
      params.execution.getVariable('httpSupressRequestBody'),
      params.execution.getVariable('httpSupressRequest'),
      params.execution.getVariable('httpSupressBody'),
      ENV['HTTP_SUPRESS_REQUEST_BODY'],
      ENV['HTTP_SUPRESS_REQUEST'],
      ENV['HTTP_SUPRESS_BODY']
    ], false))
    this.supressResponseBodyLog = Boolean.valueOf(firstNotNull([
      params.supressResponseBodyLog,
      params.execution.getVariable('httpSupressResponseBody'),
      params.execution.getVariable('httpSupressResponse'),
      params.execution.getVariable('httpSupressBody'),
      ENV['HTTP_SUPRESS_RESPONSE_BODY'],
      ENV['HTTP_SUPRESS_RESPONSE'],
      ENV['HTTP_SUPRESS_BODY']
    ], false))

    if (notEmpty(params.user) && notEmpty(params.password)) {
      this.user = params.user
      this.password = params.password
    }

    if (params.headers) {
      this.headers = params.headers
    }

    if (!cachedBuilders.containsKey(this.baseUrl)) {
      def httpClient = OkHttpBuilder.configure {
        request.uri = this.baseUrl.toString()

        request.contentType = 'application/json'
        response.success responseBlock(false, this.supressRequestBodyLog)
        response.failure responseBlock(true,  this.supressResponseBodyLog)

        client.clientCustomizer {
          it.followRedirects = true
          it.connectTimeout(ENV['HTTP_CONNECT_TIMEOUT_SEC'].toInteger(), TimeUnit.SECONDS)
          it.readTimeout(ENV['HTTP_READ_TIMEOUT_SEC'].toInteger(), TimeUnit.SECONDS)
          it.writeTimeout(ENV['HTTP_WRITE_TIMEOUT_SEC'].toInteger(), TimeUnit.SECONDS)
        }

        !ENV['HTTP_USE_SSL'].toBoolean() && ignoreSslIssues(execution)
      }

      cachedBuilders[this.baseUrl] = httpClient
    }
  }

  def private responseBlock(Boolean failure = false, Boolean supress = false) {
    { FromServer response, Object data ->
      logger.debug("Response status: ${response.statusCode}")
      logger.debug("Content-Type: ${response.contentType}")
      logger.debug("Response data: -----")
      if (data) {
        if (supress) {
          logger.debug("*Supressing response data*")
        } else {
          if (data instanceof byte[]) {
            logger.debug(new String(data))
          } else {
            logger.debug(data.toString())
          }
        }
      } else {
        logger.debug("*Empty response*")
      }
      logger.debug("--------------------")

      if (failure) {
        throw new HttpException(response, data)
      } else {
        data
      }
    }
  }

  def getBuilder(baseUrl) {
    return this.cachedBuilders[baseUrl]
  }

  def sendRequest(Map paramsInput, CharSequence method = 'get') {
    Map params = paramsInput.clone()

    Boolean supressRequestBody  = false
    Boolean supressResponseBody = false
    if (params.supressRequestBodyLog != null) {
      supressRequestBody = params.supressRequestBodyLog
      params.remove('supressRequestBodyLog')
    } else if (this.supressRequestBodyLog) {
      supressRequestBody = true
    }
    if (params.supressResponseBodyLog != null) {
      supressResponseBody = params.supressResponseBodyLog
      params.remove('supressResponseBodyLog')
    } else if (this.supressResponseBodyLog) {
      supressResponseBody = true
    }

    logger.info("/ Sending HTTP ${method.toUpperCase()} request (${absoluteUrl(params.path)})...")

    if (params.body) {
      params.body = escape(params.body)
    }
    if (params.body || params.query) {
      logger.debug("Request data: ------")
      if (supressRequestBody) {
        logger.debug("*Supressing request data*")
      } else {
        logger.debug(params.body ? params.body.toString() : params.query.toString())
      }
      logger.debug("--------------------")
    }

    def result = [:]
    if (this.testEnv) {
      YmlApiProcessor ymlAPI = new YmlApiProcessor();
      result = ymlAPI.sendRequest(params, method)
    } else {
      result = getBuilder(this.baseUrl)."${method}" {
        response.success responseBlock(false, supressResponseBody)
        response.failure responseBlock(true,  supressResponseBody)

        if (params.path) {
          request.uri.path = absolutePath(params.path.toString())
        }
        if (params.query) {
          request.uri.query = params.query
        }

        params.remove('query')
        params.remove('path')

        if (notEmpty(this.user) && notEmpty(this.password)) {
          request.auth.basic(this.user, this.password)
        }

        if (this.headers) {
          this.headers.each { k,v ->
            request.headers."${k}" = v
          }
        }

        params.each { k,v ->
          request."${k}" = v
        }
      }
    }

    logger.info("\\ HTTP request sent")
    result
  }

  private String parsePath(CharSequence rawUrl) {
    // baseUrl http://homs:3000/api
    // return /api
    URL absolute = new URL(rawUrl.toString())
    return absolute.getPath()
  }

  // https://github.com/http-builder-ng/http-builder-ng/issues/210
  private String absolutePath(CharSequence path) {
    // baseUrl http://homs:3000/api
    if (isEmpty(path)) {
      return basePath.toString()
    }
    // path /task
    if (path.getAt(0) == '/') {
      return path.toString() // /task
    }
    // path task
    return "${basePath}/${path}".toString() // /api/task
  }

  private String absoluteUrl(CharSequence path) {
    // baseUrl http://homs:3000
    if (isEmpty(path)) {
      return baseUrl.toString()
    }
    // path /api
    if (path.getAt(0) == '/') {
      return "${baseUrl}${path}".toString() // http://homs:3000/api
    }
    // path api
    return "${baseUrl}/${path}".toString() // http://homs:3000/api
  }
}
