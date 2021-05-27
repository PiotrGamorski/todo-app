package io.github.PiotrGamorski.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // DEFINES HOW LONG THE ANNOTATION LIVES
public @interface IllegalExceptionProcessing {
}
