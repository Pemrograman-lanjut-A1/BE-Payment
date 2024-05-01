package id.ac.ui.cs.advprog.bepayment.enums;

import lombok.Getter;

@Getter
public enum TopUpMethod {
    TRANSFER_BANK("TRANSFER_BANK"),
    KARTU_KREDIT("KARTU_KREDIT"),
    E_WALLET("E_WALLET");

    private final String value;

    private TopUpMethod(String value){
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
