package andres.art_connect.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import andres.art_connect.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the correoElectronico value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UsuarioCorreoElectronicoUnique.UsuarioCorreoElectronicoUniqueValidator.class
)
public @interface UsuarioCorreoElectronicoUnique {

    String message() default "{Exists.usuario.correoElectronico}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UsuarioCorreoElectronicoUniqueValidator implements ConstraintValidator<UsuarioCorreoElectronicoUnique, String> {

        private final UsuarioService usuarioService;
        private final HttpServletRequest request;

        public UsuarioCorreoElectronicoUniqueValidator(final UsuarioService usuarioService,
                final HttpServletRequest request) {
            this.usuarioService = usuarioService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(usuarioService.get(Long.parseLong(currentId)).getCorreoElectronico())) {
                // value hasn't changed
                return true;
            }
            return !usuarioService.correoElectronicoExists(value);
        }

    }

}
