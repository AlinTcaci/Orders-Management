package bll.validators;

/**
 * The Validator interface is validating objects of a specific type.
 * @param <T> the type of object to validate
 */
public interface Validator<T> {
    public void validate(T t);
}