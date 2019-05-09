package org.camunda.latera.bss.connectors.hoper.hydra

trait Subscription {
  private static LinkedHashMap SUBSCRIPTION_ENTITY_TYPE = [
    one    : 'subscription',
    plural : 'subscriptions'
  ]

  private static LinkedHashMap CHILD_SUBSCRIPTION_ENTITY_TYPE = [
    one    : 'additional_service',
    plural : 'additional_services'
  ]

  private static LinkedHashMap AVAILABLE_SERVICES_ENTITY_TYPE = [
    one    : 'available_service',
    plural : 'available_services'
  ]

  def getSubscriptionEntityType(def customerId) {
    def customerType = getCustomerEntityType()
    def parent       = "${customerType.parent}/${customerType.plural}/${customerId}"
    return SUBSCRIPTION_ENTITY_TYPE + [parent: parent]
  }

  def getChildSubscriptionEntityType(def customerId, def subscriptionId) {
    def subscriptionType = getSubscriptionEntityType(customerId)
    def parent       = "${subscriptionType.parent}/${subscriptionType.plural}/${subscriptionId}"
    return CHILD_SUBSCRIPTION_ENTITY_TYPE + [parent: parent]
  }

  def getAvailableServicesEntityType(def customerId) {
    def customerType = getCustomerEntityType()
    def parent       = "${customerType.parent}/${customerType.plural}/${customerId}"
    return AVAILABLE_SERVICES_ENTITY_TYPE + [parent: parent]
  }

  LinkedHashMap getSubscriptionDefaultParams() {
    return [
      accountId          : null,
      docId              : null,
      goodId             : null,
      equipmentId        : null,
      quant              : null,
      beginDate          : null,
      endDate            : null
    ]
  }

  LinkedHashMap getSubscriptionParamsMap(LinkedHashMap params) {
    return [
      n_account_id     : params.accountId,
      n_contract_id    : params.docId,
      n_service_id     : params.goodId,
      n_object_id      : params.equipmentId,
      n_quant          : params.quant,
      d_begin          : params.beginDate,
      d_end            : params.endDate,
      close_charge_log : params.closeChargeLog
    ]
  }

  LinkedHashMap getSubscriptionParams(LinkedHashMap input) {
    def params = getSubscriptionDefaultParams() + input
    def data   = getSubscriptionParamsMap(params)
    return nvlParams(data)
  }

  LinkedHashMap getChildSubscriptionDefaultParams() {
    return [
      accountId          : null,
      goodId             : null,
      quant              : null,
      parSubscriptionId  : null,
      beginDate          : null,
      endDate            : null
    ]
  }

  LinkedHashMap getChildSubscriptionParamsMap(LinkedHashMap params) {
    return [
      n_account_id : params.accountId,
      n_service_id : params.goodId,
      n_quant      : params.quant,
      n_par_subscription_id : params.parSubscriptionId,
      d_begin      : params.beginDate,
      d_end        : params.endDate,
      immediate    : params.immediate
    ]
  }

  LinkedHashMap getChildSubscriptionParams(LinkedHashMap input) {
    def params = getChildSubscriptionDefaultParams() + input
    def data   = getChildSubscriptionParamsMap(params)
    return nvlParams(data)
  }

  LinkedHashMap getAvailableServicesDefaultParams() {
    return getPaginationDefaultParams() + [
      accountId          : null,
      docId              : null,
      equipmentId        : null,
      parSubscriptionId  : null,
      operationDate      : null
    ]
  }

  LinkedHashMap getAvailableServicesParamsMap(LinkedHashMap params) {
    return [
      n_account_id          : params.accountId,
      n_contract_id         : params.docId,
      n_equipment_id        : params.equipmentId,
      n_par_subscription_id : params.parSubscriptionId,
      d_oper                : params.operationDate,
      per_page              : params.endDate,
      page                  : params.closeChargeLog
    ]
  }

  LinkedHashMap getAvailableServicesParams(LinkedHashMap input) {
    def params = getAvailableServicesDefaultParams() + input
    def data   = getAvailableServicesParamsMap(params)
    return nvlParams(data)
  }

  List getSubscriptions(def customerId, LinkedHashMap input = [:]) {
    def params = getPaginationDefaultParams() + input
    return getEntities(getSubscriptionEntityType(customerId), params)
  }

