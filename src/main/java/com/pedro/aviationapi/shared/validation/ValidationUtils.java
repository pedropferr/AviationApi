package com.pedro.aviationapi.shared.validation;

public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * Valida uma lista de códigos de aeroportos separados por vírgula.
     * @param codes String contendo os códigos (ex.: "AVL, KAVL")
     * @throws IllegalArgumentException se houver algum código inválido
     */
    public static void validateAirportsCodes(String codes) {
        if (codes == null || codes.isBlank())
            throw new IllegalArgumentException("Nenhum código foi informado!");

        String[] parts = codes.split(",");
        for (String part : parts) {
            String code = part.trim();

            if (code.length() != 3 && code.length() != 4) {
                throw new IllegalArgumentException("O código '" + code + "' deve ter 3 ou 4 caracteres!");
            }

            if (!code.matches("^[A-Z0-9]{3,4}$")) {
                throw new IllegalArgumentException("O código '" + code + "' deve conter apenas letras maiúsculas e/ou números!");
            }
        }
    }
}
