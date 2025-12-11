package org.example.domain.Exceptions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excepción base de dominio.
 *
 * Pensada para representar errores de reglas de negocio (domain-driven),
 * desacoplada de detalles de infraestructura (HTTP, BD, etc.).
 *
 * ✔ Lleva un "code" estable (útil para i18n/logs/tests)
 * ✔ Permite adjuntar "context" (clave/valor) para depurar mejor
 * ✔ Hereda de RuntimeException para no forzar try/catch
 */
public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final Map<String, Object> context;

    public DomainException(String message) {
        this(null, message, null, null);
    }

    public DomainException(String message, Throwable cause) {
        this(null, message, cause, null);
    }

    public DomainException(String code, String message) {
        this(code, message, null, null);
    }

    public DomainException(String code, String message, Throwable cause) {
        this(code, message, cause, null);
    }

    public DomainException(String code, String message, Map<String, Object> context) {
        this(code, message, null, context);
    }

    public DomainException(String code, String message, Throwable cause, Map<String, Object> context) {
        super(message, cause);
        this.code = code;
        if (context == null || context.isEmpty()) {
            this.context = Collections.emptyMap();
        } else {
            this.context = Collections.unmodifiableMap(new LinkedHashMap<>(context));
        }
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getContext() {
        return context;
    }


    public static DomainException validation(String message) {
        return new DomainException("validacion.error", message);
    }

    public static DomainException notFound(String entity, String key, Object value) {
        Map<String, Object> ctx = new LinkedHashMap<>();
        ctx.put("entity", entity);
        ctx.put(key, value);
        return new DomainException(entity.toLowerCase() + ".no_encontrado",
                entity + " no encontrado", ctx);
    }

    public static DomainException business(String code, String message) {
        return new DomainException(code, message);
    }

    public DomainException withContext(String key, Object value) {
        Map<String, Object> ctx = new LinkedHashMap<>(this.context.isEmpty() ? Map.of() : this.context);
        ctx.put(key, value);
        return new DomainException(this.code, this.getMessage(), this.getCause(), ctx);
    }

    @Override
    public String toString() {
        String base = "DomainException{code=" + code + ", message=" + getMessage() + "}";
        if (context.isEmpty()) return base;
        return base + " context=" + context;
    }
}
