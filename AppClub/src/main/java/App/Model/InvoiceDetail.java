package App.Model;

/**
 * @author Arbaces Restrepo, Yhogan Viancha, Kevin Parra
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="invoicedetail")
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    @JoinColumn(name="invoiceid")
    @ManyToOne
    private Invoice invoiceId;
    @Column(name="itemnumber")
    private int itemNumber;
    @Column(name="description")
    private String description;
    @Column(name="amount")
    private double amount;

}
