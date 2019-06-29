package org.camunda.latera.bss.connectors.hid.hydra

import static org.camunda.latera.bss.utils.Oracle.*
import static org.camunda.latera.bss.utils.Numeric.*
import static org.camunda.latera.bss.utils.DateTimeUtil.local

trait Subject {
  private static String SUBJECTS_TABLE                = 'SI_V_SUBJECTS'
  private static String SUBJECT_ADD_PARAMS_TABLE      = 'SI_V_SUBJ_VALUES'
  private static String SUBJECT_ADD_PARAM_TYPES_TABLE = 'SI_V_SUBJ_VALUES_TYPE'
  private static String SUBJECT_GROUPS_TABLE          = 'SI_V_SUBJECT_BIND_GROUPS'
  private static String SUBJECT_STATE_ON              = 'SUBJ_STATE_On'
  private static String SUBJECT_STATE_LOCKED          = 'SUBJ_STATE_Locked'
  private static String SUBJECT_STATE_SUSPENDED       = 'SUBJ_STATE_ManuallySuspended'
  private static String SUBJECT_STATE_DISABLED        = 'SUBJ_STATE_Disabled'
  private static String SUBJECT_COMMENT_TYPE          = 'COMMENT_TYPE_Comment'

  String getSubjectsTable() {
    return SUBJECTS_TABLE
  }

  String getSubjectAddParamsTable() {
    return SUBJECT_ADD_PARAMS_TABLE
  }

  String getSubjectAddParamTypesTable() {
    return SUBJECT_ADD_PARAM_TYPES_TABLE
  }

  String getSubjectGroupsTable() {
    return SUBJECT_GROUPS_TABLE
  }

  String getSubjectStateOn() {
    return SUBJECT_STATE_ON
  }

  Number getSubjectStateOnId() {
    return getRefIdByCode(getSubjectStateOn())
  }

  String getSubjectStateLocked() {
    return SUBJECT_STATE_LOCKED
  }

  Number getSubjectStateLockedId() {
    return getRefIdByCode(getSubjectStateLocked())
  }

  String getSubjectStateSuspended() {
    return SUBJECT_STATE_SUSPENDED
  }

  Number getSubjectStateSuspendedId() {
    return getRefIdByCode(getSubjectStateSuspended())
  }

  String getSubjectStateDisabled() {
    return SUBJECT_STATE_DISABLED
  }

  Number getSubjectStateDisabledId() {
    return getRefIdByCode(getSubjectStateDisabled())
  }

  String getSubjectCommentType() {
    return SUBJECT_COMMENT_TYPE
  }

  Number getSubjectCommentTypeId() {
    return getRefIdByCode(getSubjectCommentType())
  }

  List getSubjectsBy(Map input) {
    LinkedHashMap params = mergeParams([
      subjectId        : null,
      baseSubjectId    : null,
      parentSubjectId  : null,
      typeId           : null,
      regionId         : null,
      ownerId          : null,
      creatorId        : null,
      name             : null,
      code             : null,
      firmId           : getFirmId(),
      resellerId       : getResellerId(),
      stateId          : getSubjectStateOnId(),
      tags             : null,
      limit            : 0
    ], input)
    LinkedHashMap where = [:]

    if (params.subjectId) {
      where.n_subject_id = params.subjectId
    }
    if (params.baseSubjectId) {
      where.n_base_subject_id = params.baseSubjectId
    }
    if (params.parentSubjectId) {
      where.n_parent_subj_id = params.parentSubjectId
    }
    if (params.typeId) {
      where.n_subj_type_id = params.typeId
    }
    if (params.regionId) {
      where.n_region_id = params.regionId
    }
    if (params.ownerId) {
      where.n_owner_id = params.ownerId
    }
    if (params.creatorId) {
      where.n_creator_id = params.creatorId
    }
    if (params.name) {
      where.vc_name = params.name
    }
    if (params.code) {
      where.vc_code = params.code
    }
    if (params.firmId) {
      where.n_firm_id = params.firmId
    }
    if (params.resellerId) {
      where.n_reseller_id = params.resellerId
    }
    if (params.stateId) {
      where.n_subj_state_id = params.stateId
    }
    if (params.tags) {
      where.t_tags = params.tags
    }
    return hid.getTableData(getSubjectsTable(), where: where, limit: params.limit)
  }

  Map getSubjectBy(Map input) {
    return getSubjectsBy(input + [limit: 1])?.getAt(0)
  }

