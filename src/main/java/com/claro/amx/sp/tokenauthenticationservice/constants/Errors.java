package com.claro.amx.sp.tokenauthenticationservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.ERROR_LEVEL;

@Getter
@AllArgsConstructor
public enum Errors {
    ERROR_BADREQUEST_GENERAL("100400", "No se puede procesar la informacion entrante - Field: %s", ERROR_LEVEL),
    ERROR_BADREQUEST_FAILHEADER_REQUIRED("100401", "El campo %s es requerido en el Header para procesar su consulta", ERROR_LEVEL),

    ERROR_TOKEN_INVALID("100101", "El Token: %s no es válido", ERROR_LEVEL),
    ERROR_PARSING_TOKEN("100102", "No se pudo extraer el contenido del Token: %s", ERROR_LEVEL),
    ERROR_TOKEN_CONTENT_REQUIRED("100103", "El campo %s es nulo o vacio", ERROR_LEVEL),
    ERROR_CHANNEL_NOT_AUTHORIZED("100104", "El channel %s no está autorizado", ERROR_LEVEL),
    ERROR_CREATION_TOKEN("100105", "Error al generar el Token: %s", ERROR_LEVEL),


    ERROR_DATABASE_SP_CHANNEL_CREDENTIALS("200001", "Error al consultar la base de datos CCARD ChannelCredentialsDAO -> [%s] %s", ERROR_LEVEL),
    ERROR_DATABASE_TIMEOUT_CHANNEL_CREDENTIAL("200002", "Timeout - %s segundos - <query>", ERROR_LEVEL),
    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND("200003", "Error no se encontraron registros en SP_CHANNEL_CREDENTIAL con el valor %s", ERROR_LEVEL),
    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_VALUE("200004", "Error no se encontraron datos cargados en SP_CHANNEL_CREDENTIAL para el canal %s", ERROR_LEVEL),

    ERROR_GENERAL("900000","ERROR - %s",ERROR_LEVEL);

    private final String code;
    private final String message;
    private final String level;

}