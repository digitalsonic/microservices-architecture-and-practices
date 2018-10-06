package takeout.chefservice;

import lombok.Data;

@Data
public class TakeOutOrder {
    private Long id;
    private String items;
    private String chef;
    private String state;
}
