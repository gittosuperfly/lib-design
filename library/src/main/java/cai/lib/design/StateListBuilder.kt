package cai.lib.design

abstract class StateListBuilder<T : Any> {
    protected var mPressed: Boolean = true

    protected var mDisabled: Boolean = false

    abstract fun build(): T

    abstract fun statelessBuild(): T
}