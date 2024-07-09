package insta.soul.keycloak.actiffinances.coingecko.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FeedContainer {
    @SerializedName("data")
    private List<Feed> data;
    @SerializedName( "count")
    private Integer count;
    @SerializedName("page")
    private Integer page;
}
