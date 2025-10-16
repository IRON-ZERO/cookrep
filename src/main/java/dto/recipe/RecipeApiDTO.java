package dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecipeApiDTO {
	@JsonProperty("RCP_SEQ")
	private String rcpSeo;//일련번호

	@JsonProperty("RCP_NM")
	private String rcpNm;// 메뉴명

	@JsonProperty("RCP_WAY2")
	private String rcpWay2;// 조리방법

	@JsonProperty("RCP_PAT2")
	private String rcpPat2; // 요리종류

	@JsonProperty("INFO_WGT")
	private String infoWgt; // 중량(1인분)

	@JsonProperty("INFO_ENG")
	private String infoEng; // 열량

	@JsonProperty("INFO_CAR")
	private String infoCar; // 탄수화물

	@JsonProperty("INFO_PRO")
	private String infoPro; // 단백질

	@JsonProperty("INFO_FAT")
	private String infoFat; // 지방

	@JsonProperty("INFO_NA")
	private String infoNa; // 나트륨

	@JsonProperty("HASH_TAG")
	private String hashTag; // 해쉬태그

	@JsonProperty("ATT_FILE_NO_MAIN")
	private String attFileNoMain; // 이미지경로(소)

	@JsonProperty("ATT_FILE_NO_MK")
	private String attFileNoMk; // 이미지경로(대)

	@JsonProperty("RCP_PARTS_DTLS")
	private String rcpPartsDtls; // 재료정보

	@JsonProperty("MANUAL01")
	private String manual01; //	만드는법_01

	@JsonProperty("MANUAL_IMG01")
	private String manualImg01; //	만드는법_이미지_01

	@JsonProperty("MANUAL02")
	private String manual02; //	만드는법_02

	@JsonProperty("MANUAL_IMG02")
	private String manualImg02; // 만드는법_이미지_02

	@JsonProperty("MANUAL03")
	private String manual03; // 만드는법_03

	@JsonProperty("MANUAL_IMG03")
	private String manualImg03; // 만드는법_이미지_03

	@JsonProperty("MANUAL04")
	private String manual04;//	만드는법_04

	@JsonProperty("MANUAL_IMG04")
	private String manualImg04; //	만드는법_이미지_04

	@JsonProperty("MANUAL05")
	private String manual05;//	만드는법_05

	@JsonProperty("MANUAL_IMG05")
	private String manualImg05; //	만드는법_이미지_05

	@JsonProperty("MANUAL06")
	private String manual06;//	만드는법_06

	@JsonProperty("MANUAL_IMG06")
	private String manualImg06; //	만드는법_이미지_06

	@JsonProperty("MANUAL07")
	private String manual07;//	만드는법_07

	@JsonProperty("MANUAL_IMG07")
	private String manualImg07; //	만드는법_이미지_07

	@JsonProperty("MANUAL08")
	private String manual08;// 만드는법_08

	@JsonProperty("MANUAL_IMG08")
	private String manualImg08; //	만드는법_이미지_08

	@JsonProperty("MANUAL09")
	private String MANUAL09;// 만드는법_09

	@JsonProperty("MANUAL_IMG09")
	private String manualImg09; //	만드는법_이미지_09

	@JsonProperty("MANUAL10")
	private String manual10;//	만드는법_10

	@JsonProperty("MANUAL_IMG10")
	private String manualImg10; //	만드는법_이미지_10

	@JsonProperty("MANUAL11")
	private String manual11;// 만드는법_11

	@JsonProperty("MANUAL_IMG11")
	private String manualImg11; //	만드는법_이미지_11

	@JsonProperty("MANUAL12")
	private String manual12;// 만드는법_12

	@JsonProperty("MANUAL_IMG12")
	private String manualImg12; //	만드는법_이미지_12

	@JsonProperty("MANUAL13")
	private String manual13; // 만드는법_13

	@JsonProperty("MANUAL_IMG13")
	private String manualImg13; //	만드는법_이미지_13

	@JsonProperty("MANUAL14")
	private String manual14; // 만드는법_14

	@JsonProperty("MANUAL_IMG14")
	private String manualImg14; //	만드는법_이미지_14

	@JsonProperty("MANUAL15")
	private String manual15; //만드는법_15

	@JsonProperty("MANUAL_IMG15")
	private String manualImg15;//	만드는법_이미지_15

	@JsonProperty("MANUAL16")
	private String manual16;//	만드는법_16

	@JsonProperty("MANUAL_IMG16")
	private String manualImg16;//	만드는법_이미지_16

	@JsonProperty("MANUAL17")
	private String manual17;//	만드는법_17

	@JsonProperty("MANUAL_IMG17")
	private String manualImg17;//	만드는법_이미지_17

	@JsonProperty("MANUAL18")
	private String manual18;//	만드는법_18

	@JsonProperty("MANUAL_IMG18")
	private String manualImg18;//	만드는법_이미지_18

	@JsonProperty("MANUAL19")
	private String manual19;//	만드는법_19

	@JsonProperty("MANUAL_IMG19")
	private String manualImg19;//	만드는법_이미지_19

	@JsonProperty("MANUAL20")
	private String manual20;//	만드는법_20

	@JsonProperty("MANUAL_IMG20")
	private String manualImg20;//	만드는법_이미지_20

	@JsonProperty("RCP_NA_TIP")
	private String rcpNaTip;//저감 조리법 TIP

	public RecipeApiDTO() {}

	public String getRcpSeo() {
		return rcpSeo;
	}

	public void setRcpSeo(String rcpSeo) {
		this.rcpSeo = rcpSeo;
	}

	public String getRcpNm() {
		return rcpNm;
	}

	public void setRcpNm(String rcpNm) {
		this.rcpNm = rcpNm;
	}

	public String getRcpWay2() {
		return rcpWay2;
	}

	public void setRcpWay2(String rcpWay2) {
		this.rcpWay2 = rcpWay2;
	}

	public String getRcpPat2() {
		return rcpPat2;
	}

	public void setRcpPat2(String rcpPat2) {
		this.rcpPat2 = rcpPat2;
	}

	public String getInfoWgt() {
		return infoWgt;
	}

	public void setInfoWgt(String infoWgt) {
		this.infoWgt = infoWgt;
	}

	public String getInfoEng() {
		return infoEng;
	}

	public void setInfoEng(String infoEng) {
		this.infoEng = infoEng;
	}

	public String getInfoCar() {
		return infoCar;
	}

	public void setInfoCar(String infoCar) {
		this.infoCar = infoCar;
	}

	public String getInfoPro() {
		return infoPro;
	}

	public void setInfoPro(String infoPro) {
		this.infoPro = infoPro;
	}

	public String getInfoFat() {
		return infoFat;
	}

	public void setInfoFat(String infoFat) {
		this.infoFat = infoFat;
	}

	public String getInfoNa() {
		return infoNa;
	}

	public void setInfoNa(String infoNa) {
		this.infoNa = infoNa;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getAttFileNoMain() {
		return attFileNoMain;
	}

	public void setAttFileNoMain(String attFileNoMain) {
		this.attFileNoMain = attFileNoMain;
	}

	public String getAttFileNoMk() {
		return attFileNoMk;
	}

	public void setAttFileNoMk(String attFileNoMk) {
		this.attFileNoMk = attFileNoMk;
	}

	public String getRcpPartsDtls() {
		return rcpPartsDtls;
	}

	public void setRcpPartsDtls(String rcpPartsDtls) {
		this.rcpPartsDtls = rcpPartsDtls;
	}

	public String getManual01() {
		return manual01;
	}

	public void setManual01(String manual01) {
		this.manual01 = manual01;
	}

	public String getManualImg01() {
		return manualImg01;
	}

	public void setManualImg01(String manualImg01) {
		this.manualImg01 = manualImg01;
	}

	public String getManual02() {
		return manual02;
	}

	public void setManual02(String manual02) {
		this.manual02 = manual02;
	}

	public String getManualImg02() {
		return manualImg02;
	}

	public void setManualImg02(String manualImg02) {
		this.manualImg02 = manualImg02;
	}

	public String getManual03() {
		return manual03;
	}

	public void setManual03(String manual03) {
		this.manual03 = manual03;
	}

	public String getManualImg03() {
		return manualImg03;
	}

	public void setManualImg03(String manualImg03) {
		this.manualImg03 = manualImg03;
	}

	public String getManual04() {
		return manual04;
	}

	public void setManual04(String manual04) {
		this.manual04 = manual04;
	}

	public String getManualImg04() {
		return manualImg04;
	}

	public void setManualImg04(String manualImg04) {
		this.manualImg04 = manualImg04;
	}

	public String getManual05() {
		return manual05;
	}

	public void setManual05(String manual05) {
		this.manual05 = manual05;
	}

	public String getManualImg05() {
		return manualImg05;
	}

	public void setManualImg05(String manualImg05) {
		this.manualImg05 = manualImg05;
	}

	public String getManual06() {
		return manual06;
	}

	public void setManual06(String manual06) {
		this.manual06 = manual06;
	}

	public String getManualImg06() {
		return manualImg06;
	}

	public void setManualImg06(String manualImg06) {
		this.manualImg06 = manualImg06;
	}

	public String getManual07() {
		return manual07;
	}

	public void setManual07(String manual07) {
		this.manual07 = manual07;
	}

	public String getManualImg07() {
		return manualImg07;
	}

	public void setManualImg07(String manualImg07) {
		this.manualImg07 = manualImg07;
	}

	public String getManual08() {
		return manual08;
	}

	public void setManual08(String manual08) {
		this.manual08 = manual08;
	}

	public String getManualImg08() {
		return manualImg08;
	}

	public void setManualImg08(String manualImg08) {
		this.manualImg08 = manualImg08;
	}

	public String getMANUAL09() {
		return MANUAL09;
	}

	public void setMANUAL09(String mANUAL09) {
		MANUAL09 = mANUAL09;
	}

	public String getManualImg09() {
		return manualImg09;
	}

	public void setManualImg09(String manualImg09) {
		this.manualImg09 = manualImg09;
	}

	public String getManual10() {
		return manual10;
	}

	public void setManual10(String manual10) {
		this.manual10 = manual10;
	}

	public String getManualImg10() {
		return manualImg10;
	}

	public void setManualImg10(String manualImg10) {
		this.manualImg10 = manualImg10;
	}

	public String getManual11() {
		return manual11;
	}

	public void setManual11(String manual11) {
		this.manual11 = manual11;
	}

	public String getManualImg11() {
		return manualImg11;
	}

	public void setManualImg11(String manualImg11) {
		this.manualImg11 = manualImg11;
	}

	public String getManual12() {
		return manual12;
	}

	public void setManual12(String manual12) {
		this.manual12 = manual12;
	}

	public String getManualImg12() {
		return manualImg12;
	}

	public void setManualImg12(String manualImg12) {
		this.manualImg12 = manualImg12;
	}

	public String getManual13() {
		return manual13;
	}

	public void setManual13(String manual13) {
		this.manual13 = manual13;
	}

	public String getManualImg13() {
		return manualImg13;
	}

	public void setManualImg13(String manualImg13) {
		this.manualImg13 = manualImg13;
	}

	public String getManual14() {
		return manual14;
	}

	public void setManual14(String manual14) {
		this.manual14 = manual14;
	}

	public String getManualImg14() {
		return manualImg14;
	}

	public void setManualImg14(String manualImg14) {
		this.manualImg14 = manualImg14;
	}

	public String getManual15() {
		return manual15;
	}

	public void setManual15(String manual15) {
		this.manual15 = manual15;
	}

	public String getManualImg15() {
		return manualImg15;
	}

	public void setManualImg15(String manualImg15) {
		this.manualImg15 = manualImg15;
	}

	public String getManual16() {
		return manual16;
	}

	public void setManual16(String manual16) {
		this.manual16 = manual16;
	}

	public String getManualImg16() {
		return manualImg16;
	}

	public void setManualImg16(String manualImg16) {
		this.manualImg16 = manualImg16;
	}

	public String getManual17() {
		return manual17;
	}

	public void setManual17(String manual17) {
		this.manual17 = manual17;
	}

	public String getManualImg17() {
		return manualImg17;
	}

	public void setManualImg17(String manualImg17) {
		this.manualImg17 = manualImg17;
	}

	public String getManual18() {
		return manual18;
	}

	public void setManual18(String manual18) {
		this.manual18 = manual18;
	}

	public String getManualImg18() {
		return manualImg18;
	}

	public void setManualImg18(String manualImg18) {
		this.manualImg18 = manualImg18;
	}

	public String getManual19() {
		return manual19;
	}

	public void setManual19(String manual19) {
		this.manual19 = manual19;
	}

	public String getManualImg19() {
		return manualImg19;
	}

	public void setManualImg19(String manualImg19) {
		this.manualImg19 = manualImg19;
	}

	public String getManual20() {
		return manual20;
	}

	public void setManual20(String manual20) {
		this.manual20 = manual20;
	}

	public String getManualImg20() {
		return manualImg20;
	}

	public void setManualImg20(String manualImg20) {
		this.manualImg20 = manualImg20;
	}

	public String getRcpNaTip() {
		return rcpNaTip;
	}

	public void setRcpNaTip(String rcpNaTip) {
		this.rcpNaTip = rcpNaTip;
	}

}
