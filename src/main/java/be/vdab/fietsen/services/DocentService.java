package be.vdab.fietsen.services;

import javax.swing.plaf.basic.BasicIconFactory;
import java.math.BigDecimal;

public interface DocentService {
    void opslag(long id, BigDecimal percentage);
}
