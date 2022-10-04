package io.frozor.gastracker.util

/// The idea is to emulate Symbol from JS, where you can create a unique symbol that is equal
/// to nothing other than to the same instance of the object. It's pretty useful for async scenarios
/// in JS, but since kotlin is multithreaded, this may not be necessary anyways.
class Symbol {
    override fun equals(other: Any?): Boolean {
        return other === this
    }
}