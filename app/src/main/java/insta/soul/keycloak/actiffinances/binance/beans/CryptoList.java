package insta.soul.keycloak.actiffinances.binance.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CryptoList {
    private String symbol;
    private String nom;
    private String wsstream;
}

