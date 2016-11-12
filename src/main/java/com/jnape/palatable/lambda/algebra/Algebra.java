package com.jnape.palatable.lambda.algebra;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public interface Algebra<A, FA extends Functor<A>> extends Fn1<A, FA> {
}
