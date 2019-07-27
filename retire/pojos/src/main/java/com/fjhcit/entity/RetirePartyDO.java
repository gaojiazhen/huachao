package com.fjhcit.entity;

/**
 * @description 离（退）休人员_党组织设置情况库_实体类 
 * @author 陈麟
 * @date 2019年06月17日 上午08:48:46
 */
public class RetirePartyDO{
    private String id;                            //ID主键
    private String unit_id;                       //单位ID（base_department）
    private String gmt_statistics;                //统计时间
    private String party_general_branch;          //党总支数
    private String party_branch_number;           //党支部数
    private String party_group_number;            //党小组数
    private String quit_cadre_party_branch;       //离休干部党支部数
    private String retire_combine_number;         //离/退休合编数
    private String retire_party_branch;           //退休人员党支部数
    private String quit_inservice_combine;        //离休与在职合编数
    private String retire_inservice_combine;      //退休与在职合编数
    private String party_general_branch_secretary;//党总支书记
    private String party_general_branch_member;   //支委委员
    private String party_branch_secretary1;       //第一党支部书记
    private String branch_committee_member1;      //支委委员
    private String party_branch_secretary2;       //第二党支部书记
    private String branch_committee_member2;      //支委委员
    private String party_branch_secretary3;       //第三党支部书记
    private String branch_committee_member3;      //支委委员
    private String party_branch_secretary4;       //第四党支部书记
    private String branch_committee_member4;      //支委委员
    private String party_branch_secretary5;       //第五党支部书记
    private String branch_committee_member5;      //支委委员
    private String party_branch_secretary6;       //第六党支部书记
    private String branch_committee_member6;      //支委委员
    private String party_branch_secretary7;       //第七党支部书记
    private String branch_committee_member7;      //支委委员
    private String party_branch_secretary8;       //第八党支部书记
    private String branch_committee_member8;      //支委委员
    private String party_branch_secretary9;       //第九党支部书记
    private String branch_committee_member9;      //支委委员
    private String party_branch_secretary10;      //第十党支部书记
    private String branch_committee_member10;     //支委委员
    private String party_branch_secretary11;      //第十一党支部书记
    private String branch_committee_member11;     //支委委员
    private String party_branch_secretary12;      //第十二党支部书记
    private String branch_committee_member12;     //支委委员
    private String party_branch_secretary13;      //第十三党支部书记
    private String branch_committee_member13;     //支委委员
    private String party_branch_secretary14;      //第十四党支部书记
    private String branch_committee_member14;     //支委委员
    private String party_branch_secretary15;      //第十五党支部书记
    private String branch_committee_member15;     //支委委员
    private String create_user_id;                //创建人ID（base_user）
    private String gmt_create;                    //创建时间
    private String modified_user_id;              //维护人ID（base_user）
    private String gmt_modified;                  //维护时间
    
    /**
     * @description 以下是辅助字段
     */
    private String unit_name;		//单位名称
    private String company_party;	//公司管理党员数
    private String place_party;		//地方管理党员数
    private String modified_user_name;	//修改人姓名


