package id.ac.ui.cs.advprog.bepayment.enums;

import lombok.Getter;

@Getter
public enum TopUpStatus{
    WAITING_APPROVAL("WAITING_APPROVAL"),
    REJECTED("REJECTED"),
    SUCCESS("SUCCESS"),
    CANCELLED("CANCELLED");

    private final String value;

    private TopUpStatus(String value){
        this.value = value;
    }

    public static boolean contains(String param){
        for(TopUpStatus topUpStatus : TopUpStatus.values()){
            if(topUpStatus.name().equals(param)){
                return true;
            }
        }
        return false;
    }
}