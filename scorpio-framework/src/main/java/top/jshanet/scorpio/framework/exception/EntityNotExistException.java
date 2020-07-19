package top.jshanet.scorpio.framework.exception;

/**
 * @author seanjiang
 * @since 2020-07-19
 */
@SuppressWarnings("rawtypes")
public class EntityNotExistException extends BadArgumentException {

    public EntityNotExistException(Class entityClass) {
        super("Entity " + entityClass.getSimpleName() + " not found.");
    }

}
