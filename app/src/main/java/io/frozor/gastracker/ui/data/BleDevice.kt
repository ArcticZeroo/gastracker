package io.frozor.gastracker.ui.data

data class BleDevice(val address: String, val name: String?, val rssi: Int?) {
    override fun equals(other: Any?): Boolean {
        if (other !is BleDevice) {
            return false
        }

        return other.address == address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}