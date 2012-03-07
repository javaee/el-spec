package javax.el;

/**
 * The listener interface for receiving notification when an
 * EL expression is evaluated.
 *
 * @see EvaluationEvent
 * @since EL 3.0
 */
public interface EvaluationListener extends java.util.EventListener {

    /**
     * Receives notification before an EL expression is evaluated
     */
    void beforeEvaluation(EvaluationEvent ee);

    /**
     * Receives notification after an EL expression is evaluated
     */
    void afterEvaluation(EvaluationEvent ee);

}