  Map getSubject(def subjectId) {
    LinkedHashMap where = [
      n_subject_id: subjectId
    ]
    return hid.getTableFirst(getSubjectsTable(), where: where)
  }

  Number getSubjectTypeId(def subjectId) {
    LinkedHashMap where = [
      n_subject_id: subjectId
    ]
    return toIntSafe(hid.getTableFirst(getSubjectsTable(), 'n_subj_type_id', where))
  }

  Boolean isSubject(CharSequence entityType) {
    return entityType.contains('SUBJ_TYPE_')
  }

  Boolean isSubject(def entityIdOrEntityTypeId) {
    return getRefCodeById(entityIdOrEntityTypeId)?.contains('SUBJ_TYPE_') || getSubject(entityIdOrEntityTypeId) != null
  }

  Boolean changeSubjectState(
    def subjectId,
    def stateId
  ) {
    try {
      logger.info("Changing subject ${subjectId} state to ${stateId}")
      hid.execute('SI_SUBJECTS_PKG.CHANGE_STATE', [
        num_N_SUBJECT_ID    : subjectId,
        num_N_SUBJ_STATE_ID : stateId
      ])
      logger.info("   Subject state was changed successfully!")
      return true
    } catch (Exception e){
      logger.error("   Error while changing subject state!")
      logger.error_oracle(e)
      return false
    }
  }

  Boolean enableSubject(def subjectId) {
    return changeSubjectState(subjectId, getSubjectStateOnId())
  }

  Boolean suspendSubject(def subjectId) {
    return changeSubjectState(subjectId, getSubjectStateSuspendedId())
  }

  Boolean disableSubject(def subjectId) {
    return changeSubjectState(subjectId, getSubjectStateDisabledId())
  }

  Map getSubjectAddParamType(def paramId) {
    LinkedHashMap where = [
      n_subj_value_type_id: paramId
    ]
    return hid.getTableFirst(getSubjectAddParamTypesTable(), where: where)
  }

  List getSubjectAddParamTypesBy(Map input) {
    def params = mergeParams([
      subjValueTypeId : null,
      subjTypeId      : null,
      dataTypeId      : null,
      code            : null,
      name            : null,
      refTypeId       : null,
      canModify       : null,
      isMulti         : null,
      isReadOnly      : null,
      rem             : null,
      limit           : 0
    ], input)
    LinkedHashMap where = [:]

    if (params.subjValueTypeId || params.paramId) {
      where.n_subj_value_type_id = params.subjValueTypeId ?: params.paramId
    }
    if (params.subjTypeId) {
      where.n_subj_type_id = params.subjTypeId
    }
    if (params.dataTypeId) {
      where.n_data_type_id = params.dataTypeId
    }
    if (params.code) {
      where.vc_code = params.code
    }
    if (params.name) {
      where.vc_name = params.name
    }
    if (params.refTypeId || params.refId) {
      where.n_ref_type_id = params.refTypeId ?: params.refId
    }
    if (params.canModify != null) {
      where.c_can_modify = encodeBool(params.canModify)
    }
    if (params.isMulti != null) {
      where.c_fl_multi = encodeBool(params.isMulti)
    }
    if (params.isReadOnly != null) {
      where.c_fl_read_only = encodeBool(params.isReadOnly)
    }
    return hid.getTableData(getSubjectAddParamTypesTable(), where: where, limit: params.limit)
  }

  Map getSubjectAddParamTypeBy(Map input) {
    return getSubjectAddParamTypesBy(input + [limit: 1])?.getAt(0)
  }

  Map getSubjectAddParamTypeByCode(CharSequence code, def subjTypeId = null) {
    return getSubjectAddParamTypeBy(code: code, subjTypeId: subjTypeId)
  }

  Map prepareSubjectAddParam(Map input) {
    LinkedHashMap param = null
    if (input.containsKey('param')) {
      param = getSubjectAddParamTypeByCode(input.param.toString(), getSubjectTypeId(input.subjectId))
      input.paramId = param?.n_subj_value_type_id
      input.remove('param')
    } else if (input.containsKey('paramId')) {
      param = getSubjectAddParamType(input.paramId)
    }
    input.isMultiple = decodeBool(param.c_fl_multi)

    if (input.containsKey('value')) {
      String valueType = getAddParamDataType(param)
      input."${valueType}" = input.value
      input.remove('value')
    }
    return input
  }

