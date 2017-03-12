package org.libresonic.player.validator;

import org.libresonic.player.command.DatabaseSettingsCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DatabaseSettingsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DatabaseSettingsValidator.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DatabaseSettingsCommand command = (DatabaseSettingsCommand) target;
        // TODO:AD
    }
}
