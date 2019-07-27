var web = 'http://localhost:9001/web/baseUser';
var base = 'http://localhost:5555/base';
var retire = 'http://localhost:5555/retire';
//var web = 'http://192.168.1.23:9001/web/baseUser';
//var base = 'http://192.168.1.23:5555/base';
//var retire = 'http://192.168.1.23:5555/retire';

/**
 * 一、web拦截服务模块
 */
//1.1登录、退出
var login = web + "/zuul/login";
var signOut= web + "/zuul/signOut";
/**
 * 二、系统公共功能模块
 */
//2.1人员
var baseUser = base + '/baseUser';
var listMenuByLoginUser = baseUser + "/listMenuByLoginUser";
var updatePasswordByLoginUser = baseUser + "/updatePasswordByLoginUser";
var verifyOldPasswordByLoginUser = baseUser + "/verifyOldPasswordByLoginUser";
//2.2单位
var baseDepartment = base + '/baseDepartment';
var listBaseDepartment = baseDepartment + "/listBaseDepartment";
var listBaseDepartmentByPaging = baseDepartment + "/listBaseDepartmentByPaging";
var getBaseDepartmentNextSortnum = baseDepartment +  "/getBaseDepartmentNextSortnum";
var saveBaseDepartment = baseDepartment + "/saveBaseDepartment";
var removeBaseDepartment =baseDepartment +"/removeBaseDepartment"

// 组织机构管理
var departmentInfo= baseDepartment+"/departmentInfo";
var deleteDepartment = baseDepartment+'/deleteDepartment';
var editDepartment = baseDepartment+'/editDepartment';
//2.3数据字典下拉框数据
var baseCode = base + '/baseCode';
var listBaseCode = baseCode + "/listBaseCode";
var cache = base+'/cache';
var getCode = cache+'/getCode';
var getCategoryCode=cache+"/getCategoryCode";
//2.4附件上传、下载
var baseFile = base + '/baseFile';
var uploadFile = baseFile + '/uploadFile';
var downFile = baseFile + '/downFile'

// 菜单管理
var baseMenu = base+'/menus';
var findMenu = baseMenu+'/findMenu';
var deleteM = baseMenu+'/deleteMenu';
var selectM = baseMenu+'/selectMenu';
var editMenu = baseMenu+'/editMenu';

// 数据字典
var baseSysCodeKind= base+'/sysCodeKind';
var codeKindInfo = baseSysCodeKind+"/sysCodeKindInfo";
var deleteCodeKind = baseSysCodeKind+"/deleteSysCodeKind";
var editCodeKind = baseSysCodeKind+"/editSysCodeKind";
var codeInfo = baseSysCodeKind+"/sysCodeInfo";
var editCode = baseSysCodeKind+"/editSysCode";
var updateSysCode = baseSysCodeKind+"/updateSysCode";
var addSysCode = baseSysCodeKind+"/addSysCode";
var addSysCodeAddI = baseSysCodeKind+"/addSysCodeAddI";
// 角色管理
var baseRole = base+'/role';
var roleInfo = baseRole+"/roleInfo";
var addRole = baseRole+"/addRole";
var deleteRole = baseRole+"/deleteRole";
var selectRoleMenu = baseRole+"/selectRoleMenu";
var updateRoleMenu = baseRole+"/updateRoleMenu";
// 用户权限管理
var baseUserControl = base+'/userControl';
var userControlInfo = baseUserControl+"/userControlInfo";
var selectUserRole = baseUserControl+"/selectUserRole";
var updateUserRole = baseUserControl+"/updateUserRole";
var selectUserDep = baseUserControl+"/selectUserDep";
var updateUserDep = baseUserControl+"/updateUserDep";
var resetPwd = baseUserControl+"/resetPwd";
var updatePwd = baseUserControl+"/updatePwd";
var updateUsetState = baseUserControl+"/updateUsetState";

/**
 * 三、离退休业务模块
 */
