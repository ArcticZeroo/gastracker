# Gas Tracker

The gas gauge in my car doesn't work. The odometer doesn't either. So I will apply the power of
Machine Learning (™) to guess what the current level of the gas gauge is.️

## Requirements

1. A physical BLE beacon, so that the app knows when you're in your car
   - By default, the app scans every 60 seconds, for 5 seconds. The BT beacon should therefore
     advertise at least every 2.5 seconds, probably faster just in case there's some delay between 
     the two.
2. A BT-enabled Android phone