package es.pixelvault.tienda.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cálculo de los gastos de envío. Los valores se leen de application.properties:
 *   tienda.envio.coste          -> coste fijo del envío
 *   tienda.envio.gratis-desde   -> subtotal a partir del cual el envío es gratuito
 */
@Service
public class EnvioService {

    private final BigDecimal coste;
    private final BigDecimal gratisDesde;

    public EnvioService(@Value("${tienda.envio.coste:4.99}") BigDecimal coste,
                        @Value("${tienda.envio.gratis-desde:50.00}") BigDecimal gratisDesde) {
        this.coste = coste.setScale(2, RoundingMode.HALF_UP);
        this.gratisDesde = gratisDesde.setScale(2, RoundingMode.HALF_UP);
    }

    // Sin artículos no hay envío; a partir del umbral es gratis; en otro caso, el coste fijo.
    public BigDecimal calcular(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() <= 0 || subtotal.compareTo(gratisDesde) >= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return coste;
    }

    // Importe que falta para llegar al envío gratuito (0,00 si ya se ha alcanzado).
    public BigDecimal faltaParaEnvioGratis(BigDecimal subtotal) {
        BigDecimal actual = subtotal == null ? BigDecimal.ZERO : subtotal;
        BigDecimal falta = gratisDesde.subtract(actual);
        return falta.signum() < 0 ? BigDecimal.ZERO.setScale(2) : falta.setScale(2, RoundingMode.HALF_UP);
    }
}
