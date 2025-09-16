package com.pedro.aviationapi.api.dtos;

import jakarta.validation.constraints.*;

public class AirportRequest {
    @NotEmpty(message = "O código é obrigatório!")
    @Size(min = 3, max = 4, message = "O código deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9]{3,4}$", message = "Código do aeroporto invalido")
    public String faaCode;
}