    @Override
    public String toString() {
        return "RetirePartyDO{" +
                "id='" + id + '\'' +
                ", unit_id='" + unit_id + '\'' +
                ", gmt_statistics='" + gmt_statistics + '\'' +
                ", party_general_branch='" + party_general_branch + '\'' +
                ", party_branch_number='" + party_branch_number + '\'' +
                ", party_group_number='" + party_group_number + '\'' +
                ", quit_cadre_party_branch='" + quit_cadre_party_branch + '\'' +
                ", retire_combine_number='" + retire_combine_number + '\'' +
                ", retire_party_branch='" + retire_party_branch + '\'' +
                ", quit_inservice_combine='" + quit_inservice_combine + '\'' +
                ", retire_inservice_combine='" + retire_inservice_combine + '\'' +
                ", party_general_branch_secretary='" + party_general_branch_secretary + '\'' +
                ", party_general_branch_member='" + party_general_branch_member + '\'' +
                ", party_branch_secretary1='" + party_branch_secretary1 + '\'' +
                ", branch_committee_member1='" + branch_committee_member1 + '\'' +
                ", party_branch_secretary2='" + party_branch_secretary2 + '\'' +
                ", branch_committee_member2='" + branch_committee_member2 + '\'' +
                ", party_branch_secretary3='" + party_branch_secretary3 + '\'' +
                ", branch_committee_member3='" + branch_committee_member3 + '\'' +
                ", party_branch_secretary4='" + party_branch_secretary4 + '\'' +
                ", branch_committee_member4='" + branch_committee_member4 + '\'' +
                ", party_branch_secretary5='" + party_branch_secretary5 + '\'' +
                ", branch_committee_member5='" + branch_committee_member5 + '\'' +
                ", party_branch_secretary6='" + party_branch_secretary6 + '\'' +
                ", branch_committee_member6='" + branch_committee_member6 + '\'' +
                ", party_branch_secretary7='" + party_branch_secretary7 + '\'' +
                ", branch_committee_member7='" + branch_committee_member7 + '\'' +
                ", party_branch_secretary8='" + party_branch_secretary8 + '\'' +
                ", branch_committee_member8='" + branch_committee_member8 + '\'' +
                ", party_branch_secretary9='" + party_branch_secretary9 + '\'' +
                ", branch_committee_member9='" + branch_committee_member9 + '\'' +
                ", party_branch_secretary10='" + party_branch_secretary10 + '\'' +
                ", branch_committee_member10='" + branch_committee_member10 + '\'' +
                ", party_branch_secretary11='" + party_branch_secretary11 + '\'' +
                ", branch_committee_member11='" + branch_committee_member11 + '\'' +
                ", party_branch_secretary12='" + party_branch_secretary12 + '\'' +
                ", branch_committee_member12='" + branch_committee_member12 + '\'' +
                ", party_branch_secretary13='" + party_branch_secretary13 + '\'' +
                ", branch_committee_member13='" + branch_committee_member13 + '\'' +
                ", party_branch_secretary14='" + party_branch_secretary14 + '\'' +
                ", branch_committee_member14='" + branch_committee_member14 + '\'' +
                ", party_branch_secretary15='" + party_branch_secretary15 + '\'' +
                ", branch_committee_member15='" + branch_committee_member15 + '\'' +
                ", create_user_id='" + create_user_id + '\'' +
                ", gmt_create='" + gmt_create + '\'' +
                ", modified_user_id='" + modified_user_id + '\'' +
                ", gmt_modified='" + gmt_modified + '\'' +
                ", unit_name='" + unit_name + '\'' +
                ", company_party='" + company_party + '\'' +
                ", place_party='" + place_party + '\'' +
                ", modified_user_name='" + modified_user_name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getGmt_statistics() {
        return gmt_statistics;
    }

    public void setGmt_statistics(String gmt_statistics) {
        this.gmt_statistics = gmt_statistics;
    }

    public String getParty_general_branch() {
        return party_general_branch;
    }

    public void setParty_general_branch(String party_general_branch) {
        this.party_general_branch = party_general_branch;
    }

    public String getParty_branch_number() {
        return party_branch_number;
    }

    public void setParty_branch_number(String party_branch_number) {
        this.party_branch_number = party_branch_number;
    }

    public String getParty_group_number() {
        return party_group_number;
    }

    public void setParty_group_number(String party_group_number) {
        this.party_group_number = party_group_number;
    }

    public String getQuit_cadre_party_branch() {
        return quit_cadre_party_branch;
    }

    public void setQuit_cadre_party_branch(String quit_cadre_party_branch) {
        this.quit_cadre_party_branch = quit_cadre_party_branch;
    }

    public String getRetire_combine_number() {
        return retire_combine_number;
    }

    public void setRetire_combine_number(String retire_combine_number) {
        this.retire_combine_number = retire_combine_number;
    }

    public String getRetire_party_branch() {
        return retire_party_branch;
    }

    public void setRetire_party_branch(String retire_party_branch) {
        this.retire_party_branch = retire_party_branch;
    }

    public String getQuit_inservice_combine() {
        return quit_inservice_combine;
    }

    public void setQuit_inservice_combine(String quit_inservice_combine) {
        this.quit_inservice_combine = quit_inservice_combine;
    }

    public String getRetire_inservice_combine() {
        return retire_inservice_combine;
    }

    public void setRetire_inservice_combine(String retire_inservice_combine) {
        this.retire_inservice_combine = retire_inservice_combine;
    }

    public String getParty_general_branch_secretary() {
        return party_general_branch_secretary;
    }

    public void setParty_general_branch_secretary(String party_general_branch_secretary) {
        this.party_general_branch_secretary = party_general_branch_secretary;
    }

    public String getParty_general_branch_member() {
        return party_general_branch_member;
    }

    public void setParty_general_branch_member(String party_general_branch_member) {
        this.party_general_branch_member = party_general_branch_member;
    }

    public String getParty_branch_secretary1() {
        return party_branch_secretary1;
    }

    public void setParty_branch_secretary1(String party_branch_secretary1) {
        this.party_branch_secretary1 = party_branch_secretary1;
    }

    public String getBranch_committee_member1() {
        return branch_committee_member1;
    }

    public void setBranch_committee_member1(String branch_committee_member1) {
        this.branch_committee_member1 = branch_committee_member1;
    }

    public String getParty_branch_secretary2() {
        return party_branch_secretary2;
    }

    public void setParty_branch_secretary2(String party_branch_secretary2) {
        this.party_branch_secretary2 = party_branch_secretary2;
    }

    public String getBranch_committee_member2() {
        return branch_committee_member2;
    }

    public void setBranch_committee_member2(String branch_committee_member2) {
        this.branch_committee_member2 = branch_committee_member2;
    }

    public String getParty_branch_secretary3() {
        return party_branch_secretary3;
    }

    public void setParty_branch_secretary3(String party_branch_secretary3) {
        this.party_branch_secretary3 = party_branch_secretary3;
    }

    public String getBranch_committee_member3() {
        return branch_committee_member3;
    }

    public void setBranch_committee_member3(String branch_committee_member3) {
        this.branch_committee_member3 = branch_committee_member3;
    }

    public String getParty_branch_secretary4() {
        return party_branch_secretary4;
    }

    public void setParty_branch_secretary4(String party_branch_secretary4) {
        this.party_branch_secretary4 = party_branch_secretary4;
    }

    public String getBranch_committee_member4() {
        return branch_committee_member4;
    }

    public void setBranch_committee_member4(String branch_committee_member4) {
        this.branch_committee_member4 = branch_committee_member4;
    }

    public String getParty_branch_secretary5() {
        return party_branch_secretary5;
    }

    public void setParty_branch_secretary5(String party_branch_secretary5) {
        this.party_branch_secretary5 = party_branch_secretary5;
    }

    public String getBranch_committee_member5() {
        return branch_committee_member5;
    }

    public void setBranch_committee_member5(String branch_committee_member5) {
        this.branch_committee_member5 = branch_committee_member5;
    }

    public String getParty_branch_secretary6() {
        return party_branch_secretary6;
    }

    public void setParty_branch_secretary6(String party_branch_secretary6) {
        this.party_branch_secretary6 = party_branch_secretary6;
    }

    public String getBranch_committee_member6() {
        return branch_committee_member6;
    }

    public void setBranch_committee_member6(String branch_committee_member6) {
        this.branch_committee_member6 = branch_committee_member6;
    }

    public String getParty_branch_secretary7() {
        return party_branch_secretary7;
    }

    public void setParty_branch_secretary7(String party_branch_secretary7) {
        this.party_branch_secretary7 = party_branch_secretary7;
    }

    public String getBranch_committee_member7() {
        return branch_committee_member7;
    }

    public void setBranch_committee_member7(String branch_committee_member7) {
        this.branch_committee_member7 = branch_committee_member7;
    }

    public String getParty_branch_secretary8() {
        return party_branch_secretary8;
    }

    public void setParty_branch_secretary8(String party_branch_secretary8) {
        this.party_branch_secretary8 = party_branch_secretary8;
    }

    public String getBranch_committee_member8() {
        return branch_committee_member8;
    }

    public void setBranch_committee_member8(String branch_committee_member8) {
        this.branch_committee_member8 = branch_committee_member8;
    }

    public String getParty_branch_secretary9() {
        return party_branch_secretary9;
    }

    public void setParty_branch_secretary9(String party_branch_secretary9) {
        this.party_branch_secretary9 = party_branch_secretary9;
    }

    public String getBranch_committee_member9() {
        return branch_committee_member9;
    }

    public void setBranch_committee_member9(String branch_committee_member9) {
        this.branch_committee_member9 = branch_committee_member9;
    }

    public String getParty_branch_secretary10() {
        return party_branch_secretary10;
    }

    public void setParty_branch_secretary10(String party_branch_secretary10) {
        this.party_branch_secretary10 = party_branch_secretary10;
    }

    public String getBranch_committee_member10() {
        return branch_committee_member10;
    }

    public void setBranch_committee_member10(String branch_committee_member10) {
        this.branch_committee_member10 = branch_committee_member10;
    }

    public String getParty_branch_secretary11() {
        return party_branch_secretary11;
    }

    public void setParty_branch_secretary11(String party_branch_secretary11) {
        this.party_branch_secretary11 = party_branch_secretary11;
    }

    public String getBranch_committee_member11() {
        return branch_committee_member11;
    }

    public void setBranch_committee_member11(String branch_committee_member11) {
        this.branch_committee_member11 = branch_committee_member11;
    }

    public String getParty_branch_secretary12() {
        return party_branch_secretary12;
    }

    public void setParty_branch_secretary12(String party_branch_secretary12) {
        this.party_branch_secretary12 = party_branch_secretary12;
    }

    public String getBranch_committee_member12() {
        return branch_committee_member12;
    }

    public void setBranch_committee_member12(String branch_committee_member12) {
        this.branch_committee_member12 = branch_committee_member12;
    }

    public String getParty_branch_secretary13() {
        return party_branch_secretary13;
    }

    public void setParty_branch_secretary13(String party_branch_secretary13) {
        this.party_branch_secretary13 = party_branch_secretary13;
    }

    public String getBranch_committee_member13() {
        return branch_committee_member13;
    }

    public void setBranch_committee_member13(String branch_committee_member13) {
        this.branch_committee_member13 = branch_committee_member13;
    }

    public String getParty_branch_secretary14() {
        return party_branch_secretary14;
    }

    public void setParty_branch_secretary14(String party_branch_secretary14) {
        this.party_branch_secretary14 = party_branch_secretary14;
    }

    public String getBranch_committee_member14() {
        return branch_committee_member14;
    }

    public void setBranch_committee_member14(String branch_committee_member14) {
        this.branch_committee_member14 = branch_committee_member14;
    }

    public String getParty_branch_secretary15() {
        return party_branch_secretary15;
    }

    public void setParty_branch_secretary15(String party_branch_secretary15) {
        this.party_branch_secretary15 = party_branch_secretary15;
    }

    public String getBranch_committee_member15() {
        return branch_committee_member15;
    }

    public void setBranch_committee_member15(String branch_committee_member15) {
        this.branch_committee_member15 = branch_committee_member15;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getModified_user_id() {
        return modified_user_id;
    }

    public void setModified_user_id(String modified_user_id) {
        this.modified_user_id = modified_user_id;
    }

    public String getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(String gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getCompany_party() {
        return company_party;
    }

    public void setCompany_party(String company_party) {
        this.company_party = company_party;
    }

    public String getPlace_party() {
        return place_party;
    }

    public void setPlace_party(String place_party) {
        this.place_party = place_party;
    }

    public String getModified_user_name() {
        return modified_user_name;
    }

    public void setModified_user_name(String modified_user_name) {
        this.modified_user_name = modified_user_name;
    }
}