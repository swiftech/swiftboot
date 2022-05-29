package org.swiftboot.data.model.expression;

import org.springframework.data.spel.spi.EvaluationContextExtension;

/**
 * @author swiftech
 */
public class ModelEvaluationContextExtension implements EvaluationContextExtension {

    DataModelRootObject rootObject = new DataModelRootObject();

    @Override
    public String getExtensionId() {
        return "swiftboot";
    }

    @Override
    public Object getRootObject() {
        return rootObject;
    }
}