  LinkedHashMap getSubscription(def customerId, def subscriptionId) {
    return getEntity(getSubscriptionEntityType(customerId), subscriptionId)
  }

  List getChildSubscriptions(def customerId, def subscriptionId, LinkedHashMap input = [:]) {
    def params = getPaginationDefaultParams() + input
    return getEntities(getChildSubscriptionEntityType(customerId, subscriptionId), params)
  }

  LinkedHashMap getChildSubscription(def customerId, def subscriptionId, def childSubscriptionId) {
    return getEntity(getChildSubscriptionEntityType(customerId, subscriptionId), childSubscriptionId)
  }

  List getAvailableServices(def customerId, LinkedHashMap input) {
    LinkedHashMap params = getAvailableServicesParams(input)
    return getEntities(getAvailableServicesEntityType(customerId), params)
  }

  LinkedHashMap getAvailableService(def customerId, LinkedHashMap input) {
    def serviceId = input.serviceId ?: input.goodId
    def availableServices = getAvailableServices(customerId, input)
    for (service in availableServices) {
      if (service.n_good_id.toString() == serviceId.toString()) {
          return service
      }
    }
  }

  LinkedHashMap getAvailableServiceByName(def customerId, LinkedHashMap input) {
    def serviceName = input.serviceName ?: input.goodName
    def availableServices = getAvailableServices(customerId, input)
    for (service in availableServices) {
      if (service.vc_name == serviceName) {
        return service
      }
    }
  }

  LinkedHashMap getAvailableServiceByCode(def customerId, LinkedHashMap input) {
    def serviceCode = input.serviceCode ?: input.goodCode
    def availableServices = getAvailableServices(customer, input)
    for (service in availableServices) {
      if (service.vc_code == serviceName) {
        return service
      }
    }
  }

  LinkedHashMap createSubscription(def customerId, LinkedHashMap input = [:]) {
    LinkedHashMap params = getSubscriptionParams(input)
    return createEntity(getSubscriptionEntityType(customerId), params)
  }

  LinkedHashMap updateSubscription(def customerId, def subscriptionId, LinkedHashMap input) {
    LinkedHashMap params = getSubscriptionParams(input)
    return updateEntity(getSubscriptionEntityType(customerId), subscriptionId, params)
  }

  LinkedHashMap putSubscription(def customerId, LinkedHashMap input) {
    def subscriptionId = input.subscriptionId
    input.remove('subscriptionId')

    if (subscriptionId) {
      return updateSubscription(customerId, subscriptionId, input)
    } else {
      return createSubscription(customerId, input)
    }
  }

  LinkedHashMap closeSubscription(def customerId, def subscriptionId, def endDate, def closeChargeLog = false) {
    return updateSubscription(customerId, subscriptionId, [endDate: endDate, closeChargeLog: closeChargeLog])
  }

  LinkedHashMap createChildSubscription(def customerId, def subscriptionId, LinkedHashMap input = [:]) {
    LinkedHashMap params = getChildSubscriptionParams(input + [parSubscriptionId: subscriptionId])
    return createEntity(getChildSubscriptionEntityType(customerId, subscriptionId), params)
  }

  LinkedHashMap updateChildSubscription(def customerId, def subscriptionId, def childSubscriptionId, LinkedHashMap input = [:]) {
    LinkedHashMap params = getChildSubscriptionParams(input)
    return updateEntity(getChildSubscriptionEntityType(customerId, subscriptionId), childSubscriptionId, params)
  }

  LinkedHashMap putChildSubscription(def customerId, LinkedHashMap input) {
    def parSubscriptionId = input.parSubscriptionId
    def subscriptionId    = input.subscriptionId
    input.remove('parSubscriptionId')
    input.remove('subscriptionId')

    if (subscriptionId) {
      return updateChildSubscription(customerId, parSubscriptionId, subscriptionId, input)
    } else {
      return createChildSubscription(customerId, parSubscriptionId, input)
    }
  }

  LinkedHashMap closeChildSubscription(def customerId, def subscriptionId, def childSubscriptionId, def endDate, def immediate = false) {
    return updateChildSubscription(customerId, subscriptionId, childSubscriptionId, [endDate: endDate, immediate: immediate])
  }
}