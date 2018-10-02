package takeout.waiterservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Entity
public class TakeOutOrder {
    @Id
    @GeneratedValue
    private Long id;
    private String items;
    private String customer;
    private String waiter;
    @Column(updatable = false)
    private Date createTime;
    private Date modifyTime;
}
