package com.jnape.palatable.lambda.algebra;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public interface Nat<A, B, FA extends Functor<A>, FB extends Functor<B>> extends Fn1<FA, FB> {
}
