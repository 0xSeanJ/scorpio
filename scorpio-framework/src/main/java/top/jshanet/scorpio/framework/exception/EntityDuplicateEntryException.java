package top.jshanet.scorpio.framework.exception;

/**
 * @author seanjiang
 * @since 2020-07-19
 */
@SuppressWarnings("rawtypes")
public class EntityDuplicateEntryException extends BadArgumentException {

    public EntityDuplicateEntryException(Class entityClass) {
        super("Duplicate entry for " + entityClass.getSimpleName());
    }
}