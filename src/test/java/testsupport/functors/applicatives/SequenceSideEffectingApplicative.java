package testsupport.functors.applicatives;

import com.jnape.palatable.lambda.functor.applicative.Applicative;

import java.util.function.Function;

public final class SequenceSideEffectingApplicative<A> implements Applicative<A, SequenceSideEffectingApplicative> {
    private final A        a;
    private final Runnable sequenceIO;

    public SequenceSideEffectingApplicative(A a, Runnable sequenceIO) {
        this.a = a;
        this.sequenceIO = sequenceIO;
    }

    @Override
    public <B> SequenceSideEffectingApplicative<B> pure(B b) {
        return new SequenceSideEffectingApplicative<>(b, sequenceIO);
    }

    @Override
    public <B> SequenceSideEffectingApplicative<B> sequence(
            Applicative<Function<? super A, ? extends B>, SequenceSideEffectingApplicative> appFn) {
        sequenceIO.run();
        return new SequenceSideEffectingApplicative<>(((SequenceSideEffectingApplicative<Function<? super A, ? extends B>>) appFn).a.apply(a), sequenceIO);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SequenceSideEffectingApplicative && a.equals(((SequenceSideEffectingApplicative) other).a);
    }

    @Override
    public int hashCode() {
        return 31 * a.hashCode() + sequenceIO.hashCode();
    }
}
