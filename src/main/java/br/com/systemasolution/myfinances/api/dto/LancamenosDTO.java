package br.com.systemasolution.myfinances.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamenosDTO {

    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private Long usuario;
    private BigDecimal valor;
    private LocalDate dataCadastro;
    private String tipo;
    private String status;

}
