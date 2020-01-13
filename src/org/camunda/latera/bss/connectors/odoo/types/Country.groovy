package org.camunda.latera.bss.connectors.odoo.types

trait Country {
  private static String COUNTRY_ENTITY_TYPE       = 'res.country'
  private static String COUNTRY_STATE_ENTITY_TYPE = 'res.country_state'

  private String getCountryEntityType() {
    return COUNTRY_ENTITY_TYPE
  }

  private Map getCountryDefaultParams() {
    return [
      code          : null,
      name          : null,
      currencyId    : null,
      phoneCode     : null,
      addressFormat : null,
      vatLabel      : null
    ]
  }

  Map getCountryParamsMap(Map params) {
    return [
      code           : params.code,
      name           : params.name,
      currency_id    : params.currencyId,
      phone_code     : params.phoneCode,
      address_format : params.addressFormat,
      vat_label      : params.vatLabel
    ]
  }

  private Map getCountryParams(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryDefaultParams() + input
    return prepareParams(this.&getCountryParamsMap, params, additionalParams)
  }

  Map getCountry(def id) {
    return getEntity(getCountryEntityType(), id)
  }

  List getCountriesBy(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryParams(input, additionalParams)
    return getEntitiesBy(getCountryEntityType(), params)
  }

  Map getCountryBy(Map input, Map additionalParams = [:]) {
    return getCountriesBy(input, additionalParams)?.getAt(0)
  }

  Map createCountry(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryParams(input, additionalParams)
    return createEntity(getCountryEntityType(), params)
  }

  Map updateCountry(Map input = [:], def id, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryParams(input, additionalParams)
    return updateEntity(getCountryEntityType(), id, params)
  }

  Boolean deleteCountry(def id) {
    return deleteEntity(getCountryEntityType(), id)
  }

  private String getCountryStateEntityType() {
    return COUNTRY_STATE_ENTITY_TYPE
  }

  private Map getCountryStateDefaultParams() {
    return [
      code      : null,
      name      : null,
      countryId : null
    ]
  }

  Map getCountryStateParamsMap(Map params) {
    return [
      code       : params.code,
      name       : params.name,
      country_id : params.countryId
    ]
  }

  private Map getCountryStateParams(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryStateDefaultParams() + input
    return prepareParams(this.&getCountryStateParamsMap, params, additionalParams)
  }

  Map getCountryState(def id) {
    return getEntity(getCountryStateEntityType(), id)
  }

  List getCountryStatesBy(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryStateParams(input, additionalParams)
    return getEntitiesBy(getCountryStateEntityType(), params)
  }

  Map getCountryStateBy(Map input, Map additionalParams = [:]) {
    return getCountryStatesBy(input, additionalParams)?.getAt(0)
  }

  Map createCountryState(Map input, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryStateParams(input, additionalParams)
    return createEntity(getCountryStateEntityType(), params)
  }

  Map createCountryState(Map input = [:], def countryId, Map additionalParams = [:]) {
    return createCountryState(input + [countryId: countryId], additionalParams)
  }

  Map updateCountryState(Map input = [:], def id, Map additionalParams = [:]) {
    LinkedHashMap params = getCountryStateParams(input, additionalParams)
    return updateEntity(getCountryStateEntityType(), id, params)
  }

  Boolean deleteCountryState(def id) {
    return deleteEntity(getCountryStateEntityType(), id)
  }
}