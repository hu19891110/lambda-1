package com.jnape.palatable.lambda.functor.applicative;

import org.junit.Test;
import testsupport.functors.applicatives.SequenceSideEffectingApplicative;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ApplicativeTest {

    @Test
    public void fmap() {
        AtomicInteger sequenceInvocations = new AtomicInteger(0);
        SequenceSideEffectingApplicative<Integer> applicative = new SequenceSideEffectingApplicative<>(1, sequenceInvocations::incrementAndGet);

        assertEquals(applicative.pure(2), applicative.fmap(x -> x + 1));
        assertEquals(1, sequenceInvocations.get());
    }

    @Test
    public void discardL() {
        AtomicInteger appIntSequenceInvocations = new AtomicInteger(0);
        SequenceSideEffectingApplicative<Integer> appInt = new SequenceSideEffectingApplicative<>(1, appIntSequenceInvocations::incrementAndGet);

        AtomicInteger appStringSequenceInvocations = new AtomicInteger(0);
        SequenceSideEffectingApplicative<String> appString = new SequenceSideEffectingApplicative<>("foo", appStringSequenceInvocations::incrementAndGet);

        assertEquals(appString, appInt.discardL(appString));
        assertEquals(1, appIntSequenceInvocations.get());
        assertEquals(1, appStringSequenceInvocations.get());
    }

    @Test
    public void discardR() {
        AtomicInteger appIntSequences = new AtomicInteger(0);
        SequenceSideEffectingApplicative<Integer> appInt = new SequenceSideEffectingApplicative<>(1, appIntSequences::incrementAndGet);

        AtomicInteger appStringSequences = new AtomicInteger(0);
        SequenceSideEffectingApplicative<String> appString = new SequenceSideEffectingApplicative<>("foo", appStringSequences::incrementAndGet);

        assertEquals(appInt, appInt.discardR(appString));
        assertEquals(1, appIntSequences.get());
        assertEquals(1, appStringSequences.get());
    }
}