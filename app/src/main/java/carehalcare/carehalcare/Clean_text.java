package carehalcare.carehalcare;

public class Clean_text {
    String changeSheet;
    String changeCloth;
    String ventilation;
    String et_cleanForm;

    String cleanTodayResult;

    public Clean_text(String changeSheet, String changeCloth, String ventilation, String et_cleanForm, String cleanTodayResult) {
        this.changeSheet = changeSheet;
        this.changeCloth = changeCloth;
        this.ventilation = ventilation;
        this.et_cleanForm = et_cleanForm;
        this.cleanTodayResult = cleanTodayResult;
    }
    public String get_CleanTodayResult() {
        return cleanTodayResult;
    }

    public void set_CleanTodayResult(String cleanTodayResult) { this.cleanTodayResult = cleanTodayResult; }


    public String getChangeSheet() {
        return changeSheet;
    }

    public void setChangeSheet(String changeSheet) {
        this.changeSheet = changeSheet;
    }

    public String getChangeCloth() {
        return changeCloth;
    }

    public void setChangeCloth(String changeCloth) {
        this.changeCloth = changeCloth;
    }

    public String getVentilation() {
        return ventilation;
    }

    public void setVentilation(String ventilation) {
        this.ventilation = ventilation;
    }

    public String getEt_cleanForm() {
        return et_cleanForm;
    }

    public void setEt_cleanForm(String et_cleanForm) {
        this.et_cleanForm = et_cleanForm;
    }
}
