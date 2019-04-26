package org.camunda.latera.bss.connectors.hid.hydra

import org.camunda.latera.bss.utils.Oracle
trait Good {
  private static String GOODS_TABLE                = 'SR_V_GOODS'
  private static String GOOD_ADD_PARAMS_TABLE      = 'SR_V_GOOD_VALUES'
  private static String GOOD_ADD_PARAM_TYPES_TABLE = 'SR_V_GOOD_VALUES_TYPE'

  def getGoodsTable() {
    return GOODS_TABLE
  }

  def getGoodAddParamsTable() {
    return GOOD_ADD_PARAMS_TABLE
  }

  def getGoodAddParamTypesTable() {
    return GOOD_ADD_PARAM_TYPES_TABLE
  }

  LinkedHashMap getGood(def goodId) {
    LinkedHashMap where = [
      n_good_id: goodId
    ]
    return hid.getTableData(getGoodsTable(), where: where)
  }

  List getGoodsBy(LinkedHashMap input) {
    LinkedHashMap params = mergeParams([
      goodId     : null,
      kindId     : null,
      typeId     : null,
      groupId    : null,
      baseGoodId : null,
      unitId     : null,
      code       : null,
      name       : null,
      isProvider : null,
      isCustomer : null,
      tags       : null
    ], input)
    LinkedHashMap where = [:]

    if (params.goodId) {
      where.n_good_id = params.goodId
    }
    if (params.kindId) {
      where.n_good_kind_id = params.kindId
    }
    if (params.typeId) {
      where.n_good_type_id = params.typeId
    }
    if (params.groupId) {
      where.n_good_group_id = params.groupId
    }
    if (params.baseGoodId) {
      where.n_base_good_id = params.baseGoodId
    }
    if (params.unitId) {
      where.n_unit_id = params.unitId
    }
    if (params.code) {
      where.vc_code = params.code
    }
    if (params.name) {
      where.vc_name = params.name
    }
    if (params.isProvider != null) {
      where.c_fl_provider_equipment = Oracle.encodeBool(params.isProvider)
    }
    if (params.isCustomer != null) {
      where.c_fl_customer_equipment = Oracle.encodeBool(params.isCustomer)
    }
    if (params.tags) {
      where.t_tags = params.tags
    }
    return hid.getTableData(getGoodsTable(), where: where)
  }

  LinkedHashMap getGoodBy(LinkedHashMap input) {
    return getGoodsBy(input)?.getAt(0)
  }

  def getGoodUnitId(def goodId) {
    LinkedHashMap where = [
      n_good_id: goodId
    ]
    return getGood(goodId).n_unit_id
  }

  List getGoodAddParamsBy(LinkedHashMap input) {
    def defaultParams = [
      goodId  : null,
      paramId : null,
      date    : null,
      string  : null,
      number  : null,
      bool    : null,
      refId   : null
    ]
    if (input.containsKey('param')) {
      input.paramId = getGoodAddParamTypeIdByCode(input.param.toString())
      input.remove('param')
    }
    LinkedHashMap params = mergeParams(defaultParams, input)
    LinkedHashMap where = [:]

    if (params.goodId) {
      where.n_good_id = params.goodId
    }
    if (params.paramId) {
      where.n_good_value_type_id = params.paramId
    }
    if (params.date) {
      where.d_value = params.date
    }
    if (params.string) {
      where.vc_value = params.string
    }
    if (params.number) {
      where.n_value = params.number
    }
    if (params.bool != null) {
      where.c_fl_value = Oracle.encodeBool(params.bool)
    }
    if (params.refId) {
      where.n_ref_id = params.refId
    }
    return hid.getTableData(getGoodAddParamsTable(), where: where)
  }

  LinkedHashMap getGoodAddParamBy(LinkedHashMap input) {
    return getGoodAddParamsBy(input)?.getAt(0)
  }

  def getGoodAddParamTypeIdByCode(String code) {
    LinkedHashMap where = [
      vc_code: code
    ]
    return hid.getTableFirst(getGoodAddParamTypesTable(), where: where)?.n_good_value_type_id
  }
}