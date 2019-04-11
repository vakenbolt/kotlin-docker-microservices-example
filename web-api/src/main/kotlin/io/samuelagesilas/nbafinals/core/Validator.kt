package io.samuelagesilas.nbafinals.core

import javax.validation.Validation


object Validator {
    val validator = Validation.buildDefaultValidatorFactory().validator
}