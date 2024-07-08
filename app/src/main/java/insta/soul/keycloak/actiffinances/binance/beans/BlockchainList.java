package insta.soul.keycloak.actiffinances.binance.beans;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class BlockchainList {
    @Setter
    @Getter
    private List<CryptoList> CryptoLists;
    @Setter
    @Getter
    private List<List<CryptoList>> cryptoListsOptimized;
    private static BlockchainList instance;

    public static BlockchainList getInstance(){
        if (instance==null){
            instance = new BlockchainList();
        }
        return instance;
    }
    public void addList(List<CryptoList> list){
        cryptoListsOptimized.add(list);
    }

}
