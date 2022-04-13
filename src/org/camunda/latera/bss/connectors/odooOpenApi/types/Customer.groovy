package org.camunda.latera.bss.connectors.odooOpenApi.types

trait Customer {
  private static String CUSTOMER_ENTITY_TYPE = 'res.partner'

  private String getCustomerEntityType() {
    return CUSTOMER_ENTITY_TYPE
  }

  private Map getCustomerDefaultParams() {
    return [
      name            : null,
      email           : null,
      isCompany       : null,
      companyName     : null,
      organizationId  : null,
      userId          : null,
      teamId          : null,
      phoneNumber     : null,
      countryId       : null,
      stateId         : null,
      city            : null,
      street          : null,
      street2         : null,
      zip             : null,
      hydraCustomerId : null,
      comment         : null
    ]
  }

  Map getCustomerParamsMap(Map params) {
    return [
      name              : params.name,
      email             : params.email,
      is_company        : params.isCompany,
      company_name      : params.companyName,
      company_id        : params.organizationId,
      user_id           : params.userId,
      team_id           : params.teamId,
      phone             : params.phoneNumber,
      country_id        : params.countryId,
      state_id          : params.stateId,
      city              : params.city,
      street            : params.street,
      street2           : params.street2,
      zip               : params.zip,
      hydra_customer_id : params.hydraCustomerId,
      comment           : params.comment
    ]
  }

  private Map getCustomerParams(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCustomerDefaultParams() + input
    return prepareParams(this.&getCustomerParamsMap, params, additionalParams)
  }

  Map getCustomer(def id) {
    return getEntity(getCustomerEntityType(), id)
  }

  List getCustomersBy(List<Map> input, Boolean idOnly=true) {
    List response = getEntitiesBy(getCustomerEntityType(), input)
    if (idOnly) {
       return response
    } else {
      List result = []
      response.each {
        result.add(getCustomer(it))
      }
      return result
    }
  }

  Map getCustomerBy(List input, Boolean idOnly=false) {
    Integer result = getCustomersBy(input)?.getAt(0)
    if (!idOnly && result) {
      return getCustomer(result)
    } else {
      return [id: result]
    }
  }

  Map createCustomer(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCustomerParams(input, additionalParams)
    return createEntity(getCustomerEntityType(), params)
  }

  Boolean updateCustomer(Map input, def id, Map additionalParams = [:]) {
    LinkedHashMap params = getCustomerParams(input, additionalParams)
    return updateEntity(getCustomerEntityType(), id, params)
  }

  Boolean deleteCustomer(def id) {
    return deleteEntity(getCustomerEntityType(), id)
  }
}