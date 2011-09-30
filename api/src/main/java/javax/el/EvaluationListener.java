package javax.el;

interface EvaluationListener extends java.util.EventListener {

    /**
     * Receives notification before an EL expression is evaluated
     */
    void beforeEvaluation(EvaluationEvent ee);

    /**
     * Receives notification before an EL expression is evaluated
     */
    void afterEvaluation(EvaluationEvent ee);

}
