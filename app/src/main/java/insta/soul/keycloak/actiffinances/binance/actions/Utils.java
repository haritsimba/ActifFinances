package insta.soul.keycloak.actiffinances.binance.actions;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import insta.soul.keycloak.actiffinances.binance.beans.CryptoList;

public class Utils {
    public String makeSymbol(List<String> symbols) {
        String finalsymbol = "[";

        for (String s : symbols) {
            if (symbols.indexOf(s) == 0) {
                finalsymbol = finalsymbol.concat("\"" + s + "\"");
                ;
            } else {
                finalsymbol = finalsymbol.concat(",\"".concat(s).concat("\""));
            }
        }


        finalsymbol = finalsymbol.concat("]");
        Log.d("symbol maker", finalsymbol);
        return finalsymbol;
    }

    public String makeWebsocketParams(List<CryptoList> CryptoLists) {
        String params = "";
        for (CryptoList cryptoList : CryptoLists) {
            if (CryptoLists.indexOf(cryptoList) == 0) {
                params = "[\"" + cryptoList.getWsstream() + "\"";
            } else {
                params = params.concat(",\"" + cryptoList.getWsstream() + "\"");
            }
        }
        params = params.concat("]");
        return params;
    }

    public String getBlockChainsName(List<CryptoList> cryptoLists, String symbol) {
        for (CryptoList cryptoList : cryptoLists) {
            if (Objects.equals(cryptoList.getSymbol(), symbol)) {
                return cryptoList.getNom();
            }
        }
        return "uknown";
    }
}
