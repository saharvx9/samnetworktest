package com.saharv.samnetworktest.utils.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure:Exception() {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object ArticlesEmpty: Failure()


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}