  List getSubjectAddParamsBy(Map input) {
    LinkedHashMap params = mergeParams([
      subjValueId : null,
      subjectId   : null,
      paramId     : null,
      date        : null,
      string      : null,
      number      : null,
      bool        : null,
      refId       : null,
      limit       : 0
    ], prepareSubjectAddParam(input))
    LinkedHashMap where = [:]

    if (params.subjValueId) {
      where.n_subj_value_id = params.subjValueId
    }
    if (params.subjectId) {
      where.n_subject_id = params.subjectId
    }
    if (params.paramId) {
      where.n_subj_value_type_id = params.paramId
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
      where.c_fl_value = encodeBool(params.bool)
    }
    if (params.refId) {
      where.n_ref_id = params.refId
    }
    return hid.getTableData(getSubjectAddParamsTable(), where: where, limit: params.limit)
  }

  Map getSubjectAddParamBy(Map input) {
    return getSubjectAddParamsBy(input + [limit: 1])?.getAt(0)
  }

  Map putSubjectAddParam(Map input) {
    LinkedHashMap params = mergeParams([
      subjValueId : null,
      subjectId   : null,
      paramId     : null,
      date        : null,
      string      : null,
      number      : null,
      bool        : null,
      refId       : null
    ], prepareSubjectAddParam(input))
    try {

      if (!params.subjValueId && !params.isMultiple) {
        params.subjValueId = getSubjectAddParamBy(
          subjectId : input.subjectId,
          paramId   : input.paramId
        )?.n_subj_value_id
      }

      logger.info("${params.subjValueId ? 'Putting' : 'Creating'} subject additional value with params ${params}")
      LinkedHashMap result = hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_VALUES_PUT', [
        num_N_SUBJ_VALUE_ID      : params.subjValueId,
        num_N_SUBJECT_ID         : params.subjectId,
        num_N_SUBJ_VALUE_TYPE_ID : params.paramId,
        dt_D_VALUE               : params.date,
        vch_VC_VALUE             : params.string,
        num_N_VALUE              : params.number,
        ch_C_FL_VALUE            : encodeBool(params.bool),
        num_N_REF_ID             : params.refId
      ])
      logger.info("   Subject additional value was ${params.subjValueId ? 'put' : 'created'} successfully!")
      return result
    } catch (Exception e){
      logger.error("   Error while putting or creating subject additional value!")
      logger.error_oracle(e)
      return null
    }
  }

  Map addSubjectAddParam(Map input) {
    return putSubjectAddParam(input)
  }

  Map addSubjectAddParam(def subjectId, Map input) {
    return putSubjectAddParam(input + [subjectId: subjectId])
  }

  Map addSubjectAddParam(Map input, def subjectId) {
    return putSubjectAddParam(subjectId, input)
  }

  Boolean deleteSubjectAddParam(def subjValueId) {
    try {
      logger.info("Deleting subject additional value id ${subjValueId}")
      hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_VALUES_DEL', [
        num_N_SUBJ_VALUE_ID : subjValueId
      ])
      logger.info("   Subject additional value was deleted successfully!")
      return true
    } catch (Exception e){
      logger.error("   Error while deleting subject additional value!")
      logger.error_oracle(e)
      return false
    }
  }

  Boolean deleteSubjectAddParam(Map input) {
    def subjValueId = getSubjectAddParamBy(input)?.n_subj_value_id
    return deleteSubjectAddParam(subjValueId)
  }

  List getSubjectGroupsBy(Map input) {
    LinkedHashMap params = mergeParams([
      subjectId : null,
      groupId   : null,
      isMain    : null,
      limit     : 0
    ], input)
    LinkedHashMap where = [:]
    LinkedHashMap order = [c_fl_main: 'DESC']

    if (params.subjectId) {
      where.n_subject_id = params.subjectId
    }
    if (params.groupId) {
      where.n_subj_group_id = params.groupId
    }
    if (params.isMain != null) {
      where.c_fl_main = encodeBool(params.isMain)
    }
    return hid.getTableData(getSubjectGroupsTable(), where: where, order: order, limit: params.limit)
  }

  Map getSubjectGroupBy(Map input) {
    return getSubjectGroupsBy(input + [limit: 1])?.getAt(0)
  }

  List getSubjectGroups(def subjectId) {
    return getSubjectGroupsBy(subjectId: subjectId)
  }

  Map getSubjectGroup(def subjectId) {
    return getSubjectGroupBy(subjectId: subjectId, isMain: true)
  }

  Map putSubjectGroup(Map input) {
    LinkedHashMap params = mergeParams([
      subjSubjectId : null,
      subjectId     : null,
      groupId       : null,
      isMain        : null
    ], input)
    try {
      logger.info("Putting subject id ${params.subjectId} group id ${params.groupId} with main flag ${params.isMain}")

      LinkedHashMap subjSubject = hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_SUBJECTS_PUT', [
        num_N_SUBJ_SUBJECT_ID : params.subjSubjectId,
        num_N_SUBJECT_ID      : params.subjectId,
        num_N_SUBJECT_BIND_ID : params.groupId,
        ch_C_FL_MAIN          : encodeBool(params.isMain)
      ])
      logger.info("   Subject group was put successfully!")
      return subjSubject
    } catch (Exception e){
      logger.error("   Error while putting subject group!")
      logger.error_oracle(e)
      return null
    }
  }

  Boolean addSubjectGroup(Map input) {
    return putSubjectGroup(input)
  }

  Boolean addSubjectGroup(def subjectId, Map input) {
    return putSubjectGroup(input + [subjectId: subjectId])
  }

  Boolean addSubjectGroup(Map input, def subjectId) {
    return putSubjectGroup(subjectId, input)
  }

  Boolean deleteSubjectGroup(Map input) {
    LinkedHashMap params = mergeParams([
      subjSubjectId : null,
      subjectId     : null,
      groupId       : null,
      isMain        : null
    ], input)
    try {
      if (params.subjSubjectId == null) {
        def group = getSubjectGroupBy(input)
        if (group) {
          params.subjSubjectId = group.n_subj_subject_id
        } else {
          throw new Exception('No group found!')
        }
      }

      logger.info("Deleting subject group id ${subjSubjectId}")
      hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_SUBJECTS_DEL', [
        num_N_SUBJ_SUBJECT_ID : subjSubjectId
      ])
      logger.info("   Subject group was deleted successfully!")
      return true
    } catch (Exception e){
      logger.error("   Error while deleting a subject group!")
      logger.error_oracle(e)
      return false
    }
  }

  Boolean deleteSubjectGroup(def subjSubjectId) {
    return deleteSubjGroup(subjSubjectId: subjSubjectId)
  }

  Map putSubjectComment(Map input) {
    LinkedHashMap params = mergeParams([
      lineId        : null,
      subjectId     : null,
      typeId        : getSubjectCommentTypeId(),
      operationDate : local(),
      signalDate    : null,
      content       : null,
      authorId      : null
    ], input)
    try {
      logger.info("Putting subject id ${params.subjectId} comment line ${params.lineId} with content ${params.content} and signal date ${params.signalDate}")

      LinkedHashMap subjComment = hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_COMMENTS_PUT', [
        num_N_LINE_ID         : params.lineId,
        num_N_SUBJECT_ID      : params.subjectId,
        num_N_COMMENT_TYPE_ID : params.typeId,
        dt_D_OPER             : params.operationDate,
        dt_D_SIGNAL           : params.signalDate,
        clb_CL_COMMENT        : params.content
      ] + (params.authorId != null ?
      [
        num_N_AUTHOR_ID       : params.authorId
      ] : [:])
      )
      logger.info("   Subject comment was put successfully!")
      return subjComment
    } catch (Exception e){
      logger.error("   Error while putting subject comment!")
      logger.error_oracle(e)
      return null
    }
  }

  Boolean addSubjectComment(Map input) {
    return putSubjectComment(input)
  }

  Boolean addSubjectComment(def subjectId, Map input) {
    return putSubjectComment(input + [subjectId: subjectId])
  }

  Boolean addSubjectComment(Map input, def subjectId) {
    return putSubjectComment(subjectId, input)
  }

  Boolean deleteSubjectComment(def lineId) {
    try {
      logger.info("Deleting subject comment line id ${lineId}")
      hid.execute('SI_SUBJECTS_PKG.SI_SUBJ_COMMENTS_DEL', [
        num_N_LINE_ID : lineId
      ])
      logger.info("   Subject comment was deleted successfully!")
      return true
    } catch (Exception e){
      logger.error("   Error while deleting a subject comment!")
      logger.error_oracle(e)
      return false
    }
  }
}