package org.camunda.latera.bss.connectors.odooOpenApi

import org.camunda.latera.bss.utils.ListUtil

trait Entity {
  Map getEntity(CharSequence type, def id) {
    LinkedHashMap result = null
    try {
      result = sendRequest(
        'get',
        path : "${type}/${id}"
      )
    } catch (Exception e) {
      logger.error(e)
    }
    return result
  }

  List getEntitiesBy(CharSequence type, List params) {
    List query = getSearchConditionsList(params)
    return callEntityMethod(type, 'search', [args: query])
  }

  Map getEntityBy(CharSequence type, List params) {
    return getEntitiesBy(type, params)?.getAt(0)
  }

  List callEntityMethod(String type, String methodName, Map params) {
    def result = null
    try {
      logger.info("Calling method ${type}/${methodName} with params ${params}")
      result = sendRequest(
              'patch',
              path : "${type}/call/$methodName",
              body : params
      )
    } catch (Exception e) {
      logger.error("   Error while calling method ${type}/${methodName} with params ${params}")
      logger.error(e)
    }
    return result
  }

  Map createEntity(CharSequence type, Map params) {
    LinkedHashMap result = null
    try {
      logger.info("Creating ${type} with params ${params}")
      result = sendRequest(
        'post',
        path : "${type}",
        body : params
      )
    } catch (Exception e) {
      logger.error("   Error while creating ${type}")
      logger.error(e)
    }
    return result
  }

  Boolean updateEntity(CharSequence type, Integer id, Map params) {
    try {
      logger.info("Updating ${type} id ${id} with params ${params}")
      sendRequest(
        'put',
        path : "${type}/${id}",
        body : params
      )
      return true
    } catch (Exception e) {
      logger.error("   Error while updating ${type}")
      logger.error(e)
      return false
    }
  }

  Boolean deleteEntity(CharSequence type, def id) {
    try {
      logger.info("Deleting ${type} id ${id}")
      sendRequest(
        'delete',
        path : "${type}/${id}"
      )
      return true
    } catch (Exception e) {
      logger.error("   Error while deleting ${type}")
      logger.error(e)
      return false
    }
  }
}