//3.1离退休人员基本信息
var retireUser = retire + '/retireUser';
//var listRetireUser = retireUser + "/listRetireUser";
var listRetireUserByPaging = retireUser + "/listRetireUserByPaging";
var saveRetireUser = retireUser + "/saveRetireUser";
var removeRetireUser = retireUser + "/removeRetireUser";
var saveRetireUserByExcel = retireUser + "/saveRetireUserByExcel";
//3.2离休人员库
var retireQuit = retire + '/retireQuit';
var listRetireQuitUser = retireQuit + "/listRetireQuitUser";
var saveRetireQuit = retireQuit + "/saveRetireQuit";
var removeRetireQuit = retireQuit + "/removeRetireQuit";
var saveRetireQuitByExcel = retireQuit + "/saveRetireQuitByExcel";
//3.2中共党员库
var retireCommunist = retire + '/retireCommunist';
var listRetireCommunistByPaging = retireCommunist + "/listRetireCommunistByPaging";
var removeRetireCommunist = retireCommunist + "/removeRetireCommunist";
var saveRetireCommunist = retireCommunist + "/saveRetireCommunist";
var getRetireCommunistByUserid = retireCommunist + "/getRetireCommunistByUserid";
var saveRetireCommunistByExcel = retireCommunist + "/saveRetireCommunistByExcel";
//3.3家庭主要成员表
var retireFamily = retire + '/retireFamily';
var listRetireFamily = retireFamily + '/listRetireFamily';
//3.4通讯录
var retireContact = retire + '/retireContact';
var listRetireContactByPaging = retireContact + '/listRetireContactByPaging';
var removeRetireContact = retireContact + '/removeRetireContact';
var saveRetireContact = retireContact + '/saveRetireContact';
var getRetireContactNextSortnum = retireContact + '/getRetireContactNextSortnum';
var saveRetireContactByExcel = retireContact + '/saveRetireContactByExcel';
//3.5党组织设置情况
var retireParty = retire + '/retireParty';
var listRetireParty = retireParty + '/listRetireParty';
var saveRetireParty = retireParty + '/saveRetireParty';
//3.6国网统计报表
var listQuitCadreBiennialChange = retireUser + "/listQuitCadreBiennialChange";
var excelQuitCadreBiennialChange = retireUser + "/excelQuitCadreBiennialChange";
var listQuitCadreByRegion = retireUser + "/listQuitCadreByRegion";
var excelQuitCadreByRegion = retireUser + "/excelQuitCadreByRegion";
var listQuitCadreBasic = retireUser + "/listQuitCadreBasic";
var excelQuitCadreBasic = retireUser + "/excelQuitCadreBasic";
var listRetireCadreTreatment = retireUser + "/listRetireCadreTreatment";
var excelRetireCadreTreatment = retireUser + "/excelRetireCadreTreatment";
var listRetireCadreBiennialChange = retireUser + "/listRetireCadreBiennialChange";
var excelRetireCadreBiennialChange = retireUser + "/excelRetireCadreBiennialChange";
var listRetireWorkerBiennialChange = retireUser + "/listRetireWorkerBiennialChange";
var excelRetireWorkerBiennialChange = retireUser + "/excelRetireWorkerBiennialChange";
var listQuitWorkingBiennialChange = retireUser + "/listQuitWorkingBiennialChange";
var excelQuitWorkingBiennialChange = retireUser + "/excelQuitWorkingBiennialChange";
var listQuitCadreCurrentSituation = retireUser + "/listQuitCadreCurrentSituation";
var excelQuitCadreCurrentSituation = retireUser + "/excelQuitCadreCurrentSituation";
var listRetireCadreCurrentSituation = retireUser + "/listRetireCadreCurrentSituation";
var excelRetireCadreCurrentSituation = retireUser + "/excelRetireCadreCurrentSituation";
var listRetireWorkerCurrentSituation = retireUser + "/listRetireWorkerCurrentSituation";
var excelRetireWorkerCurrentSituation = retireUser + "/excelRetireWorkerCurrentSituation";
var listRetirePartyOrganization = retireParty + '/listRetirePartyOrganization';
var excelRetirePartyOrganization = retireParty + '/excelRetirePartyOrganization';
var listRetireContactStaff = retireContact + '/listRetireContactStaff';
var excelRetireContactStaff = retireContact + '/excelRetireContactStaff';
var listRetirePartyRelation = retireUser + "/listRetirePartyRelation";
var excelRetirePartyRelation = retireUser + "/excelRetirePartyRelation";
var listRetireUserBasic = retireUser + "/listRetireUserBasic";
var excelRetireUserBasic = retireUser + "/excelRetireUserBasic";

var retireCost = retire + '/retireCost';
var listRetireEmployeeCost = retireCost + "/listRetireEmployeeCost";
var excelRetireEmployeeCost = retireCost + "/excelRetireEmployeeCost";
var saveRetireEmployeeCost = retireCost + "/saveRetireEmployeeCost";
var listRetireBasicEndowmentInsurance = retireCost + "/listRetireBasicEndowmentInsurance";
var excelRetireBasicEndowmentInsurance = retireCost + "/excelRetireBasicEndowmentInsurance";
var listRetireUserCorrelativeCharges = retireCost + "/listRetireUserCorrelativeCharges";
var excelRetireUserCorrelativeCharges = retireCost + "/excelRetireUserCorrelativeCharges";
var retireArena = retire + '/retireArena';
var saveRetireArena = retireArena + "/saveRetireArena";
var listRetireArenaAndUniversity = retireArena + "/listRetireArenaAndUniversity";
var excelRetireArenaAndUniversity = retireArena + "/excelRetireArenaAndUniversity";
var listRetireArenaServiceCondition = retireArena + "/listRetireArenaServiceCondition";
var excelRetireArenaServiceCondition = retireArena + "/excelRetireArenaServiceCondition";
var saveRetireArenaByExcel = retireArena + "/saveRetireArenaByExcel";
//var retireOrganization = retire + '/retireOrganization';
//var listRetireOrganizationStatistics = retireOrganization + "/listRetireOrganizationStatistics";
//var excelRetireOrganizationStatistics = retireOrganization + "/excelRetireOrganizationStatistics";
//var saveRetireOrganization = retireOrganization + "/saveRetireOrganization";

var listRetireDepartmentAndCost = retireCost + "/listRetireDepartmentAndCost";
var excelRetireDepartmentAndCost = retireCost + "/excelRetireDepartmentAndCost";

var listRetireUserPersonalInformation = retireUser + "/listRetireUserPersonalInformation";
var excelRetireUserPersonalInformation = retireUser + "/excelRetireUserPersonalInformation";